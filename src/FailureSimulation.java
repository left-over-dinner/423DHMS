import server.DHMSRequest;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class FailureSimulation {
    public static void main(String[] args){
        try{
            DHMSRequest failureTest = new DHMSRequest();
            failureTest.simulateFailure=true;
            failureTest.RMId=3;
            byte[] serializedMessage = DHMSRequest.encodeStreamAsDHMRequest(failureTest);
            MulticastSocket socket = new MulticastSocket(3050);
            byte[] message = serializedMessage;
            DatagramPacket request = new DatagramPacket(message,message.length, InetAddress.getByName("225.4.5.7"),3434);
            socket.send(request);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
