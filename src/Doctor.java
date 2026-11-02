public class Doctor extends Thread {
    private final int doctorId;
    private final waitingRoom waitingRoom;
    private final treatmentRoom treatmentRoom;

    public Doctor(int id, waitingRoom wt, treatmentRoom tr){
        this.doctorId = id;
        this.waitingRoom = wt;
        this.treatmentRoom = tr;
    }

    public int getDoctorId(){
        return doctorId;
    }

    @Override 
    public void run() {
        while (!isInterrupted()) {
            // try to take in a patient
            Patient patient = waitingRoom.take();
            if (patient == null) break;

            // assigning a patient to the doctor if available and trying to get a treatment room
            messageDisplay.doctorAssigned(doctorId, patient.getPatientId());
            messageDisplay.doctorTryingToAcquireRoom(doctorId);

            try {
                if(treatmentRoom.tryAcquireTreatmentRoom(3000)){ //if the doctor could get a treatment room,
                    try {
                        //treat the patient
                        messageDisplay.doctorAcquiredRoom(doctorId);
                        messageDisplay.treatmentStarted(doctorId, patient.getPatientId());
                        Thread.sleep(2000 + (int)(Math.random() * 3000));
                        messageDisplay.treatmentFinished(doctorId, patient.getPatientId());
                    } finally {
                        // release the patient after treatment, and if the doctor got interrupted while sleeping, the patient get released normally
                        treatmentRoom.releaseTreatmentRoom();
                        messageDisplay.doctorReleasedRoom(doctorId);
                    }
                    
                    // announce the doctor availability, and discharging the patient
                    messageDisplay.doctorAvailable(doctorId);
                    messageDisplay.patientLeft(patient.getPatientId());

                } else{// if the doctor couldn't get a treatment room
                    // sending the patient back to the waiting room
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
