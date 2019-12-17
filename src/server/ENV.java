package server;

public class ENV {
    //a
    //b
    //c
    public static String[] ips = {
            "172.20.10.3",
            "172.20.10.11",
            "172.20.10.2"
    };
    public static String getIpById(int id){
        return ips[id-1];
    }
}
