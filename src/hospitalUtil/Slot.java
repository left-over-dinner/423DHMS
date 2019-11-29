package hospitalUtil;

import java.io.Serializable;

public class Slot implements Serializable {
    private String patientId;
    private int slotId;

    public Slot(int slotId){
        this.slotId=slotId;
        patientId=null;
    }
    public void bookSlot(String patientId){
        this.patientId=patientId;
    }
    public void unBookSlot(){
        patientId=null;
    }
    public String getPatientId(){
        return patientId;
    }
    public boolean isAvailable(){
        return patientId==null;
    }
    public int getSlotId(){
        return slotId;
    }
}
