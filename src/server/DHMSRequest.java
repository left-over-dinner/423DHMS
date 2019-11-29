package server;

import java.io.*;
import java.util.Comparator;

public class DHMSRequest implements Serializable {
    public boolean simulateFailure=false;
    public int replyPort;
    public boolean isSequenceRequest = false;
    public int transactionId;
    public String transactionName;
    public String patientId;
    public String adminId;
    public String appointmentId;
    public String appointmentType;
    public int capacity;
    public String newAppointmentId;
    public String newAppointmentType;
    public int RMId;
    public String message;
    public boolean isFailure = false;
    public DHMSRequest(){}
    public DHMSRequest(int replyPort){
        this.replyPort = replyPort;
        this.isSequenceRequest=true;
        this.transactionId=-1;
        this.transactionName=null;
        this.patientId=null;
        this.adminId = null;
        this.appointmentId = null;
        this.appointmentType = null;
        this.capacity = -1;
        this.newAppointmentId = null;
        this.newAppointmentType = null;
        this.isFailure=false;
    }
    public DHMSRequest(int RMid, String message){
        this.RMId=RMid;
        this.message=message;
        this.replyPort = -1;
        this.isSequenceRequest=false;
        this.transactionId=-1;
        this.transactionName=null;
        this.patientId=null;
        this.adminId = null;
        this.appointmentId = null;
        this.appointmentType = null;
        this.capacity = -1;
        this.newAppointmentId = null;
        this.newAppointmentType = null;
        this.isFailure=isFailure=false;
    }
    public DHMSRequest(int RMid, boolean isFailure){
        this.RMId=RMid;
        this.message=null;
        this.isFailure=isFailure;
        this.replyPort = -1;
        this.isSequenceRequest=false;
        this.transactionId=-1;
        this.transactionName=null;
        this.patientId=null;
        this.adminId = null;
        this.appointmentId = null;
        this.appointmentType = null;
        this.capacity = -1;
        this.newAppointmentId = null;
        this.newAppointmentType = null;
    }
    public DHMSRequest(int replyPort, int transactionId){
        this.replyPort = replyPort;
        this.isSequenceRequest=true;
        this.transactionId=transactionId;
        this.transactionName=null;
        this.patientId=null;
        this.adminId = null;
        this.appointmentId = null;
        this.appointmentType = null;
        this.capacity = -1;
        this.newAppointmentId = null;
        this.newAppointmentType = null;
        this.isFailure=false;
    }

    public DHMSRequest(int replyPort, int transactionId,String transactionName,String patientId, String adminId, String appointmentId, String appointmentType, int capacity, String newAppointmentId, String newAppointmentType){
        this.replyPort=replyPort;
        this.isSequenceRequest=false;
        this.transactionId=transactionId;
        this.transactionName=transactionName;
        this.patientId=patientId;
        this.adminId = adminId;
        this.appointmentId = appointmentId;
        this.appointmentType = appointmentType;
        this.capacity = capacity;
        this.newAppointmentId = newAppointmentId;
        this.newAppointmentType = newAppointmentType;
        this.isFailure=false;
    }
    public static DHMSRequest decodeStreamAsDHMSRequest(byte[] buffer_in){
        try{
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(buffer_in);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
            DHMSRequest result = (DHMSRequest) objectInputStream.readObject();
            byteInputStream.close();
            objectInputStream.close();
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] encodeStreamAsDHMRequest(DHMSRequest dhmsResponse){
        try{
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject( dhmsResponse );
            byte[] result = byteOutputStream.toByteArray();
            objectOutputStream.close();
            byteOutputStream.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
