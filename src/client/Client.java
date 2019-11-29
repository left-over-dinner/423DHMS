package client;

import hospitalModule.Hospital;
import hospitalModule.HospitalHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import server.DHMSLogFormatter;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class Client {

    public Hospital createIDLHospitalConn(String clientId){
        try{
            ORB orb = ORB.init(new String[1], null);
            //-ORBInitialPort 1050 -ORBInitialHost localhost
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            return (hospitalModule.Hospital) HospitalHelper.narrow(ncRef.resolve_str("DHMSFrontend"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Logger getLogger(String clientId){
        Logger logger;
        FileHandler fh;
        try{
            logger = Logger.getLogger("DHMS");
            fh = new FileHandler("logs/client/"+clientId+"Log.txt");
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            fh.setFormatter(new DHMSLogFormatter());
            logger.info("HELLO");
            return logger;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String reqLogSending(){
        return "SENDING REQUEST: ";
    }
    public String reqLogResponse(){
        return "SERVER RESULT RESPONSE: ";
    }
    public void prettyPrintSchedule(Logger logger, String msg){
        if(msg.equals("")){
            logger.info("[Empty]\n");
            return;
        }
        String[] lines = msg.split("\n");
        for(int i =0; i<lines.length-1;i++){
            logger.info(lines[i]);
        }
        logger.info(lines[lines.length-1]+"\n");
    }
    public int getNumber(Scanner scanner, String msg, int maxNumber){
        int number;
        do {
            System.out.print(msg);
            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.print(msg);
            }
            number = scanner.nextInt();
        } while (number < 0 || number > maxNumber);
        return number;
    }
    public String getAppointmentId(Scanner scanner, String msg){
        String appointmentId=null;
        do {
            System.out.print(msg);
            appointmentId = scanner.next();
        } while (!ClientUtil.isValidApptId(appointmentId));
        return appointmentId;
    }
    public String getPatientId(Scanner scanner, String msg){
        String appointmentId=null;
        System.out.print(msg);
        appointmentId = scanner.next();
        return appointmentId;
    }
    public String getAppointmentType(Scanner scanner, String msg){
        String appointmentType=null;
        do {
            System.out.print(msg);
            appointmentType = scanner.next();
        } while (!ClientUtil.isValidApptType(appointmentType));
        return appointmentType;
    }
}
