package client;

import hospitalModule.Hospital;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Logger;

public class AdminClient extends Client {
    private String adminId;
    private Hospital hospital;
    private Scanner scanner;
    private Logger logger;
    public AdminClient(String adminId){
        this. adminId = adminId;
        scanner = new Scanner(System.in);
        logger = getLogger(adminId);
        System.out.println("Connecting to "+ClientUtil.getHospName(adminId));
        hospital = createIDLHospitalConn(adminId);
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
        System.out.println("Admin Menu");
        System.out.println("1. addAppointment");
        System.out.println("2. removeAppointment");
        System.out.println("3. listAppointmentAvailability");
        System.out.println("4. bookAppointment");
        System.out.println("5. getAppointmentSchedule");
        System.out.println("6. cancelAppointment");
        System.out.println("7. swapAppointment");
        System.out.println("8. Exit System");
        System.out.println();
        int commandNumber = getNumber(scanner, "Please enter a command number between 1 to 8: ",8);
        System.out.println();
        return proceedWithCommand(commandNumber);
    }
    private boolean proceedWithCommand(int commandNumber){
        switch (commandNumber){
            case 1:
                paramsForAddAppointment();
                return true;
            case 2:
                paramsForRemoveAppointment();
                return true;
            case 3:
                paramsForListAppointmentAvailability();
                return true;
            case 4:
                paramsForBookAppointment();
                return true;
            case 5:
                paramsForGetAppointmentSchedule();
                return true;
            case 6:
                paramsForCancelAppointment();
                return true;
            case 7:
                paramsSwapAppointment();
                return true;
            case 8:
                return false;
        }
        return false;
    }
    private void paramsForAddAppointment(){
        System.out.println("addAppointment(appointmentId, appointmentType, capacity)");
        String appointmentId = getAppointmentId(scanner,"Please enter the appointment id (date format ddmmyy): ");
        String appointmentType = getAppointmentType(scanner,"Please enter the appointment type (Surgeon, Dental or Physician): ");
        int capacity = getNumber(scanner,"Please enter a valid capacity (0 to 100): ",100);
        try{
            logger.info(reqLogSending()+"addAppointment("+appointmentId+","+appointmentType+","+capacity+") by "+adminId);
            String result = hospital.addAppointment(appointmentId,appointmentType,capacity);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connection to hospital server");
        }
    }
    private void paramsForRemoveAppointment(){
        System.out.println("removeAppointment(appointmentId, appointmentType)");
        String appointmentId = getAppointmentId(scanner,"Please enter the appointment id (date format ddmmyy): ");
        String appointmentType = getAppointmentType(scanner,"Please enter the appointment type (Surgeon, Dental or Physician): ");
        try{
            logger.info(reqLogSending()+"removeAppointment("+appointmentId+","+appointmentType+") by "+adminId);
            String result = hospital.removeAppointment(appointmentId,appointmentType);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connection to hospital server");
        }
    }
    private void paramsForListAppointmentAvailability(){
        System.out.println("listAppointmentAvailability(appointmentType)");
        String appointmentType = getAppointmentType(scanner,"Please enter the appointment type (Surgeon, Dental or Physician): ");
        try{
            logger.info(reqLogSending()+"listAppointmentAvailability("+appointmentType+") by "+adminId);
            String result = hospital.listAppointmentAvailability(appointmentType);
            logger.info(reqLogResponse());
            prettyPrintSchedule(logger,result);
            System.out.println("Server Response:\n"+ClientUtil.formatEmptyResult(result));
        }catch(Exception e){
            System.out.println("Unable to establish connection to hospital server");
        }
    }
    private void paramsForBookAppointment(){
        System.out.println("bookAppointment (patientID, appointmentID, appointmentType)");
        String patientId = getPatientId(scanner,"Please enter the patient id: ");
        String appointmentId = getAppointmentId(scanner,"Please enter the appointment id (date format ddmmyy): ");
        String appointmentType = getAppointmentType(scanner,"Please enter the appointment type (Surgeon, Dental or Physician): ");
        try{
            logger.info(reqLogSending()+"bookAppointment("+patientId+","+appointmentId+","+appointmentType+") by "+adminId);
            String result = hospital.bookAppointment(patientId, appointmentId,appointmentType);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connecting to hospital server");
        }
    }
    private void paramsForGetAppointmentSchedule(){
        System.out.println("getAppointmentSchedule (patientID)");
        String patientId = getPatientId(scanner,"Please enter the patient id: ");
        try{
            logger.info(reqLogSending()+"getAppointmentSchedule("+patientId+") by "+adminId);
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
        String patientId = getPatientId(scanner,"Please enter the patient id: ");
        String appointmentId = getAppointmentId(scanner,"Please enter the appointment id (date format ddmmyy): ");
        try{
            logger.info(reqLogSending()+"cancelAppointment("+patientId+","+appointmentId+") by "+adminId);
            String result = hospital.cancelAppointment(patientId,appointmentId);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connecting to hospital server");
        }
    }
    private void paramsSwapAppointment(){
        System.out.println("swapAppointment (patientID, oldAppointmentID, oldAppointmentType, newAppointmentID, newAppointmentType)");
        String patientId = getPatientId(scanner,"Please enter the patient id: ");
        String oldAppointmentId = getAppointmentId(scanner,"Please enter the old appointment id (date format ddmmyy): ");
        String oldAppointmentType = getAppointmentType(scanner,"Please enter the old appointment type (Surgeon, Dental or Physician): ");
        String newAppointmentId = getAppointmentId(scanner,"Please enter the new appointment id (date format ddmmyy): ");
        String newAppointmentType = getAppointmentType(scanner,"Please enter the new appointment type (Surgeon, Dental or Physician): ");
        try{
            logger.info(reqLogSending()+"swapAppointment ("+patientId+", "+oldAppointmentId+", "+oldAppointmentType+", "+newAppointmentId+", "+newAppointmentType+") by "+adminId);
            String result = hospital.swapAppointment(patientId,oldAppointmentId, oldAppointmentType, newAppointmentId, newAppointmentType);
            logger.info(reqLogResponse()+result+"\n");
            System.out.println("Server Response: "+result);
        }catch(Exception e){
            System.out.println("Unable to establish connecting to hospital server");
        }
    }
}
