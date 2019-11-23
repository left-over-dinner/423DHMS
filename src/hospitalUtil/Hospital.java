package hospitalUtil;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hospital extends Remote {
    public String addAppointment(String appointmentID, String appointmentType, int capacity) throws RemoteException;
    public String removeAppointment (String appointmentID, String appointmentType)  throws RemoteException;
    public String listAppointmentAvailability(String appointmentType)  throws RemoteException;
    public String bookAppointment(String patientID, String appointmentID, String appointmentType)  throws RemoteException;
    public String getAppointmentSchedule(String patientID)  throws RemoteException;
    public String cancelAppointment(String patientID, String appointmentID)  throws RemoteException;
}
