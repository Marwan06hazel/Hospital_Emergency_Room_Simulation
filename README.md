# Hospital_Emergency_Room_Simulation
Hospital ER simulation in Java using semaphores, bounded buffer and producer-consumer


## Task Dependencies — Who Waits for Whom?

### 1. Semaphore + Treatment Rooms
- Starts immediately.
- Must agree on the semaphore methods and interface with the Waiting Room and Doctor developers.
- The Treatment Room implementation depends on the custom Semaphore.

### 2. Waiting Room
- Can start developing the queue structure and class interface immediately.
- Must coordinate with the Semaphore developer before finalizing synchronization logic.
- Must agree with the Patient developer on patient entry methods.
- Must agree with the Doctor developer on patient retrieval and returning patients.

### 3. Doctor
- Can start designing the class structure and workflow immediately.
- Depends on the Waiting Room interface to retrieve and return patients.
- Depends on the Treatment Room interface to acquire and release treatment rooms.
- Can finalize and test the complete logic after both interfaces are agreed upon.


### 4. Patient + Display Messages
- Can start immediately.
- Must coordinate with the Waiting Room developer on how patients enter and wait for available seats.
- Does not need to wait for the Doctor implementation.

### 5. Hospital — Main
- Can start implementing input handling and the main program structure immediately.
- Must agree on constructors and method signatures with all developers.
- Can complete integration after the required classes and interfaces are ready.

### Critical Dependencies — Blocking Rules

Semaphore → Treatment Rooms:
 The custom Semaphore interface must be agreed upon before Treatment Room synchronization can be finalized.

Semaphore → Waiting Room:
 The Waiting Room developer needs the Semaphore interface to complete bounded-buffer synchronization.

Waiting Room → Patient:
 The patient-entry method and its blocking behavior must be agreed upon before final integration.

Waiting Room + Treatment Rooms → Doctor:
 The Doctor developer needs both interfaces to complete patient retrieval, treatment-room acquisition, and patient-return logic.

All Classes → Hospital:
 The Hospital developer needs the agreed constructors and public methods to complete integration.


### Important Coordination Rule
No developer should wait for another developer to finish the entire class before starting. Agree on method signatures and expected behavior first, then work in parallel using those interfaces.


# Hospital Emergency Room Simulation — Team Tasks

## 1. Semaphore + Treatment Rooms (((  Marwan  )))
**Responsibilities:**
- Implement the custom `Semaphore` class.
- Implement basic semaphore operations: `acquire()` and `release()`.
- Add a timed acquisition method that returns `false` if the resource cannot be acquired within 3 seconds.
- Implement the `TreatmentRoom` class using the semaphore to manage the two available treatment rooms.
- Ensure that no more than two doctors can use treatment rooms simultaneously.


## 2. Waiting Room  (((  Nour  )))
**Responsibilities:**
- Implement the `WaitingRoom` class using a bounded `Queue` to store waiting patients.
- Enforce the waiting room capacity specified by the user.
- Synchronize patient insertion and removal using semaphores.
- Ensure that patients wait outside when the waiting room is full.
- Allow patients to enter when a seat becomes available.
- Handle patients returned by doctors when treatment rooms cannot be acquired.

**Dependencies:** Coordinate with the Semaphore developer and agree on the methods used by `Patient` and `Doctor` to enter and leave the waiting room.

## 3. Doctor  (((  Mohamed Sayed  )))
**Responsibilities:**
- Implement the `Doctor` class as a separate thread.
- Keep each doctor idle until a patient becomes available.
- Retrieve one patient from the waiting room and assign that patient to the doctor.
- Attempt to acquire a treatment room within 3 seconds.
- If acquisition fails, return the patient to the waiting room.
- If acquisition succeeds, simulate treatment for a random duration.
- Release the treatment room after treatment is completed.
- Display the required status messages throughout the process.


## 4. Patient + Display Messages  (((  Mohamed Arafa  )))
**Responsibilities:**
- Implement the `Patient` class as a separate thread.
- Assign each patient a unique ID.
- Simulate patient arrival at the hospital.
- Attempt to enter the waiting room.
- If the waiting room is full, wait outside until a seat becomes available.
- Display the required messages for arrival, waiting, entering the waiting room, and leaving the hospital.
- Ensure that each patient's status messages accurately reflect the simulation events.


## 5. Hospital — Main   (((  Ziad  )))
**Responsibilities:**
- Implement the `Hospital` class and the `main()` method.
- Read the following inputs from the user:
  - Waiting room capacity
  - Number of doctors
  - Number of patients
- Create and initialize the shared resources.
- Create all patient and doctor objects.
- Start the required threads in the correct order.
- Wait for all patients to finish before displaying the final completion messages.
- Ensure that the simulation terminates correctly without losing patients or leaving threads running indefinitely.

---