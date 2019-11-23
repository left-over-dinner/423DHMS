package server;

import hospitalUtil.HospitalServer;
import org.omg.CORBA.ORB;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class MTLServer {
    private static DatagramSocket socket;
    private static Logger logger;
    private static FileHandler fh;
    private static UDPProcessor udpProcessor;

    public static void setupLogger(){
        try{
            logger = Logger.getLogger("DHMS");
            fh = new FileHandler("logs/ServerLog.txt");
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            fh.setFormatter(new DHMSLogFormatter());
            logger.info("HELLO");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        try{
            udpProcessor = new UDPProcessor();
            //MTLHosp
            ORB orb = CorbaHospitalHandler.createServer("MTL",udpProcessor);
            System.out.println("Addition Server ready and waiting ...");
            // wait for invocations from clients
            byte[] buffer = new byte[500];
            socket = new DatagramSocket(1231);
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                String result = udpProcessor.decodeHospitalMessage(new String(request.getData()))+"@";
                DatagramPacket reply = new DatagramPacket(result.getBytes(),result.length(),request.getAddress(), request.getPort());
                socket.send(reply);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
