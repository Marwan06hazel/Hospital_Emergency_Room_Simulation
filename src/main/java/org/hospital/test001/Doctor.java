package org.hospital.test001;

public class Doctor extends Thread {
    private final int doctorId;
    private final waitingRoom waitingRoom;
    private final treatmentRoom treatmentRoom;
    private final Semaphore treatedPatients;

    public Doctor(int id, waitingRoom wt, treatmentRoom tr, Semaphore tp) {
        this.doctorId = id;
        this.waitingRoom = wt;
        this.treatmentRoom = tr;
        this.treatedPatients = tp;
    }

    public int getDoctorId(){
        return doctorId;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            Patient patient = waitingRoom.take();
            if (patient == null) break;

            messageDisplay.doctorAssigned(doctorId, patient.getPatientId());
            messageDisplay.doctorTryingToAcquireRoom(doctorId);

            try {
                if(treatmentRoom.tryAcquireTreatmentRoom(3000)){
                    try {
                        messageDisplay.doctorAcquiredRoom(doctorId);
                        messageDisplay.treatmentStarted(doctorId, patient.getPatientId());
                        Thread.sleep(2000 + (int)(Math.random() * 3000));
                        messageDisplay.treatmentFinished(doctorId, patient.getPatientId());
                    } finally {
                        treatmentRoom.releaseTreatmentRoom();
                        messageDisplay.doctorReleasedRoom(doctorId);
                    }

                    messageDisplay.doctorAvailable(doctorId);
                    messageDisplay.patientLeft(patient.getPatientId());
                    treatedPatients.release();

                } else{
                    messageDisplay.doctorFailedToAcquireRoom(doctorId);
                    messageDisplay.patientReturned(doctorId, patient.getPatientId());
                    waitingRoom.enter(patient);
                    messageDisplay.doctorAvailable(doctorId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
