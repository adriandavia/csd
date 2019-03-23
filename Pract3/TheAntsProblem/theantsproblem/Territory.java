package theantsproblem;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;

public class Territory {
    private int tam; // Matrix size
    private boolean occupied[][];
    //Original -> String description = "Basic Java Synchronization (using synchronized)";
    String description = "Using Barrers";
    private Log log;
    final CyclicBarrier barrier;
    private Condition bloqueo[][];
    final ReentrantLock lock;
    public String getDesc() {
        return description;
    }

    public Territory(int tamT, Log l) {
        tam = tamT;
        occupied = new boolean[tam][tam];
        bloqueo = new Condition[tam][tam];
        barrier = new CyclicBarrier(tam);
        log = l;
        lock = new ReentrantLock();
        // Initializing the matrix
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                occupied[i][j] = false;
                bloqueo[i][j] = lock.newCondition();
            }
        }
    }

    public int getTam() {
        return tam;
    }

    //Original -> public synchronized void putAnt(Ant h, int x, int y) {
    public void putAnt(Ant h, int x, int y) {
        try {
            lock.lock();
            while (occupied[x][y]) {                
                    // Write in the log: ant waiting
                    log.writeLog(LogItem.PUT, h.getid(), x, y, LogItem.WAITINS,
                            "Ant " + h.getid() + " waiting for [" + x + "," + y + "]");
                    //Original -> wait();
                    bloqueo[x][y].await(1000, TimeUnit.MILLISECONDS);
            }
            occupied[x][y] = true;
            barrier.await();
            h.setPosition(x, y);
            // Write in the log: ant inside territory
            log.writeLog(LogItem.PUT, h.getid(), x, y, LogItem.OK, "Ant " + h.getid() + " : [" + x + "," + y + "]  inside");
        } catch (InterruptedException e) {
        } catch (BrokenBarrierException e) {} finally {
            lock.unlock();
        }
    }     
        
            //Original -> public synchronized void takeAnt(Ant h) {
    public void takeAnt(Ant h) {
        try{
            lock.lock();
            int x = h.getX();
            int y = h.getY();
            occupied[x][y] = false;
            // Write in the log: ant outside territory
            log.writeLog(LogItem.TAKE, h.getid(), x, y, LogItem.OUT, "Ant " + h.getid() + " : [" + x + "," + y + "] out");
    
            bloqueo[x][y].signal();
        } catch(Exception e){} finally {
            lock.unlock();
        }
    }

    //Original -> public synchronized void moves(Ant h, int x1, int y1, int step) {
    public void moves(Ant h, int x1, int y1, int step) {
        try{
            lock.lock();
            int x = h.getX();
            int y = h.getY();
            while (occupied[x1][y1]) {
                // Write in the log: ant waiting
                log.writeLog(LogItem.MOVE, h.getid(), x1, y1, LogItem.WAIT,
                "Ant " + h.getid() + " waiting for [" + x1 + "," + y1 + "]");
                bloqueo[x1][y1].await(1000, TimeUnit.MILLISECONDS);
            }
            occupied[x][y] = false;
            occupied[x1][y1] = true;
            barrier.await();
            h.setX(x1);
            h.setY(y1);
            // Write in the log: ant moving
            log.writeLog(LogItem.MOVE, h.getid(), x1, y1, LogItem.OK,
                    "Ant " + h.getid() + " : [" + x + "," + y + "] -> [" + x1 + "," + y1 + "] step:" + step);
            bloqueo[x][y].signal();
        } catch(InterruptedException e) {
        } catch (BrokenBarrierException e){} finally {
            lock.lock();
        }
    }
}

