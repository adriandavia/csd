// CSD Mar 2013 Juansa Sendra

public class LimitedTable extends RegularTable { //max 4 in dinning-room
    private int philos = 0;
    public LimitedTable(StateManager state) {super(state);}
    public synchronized void enter(int id) throws InterruptedException {
        while(philos >= 4) { state.wenter(id); wait(); }
        philos++;
        state.enter(id);
    }
    
    public synchronized void exit(int id)  {
        philos--;
        notifyAll();
        state.exit(id);
    }
}
