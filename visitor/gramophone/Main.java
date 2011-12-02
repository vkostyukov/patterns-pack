import java.util.LinkedList;
import java.util.Queue;

/**
 * Events
 */
interface GramophoneEvent {
	void apply(GramophoneHandler handler);
}

class ToogleEvent implements GramophoneEvent {
	@Override
	public void apply(GramophoneHandler handler) {
		handler.handle(this);
	}
}

class NextEvent implements GramophoneEvent {
	@Override
	public void apply(GramophoneHandler handler) {
		handler.handle(this);
	}
}

/**
 * Visitor implementation 
 */
interface GramophoneHandler {
	void handle(ToogleEvent event);
	void handle(NextEvent event);
}

class RadioHandler implements GramophoneHandler {

	private Gramophone gramophone;
	public RadioHandler(Gramophone gramophone) { this.gramophone = gramophone; }

	@Override
	public void handle(ToogleEvent event) {
		gramophone.toogle(new CDHandler(gramophone));
	}

	@Override
	public void handle(NextEvent event) {
		gramophone.nextStation();
	}
}

class CDHandler implements GramophoneHandler {
	
	private Gramophone gramophone;
	public CDHandler(Gramophone gramophone) { this.gramophone = gramophone; }

	@Override
	public void handle(ToogleEvent event) {
		gramophone.toogle(new RadioHandler(gramophone));		
	}

	@Override
	public void handle(NextEvent event) {
		gramophone.nextTrack();
	}
}

/**
 * FSM (Finit-State-Machine) implementation 
 */
class Gramophone implements Runnable {
	
	private GramophoneHandler handler = new CDHandler(this);
	private Queue<GramophoneEvent> pool = new LinkedList<GramophoneEvent>();
	
	private Thread self = new Thread(this);
	
	private int track = 0, station = 0;
	
	private boolean started = false;
	
	public void enable() {
		started = true; 
		self.start();
	}
	
	public void disable() {
		started = false;
		self.interrupt();
		
		try { self.join(); } catch (InterruptedException ignored) { }
	}
	
	public synchronized void dispatch(GramophoneEvent event) {
		pool.offer(event);
		notify();
	}

	@Override
	public void run() {
		for (;!pool.isEmpty() || started;) {
			for (;!pool.isEmpty();) {
				GramophoneEvent event = pool.poll();
				event.apply(handler);
			}
			
			synchronized (this) { try { wait(); } catch (InterruptedException ignored) {  } }
		}
	}
	
	void toogle(GramophoneHandler handler) {
		this.handler = handler;		
		System.out.println("State changed: " + handler.getClass().getSimpleName());
	}
	
	void nextTrack() { track++; System.out.println("Track changed: " + track); }
	void nextStation() { station++; System.out.println("Station changed: " + station); }
}

public class Main {
	public static void main(String args[]) {
		Gramophone gramophone = new Gramophone(); 	// it's CD by default
		
		gramophone.enable();						// start event loop
		
		gramophone.dispatch(new ToogleEvent()); 	// toogle to radio
		gramophone.dispatch(new NextEvent()); 		// next station
		gramophone.dispatch(new ToogleEvent());		// toogle to CD player
		gramophone.dispatch(new NextEvent());		// next CD track
		
		gramophone.disable();						// stop event loop
	}
}
