package server;

public class ENV {
    //a
    //b
    //c
    public static String[] ips = {
            "192.168.1.13",
            "192.168.1.12",
            "192.168.1.9"
    };
    public static String getIpById(int id){
        return ips[id-1];
    }
}
