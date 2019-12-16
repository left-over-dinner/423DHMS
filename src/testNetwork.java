import server.DHMSRequest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class testNetwork {
    public static void main (String[] args){
        try{
            int replyPort = 5151;
            DHMSRequest seqDHMSRequest = new DHMSRequest(replyPort, 1);
            byte[] transaction = DHMSRequest.encodeStreamAsDHMRequest(seqDHMSRequest);
            DatagramSocket socket = new DatagramSocket(replyPort);
            DatagramPacket request1 = new DatagramPacket(transaction,transaction.length, InetAddress.getByName("192.168.1.9"),3434);
            DatagramPacket request2 = new DatagramPacket(transaction,transaction.length, InetAddress.getByName("192.168.1.12"),3434);
            DatagramPacket request3 = new DatagramPacket(transaction,transaction.length, InetAddress.getByName("192.168.1.13"),3434);
            socket.send(request1);
            socket.send(request2);
            socket.send(request3);

            byte[] buffer = new byte[10000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);
            DHMSRequest result = DHMSRequest.decodeStreamAsDHMSRequest(buffer);
            System.out.println("Received reply "+result.transactionId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
