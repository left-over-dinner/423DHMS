package hospitalModule;


/**
* hospitalModule/HospitalOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from hospitalModule.idl
* Sunday, October 27, 2019 2:11:42 o'clock PM EDT
*/

public interface HospitalOperations 
{
  String addAppointment (String appointmentID, String appointmentType, int capacity);
  String removeAppointment (String appointmentID, String appointmentType);
  String listAppointmentAvailability (String appointmentType);
  String bookAppointment (String patientID, String appointmentID, String appointmentType);
  String getAppointmentSchedule (String patientID);
  String cancelAppointment (String patientID, String appointmentID);
  String swapAppointment (String patientID, String oldAppointmentID, String oldAppointmentType, String newAppointmentID, String newAppointmentType);
} // interface HospitalOperations
