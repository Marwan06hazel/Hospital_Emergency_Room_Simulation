package org.hospital.test001;

public class Semaphore {
    private int counter;

    public Semaphore(int counter){
        this.counter = counter;
    }

    public synchronized void acquire() throws InterruptedException{
        while(counter<=0) wait();
        counter--;
    }

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

    public synchronized void release(){
        counter++;
        notify();
    }
}
