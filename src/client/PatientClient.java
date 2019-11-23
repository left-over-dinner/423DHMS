package client;

import hospitalModule.Hospital;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Logger;

public class PatientClient extends Client {
    private String patientId;
    private Hospital hospital;
    private Scanner scanner;
    private Logger logger;
    public PatientClient(String patientId){
        this.patientId = patientId;
        scanner = new Scanner(System.in);
        logger = getLogger(patientId);
        System.out.println("Connecting to "+ClientUtil.getHospName(patientId));
        hospital = createIDLHospitalConn(patientId);
        System.out.println("Connection Successful\n");
    }
    public void startMenu(){
        boolean moreTransaction = false;
        do{
           moreTransaction = menu();
        }while(moreTransaction);
        System.out.println();
        System.out.println("Exiting DHMS");
    }
    private boolean menu(){
        System.out.println();
        System.out.println("Patient Menu");
        System.out.println("1. bookAppointment");
        System.out.println("2. getAppointmentSchedule");
        System.out.println("3. cancelAppointment");
        System.out.println("4. swapAppointment");
        System.out.println("5. Exit System");
        System.out.println();
        int commandNumber = getNumber(scanner, "Please enter a command number between 1 to 5: ",5);
        System.out.println();
        return proceedWithCommand(commandNumber);
    }
    private boolean proceedWithCommand(int commandNumber){
        switch (commandNumber){
            case 1:
                paramsForBookAppointment();
                return true;
            case 2:
                paramsForGetAppointmentSchedule();
                return true;
            case 3:
                paramsForCancelAppointment();
                return true;
            case 4:
                paramsSwapAppointment();
                return true;
            case 5:
                return false;
        }
        return false;
    }
    private void paramsForBookAppointment(){
        System.out.println("bookAppointment (patientID, appointmentID, appointmentType)");
        String appointmentId = getAppointmentId(scanner,"Please enter the appointment id (date format ddmmyy): ");
        String appointmentType = getAppointmentType(scanner,"Please enter the appointment type (Surgeon, Dental or Physician): ");
        try{
            logger.info(reqLogSending()+"bookAppointment("+patientId+","+appointmentId+","+appointmentType+") by "+patientId);
            String result = hospital.bookAppointment(patientId, appointmentId,appointmentType);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connecting to hospital server");
        }
    }
    private void paramsForGetAppointmentSchedule(){
        System.out.println("getAppointmentSchedule (patientID)");
        try{
            logger.info(reqLogSending()+"getAppointmentSchedule("+patientId+") by "+patientId);
            String result = hospital.getAppointmentSchedule(patientId);
            logger.info(reqLogResponse());
            prettyPrintSchedule(logger,result);
            System.out.println("Server Response:\n"+result);
        }catch(Exception e){
            System.out.println("Unable to establish connecting to hospital server");
        }
    }
    private void paramsForCancelAppointment(){
        System.out.println("cancelAppointment (patientID, appointmentID) ");
        String appointmentId = getAppointmentId(scanner,"Please enter the appointment id (date format ddmmyy): ");
        try{
            logger.info(reqLogSending()+"cancelAppointment("+patientId+","+appointmentId+") by "+patientId);
            String result = hospital.cancelAppointment(patientId,appointmentId);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connecting to hospital server");
        }
    }
    private void paramsSwapAppointment(){
        System.out.println("swapAppointment (patientID, oldAppointmentID, oldAppointmentType, newAppointmentID, newAppointmentType)");
        String oldAppointmentId = getAppointmentId(scanner,"Please enter the old appointment id (date format ddmmyy): ");
        String oldAppointmentType = getAppointmentType(scanner,"Please enter the old appointment type (Surgeon, Dental or Physician): ");
        String newAppointmentId = getAppointmentId(scanner,"Please enter the new appointment id (date format ddmmyy): ");
        String newAppointmentType = getAppointmentType(scanner,"Please enter the new appointment type (Surgeon, Dental or Physician): ");
        try{
            logger.info(reqLogSending()+"swapAppointment ("+patientId+", "+oldAppointmentId+", "+oldAppointmentType+", "+newAppointmentId+", "+newAppointmentType+") by "+patientId);
            String result = hospital.swapAppointment(patientId,oldAppointmentId, oldAppointmentType, newAppointmentId, newAppointmentType);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connecting to hospital server");
        }
    }
}
