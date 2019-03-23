// CSD feb 2015 Juansa Sendra

public class Pool1 extends Pool {   //no kids alone
    private int instructores;
    private int kids;
    public void init(int ki, int cap) {
        kids = 0;
        instructores = 0;
    }
    
    public synchronized void kidSwims() throws InterruptedException {
        while(instructores == 0) { //Condicion de espera
            log.waitingToSwim();
            wait();
        }
        kids++;//Actualiza estado
        log.swimming();
    }
    
    public synchronized void kidRests() {
        kids--;
        log.resting();
        notifyAll();
    }
    
    public synchronized void instructorSwims()   {
        instructores++;
        log.swimming();
        notifyAll();
    }
    
    public synchronized void instructorRests() throws InterruptedException  {
        while(instructores == 1 && kids != 0) {
            log.waitingToRest();
            wait();
        }
        instructores--;
        log.resting(); 
    }
}
