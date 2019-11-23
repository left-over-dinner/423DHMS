package server;

import hospitalUtil.HospitalServer;

public class UDPProcessor {
    private HospitalServer hospital;
    public UDPProcessor(HospitalServer hospital){
           this.hospital=hospital;
    }
    public UDPProcessor(){}
    public void setHospital(HospitalServer hospital){
        this.hospital=hospital;
    }
    public String decodeHospitalMessage(String message){
        String result = "";
        String[] args = message.split("@");
        switch(args[0]){
            case "addAppointment":
                result = addAppointment(args);
                break;
            case "removeAppointment":
                result = removeAppointment(args);
                break;
            case "listAppointmentAvailability":
                result = listAppointmentAvailability(args);
                break;
            case "bookAppointment":
                result = bookAppointment(args);
                break;
            case "getAppointmentSchedule":
                result = getAppointmentSchedule(args);
                break;
            case "cancelAppointment":
                result = cancelAppointment(args);
                break;
            case "appointmentAvailable":
                result = appointmentAvailable(args);
        }
        return result;
    }
    public HospitalServer determineHospServer(String id){
        /*if(id.contains("MTL")) return MTLHosp;
        if(id.contains("SHE")) return SHEHosp;
        if(id.contains("QUE")) return QUEHosp;
        */
        return hospital;
    }
    public String addAppointment(String[] args){
        System.out.println("Adding");
        HospitalServer hospServer = determineHospServer(args[1]);
        if(hospServer==null) System.out.println("NULL");
        return hospServer.addAppointment(args[1],args[2],Integer.valueOf(args[3]));
    }
    public String removeAppointment(String[] args){
        System.out.println("Removing");
        HospitalServer hospServer = determineHospServer(args[1]);;
        if(hospServer==null) System.out.println("NULL");
        return hospServer.removeAppointment(args[1],args[2]);
    }
    public String listAppointmentAvailability(String[] args){
        System.out.println("listing app. avail.");
        String result="";
        result += hospital.listAppointmentAvailabilityHosp(args[1]);
        return result;
    }
    public String bookAppointment(String[] args){
        System.out.println("booking");
        HospitalServer hospServer = determineHospServer(args[2]);;
        if(hospServer==null) System.out.println("NULL");
        return hospServer.bookAppointmentHosp(args[1],args[2],args[3]);
    }
    public String getAppointmentSchedule(String[] args){
        System.out.println("appointment sche.");
        String result="";
        result += hospital.getAppointmentScheduleHops(args[1]);
        return result;
    }
    public String cancelAppointment(String[] args){
        System.out.println("cancel app.");
        HospitalServer hospServer = determineHospServer(args[2]);
        if(hospServer==null) System.out.println("NULL");
        return hospServer.cancelAppointment(args[1],args[2]);
    }
    public String appointmentAvailable(String[] args){
        System.out.println("appt. available");
        System.out.println(args[1]);
        System.out.println(args[2]);
        HospitalServer hospServer = determineHospServer(args[2]);
        if(hospServer==null) System.out.println("NULL");
        return hospServer.appointmentAvailableHosp(args[1],args[2]);
    }
}
