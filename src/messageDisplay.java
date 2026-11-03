public class messageDisplay {

    // === Patient Messages ===

    public static synchronized void patientsArriving(int numPatients){

        System.out.println("Patients arriving:");

        for (int i = 1; i <= numPatients; i++) {
            System.out.print("P" + i + " ");
        }

        System.out.println();
    }

    public static synchronized void patientArrived(int patientId) {
        System.out.println("P" + patientId + " arrived.");
    }

    public static synchronized void patientWaitingOutside(int patientId) {
        System.out.println("P" + patientId +
                " is waiting outside for an available seat.");
    }

    public static synchronized void patientEntered(int patientId) {
        System.out.println("P" + patientId + " entered the waiting room.");
    }

    public static synchronized void patientLeft(int patientId) {
        System.out.println("P" + patientId + " left the hospital.");
    }

    public static synchronized void allPatientsTreated() {
        System.out.println("All patients have been treated.");
    }

    //  === Waiting Room Messages ===

    public static synchronized void waitingRoomFull() {
        System.out.println("The waiting room is full.");
    }

    public static synchronized void patientReturned(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId +
                " is sending P" + patientId + " back to the waiting room.");
    }

    //  === Doctor Messages ===

    public static synchronized void doctorAssigned(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId +
                " is assigned to Patient " + patientId + ".");
    }

    public static synchronized void doctorTryingToAcquireRoom(int doctorId) {
        System.out.println("Doctor " + doctorId +
                " is trying to acquire a treatment room.");
    }

    public static synchronized void doctorAcquiredRoom(int doctorId) {
        System.out.println("Doctor " + doctorId +
                " acquired a treatment room.");
    }

    public static synchronized void doctorFailedToAcquireRoom(int doctorId) {
        System.out.println("Doctor " + doctorId +
                " could not acquire a treatment room within 3 seconds.");
    }
    
    public static synchronized void doctorReleasedRoom(int doctorId) {
        System.out.println("Doctor " + doctorId + 
                " released the treatment room.");
    }
                           
    public static synchronized void treatmentStarted(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId +
                " started treating Patient " + patientId + ".");
    }

    public static synchronized void treatmentFinished(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId +
                " finished treating Patient " + patientId + ".");
    }
    
    public static synchronized void doctorAvailable(int doctorId) {
         System.out.println("Doctor " + doctorId + 
                " is now available.");
    }

    //  === Simulation Messages ===

    public static synchronized void simulationCompleted() {
        System.out.println("Simulation completed.");
    }
}
