package hospitalUtil;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hospital {
    public String addAppointment(String appointmentID, String appointmentType, int capacity);
    public String removeAppointment (String appointmentID, String appointmentType);
    public String listAppointmentAvailability(String appointmentType);
    public String bookAppointment(String patientID, String apt, String appointmentID, String appointmentType);
    public String getAppointmentSchedule(String patientID);
    public String cancelAppointment(String patientID, String appointmentID);
    public String swapAppointment (String patientID, String appts, String oldAppointmentID, String oldAppointmentType, String newAppointmentID, String newAppointmentType);
}
