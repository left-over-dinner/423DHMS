package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DHMSReplicaSever extends Thread {
    private byte[] buffer_in;
    private DatagramSocket multicastSocket;
    private int multicastPort;
    private SequencerProcessor sequencerProcessor = new SequencerProcessor();
    private volatile DHMSReplica replica;
    private volatile int replicaId;
    private volatile int agreedMaxIdentifier;
    private TransactionHandler transactionHandler;
    private int internalPort;
    private volatile boolean failure;
    private DatagramSocket internalSocket;
    private volatile boolean simulateFailure=false;
    public DHMSReplicaSever(int id){
        try{
            this.failure=false;
            this.agreedMaxIdentifier=1;
            this.replicaId=id;
            this.multicastPort = 3434;
            this.internalPort=5656;
            buffer_in = new byte[10000];
            multicastSocket = new DatagramSocket(multicastPort);
            internalSocket = new DatagramSocket(internalPort);
            replica = new DHMSReplica();
            transactionHandler = new TransactionHandler();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void startListeningMulticast(){
        DHMSRequest dhmsRequest;
        transactionHandler.start();
        try{
            while(true){
                DatagramPacket request = new DatagramPacket(buffer_in, buffer_in.length);
                multicastSocket.receive(request);
                dhmsRequest = DHMSRequest.decodeStreamAsDHMSRequest(buffer_in);
                //for testing purpose only, to test the recovery
                if(dhmsRequest.simulateFailure){
                    //fail corresponding replica
                    if(dhmsRequest.RMId==replicaId){
                        System.out.println("Failure Simulation on Next Request");
                        this.simulateFailure=true;
                    }

                }
                //Sequencer request for identifier
                else if(dhmsRequest.isSequenceRequest){
                    sequencerProcessor.sendTransactionId(request, dhmsRequest.replyPort, agreedMaxIdentifier);
                }
                //this replica has failed a result which was previously sent
                //stop transaction queue, and do recovery
                else if(dhmsRequest.isFailure){
                    failure=true;
                    handleFailure(dhmsRequest);
                    failure=false;
                }
                //proceed with DHMS transactions (addAppointment, removeAppointment, etc)
                else{
                    sequencerProcessor.setTransactionId(++agreedMaxIdentifier);
                    dhmsRequest.inetAddress = request.getAddress();
                    transactionHandler.add(dhmsRequest);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void handleFailure(DHMSRequest dhmsRequest){
        byte[] buffer = new byte[10000];
        try{
            if(dhmsRequest.RMId==replicaId){
                System.out.println("Failure for RM "+replicaId+", waiting to receive recovery");
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                internalSocket.receive(request);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                replica = (DHMSReplica) objectInputStream.readObject();
                byteArrayInputStream.close();
                objectInputStream.close();
                System.out.println("Received recovery");
                simulateFailure=false;
            }else if( ((dhmsRequest.RMId%3+1)) == replicaId ) {
                System.out.println("Sending recovery for RM "+dhmsRequest.RMId +" from RM "+replicaId);
                TimeUnit.SECONDS.sleep(2);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(byteArrayOutputStream);
                oo.writeObject(replica);
                oo.close();
                byte[] message = byteArrayOutputStream.toByteArray();
                DatagramPacket request = new DatagramPacket(message, message.length, InetAddress.getByName(ENV.getIpById(dhmsRequest.RMId)), internalPort);
                internalSocket.send(request);
                System.out.println("Recovery for RM "+dhmsRequest.RMId+ " sent");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private class TransactionHandler extends Thread{
        private volatile PriorityQueue<DHMSRequest> transactionQ;
        private Comparator<DHMSRequest> comparator;
        private volatile DatagramSocket replySocket;
        public TransactionHandler(){
            comparator = new Comparator<DHMSRequest>() {
                @Override
                public int compare(DHMSRequest o1, DHMSRequest o2) {
                    if(o1.transactionId<o2.transactionId){
                        return 1;
                    }
                    return -1;
                }
            };
            transactionQ = new PriorityQueue<>(comparator);
            try{
                replySocket = new DatagramSocket();
                //host = InetAddress.getByName("localhost");
            }catch(Exception e){

            }
        }
        public void add(DHMSRequest dhmsRequest){
            transactionQ.add(dhmsRequest);
            System.out.println("add complete inside");
            System.out.println(transactionQ.size());
        }
        public void run(){
            System.out.println("RM id "+replicaId+": listening for new transaction");
            while(true){
                //System.out.println(this.transactionQ.size());
                if(!failure && this.transactionQ.size()>0){
                    DHMSRequest request = (DHMSRequest) this.transactionQ.poll();
                    System.out.println("[Transaction Handler] New Request Received From "+request.inetAddress.getHostAddress()+"/"+request.replyPort);
                    String result ="";
                    if(simulateFailure){
                        result = "alkfjhlksdbfjkhl";
                    }else{
                        result = replica.processTransaction(request);
                    }
                    byte[] message = DHMSRequest.encodeStreamAsDHMRequest(new DHMSRequest(replicaId,result));
                    DatagramPacket reply = new DatagramPacket(message,message.length,request.inetAddress,request.replyPort);
                    try{
                        replySocket.send(reply);
                        System.out.println("[Transaction Handler] Sent DHMS System Response");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

