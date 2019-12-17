package server;

public class ENV {
    //a
    //b
    //c
    public static String[] ips = {
            "172.31.113.208",
            "172.31.57.84",
            "172.31.58.68"
    };
    public static String getIpById(int id){
        return ips[id-1];
    }
}
