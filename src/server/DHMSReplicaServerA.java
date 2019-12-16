package server;

public class DHMSReplicaServerA {
    public static void main(String[] args){
        DHMSReplicaSever dhmsReplicaSever = new DHMSReplicaSever(1);
        dhmsReplicaSever.startListeningMulticast();
    }
}
