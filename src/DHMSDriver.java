import client.AdminClient;
import client.ClientUtil;
import client.PatientClient;
import hospitalUtil.Hospital;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class DHMSDriver {
    public static void main(String[] args){
        //generate sample appointments for for following two weeks
        //createSampleAppointments();
        runDHMS();

    }
    private static void runDHMS(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to DHMS");
        String clientId = login();
        if(ClientUtil.isAdmin(clientId)){
            AdminClient  admin = new AdminClient(clientId);
            admin.startMenu();
        }else{
            PatientClient  patient = new PatientClient(clientId);
            patient.startMenu();
        }
        System.out.println("Exit Hospital? (y/n)?");
        String ans= scanner.next();
        if(!ans.equals("y")){
            runDHMS();
        }
        return;
    }
    private static String login(){
        Scanner scanner = new Scanner(System.in);
        boolean validLogin = true;
        String clientId;
        do{
            System.out.print("Please login by entering your ID: ");
            clientId= scanner.next();
            validLogin = ClientUtil.validClientId(clientId);
            if(!validLogin) {System.out.println("Invalid id, please try again");}
        }while(!validLogin);
        return clientId;
    }
    private static void createSampleAppointments(){
        Hospital hospital;
        try{
            Registry registry = LocateRegistry.getRegistry(2323);
            hospital = (Hospital) registry.lookup("MTLHosp");
            //week of 13-19 october
            hospital.addAppointment("SHEM141019","Surgeon",2);
            hospital.addAppointment("QUEA151019","Dental",2);
            hospital.addAppointment("QUEA161019","Dental",2);
            hospital.addAppointment("QUEE181019","Physician",2);
            hospital.addAppointment("QUEM191019","Dental",2);
            //week of 20 - 26
            hospital.addAppointment("MTLA211019","Surgeon",1);
            hospital.addAppointment("SHEE211019","Surgeon",1);
            hospital.addAppointment("SHEM221019","Dental",2);
            hospital.addAppointment("QUEM231019","Physician",5);
            hospital.addAppointment("MTLE241019","Dental",2);
            hospital.addAppointment("SHEA251019","Surgeon",2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
