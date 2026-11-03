import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hospital {

    private static final int TREATMENT_ROOMS = 2;

    public static void main(String[] args) throws InterruptedException {
        Scanner in  = new Scanner(System.in);
        int capacity = readPositiveInt(in, "Waiting room capacity: ");
        int numDoctors = readPositiveInt(in, "Number of doctors: ");
        int numPatients = readPositiveInt(in, "Number of patients: ");
        in.close();

        // Create rooms.
        waitingRoom waitingRoom = new waitingRoom(capacity);
        treatmentRoom treatmentRoom = new treatmentRoom(TREATMENT_ROOMS);

        // Released by a doctor when a patient has been treated.
        // The hospital acquires it numPatients times to know when everyone is treated and left.
        Semaphore treatedPatients = new Semaphore(0);

        // Create doctors
        List<Doctor> doctors = new ArrayList<>();
        for (int i = 1; i <= numDoctors; i++) {
            Doctor doctor = new Doctor(i, waitingRoom, treatmentRoom, treatedPatients);
            doctors.add(doctor);
            doctor.start();
        }

        messageDisplay.patientsArriving(numPatients);

        // Create patients.
        List<Patient> patients = new ArrayList<>();
        for (int i = 1; i <= numPatients; i++) {
            Patient patient = new Patient(i, waitingRoom);
            patients.add(patient);
            patient.start();
            Thread.sleep(100);
        }

        // Wait until every patient has been treated and left.
        for (int i = 0; i < numPatients; i++) {
            treatedPatients.acquire();
        }
        for (Patient p : patients) p.join();

        // All doctors are now idle, waiting for patients that won't come: interrupt them to stop.
        for (Doctor d : doctors) d.interrupt();
        for (Doctor d : doctors) d.join();

        messageDisplay.allPatientsTreated();
        messageDisplay.simulationCompleted();
    }

    private static int readPositiveInt(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            // Make sure it's a positive whole number.
            if (in.hasNextInt()) {
                int value = in.nextInt();
                if (value > 0) return value;
            } else {
                in.next(); // discard the bad input.
            }
            System.out.println("Please enter a positive whole number.");
        }
    }
}