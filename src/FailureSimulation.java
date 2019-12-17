import server.DHMSRequest;
import server.ENV;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class FailureSimulation {
    public static void main(String[] args){
        try{
            DHMSRequest failureTest = new DHMSRequest();
            failureTest.simulateFailure=true;
            failureTest.RMId=1;
            byte[] serializedMessage = DHMSRequest.encodeStreamAsDHMRequest(failureTest);
            DatagramSocket socket = new DatagramSocket();
            byte[] message = serializedMessage;
            DatagramPacket request = new DatagramPacket(message,message.length, InetAddress.getByName(ENV.getIpById(failureTest.RMId)),3434);
            socket.send(request);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
