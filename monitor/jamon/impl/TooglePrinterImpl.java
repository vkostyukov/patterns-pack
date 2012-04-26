package impl;

import core.CachingMonitor;


public class TooglePrinterImpl extends CachingMonitor implements TooglePrinter {

	public TooglePrinterImpl() { }
	
	public void print(String arg) {
		System.out.println("STR PRINT: " + arg);
	}
	
	public void print (Number arg) {
		System.out.println("NUM PRINT: " + arg);
	}
	
	public void print (Object arg) {
		System.out.println("OBJ PRINT: " + arg);
	}
	
	
}
