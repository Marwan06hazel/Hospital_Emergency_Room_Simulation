package org.hospital.test001;

import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private static final int TREATMENT_ROOMS = 2;
    private Thread simulationThread;

    public void runSimulation(int capacity, int numDoctors, int numPatients, Runnable onComplete) {
        simulationThread = new Thread(() -> {
            try {
                SimulationState.reset(numDoctors, numPatients);

                waitingRoom waitingRoom = new waitingRoom(capacity);
                treatmentRoom treatmentRoom = new treatmentRoom(TREATMENT_ROOMS);
                Semaphore treatedPatients = new Semaphore(0);

                List<Doctor> doctors = new ArrayList<>();
                for (int i = 1; i <= numDoctors; i++) {
                    Doctor doctor = new Doctor(i, waitingRoom, treatmentRoom, treatedPatients);
                    doctors.add(doctor);
                    doctor.start();
                }

                messageDisplay.patientsArriving(numPatients);
                List<Patient> patients = new ArrayList<>();
                for (int i = 1; i <= numPatients; i++) {
                    Patient patient = new Patient(i, waitingRoom);
                    patients.add(patient);
                    patient.start();
                    Thread.sleep(1000 + (int) (Math.random() * 400));
                }

                for (int i = 0; i < numPatients; i++) {
                    treatedPatients.acquire();
                }

                for (Patient p : patients) p.join();
                for (Doctor d : doctors) d.interrupt();
                for (Doctor d : doctors) d.join();

                messageDisplay.allPatientsTreated();
                messageDisplay.simulationCompleted();

                if (onComplete != null) onComplete.run();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        simulationThread.setDaemon(true);
        simulationThread.start();
    }
}
