package org.hospital.test001;

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
        messageDisplay.patientArrived(patientId);
        waitingRoom.enter(this);
    }
}
