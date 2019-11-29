import server.DHMSReplicaInterface;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TestServerA {
        public static void main(String[] args){
            TestServerThread testServer = new TestServerThread();
            testServer.run();
        }
}
