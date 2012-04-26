package icicle;

public interface Dispatcher {
	
	public void add(Module module);
	public void remove(Module module);
	
	public void activate();
	public void deactivate();
	
	public void waitForDeactivate();
}
