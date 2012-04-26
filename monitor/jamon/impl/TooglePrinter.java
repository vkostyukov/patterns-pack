package impl;

import core.Monitor;

public interface TooglePrinter extends Monitor {
	
	public void print(String arg);
	
	public void print (Number arg);
	
	public void print (Object arg);
}
