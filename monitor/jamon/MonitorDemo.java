import core.ProxyHelper;
import impl.TooglePrinter;
import impl.TooglePrinterImpl;


public class MonitorDemo {

	public static void main(String[] args) {
		
		ProxyHelper helper = new ProxyHelper(TooglePrinterImpl.class);
		
		TooglePrinter printer = (TooglePrinter) helper.getProxy();
		
		printer.activate();
		printer.print(10);
		printer.deactivate();
		
	}

}
