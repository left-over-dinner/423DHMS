package hospitalUtil;

import hospitalModule.HospitalPOA;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HospitalServer implements Hospital, Serializable {
    private String id;
    private HashMap<String, HashMap<String,CapacitySlots>> database = new HashMap<String, HashMap<String,CapacitySlots>>();
    private static final Logger logger = Logger.getLogger("DHMS");;

    public HospitalServer(String id) {
        super();
        this.id=id;
        //For a new server, initiate default database
        database.put("Physician", new HashMap<String,CapacitySlots>());
        database.put("Surgeon", new HashMap<String,CapacitySlots>());
        database.put("Dental", new HashMap<String,CapacitySlots>());
    }
    private String logReqHeader(){
        return "REQUEST at "+id+" Hosp.:";
    }
    private String logReqConnecting(){
        return "CONNECTING REQUEST to different hospital(s), from "+id;
    }
    private String logReqComplete(){
        return "COMPLETED REQUEST at "+id+" Hosp. Request RESULT:";
    }
    private String logReqGathering(){
        return "GATHERING INFO at "+id+" Hosp.:";
    }
    private String logReqValidating(){ return "VALIDATING BOOKING with respect to all cities"; }
    private String logReqValid(){return "BOOKING REQUEST valid";}
    private String logReqInvalid(){return "BOOKING REQUEST invalid";}
    private String logReqBookingFound(){return "BOOOKING(S) FOUND for appt.: proceeding to rebook patient(s)";}
    private String logReqRebooked(String patientId, String appointmentId, String appointmentType){return "REBOOKED "+patientId+" for "+appointmentType+" "+appointmentId;}
    private String logReqNoNextBooking(String patientId, String appointmentType){return "NO NEXT APPOINTMENT found for "+patientId+" for a "+appointmentType+" appt.";}
    private boolean isCorrectHospital(String fullId){
        return fullId.contains(id);
    }
    private HashMap<String, CapacitySlots> getTimeSlots(String appointmentType){
        return database.get(appointmentType);
    }
    private CapacitySlots createSlots(int capacity){
        CapacitySlots capacitySlots = new CapacitySlots();
        while(capacity>0) {
            capacitySlots.addSlot();
            capacity--;
        }
        return capacitySlots;
    }
    @Override
    public synchronized String addAppointment(String appointmentID, String appointmentType, int capacity) {
        logger.info(logReqHeader()+" addAppointement("+appointmentID+","+appointmentType+","+capacity+")");
        HashMap<String, CapacitySlots> timeSlots = getTimeSlots(appointmentType);
        if(timeSlots.get(appointmentID)!=null) {
            logger.info(this.logReqComplete()+" false,");
            logger.info("appointment already exists\n");
            return "false, appointment already exists";}
        timeSlots.put(appointmentID, createSlots(capacity));
        logger.info(this.logReqComplete()+" true\n");
        return "true";
    }
    private void rebookPatientNextAvailAppt(String[] bookedPatiend, String removedApptId, String appointmentType){
        HashMap<String, CapacitySlots> timeSlots = getTimeSlots(appointmentType);
        for(String patientId : bookedPatiend){
            String appointmentId = RequestUtil.getNextAvailableAppt(timeSlots, removedApptId);
            if (appointmentId !=  null){
                logger.info(logReqRebooked(patientId,appointmentId,appointmentType));
                bindBookingAppt(patientId,appointmentId,appointmentType);
                continue;
            }
            logger.info(logReqNoNextBooking(patientId,appointmentType));
        }

    }
    @Override
    public synchronized String removeAppointment(String appointmentID, String appointmentType) {
        logger.info(logReqHeader()+" removeAppointment("+appointmentID+","+appointmentType+")");
        HashMap<String, CapacitySlots> timeSlots = getTimeSlots(appointmentType);
        //initially the appointment has never existed
        if (timeSlots.get(appointmentID) == null ) {
            logger.info(this.logReqComplete()+" false,");
            logger.info("no such appointment exists");
            return "false, no such appointment exists";
        }
        //remove appointment
        CapacitySlots removedCapSlots = timeSlots.remove(appointmentID);
        if(removedCapSlots.hasBooking()){
            //move patient to next available appointment
            logger.info(logReqBookingFound());
            String[] bookedPatient = removedCapSlots.getBookedPatients();
            rebookPatientNextAvailAppt(bookedPatient, appointmentID ,appointmentType);
        }
        logger.info(this.logReqComplete()+" true\n");
        return "true";
    }
    public String listAppointmentAvailabilityHosp(String appointmentType) {
        logger.info(logReqGathering()+" listAppointmentAvailability("+appointmentType+")");
        String availabilities = "";
        HashMap<String, CapacitySlots> timeSlots = getTimeSlots(appointmentType);
        for(Map.Entry entry : timeSlots.entrySet()){
            availabilities+=(entry.getKey()+" "+((CapacitySlots)entry.getValue()).availableSlot())+"\n";
        }
        return availabilities;
    }
    @Override
    public synchronized String listAppointmentAvailability(String appointmentType) {
        logger.info(this.logReqHeader()+" listAppointmentAvailability("+appointmentType+")");
        logger.info(this.logReqConnecting());
        String result =listAppointmentAvailabilityHosp(appointmentType);
        logger.info(logReqComplete()+" true\n");
        return result;
    }
    private String[] validBookingRequest(String patientID, String appts,String appointmentID, String appointmentType){
        String[] result = new String[2];
        result[0]="true";
        result[1]="result here";
        result = RequestUtil.validate(appts, patientID,appointmentID,appointmentType);
        //System.out.println(appts);
        return result;
    }
    private String bindBookingAppt(String patientID, String appointmentID, String appointmentType){
        HashMap<String, CapacitySlots> timeSlots = getTimeSlots(appointmentType);
        if(timeSlots.get(appointmentID) == null ){
            logger.info(logReqComplete()+" false,");
            logger.info("appointment does not exists\n");
            return "false, appointment does not exists";
        }
        else if(timeSlots.get(appointmentID).availableSlot()==0 ){
            logger.info(logReqComplete()+" false");
            logger.info("appointment is full\n");
            return "false, appointment is full";
        }
        timeSlots.get(appointmentID).bookSlot(patientID);
        return "true";
    }
    public String bookAppointmentHosp(String patientID, String appointmentID, String appointmentType){
        logger.info(logReqHeader()+" bookAppointment("+patientID+", "+appointmentID+", "+appointmentType+")");
        String result = bindBookingAppt(patientID,appointmentID,appointmentType);
        logger.info(logReqComplete()+" true\n");
        return result;
    }
    @Override
    public synchronized String bookAppointment(String patientID, String appts, String appointmentID, String appointmentType) {
        logger.info(logReqHeader()+" bookAppointment("+patientID+", "+appointmentID+", "+appointmentType+")");
        logger.info(logReqValidating());
        String[] responceValidRequest = validBookingRequest(patientID, appts, appointmentID, appointmentType);
        if(responceValidRequest[0].equals("false")){
            logger.info(logReqInvalid());
            logger.info(logReqComplete()+"false,");
            logger.info(responceValidRequest[1]+"\n");
            return "false, "+responceValidRequest[1];
        }
        logger.info(logReqValid());
        String result = bindBookingAppt(patientID, appointmentID, appointmentType);
        logger.info(logReqComplete()+" true\n");
        return result;
    }
    private String getScheduleForTimeSlots(HashMap<String, CapacitySlots> timeSlots, String patientId){
        String schedule="";
        for(Map.Entry entry : timeSlots.entrySet()){
            if(((CapacitySlots)entry.getValue()).hasBooking(patientId))
                schedule+=entry.getKey()+",";
        }
        return schedule;
    }
    public String getAppointmentScheduleHops(String patientID){
        logger.info(logReqGathering()+" getAppointmentSchedule("+ patientID+")");
        String scheduleOverall="Dental Appointment(s):";
        HashMap<String, CapacitySlots> timeSlotsDental = getTimeSlots("Dental");
        scheduleOverall+=getScheduleForTimeSlots(timeSlotsDental,patientID)+";";
        scheduleOverall+="\nSurgeon Appointment(s):";
        HashMap<String, CapacitySlots> timeSlotsSurgeon = getTimeSlots("Surgeon");
        scheduleOverall+=getScheduleForTimeSlots(timeSlotsSurgeon,patientID)+";";
        scheduleOverall+="\nPhysician Appointment(s):";
        HashMap<String, CapacitySlots> timeSlotsPhysician = getTimeSlots("Physician");
        scheduleOverall+=getScheduleForTimeSlots(timeSlotsPhysician,patientID)+";\n";
        return scheduleOverall;
    }
    @Override
    public synchronized String getAppointmentSchedule(String patientID) {
        logger.info(logReqHeader()+" getAppointmentSchedule("+patientID+")");
        logger.info(logReqConnecting());
        String result = getAppointmentScheduleHops(patientID);
        logger.info(logReqComplete()+" true\n");
        //result  = RequestUtil.prettyPrintSchedule(result);
        return result;

    }
    private boolean cancelAppForTimeSlots(HashMap<String, CapacitySlots> timeSlots, String patientId, String appointmentId){
        boolean isAppointment;
        boolean hasBooking;
        for(Map.Entry entry : timeSlots.entrySet()){
            isAppointment =(entry.getKey()).equals(appointmentId);
            if(isAppointment){
                hasBooking = ((CapacitySlots)entry.getValue()).hasBooking(patientId);
                if(hasBooking){
                    return ((CapacitySlots)entry.getValue()).unBookSlot(patientId);
                }
            }
        }
        return false;
    }
    @Override
    public synchronized String cancelAppointment(String patientID, String appointmentID) {
        logger.info(logReqHeader()+" cancelAppointment("+patientID+","+appointmentID+")");
        boolean result = false;
        HashMap<String, CapacitySlots> timeSlotsDental = getTimeSlots("Dental");
        if (cancelAppForTimeSlots(timeSlotsDental,patientID,appointmentID)) result = true;
        HashMap<String, CapacitySlots> timeSlotsSurgeon = getTimeSlots("Surgeon");
        if (!result && cancelAppForTimeSlots(timeSlotsSurgeon,patientID,appointmentID)) result = true;
        HashMap<String, CapacitySlots> timeSlotsPhysician = getTimeSlots("Physician");
        if (!result && cancelAppForTimeSlots(timeSlotsPhysician,patientID,appointmentID)) result = true;
        if(!result){
            logger.info(logReqComplete()+" false,");
            logger.info("no such appointment booked by patient\n");
            return "false, no such appointment booked by patient";
        }
        logger.info(logReqComplete()+" true\n");
        return "true";
    }
    private boolean apptAlreadyBookedByPatient(String patientID, String appointmentID, String appointmentType){
        String schedule = getAppointmentScheduleHops(patientID);
        System.out.println(schedule);
        return RequestUtil.appointmentAlreadyBooked(schedule,appointmentID,appointmentType);
    }
    public String appointmentAvailableHosp(String appointmentID, String appointmentType){
        HashMap<String, CapacitySlots> timeSlots = getTimeSlots(appointmentType);
        if(timeSlots.get(appointmentID) == null ){
            System.out.println("false, appointment does not exists");
            return "false, appointment does not exists";
        }
        else if(timeSlots.get(appointmentID).availableSlot()==0 ){
            System.out.println("false, appointment is full");
            return "false, appointment is full";
        }
        System.out.println("returning true");
        return "true";
    }
    public boolean appointmentAvailable(String appointmentID, String appointmentType){
        String result = appointmentAvailableHosp(appointmentID,appointmentType);
        if(result.contains("false")) return false;
        return true;
    }
    @Override
    public String swapAppointment (String patientID, String appts, String oldAppointmentID, String oldAppointmentType, String newAppointmentID, String newAppointmentType){
        //check old and new appointment are already booked by patient
        if(!apptAlreadyBookedByPatient(patientID,oldAppointmentID,oldAppointmentType)) return "false, old appointment is not booked by patient";
        //check if new booking is valid (max 3 appointments in different city, no same type of appointment in same day, etc)
        String[] responseValidRequest = validBookingRequest(patientID, appts, newAppointmentID, newAppointmentType);
        if(responseValidRequest[0].equals("false")) return "false, "+responseValidRequest[1];
        //check new booking appointment exists and not full
        if(!appointmentAvailable(newAppointmentID,newAppointmentType)) return "false, new appointment does not exists or is already full";
        //cancel appointment
        String cancelRes = cancelAppointment(patientID,oldAppointmentID);
        if(cancelRes.contains("false")) return cancelRes;
        //book new appointment
        String bookingRes = bookAppointment(patientID, appts, newAppointmentID,newAppointmentType);
        if(bookingRes.contains("false")){
            //rebook appointment
            bookAppointment(patientID, appts, oldAppointmentID,oldAppointmentType);
            return "false, unable to book new appt. after cancelling old appt. Rebooked old appt.";
        }
        return "true";
    }
}
