package hospitalUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RequestUtil {
    private static Calendar cal = Calendar.getInstance();
    private static DateFormat df = new SimpleDateFormat("ddMMyy");
    public static String[] validate(String appts, String patientID, String appointmentID, String appointmentType){
        String[] result = new String[2];
        result[0]="true";
        result[1]="";
        HashMap<String, String[]> apptsByType = groupByType(appts);
        if(hasAlreadyThreeApptPerWeek(apptsByType,patientID,appointmentID)){
            result[0]="false";
            result[1]="3 other appointments at other city(ies) already booked";
        }
        else if(patientAlreadyBookedAppt(apptsByType.get(appointmentType), appointmentID)){
            result[0]="false";
            result[1]="the appointment is already booked by patient";
        }
        else if(sameDayApptAlreadyExist(apptsByType.get(appointmentType),appointmentID)){
            result[0]="false";
            result[1]="similar appointment already booked by patient in same day";
        }
        return result;
    }
    public static boolean appointmentAlreadyBooked(String appts, String appointmentID, String appointmentType){
        HashMap<String, String[]> apptsByType = groupByType(appts);
        return patientAlreadyBookedAppt(apptsByType.get(appointmentType),appointmentID);
    }
    public static String getNextAvailableAppt(HashMap<String, CapacitySlots> timeSlots, String removedApptId){
        String appointmentId;
        for(Map.Entry el : timeSlots.entrySet()){
            appointmentId = (String)el.getKey();
            if((getDate(appointmentId)).compareTo((getDate(removedApptId)))>0){
                return appointmentId;
            }
        }
        return null;
    }
    private static boolean sameDayApptAlreadyExist(String[] appts, String appointmentID){
        Date apptDate = getDate(appointmentID);
        for(String el: appts){
            if((getDate(el)).compareTo(apptDate)==0) return true;
        }
        return false;
    }
    private static Date getDate(String appt){
        String date = appt.substring(4);
        try{
            return df.parse(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static boolean patientAlreadyBookedAppt(String[] appts, String appointmentID){
        for (String el:appts){
            if(el.equals(appointmentID)) return true;
        }
        return false;
    }
    private static boolean hasAlreadyThreeApptPerWeek(HashMap<String, String[]> apptsByType, String patientID, String appointmentID){
        int count = 0;
        int apptWeekNumber = getAppWeekNumber(appointmentID);
        String patientHospId = patientID.substring(0,3);
        count += countApptInSameWeek(apptsByType.get("Surgeon"), patientHospId, apptWeekNumber);
        if(count > 2) return true;
        count += countApptInSameWeek(apptsByType.get("Dental"), patientHospId, apptWeekNumber);
        if(count > 2) return true;
        count += countApptInSameWeek(apptsByType.get("Physician"), patientHospId, apptWeekNumber);
        if(count > 2) return true;
        //System.out.println(count);
        return false;
    }
    private static int countApptInSameWeek(String[] appts, String patientHospId, int weekNumber){
        int count = 0;
        for(String el :appts){
            if (getAppWeekNumber(el) == weekNumber && !el.contains(patientHospId)) count++;
        }
        return count;
    }
    private static int getAppWeekNumber(String appt){
        String date = appt.substring(4);
        try{
            cal.setTime(df.parse(date));
            return cal.get(Calendar.WEEK_OF_YEAR);
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;

    }
    public static HashMap<String, String[]> groupByType(String appts){
        HashMap<String, String[]> groupedByType = new HashMap<>();
        groupedByType.put("Surgeon",groupBy(appts, "Surgeon"));
        groupedByType.put("Dental",groupBy(appts, "Dental"));
        groupedByType.put("Physician",groupBy(appts, "Physician"));
        return groupedByType;
    }
    public static String prettyPrintSchedule(String appts){
        HashMap<String, String[]> apptsByType = groupByType(appts);
        return printTypeInOneLine(apptsByType);
    }
    public static String[] groupBy(String appts, String type){
        String[] separation = appts.split(";");
        ArrayList<String> groupAppt= new ArrayList<>();
        String[] temp;
        for (String el : separation){
            if(el.contains(type)){
                temp = el.split(":");
                if(temp.length>1){
                    temp = temp[1].split(",");
                    for(String elDental:temp){
                        groupAppt.add(elDental);
                    }
                }
            }
        }
        return groupAppt.toArray(new String[groupAppt.size()]);
    }
    public static String printTypeInOneLine(HashMap<String, String[]> appts){
        String result ="";
        result+= "Surgeon Appointment(s): "+Arrays.toString(appts.get("Surgeon"))+"\n";
        result+= "Dental Appointment(s): "+Arrays.toString(appts.get("Dental"))+"\n";
        result+= "Physician Appointment(s): "+Arrays.toString(appts.get("Physician"))+"\n";
        return result;
    }
}
