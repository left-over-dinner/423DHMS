package server;

public class DHMSReplicaServerC {
    public static void main(String[] args){
        DHMSReplicaSever dhmsReplicaSever = new DHMSReplicaSever(3);
        dhmsReplicaSever.startListeningMulticast();
    }
}
