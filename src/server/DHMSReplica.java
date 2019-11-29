package server;

import hospitalUtil.Hospital;
import hospitalUtil.HospitalServer;
import hospitalUtil.RequestUtil;

import java.io.Serializable;

public class DHMSReplica implements Serializable,DHMSReplicaInterface{
    private HospitalServer MTLHosp;
    private HospitalServer SHEHosp;
    private HospitalServer QUEHosp;

    public DHMSReplica(){
        MTLHosp = new HospitalServer("MTL");
        SHEHosp = new HospitalServer("SHE");
        QUEHosp = new HospitalServer("QUE");
    }
    private HospitalServer determineHospital(String id ){
        if(id.contains("MTL")) return MTLHosp;
        if(id.contains("QUE")) return QUEHosp;
        if(id.contains("SHE")) return SHEHosp;
        return null;
    }
    public String hello(){
        return MTLHosp.getAppointmentScheduleHops("MTLP1234");
    }
    @Override
    public String addAppointment(String appointmentID, String appointmentType, int capacity) {
        return determineHospital(appointmentID).addAppointment(appointmentID,appointmentType,capacity);
    }
    @Override
    public String removeAppointment(String appointmentID, String appointmentType) {
        return determineHospital(appointmentID).removeAppointment(appointmentID,appointmentType);
    }
    @Override
    public String listAppointmentAvailability(String appointmentType) {
        String result = MTLHosp.listAppointmentAvailability(appointmentType);
        result+=SHEHosp.listAppointmentAvailability(appointmentType);
        result+=QUEHosp.listAppointmentAvailability(appointmentType);
        return result;
    }
    @Override
    public String bookAppointment(String patientID, String appointmentID, String appointmentType) {
        String appts = getAllHospScheduleRaw(patientID);
        return determineHospital(patientID).bookAppointment(patientID,appts,appointmentID,appointmentType);
    }
    @Override
    public String getAppointmentSchedule(String patientID) {
        return getAllHospSchedule(patientID);
    }
    @Override
    public String cancelAppointment(String patientID, String appointmentID) {
        return determineHospital(patientID).cancelAppointment(patientID,appointmentID);
    }
    @Override
    public String swapAppointment(String patientID, String oldAppointmentID, String oldAppointmentType, String newAppointmentID, String newAppointmentType) {
        String appts = getAllHospScheduleRaw(patientID);
        return determineHospital(patientID).swapAppointment(patientID,appts,oldAppointmentID,oldAppointmentType,newAppointmentID, newAppointmentType);
    }
    public String getAllHospSchedule(String patientID){
        String result = MTLHosp.getAppointmentSchedule(patientID);
        result += SHEHosp.getAppointmentSchedule(patientID);
        result += QUEHosp.getAppointmentSchedule(patientID);
        return RequestUtil.prettyPrintSchedule(result);

    }
    public String getAllHospScheduleRaw(String patientID){
        String appts = MTLHosp.getAppointmentScheduleHops(patientID);
        appts += SHEHosp.getAppointmentScheduleHops(patientID);
        appts += QUEHosp.getAppointmentScheduleHops(patientID);
        return appts;
    }
    public String processTransaction(DHMSRequest request){
        if(request.transactionName.equals("addAppointment")){
            return addAppointment(request.appointmentId,request.appointmentType,request.capacity);
        }else if(request.transactionName.equals("removeAppointment")){
            return removeAppointment(request.appointmentId, request.appointmentType);
        }else if(request.transactionName.equals("listAppointmentAvailability")){
            return listAppointmentAvailability(request.appointmentType);
        }else if(request.transactionName.equals("bookAppointment")){
            return bookAppointment(request.patientId,request.appointmentId,request.appointmentType);
        }else if(request.transactionName.equals("getAppointmentSchedule")){
            return getAppointmentSchedule(request.patientId);
        }else if(request.transactionName.equals("cancelAppointment")){
            return cancelAppointment(request.patientId,request.appointmentId);
        }else if(request.transactionName.equals("swapAppointment")){
            return swapAppointment(request.patientId,request.appointmentId,request.appointmentType,request.newAppointmentId, request.newAppointmentType);
        }
        return "No matching method found";
    }

}
