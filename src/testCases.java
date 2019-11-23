import client.Client;
import client.ClientUtil;
import hospitalModule.Hospital;
import hospitalModule.HospitalHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class testCases {
    private static String newAppointmentID_1 = "MTLM301019";
    private static String newAppointmentType_1 = "Surgeon";
    private static int newAppointmentCap_1 = 2;
    private static String newAppointmentID_2 = "QUEA301119";
    private static String newAppointmentType_2 = "Dental";
    private static int newAppointmentCap_2 = 2;
    public static void main(String[] args){
        SwapAppointments[] swapAppointmentsTests ={
          new SwapAppointments("MTLP1111","MTLE261019", "Dental",newAppointmentID_1, newAppointmentType_1),
          new SwapAppointments("SHEP2222","SHEM271019","Surgeon",newAppointmentID_1, newAppointmentType_1),
          new SwapAppointments("QUEP3333","SHEM281019","Physician",newAppointmentID_1, newAppointmentType_1),
          new SwapAppointments("MTLP4444","QUEA191119", "Dental",newAppointmentID_2, newAppointmentType_2),
          new SwapAppointments("SHEP5555","SHEM201119","Surgeon",newAppointmentID_2, newAppointmentType_2),
          new SwapAppointments("QUEP6666","MTLE211119","Physician",newAppointmentID_2, newAppointmentType_2),
        };
        createNecessaryAppointments();
        for(SwapAppointments apptSwap :swapAppointmentsTests){
            apptSwap.run();
        }
    }
    private static void createNecessaryAppointments(){
        try{
            ORB orb = ORB.init(new String[1], null);
            //-ORBInitialPort 1050 -ORBInitialHost localhost
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            Hospital mtlHosp= (hospitalModule.Hospital) HospitalHelper.narrow(ncRef.resolve_str("MTLHosp"));
            Hospital sheHosp= (hospitalModule.Hospital) HospitalHelper.narrow(ncRef.resolve_str("SHEHosp"));
            Hospital queHosp= (hospitalModule.Hospital) HospitalHelper.narrow(ncRef.resolve_str("QUEHosp"));
            mtlHosp.addAppointment("MTLE261019","Dental",1);
            mtlHosp.addAppointment("MTLE211119","Physician",1);
            sheHosp.addAppointment("SHEM271019","Surgeon",1);
            sheHosp.addAppointment("SHEM281019","Physician",1);
            sheHosp.addAppointment("SHEM201119","Surgeon",1);
            queHosp.addAppointment("QUEA191119","Dental",1);
            mtlHosp.addAppointment(newAppointmentID_1,newAppointmentType_1,newAppointmentCap_1);
            queHosp.addAppointment(newAppointmentID_2,newAppointmentType_2,newAppointmentCap_2);
        }catch(Exception e){
            System.out.println("Problem creating connection to hospital servers");
            System.exit(-1);
        }
    }

}

class SwapAppointments extends Client implements Runnable{
    Hospital connHospital;
    String patientID;
    String oldAppointmentID;
    String oldAppointmentType;
    String newAppointmentID;
    String newAppointmentType;
    public SwapAppointments(String patiendID, String oldAppointmentID, String oldAppointmentType, String newAppointmentID, String newAppointmentType){
        this.patientID=patiendID;
        this.oldAppointmentID=oldAppointmentID;
        this.oldAppointmentType=oldAppointmentType;
        this.newAppointmentID=newAppointmentID;
        this.newAppointmentType=newAppointmentType;
    }
    public void run(){
        connHospital = createIDLHospitalConn(patientID);
        try{
            //book appointment first and print the result
            String bookingRes = connHospital.bookAppointment(patientID,oldAppointmentID,oldAppointmentType);
            //wait 3sec before swapping
            Thread.sleep(3000);
            //try swapping
            String swapRes = connHospital.swapAppointment(patientID,oldAppointmentID,oldAppointmentType,newAppointmentID,newAppointmentType);
            //get schedule
            String scheduleRes = connHospital.getAppointmentSchedule(patientID);
            //print booking response, swap response and schedule
            System.out.println("bookAppointment("+patientID+","+oldAppointmentID+","+oldAppointmentType+"): "+bookingRes);
            System.out.println("swapAppointment("+patientID+","+oldAppointmentID+","+oldAppointmentType+","+newAppointmentID+","+newAppointmentType+"): "+swapRes);
            System.out.println("getAppointmentSchedule("+patientID+"): ");
            System.out.println(scheduleRes);
        }catch(Exception e){
            System.out.println("Thread error with patientID "+patientID);
        }
    }
}
