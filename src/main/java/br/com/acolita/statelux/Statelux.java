package br.com.acolita.statelux;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Statelux {

    private static class BlockingClass {
        final ReentrantLock lock = new ReentrantLock();

        public BlockingClass() {
            lock.lock();
        }

        public void theLockingOperation() {
            lock.lock();
        }

        public synchronized void theSynchronizedMethod() {
            while (true) {
            }
        }
    }

    public void run() {
        final BlockingClass blockingObject = new BlockingClass();
        final Thread runnable = new Thread() {
            {
                setName("Statelux - The runnable thread");
                setDaemon(true);
            }
            @Override
            public void run() {
                // this will run forever
                blockingObject.theSynchronizedMethod();
            }
        };
        runnable.start();

        final Thread blocked = new Thread() {
            {
                setName("Statelux - The blocked thread");
                setDaemon(true);
            }
            @Override
            public void run() {
                // this will never run
                blockingObject.theSynchronizedMethod();
            }
        };
        blocked.start();

        final Thread waitingOnCondition = new Thread() {
            {
                setName("Statelux - The waiting on condition thread");
                setDaemon(true);
            }
            @Override
            public void run() {
                // this will never run
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    // will not happen
                    Thread.currentThread().interrupt();
                }
            }
        };
        waitingOnCondition.start();

        final Object waitedObject = new Object();
        final Thread inObjectWait = new Thread() {
            {
                setName("Statelux - The in object wait thread");
                setDaemon(true);
            }
            @Override
            public void run() {
                synchronized (waitedObject) {
                    try {
                        waitedObject.wait();
                    } catch (InterruptedException e) {
                        // will not happen
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        inObjectWait.start();

        final Thread parkedOnLock = new Thread() {
            {
                setName("Statelux - The parked on lock thread");
                setDaemon(true);
            }
            @Override
            public void run() {
                blockingObject.theLockingOperation();
            }
        };
        parkedOnLock.start();

        final Thread manuallyParked = new Thread() {
            {
                setName("Statelux - The manually parked thread");
                setDaemon(true);
            }
            @Override
            public void run() {
                LockSupport.park();
            }
        };
        manuallyParked.start();
    }
}
