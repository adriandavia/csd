// CSD feb 2015 Juansa Sendra

public class Pool2 extends Pool{ //max kids/instructor
    int instructores = 0, niños = 0, maxK;
    public void init(int ki, int cap){
        maxK = ki;
    }
    
    public synchronized void kidSwims() throws InterruptedException {
        while(niños >= maxK*instructores || instructores == 0) { //Condicion de espera
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
    
    public synchronized void instructorSwims()   {
        instructores++;
        log.swimming();
        notifyAll();
    }
    
    public synchronized void instructorRests() throws InterruptedException  {
        while((instructores == 1 || instructores <= (niños/maxK)+1) && niños != 0) {
            log.waitingToRest();
            wait();
        }
        instructores--;
        log.resting(); 
    }
}
