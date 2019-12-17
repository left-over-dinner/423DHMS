package server;

public class ENV {
    //a
    //b
    //c
    public static String[] ips = {
            "192.168.43.127",
            "192.168.43.55",
            "192.168.43.118"
    };
    public static String getIpById(int id){
        return ips[id-1];
    }
}
