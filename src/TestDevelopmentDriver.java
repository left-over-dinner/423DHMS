import hospitalModule.Hospital;
import hospitalModule.HospitalHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import server.DHMSReplica;
import server.DHMSRequest;
import server.Frontend;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TestDevelopmentDriver {
    public static void main(String[] args){
        try{
            try{
                //orbd -ORBInitialPort 900 -ORBInitialHost localhost
                ORB orb = ORB.init(new String[1], null);
                //-ORBInitialPort 1050 -ORBInitialHost localhost
                org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
                Hospital hospital = (hospitalModule.Hospital) HospitalHelper.narrow(ncRef.resolve_str("DHMSFrontend"));
                System.out.println(hospital.addAppointment("MTLE121019","Dental",5));
            }catch (Exception e){
                e.printStackTrace();
            }
            /*
            Frontend frontend = new Frontend();
            System.out.println(frontend.addAppointment("MTLE121019","Dental",1));
            System.out.println(frontend.addAppointment("MTLE131019","Dental",1));
            //System.out.println(frontend.removeAppointment("MTLE121019","Dental"));
            System.out.println(frontend.listAppointmentAvailability("Dental"));
            System.out.println(frontend.bookAppointment("MTLP1234","MTLE121019","Dental"));
            System.out.println(frontend.getAppointmentSchedule("MTLP1234"));
            System.out.println(frontend.swapAppointment("MTLP1234","MTLE121019","Dental", "MTLE131019", "Dental"));
            System.out.println(frontend.getAppointmentSchedule("MTLP1234"));
            /*
            DHMSRequest dhmsRequest = new DHMSRequest(1234);
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bStream);
            oo.writeObject(dhmsRequest);
            oo.close();
            byte[] serializedMessage = DHMSRequest.encodeStreamAsDHMRequest(dhmsRequest);
            DatagramSocket socket2 = new DatagramSocket(1234);
            try{
                int serverPort = 3434;
                MulticastSocket socket = new MulticastSocket(serverPort);
                byte[] message = serializedMessage;
                DatagramPacket request = new DatagramPacket(message,message.length,InetAddress.getByName("225.4.5.7"),serverPort);
                socket.send(request);
                for(int i = 0; i<3;i++){
                    byte[] buffer = new byte[10000];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    socket2.receive(reply);
                }
                DHMSRequest dhmsRequest1 = new DHMSRequest(1234,1,"addMethod", null, null,null,null,-1,null,null);
                byte[] transaction = DHMSRequest.encodeStreamAsDHMRequest(dhmsRequest1);
                DatagramPacket request3 = new DatagramPacket(transaction,transaction.length,InetAddress.getByName("225.4.5.7"),serverPort);
                socket.send(request3);
                for(int i = 0; i<3;i++){
                    byte[] buffer = new byte[10000];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    socket2.receive(reply);
                    DHMSRequest result = DHMSRequest.decodeStreamAsDHMSRequest(buffer);
                    System.out.println(result.RMId+ ": "+result.message);
                }
                byte[] message3 = DHMSRequest.encodeStreamAsDHMRequest(new DHMSRequest(3,true));
                request = new DatagramPacket(message3,message3.length,InetAddress.getByName("225.4.5.7"),serverPort);
                socket.send(request);
            }catch(Exception e){
                e.printStackTrace();
            }


            /*
            DHMSReplica replica = new DHMSReplica();
            System.out.println(replica.addAppointment("MTLA1234","MTLM121019","Dental",5));
            System.out.println(replica.addAppointment("MTLA1234","MTLA121019","Dental",5));
            System.out.println(replica.addAppointment("MTLA1234","MTLE121019","Dental",5));
            System.out.println(replica.addAppointment("MTLA1234","MTLM121119","Dental",5));
            System.out.println(replica.removeAppointment("MTLA1234","SHEE121119","Dental"));
            System.out.println(replica.bookAppointment("MTLP1234","MTLM121019","Dental"));
            System.out.println(replica.getAppointmentSchedule("MTLP1234"));
            System.out.println(replica.swapAppointment("MTLP1234","MTLM121019","Dental","MTLM121119", "Dental"));
            System.out.println(replica.getAppointmentSchedule("MTLP1234"));
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bStream);
            oo.writeObject(replica);
            oo.close();
            byte[] serializedMessage = bStream.toByteArray();
            DatagramSocket socket2 = new DatagramSocket(1234);
                try{
                    int serverPort = 1231;
                    MulticastSocket socket = new MulticastSocket(serverPort);
                    byte[] message = serializedMessage;
                    InetAddress host = InetAddress.getByName("localhost");
                    DatagramPacket request = new DatagramPacket(message,message.length,InetAddress.getByName("225.4.5.6"),serverPort);
                    socket.send(request);
                    for(int i = 0; i<3;i++){
                        byte[] buffer = new byte[1000];
                        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                        socket2.receive(reply);
                        String reply_msg = new String(reply.getData());
                        //UDP String side effect being fixed
                        System.out.println(reply_msg);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            /*
            ORB orb = ORB.init(args, null);
            //-ORBInitialPort 1050 -ORBInitialHost localhost
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            Hospital CA = (Hospital) HospitalHelper.narrow(ncRef.resolve_str("MTLHosp"));
            Hospital QUE = (Hospital) HospitalHelper.narrow(ncRef.resolve_str("QUEHosp"));
            Hospital SHE = (Hospital) HospitalHelper.narrow(ncRef.resolve_str("SHEHosp"));
            */
            /*
            String ok1 = CA.addAppointment("MTLE121019","Dental",1);
            String ok2 = QUE.addAppointment("MTLE121019","Dental",1);
            String ok3 = SHE.addAppointment("MTLE121019","Dental",1);
            System.out.println(ok1);
            System.out.println(ok2);
            System.out.println(ok3);
            */
            /*
            System.out.println(CA.addAppointment("MTLE121019","Dental",1));
            System.out.println(SHE.addAppointment("SHEE131019","Dental",1));
            System.out.println(CA.bookAppointment("MTLP1234","MTLE121019","Dental"));
            //System.out.println(CA.bookAppointment("MTLP1234","SHEE131019","Dental"));
            System.out.println(CA.getAppointmentSchedule("MTLP1234"));
            System.out.println(CA.swapAppointment("MTLP1234","MTLE121019","Dental","SHEE131019","Dental"));
            System.out.println("HELLO");
            System.out.println(CA.getAppointmentSchedule("MTLP1234"));

            /*
            //String result = CA.testUDP();
            System.out.println(CA.getAppointmentSchedule("MTLP101234"));

            System.out.println(CA.listAppointmentAvailability("Dental"));
            System.out.println(CA.addAppointment("SHEA011232","Surgeon",5));
            System.out.println(CA.addAppointment("MTLA011232","Surgeon",4));
            System.out.println(CA.addAppointment("QUE011232","Surgeon",3));
            System.out.println(CA.listAppointmentAvailability("Surgeon"));


            System.out.println(CA.addAppointment("SHEA141019","Surgeon", 5));
            System.out.println(CA.addAppointment("SHEA151019","Surgeon", 5));
            System.out.println(CA.bookAppointment("MTLP101234","SHEA141019","Surgeon"));
            System.out.println(CA.getAppointmentSchedule("MTLP101234"));
            System.out.println(CA.removeAppointment("SHEA141019","Surgeon"));

            System.out.println(CA.getAppointmentSchedule("MTLP101234"));

            System.out.println("ADDING");
            System.out.println(CA.addAppointment("SHEA141019","Surgeon", 5));
            System.out.println(CA.addAppointment("QUEA171019","Dental", 5));
            System.out.println(CA.addAppointment("QUEA181019","Physician", 5));

            System.out.println(CA.addAppointment("QUEA181119","Physician", 2));

            System.out.println(CA.addAppointment("MTLA221019","Dental", 5));
            System.out.println(CA.addAppointment("MTLA231019","Dental", 5));
            System.out.println(CA.addAppointment("MTLA241019","Dental", 5));
            System.out.println(CA.addAppointment("MTLA251019","Dental", 5));
            System.out.println(CA.addAppointment("MTLA261019","Dental", 5));

            System.out.println(CA.addAppointment("QUEA151019","Physician",5));
            System.out.println(CA.addAppointment("QUEA191019","Physician",5));
            System.out.println(CA.addAppointment("QUEA191019","Physician",5));

            System.out.println(CA.listAppointmentAvailability("Physician"));


            System.out.println("BOOKING");
            System.out.println(CA.listAppointmentAvailability("Surgeon"));
            System.out.println(CA.bookAppointment("MTLP101234","SHEA141019","Surgeon"));
            System.out.println(CA.bookAppointment("MTLP101234","QUEA171019","Dental"));
            System.out.println(CA.bookAppointment("MTLP101234","QUEA181019","Physician"));
            System.out.println(CA.bookAppointment("MTLP101234","QUEA151019","Physician"));
            System.out.println(CA.bookAppointment("MTLP101234","QUEA191019","Physician"));

            System.out.println(CA.addAppointment("MTLA291019","Surgeon", 5));
            System.out.println(CA.addAppointment("QUEA291019","Surgeon", 5));
            System.out.println(CA.addAppointment("SHEM291019","Surgeon", 5));
            System.out.println(CA.addAppointment("SHEE291019","Dental", 5));


            System.out.println(CA.bookAppointment("MTLP101234","MTLA221019","Dental"));
            System.out.println(CA.bookAppointment("MTLP101234","MTLA231019","Dental"));
            System.out.println(CA.bookAppointment("MTLP101234","MTLA241019","Dental"));
            System.out.println(CA.bookAppointment("MTLP101234","MTLA251019","Dental"));
            System.out.println(CA.bookAppointment("MTLP101234","MTLA261019","Dental"));

            System.out.println(CA.bookAppointment("MTLP101234","MTLA261019","Dental"));
            System.out.println(CA.bookAppointment("MTLP101234","MTLA241019","Dental"));

            System.out.println(CA.bookAppointment("MTLP101234","MTLA241019","Dental"));

            System.out.println(CA.bookAppointment("MTLP101234","MTLA221019","Dental"));

            System.out.println(CA.bookAppointment("MTLP101234","MTLA291019","Surgeon"));
            System.out.println(CA.bookAppointment("MTLP101234","QUEA291019","Surgeon"));
            System.out.println(CA.bookAppointment("MTLP101234","SHEM291019","Surgeon"));
            System.out.println(CA.bookAppointment("MTLP101234","SHEE291019","Dental"));

            System.out.println(CA.bookAppointment("MTLP101234","QUEA181119","Physician"));
            System.out.println(CA.bookAppointment("MTLP123334","QUEA181119","Physician"));
            System.out.println(CA.bookAppointment("MTLP144424","QUEA181119","Physician"));

            System.out.println(CA.cancelAppointment("MTLP123334","QUEA181119"));
            System.out.println(CA.bookAppointment("MTLP144424","QUEA181119","Physician"));
            System.out.println(CA.getAppointmentSchedule("MTLP144424"));
            //System.out.println(result);
            */
        }catch (Exception e){
            e.printStackTrace();
        }
        /*CapacitySlots timeSlots = new CapacitySlots("G");
        timeSlots.addSlot();
        timeSlots.addSlot();
        timeSlots.addSlot();
        if(timeSlots.availableSlot()){
            timeSlots.bookSlot("TMLA123");
        }
        System.out.println(timeSlots.availableSlot());
        if(timeSlots.availableSlot()){
            timeSlots.bookSlot("TMLA123");
        }
        System.out.println(timeSlots.removeSlots());
        */
    }
}
