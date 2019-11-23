package client;

public class ClientUtil {
    public static boolean validClientId(String clientId){
        return clientId.matches("(MTL|SHE|QUE){1}(A|P)[0-9]{4}");
    }
    public static boolean isAdmin(String clientId){
        return clientId.matches("(MTL|SHE|QUE){1}A[0-9]{4}");
    }
    public static boolean isPatient(String clientId){
        return clientId.matches("(MTL|SHE|QUE){1}P[0-9]{4}");
    }
    public static String getHospName(String clientId){
        if(clientId.contains("MTL")) return "MTLHosp";
        if(clientId.contains("QUE")) return "QUEHosp";
        if(clientId.contains("SHE")) return "SHEHosp";
        return "";
    }
    public static int getHospPort(String clientId){
        if(clientId.contains("MTL")) return 2323;
        if(clientId.contains("QUE")) return 2424;
        if(clientId.contains("SHE")) return 2525;
        return 0;
    }
    public static boolean isValidApptId(String appointmentId){
        //return appointmentId.matches("(MTL|SHE|QUE){1}(M|A|E)[0-9]{6}");
        return true;
    }
    public static boolean isValidApptType(String appointmentId){
        return appointmentId.matches("(Surgeon|Dental|Physician)");
    }
    public static String formatEmptyResult(String result){
        if(result.equals("")) return "[Empty]";
        return result;
    }
}
