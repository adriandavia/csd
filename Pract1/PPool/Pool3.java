// CSD feb 2015 Juansa Sendra

public class Pool3 extends Pool{ //max capacity
    int instructores=0, niños = 0, maxK, cap;
    public void init(int ki, int cap){
        maxK = ki;
        this.cap = cap;
    }
    
    public synchronized void kidSwims() throws InterruptedException {
        while((niños >= maxK*instructores || instructores == 0) || instructores+niños == cap) { //Condicion de espera
            log.waitingToSwim();
            wait();
        }
        niños++;//Actualiza estado
        log.swimming();
    }
    
    public synchronized void kidRests() {
        niños--;
        log.resting();
        notifyAll();
    }
    
    public synchronized void instructorSwims() throws InterruptedException  {
        while(instructores+niños == cap) { //Condicion de espera
            log.waitingToSwim();
            wait();
        }
        instructores++;
        log.swimming();
        notifyAll();
    }
    
    public synchronized void instructorRests() throws InterruptedException  {
        while(((instructores == 1 || instructores <= (niños/maxK)+1) && niños != 0)) {
            log.waitingToRest();
            wait();
        }
        instructores--;
        log.resting(); 
        notifyAll();
    }
}