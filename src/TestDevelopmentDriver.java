import hospitalModule.Hospital;
import hospitalModule.HospitalHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class TestDevelopmentDriver {
    public static void main(String[] args){
        try{
            ORB orb = ORB.init(args, null);
            //-ORBInitialPort 1050 -ORBInitialHost localhost
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            Hospital CA = (Hospital) HospitalHelper.narrow(ncRef.resolve_str("MTLHosp"));
            Hospital QUE = (Hospital) HospitalHelper.narrow(ncRef.resolve_str("QUEHosp"));
            Hospital SHE = (Hospital) HospitalHelper.narrow(ncRef.resolve_str("SHEHosp"));

            /*
            String ok1 = CA.addAppointment("MTLE121019","Dental",1);
            String ok2 = QUE.addAppointment("MTLE121019","Dental",1);
            String ok3 = SHE.addAppointment("MTLE121019","Dental",1);
            System.out.println(ok1);
            System.out.println(ok2);
            System.out.println(ok3);
            */

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
