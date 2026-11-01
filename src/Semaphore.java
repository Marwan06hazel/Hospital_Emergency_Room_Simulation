public class Semaphore {
    private int counter;

    public Semaphore(int counter){
        this.counter = counter;
    }

    /* Acquire() waits for a unit to be available
    * If the treatment room is full then doctor goes in the waiting list
    * */
    public synchronized void acquire() throws InterruptedException{
        while(counter<=0) wait();
        counter--;
    }

    /* tryAcquire(long timeoutMS) used If the doctor needs the room and waits for a limited time
     * If this time is out then the doctor is free for another patient
     * Patient needs to go back to the waiting room
     * */
    public synchronized boolean tryAcquire(long timeoutMS) throws InterruptedException{
        long timeout = System.currentTimeMillis() + timeoutMS;
        while(counter<=0){
            long remainingTime = timeout - System.currentTimeMillis();
            if(remainingTime<=0) return false;
            wait(remainingTime);
        }
        counter--;
        return true;
    }

    /* release() used when there is a unit is available to use
     * Then notify the next the waiting list
     * */
    public synchronized void release(){
        counter++;
        notify();
    }
}
