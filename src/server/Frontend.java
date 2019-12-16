package server;

import hospitalModule.HospitalPOA;

public class Frontend extends HospitalPOA {
    private Sequencer sequencer;
    public Frontend(){
        sequencer = new Sequencer();
    }
    @Override
    public synchronized String addAppointment(String appointmentID, String appointmentType, int capacity) {
        DHMSRequest dhmsRequest = new DHMSRequest();
        dhmsRequest.transactionName="addAppointment";
        dhmsRequest.appointmentId = appointmentID;
        dhmsRequest.appointmentType = appointmentType;
        dhmsRequest.capacity = capacity;
        return sequencer.sendTransaction(dhmsRequest);
    }

    @Override
    public synchronized String removeAppointment(String appointmentID, String appointmentType) {
        DHMSRequest dhmsRequest = new DHMSRequest();
        dhmsRequest.transactionName="removeAppointment";
        dhmsRequest.appointmentId=appointmentID;
        dhmsRequest.appointmentType=appointmentType;
        return sequencer.sendTransaction(dhmsRequest);
    }
    @Override
    public synchronized String listAppointmentAvailability(String appointmentType) {
        DHMSRequest dhmsRequest = new DHMSRequest();
        dhmsRequest.transactionName="listAppointmentAvailability";
        dhmsRequest.appointmentType=appointmentType;
        return sequencer.sendTransaction(dhmsRequest);
    }

    @Override
    public synchronized String bookAppointment(String patientID, String appointmentID, String appointmentType) {
        DHMSRequest dhmsRequest = new DHMSRequest();
        dhmsRequest.transactionName="bookAppointment";
        dhmsRequest.patientId=patientID;
        dhmsRequest.appointmentId=appointmentID;
        dhmsRequest.appointmentType=appointmentType;
        return sequencer.sendTransaction(dhmsRequest);
    }

    @Override
    public synchronized String getAppointmentSchedule(String patientID) {
        DHMSRequest dhmsRequest = new DHMSRequest();
        dhmsRequest.transactionName="getAppointmentSchedule";
        dhmsRequest.patientId=patientID;
        return sequencer.sendTransaction(dhmsRequest);
    }

    @Override
    public synchronized String cancelAppointment(String patientID, String appointmentID) {
        DHMSRequest dhmsRequest = new DHMSRequest();
        dhmsRequest.transactionName="cancelAppointment";
        dhmsRequest.patientId=patientID;
        dhmsRequest.appointmentId=appointmentID;
        return sequencer.sendTransaction(dhmsRequest);
    }

    @Override
    public synchronized String swapAppointment(String patientID, String oldAppointmentID, String oldAppointmentType, String newAppointmentID, String newAppointmentType) {
        DHMSRequest dhmsRequest = new DHMSRequest();
        dhmsRequest.transactionName="swapAppointment";
        dhmsRequest.patientId=patientID;
        dhmsRequest.appointmentId=oldAppointmentID;
        dhmsRequest.appointmentType=oldAppointmentType;
        dhmsRequest.newAppointmentId=newAppointmentID;
        dhmsRequest.newAppointmentType=newAppointmentType;
        return sequencer.sendTransaction(dhmsRequest);
    }
}
