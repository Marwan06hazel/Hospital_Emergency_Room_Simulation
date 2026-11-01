import java.util.Queue;
import java.util.LinkedList;

public class waitingRoom {
    private Queue<Patient> patients = new LinkedList<>();
    private Semaphore emptySeats;
    private Semaphore availablePatients = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);
    private messageDisplay msgDisplay = new messageDisplay();



    public waitingRoom(int capacity){
        emptySeats = new Semaphore(capacity);
    }

    public void enter(Patient patient){
        try{
            if(!emptySeats.tryAcquire(0)){
                msgDisplay.displayMessage("The waiting room is full.");
                msgDisplay.displayMessage(patient + " is waiting outside for an available seat.");
                emptySeats.acquire();
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }

        try{
            mutex.acquire();
            try {
                patients.offer(patient);
                availablePatients.release();
                msgDisplay.displayMessage( patient + " entered the waiting room.");
            }
            finally{
                mutex.release();
            }
        } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                emptySeats.release();
                return;
        }

    }

    public Patient take(){
        Patient leftPatient = null;
        try{
            availablePatients.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        try{
            mutex.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            availablePatients.release();
            return null;
        }
        try{
            leftPatient = patients.poll();
            emptySeats.release();

        } finally{
            mutex.release();
        }
        
        
        return leftPatient;
    }
  
}
