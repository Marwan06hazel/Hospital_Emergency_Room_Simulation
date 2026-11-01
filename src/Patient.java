public class Patient extends Thread {

    private int patientId;
    private waitingRoom waitingRoom;

    public Patient(int patientId, waitingRoom waitingRoom) {
        this.patientId = patientId;
        this.waitingRoom = waitingRoom;
    }

     public int getPatientId() {
        return patientId;
    }

    @Override
    public void run() {

        // Patient arrives at the hospital
        messageDisplay.patientArrived(patientId);

        // Patient tries to enter the waiting room
        waitingRoom.enter(this);
    }
}