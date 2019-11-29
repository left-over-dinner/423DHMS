import server.DHMSReplicaInterface;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TestServerThread  extends Thread{
    public void run(){
        try{
            byte[] buffer = new byte[10000];
            int multicastPort = 1231;
            MulticastSocket multicastSocket = new MulticastSocket(1231);
            InetAddress group = InetAddress.getByName("225.4.5.6");
            multicastSocket.joinGroup(group);
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length, group, multicastPort);
                multicastSocket.receive(request);
                System.out.println("Packet received");
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = new ObjectInputStream(bais);
                DHMSReplicaInterface messageClass = (DHMSReplicaInterface) ois.readObject();
                System.out.println(messageClass.getAppointmentSchedule("MTLP1234"));
                String result = "RECEIVED AND PROCESSED";
                //DatagramSocket socketReply = new DatagramSocket();
                DatagramSocket socket2 = new DatagramSocket();
                InetAddress host = InetAddress.getByName("localhost");
                DatagramPacket reply = new DatagramPacket(result.getBytes(),result.length(),host,1234);
                socket2.send(reply);
                System.out.println("SENT BACK");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
