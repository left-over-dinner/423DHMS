package server;

public class DHMSReplicaServerB {
    public static void main(String[] args){
        DHMSReplicaSever dhmsReplicaSever = new DHMSReplicaSever(2,2002);
        dhmsReplicaSever.startListeningMulticast();
    }
}
