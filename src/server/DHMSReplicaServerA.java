package server;

public class DHMSReplicaServerA {
    public static void main(String[] args){
        DHMSReplicaSever dhmsReplicaSever = new DHMSReplicaSever(1, 2001);
        dhmsReplicaSever.startListeningMulticast();
    }
}
