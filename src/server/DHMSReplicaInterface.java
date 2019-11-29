package server;

public interface DHMSReplicaInterface {
    public String hello();
    public String addAppointment(String appointmentID, String appointmentType, int capacity);
    public String removeAppointment (String appointmentID, String appointmentType);
    public String listAppointmentAvailability(String appointmentType);
    public String bookAppointment(String patientID, String appointmentID, String appointmentType);
    public String getAppointmentSchedule(String patientID);
    public String cancelAppointment(String patientID, String appointmentID);
    public String swapAppointment (String patientID, String oldAppointmentID, String oldAppointmentType, String newAppointmentID, String newAppointmentType);
}
