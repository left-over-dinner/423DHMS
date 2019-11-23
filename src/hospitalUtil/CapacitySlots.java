package hospitalUtil;

import java.util.ArrayList;
import java.util.List;

public class CapacitySlots {
    private int latestId;
    private List<Slot> slots = new ArrayList<Slot>();

    public CapacitySlots(){
        this.latestId=1;
    }
    public void addSlot(){
        slots.add(new Slot(latestId++));
    }
    public boolean removeSlots(){
        for(int i=0; i<slots.size();i++){
            if(slots.get(i).isAvailable()){
                return false;
            }
        }
        slots = new ArrayList<Slot>();
        return true;
    }
    public int availableSlot(){
        int  availability = 0;
        for(int i=0; i<slots.size();i++){
            if(slots.get(i).isAvailable()){
                availability++;
            }
        }
        return availability;
    }
    public String[] getBookedPatients(){
        ArrayList<String> bookedPatient = new ArrayList<String>();
        for(int i=0; i<slots.size();i++){
            if(!slots.get(i).isAvailable()){
                bookedPatient.add(slots.get(i).getPatientId());
            }
        }
        return bookedPatient.toArray(new String[bookedPatient.size()]);
    }
    public boolean hasBooking(){
        for(int i=0; i<slots.size();i++){
            if(!slots.get(i).isAvailable()){
                return true;
            }
        }
        return false;
    }
    public boolean hasBooking(String patientId){
        for(int i=0; i<slots.size();i++){
            if(slots.get(i).getPatientId()!=null && (slots.get(i).getPatientId()).equals(patientId)){
                return true;
            }
        }
        return false;
    }
    public boolean bookSlot(String patientId){
        Slot slot;
        for(int i=0; i<slots.size();i++){
            slot = slots.get(i);
            if(slot.isAvailable()){
                slot.bookSlot(patientId);
                return false;
            }
        }
        return false;
    }
    public boolean unBookSlot(String patientId){
        Slot slot;
        for(int i=0; i<slots.size();i++){
            slot = slots.get(i);
            if((slot.getPatientId()).equals(patientId)){
                slot.unBookSlot();
                return true;
            }
        }
        return false;
    }
}
