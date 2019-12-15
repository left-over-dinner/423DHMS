package server;

import javax.xml.crypto.Data;
import java.net.*;

public class Sequencer {
    private int serverPort = 3434;
    private int replyPort;
    private int replyPortResult;
    private String groupAddress = "225.4.5.7";
    private MulticastSocket multiSocket;
    private DatagramSocket replySocket;
    private DatagramSocket replySocketResult;
    byte[] buffer = new byte[10000];
    public Sequencer(int replyPort, int replyPortResult){
        try{
            this.replyPort = replyPort;
            this.replyPortResult = replyPortResult;
            multiSocket = new MulticastSocket(serverPort);
            replySocket = new DatagramSocket(replyPort);
            replySocketResult = new DatagramSocket(replyPortResult);
            //3s timeout
            replySocketResult.setSoTimeout(3000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public String sendTransaction(DHMSRequest dhmsRequest){
        int identifier = agreedIdentifier();
        System.out.println("[Sequencer] Agreed Identifier: "+identifier);
        dhmsRequest.transactionId = identifier;
        dhmsRequest.replyPort = replyPortResult;
        sendAgreedTransaction(dhmsRequest);
        String agreedResponse = agreedResponse();
        return agreedResponse;
    }
    private String agreedResponse(){
        String[] results = new String[3];
        results[0]=null;
        results[1]=null;
        results[2]=null;
        try{
            for(int i = 0; i<3;i++){
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                replySocketResult.receive(reply);
                DHMSRequest response = DHMSRequest.decodeStreamAsDHMSRequest(buffer);
                results[response.RMId-1]=response.message;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return agreedOnMessage(results);
    }
    private String agreedOnMessage(String[] results){
        System.out.println("[Sequencer] Responses Received From RMs");
        for(int i = 0; i<results.length;i++){
            if(results[i]!=null){ System.out.println("RM "+(i+1)+": "+results[i]);}
            else{
                System.out.println("RM "+(i+1)+": null");
                results[i]="NULL";
            }
        }
        //all three messages are equal
        if(results[0].equals(results[1]) && results[0].equals(results[2]) && results[1].equals(results[2])){
            System.out.println("[Sequencer] All responses are the same");
            return results[0];
        }else{
            System.out.println("[Sequencer] Only two responses are the same");
            System.out.println("[Sequencer] Notifying failure to all RMs");
            if(results[0].equals(results[1])){
                notifyFailure(3);
                return results[0];
            }else if(results[0].equals(results[2])){
                notifyFailure(2);
                return results[0];
            }
            else if(results[1].equals(results[2])){
                notifyFailure(1);
                return results[1];
            }
        }

        return "";
    }
    private void notifyFailure(int RMFailureId){
        DHMSRequest failureNotify = new DHMSRequest();
        failureNotify.RMId=RMFailureId;failureNotify.isFailure=true;
        byte[] message = DHMSRequest.encodeStreamAsDHMRequest(failureNotify);
        multicast(message);
    }
    private String sendAgreedTransaction(DHMSRequest dhmsRequest){
        byte[] message = DHMSRequest.encodeStreamAsDHMRequest(dhmsRequest);
        multicast(message);
        return "";
    }
    private int agreedIdentifier(){
        int maxIdentifier = -1;
        try{
            System.out.println("[Sequencer] Multicasting for identifier agreement");
            byte[] message = DHMSRequest.encodeStreamAsDHMRequest(new DHMSRequest(replyPort));
            multicast(message);
            System.out.println("[Sequencer] Waiting to receive identifiers");
            for(int i = 0; i<3;i++){
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                replySocket.receive(reply);
                DHMSRequest response = DHMSRequest.decodeStreamAsDHMSRequest(buffer);
                if(maxIdentifier<response.transactionId) maxIdentifier=response.transactionId;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return maxIdentifier;
    }
    private void multicast(byte[] message){
        try{
            System.out.println("[Sequencer] Sending Transaction to DHMS");
            /*DatagramPacket request = new DatagramPacket(message,message.length, InetAddress.getByName(groupAddress),serverPort);
            multiSocket.send(request);
             */
            DatagramPacket request = new DatagramPacket(message, message.length, InetAddress.getByName("192.168.43.172"),2001);
            DatagramSocket internalPort = new DatagramSocket(5001);
            internalPort.send(request);
            System.out.println("[Sequencer] Transaction Sent");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
