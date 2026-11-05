package org.hospital.test001;

import javafx.application.Platform;

public class messageDisplay {

    public static synchronized void patientsArriving(int numPatients){
        System.out.println("Patients arriving: " + numPatients);
        SimulationState.addLog("Patients arriving", numPatients + " total patients arriving at facility", "INFO");
    }

    public static synchronized void patientArrived(int patientId) {
        System.out.println("P" + patientId + " arrived.");
        Platform.runLater(() -> SimulationState.patientsArrived.set(SimulationState.patientsArrived.get() + 1));
        SimulationState.updatePatient(patientId, "Registration complete", "Arrived");
        SimulationState.addLog("P" + String.format("%02d", patientId) + " arrived at hospital", "Registration complete", "INFO");
    }

    public static synchronized void patientWaitingOutside(int patientId) {
        System.out.println("P" + patientId + " is waiting outside for an available seat.");
        Platform.runLater(() -> {
            if (SimulationState.patientsWaiting.get() > 0) {
                SimulationState.patientsWaiting.set(SimulationState.patientsWaiting.get() - 1);
            }
            SimulationState.patientsOutside.set(SimulationState.patientsOutside.get() + 1);
        });
        SimulationState.updatePatient(patientId, "Waiting room full · Standing outside", "Outside");
        SimulationState.addLog("P" + String.format("%02d", patientId) + " waiting outside", "No seats available in waiting room", "WARN");
    }

    public static synchronized void patientEntered(int patientId) {
        System.out.println("P" + patientId + " entered the waiting room.");
        Platform.runLater(() -> {
            if (SimulationState.patientsOutside.get() > 0) {
                SimulationState.patientsOutside.set(SimulationState.patientsOutside.get() - 1);
            }
            SimulationState.patientsWaiting.set(SimulationState.patientsWaiting.get() + 1);
        });
        SimulationState.updatePatient(patientId, "Seat assigned in waiting room", "Waiting");
        SimulationState.addLog("P" + String.format("%02d", patientId) + " entered waiting room", "Seat assigned", "INFO");
    }

    public static synchronized void doctorAssigned(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId + " is assigned to Patient " + patientId + ".");
        Platform.runLater(() -> {
            if (SimulationState.patientsWaiting.get() > 0) {
                SimulationState.patientsWaiting.set(SimulationState.patientsWaiting.get() - 1);
            }
        });
        SimulationState.updatePatient(patientId, "With Dr. " + doctorId + " · Seeking room", "Evaluating");
        SimulationState.addLog("Doctor " + doctorId + " assigned to P" + String.format("%02d", patientId), "Taken from waiting room, seeking treatment room", "INFO");
    }

    public static synchronized void doctorTryingToAcquireRoom(int doctorId) {
        System.out.println("Doctor " + doctorId + " is trying to acquire a treatment room.");
        SimulationState.addLog("Doctor " + doctorId + " seeking room", "Requesting treatment room access", "INFO");
        Platform.runLater(() -> {
            SimulationState.availableDoctors.set(SimulationState.availableDoctors.get() - 1);
        });
    }

    public static synchronized void doctorAcquiredRoom(int doctorId) {
        System.out.println("Doctor " + doctorId + " acquired a treatment room.");
        Platform.runLater(() -> SimulationState.treatmentRoomsUsed.set(SimulationState.treatmentRoomsUsed.get() + 1));
        SimulationState.addLog("Doctor " + doctorId + " acquired treatment room", "Room acquired successfully", "INFO");
    }

    public static synchronized void doctorFailedToAcquireRoom(int doctorId) {
        System.out.println("Doctor " + doctorId + " could not acquire a treatment room within 3 seconds.");
        SimulationState.addLog("Doctor " + doctorId + " failed room acquisition", "Timeout 3s reached", "WARN");
        Platform.runLater(() -> {
            SimulationState.availableDoctors.set(SimulationState.availableDoctors.get() + 1);
        });
    }

    public static synchronized void patientReturned(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId + " is sending P" + patientId + " back to the waiting room.");
        SimulationState.updatePatient(patientId, "Dr. " + doctorId + " · Room unavailable", "Returned");
        SimulationState.addLog("Doctor " + doctorId + " returned P" + String.format("%02d", patientId), "No rooms free. Returning to waiting room / outside.", "WARN");

    }

    public static synchronized void treatmentStarted(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId + " started treating Patient " + patientId + ".");
        Platform.runLater(() -> {
            SimulationState.patientsInTreatment.set(SimulationState.patientsInTreatment.get() + 1);
//            SimulationState.availableDoctors.set(SimulationState.availableDoctors.get() - 1);
        });
        SimulationState.updatePatient(patientId, "Dr. " + doctorId + " · In Treatment", "In treatment");
        SimulationState.addLog("Doctor " + doctorId + " started treating P" + String.format("%02d", patientId), "Treatment active in room", "INFO");
    }

    public static synchronized void doctorReleasedRoom(int doctorId) {
        System.out.println("Doctor " + doctorId + " released the treatment room.");
        Platform.runLater(() -> {
            SimulationState.treatmentRoomsUsed.set(SimulationState.treatmentRoomsUsed.get() - 1);
            SimulationState.availableDoctors.set(SimulationState.availableDoctors.get() + 1);
        });
        SimulationState.addLog("Doctor " + doctorId + " released room", "Treatment room free", "INFO");
    }

    public static synchronized void treatmentFinished(int doctorId, int patientId) {
        System.out.println("Doctor " + doctorId + " finished treating Patient " + patientId + ".");
        Platform.runLater(() -> {
            if (SimulationState.patientsInTreatment.get() > 0) {
                SimulationState.patientsInTreatment.set(SimulationState.patientsInTreatment.get() - 1);
            }
            SimulationState.patientsCompleted.set(SimulationState.patientsCompleted.get() + 1);
            if (SimulationState.totalPatients.get() > 0) {
                SimulationState.throughputProgress.set((double) SimulationState.patientsCompleted.get() / SimulationState.totalPatients.get());
            }
        });
        SimulationState.updatePatient(patientId, "Dr. " + doctorId + " · Discharged", "Completed");
        SimulationState.addLog("P" + String.format("%02d", patientId) + " treatment complete", "Doctor " + doctorId + " finished treatment", "SUCCESS");
    }

    public static synchronized void doctorAvailable(int doctorId) {
        System.out.println("Doctor " + doctorId + " is now available.");
        SimulationState.addLog("Doctor " + doctorId + " is available", "Ready for next patient", "INFO");
    }

    public static synchronized void patientLeft(int patientId) {
        System.out.println("P" + patientId + " left the hospital.");
        SimulationState.addLog("P" + String.format("%02d", patientId) + " left hospital", "Patient discharged", "SUCCESS");
    }

    public static synchronized void waitingRoomFull() {
        System.out.println("The waiting room is full.");
        SimulationState.addLog("Waiting room full", "Seats occupied at capacity limit", "WARN");
    }

    public static synchronized void allPatientsTreated() {
        System.out.println("All patients have been treated.");
        SimulationState.addLog("All patients treated", "Pipeline cleared", "SUCCESS");
    }

    public static synchronized void simulationCompleted() {
        System.out.println("Simulation completed.");
        SimulationState.addLog("Simulation completed", "System idle", "SUCCESS");
    }
}