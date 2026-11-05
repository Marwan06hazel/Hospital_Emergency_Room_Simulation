package org.hospital.test001;

public class treatmentRoom {
    private final Semaphore room;

    public treatmentRoom(int counter){
        room = new Semaphore(counter);
    }

    public synchronized boolean tryAcquireTreatmentRoom(long timeoutMS) throws InterruptedException{
        return room.tryAcquire(timeoutMS);
    }

    public void releaseTreatmentRoom(){
        room.release();
    }
}
