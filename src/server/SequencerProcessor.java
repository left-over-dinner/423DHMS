package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Deque;

public class SequencerProcessor {
    private int transactionId;
    private DatagramSocket replySocket;
    private InetAddress host;
    public SequencerProcessor(){
        this.transactionId=1;
        try{
            replySocket = new DatagramSocket();
            host = InetAddress.getByName("localhost");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setTransactionId(int id){
        transactionId=id;
    }
    public void sendTransactionId(int replyPort, int agreedMaxIdentifier){
        try{
            System.out.println("[SequencerListener] New Request Received From Port "+replyPort);
            byte[] message = DHMSRequest.encodeStreamAsDHMRequest( new DHMSRequest(-1, Math.max(agreedMaxIdentifier,transactionId)+1));
            DatagramPacket reply = new DatagramPacket(message,message.length,host,replyPort);
            replySocket.send(reply);
            System.out.println("[SequencerListener] Sent New Transaction ID");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
