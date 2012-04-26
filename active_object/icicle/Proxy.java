package icicle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.UUID;

public class Proxy {
	
	public static class ResultSaver implements Callback {
	
		private static final long serialVersionUID = -8484137929061558352L;
		
		private Object result;

		@Override
		public void callback(Object returnObject) {
			result = returnObject;
		}
		
		public Object result() {
			return result;
		}
		
	}
	
	public static class ResultReception extends Thread {
		
		private Hashtable<UUID, ModuleResult> results;
		private Socket fromClient;

		public ResultReception(Hashtable<UUID, ModuleResult> results, Socket fromClient) {
			this.results = results;
			this.fromClient = fromClient;
		}

		@Override
		public void run() {
			try {
				
				ObjectInputStream ois = new ObjectInputStream(fromClient.getInputStream());
				ModuleResult result = (ModuleResult) ois.readObject();
				
				results.put(result.uuid(), result);

				ois.close();
				fromClient.close();
				
			} catch (IOException ignored) { 
				System.err.println(ignored.toString());
			} catch (ClassNotFoundException ignored) {
				System.err.println(ignored.toString());
			} 
			
		}
		
		
		
	}
	
	public static class Invokation extends Thread {
		
		private ModuleRequest request;
		private Callback callback;
		private Hashtable<UUID, ModuleResult> results;

		public Invokation(ModuleRequest request, Callback callback, Hashtable<UUID, ModuleResult> results) {
			this.request = request;
			this.callback = callback;
			this.results = results;
		}

		@Override
		public void run() {
			
			try {
				Socket servant = new Socket(request.to(), 10000);
				ObjectOutputStream oos = new ObjectOutputStream(servant.getOutputStream());
				oos.flush();
				oos.writeObject(request);
				oos.flush();
				oos.close();
				servant.close();
				
				ModuleResult result = null;
				
				while ((result = results.get(request.uuid())) == null) ;
				
				results.remove(result.uuid());
				callback.callback(result.result());
				
			} catch (IOException ignored) { }

		}
		
	}
	
	public static class ResultReciver extends Thread {
		
		private Hashtable<UUID, ModuleResult> results;
		private ServerSocket socket;
		
		public ResultReciver(Hashtable<UUID, ModuleResult> results, ServerSocket socket) {
			this.results = results;
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				for (;;) {
					
					Socket fromClient = socket.accept();
					
					ResultReception reception = new ResultReception(results, fromClient);
					reception.start();
				}
			} catch (IOException ignored) { }  
			
		}
	}
	
	private Hashtable<UUID, ModuleResult> results;
	private Thread resultsReciver;
	
	private ServerSocket reciverSocket;
	private InetAddress servantAddr;

	public Proxy(InetAddress servantAddr) {
		
		try {
			this.reciverSocket = new ServerSocket(10001);
		} catch (IOException ignored) { System.err.println(ignored.getMessage());}

		this.servantAddr = servantAddr;
		
		this.results = new Hashtable<UUID, ModuleResult>();
		this.resultsReciver = new ResultReciver(results, reciverSocket);
	}
	
	public void activate() {
		resultsReciver.start();
	}
	
	public void deactivate() {
		
	}
	
	public void waitForDeactivate() {
		try {
			resultsReciver.join();
		} catch (InterruptedException ignored) { }
	}
	
	
	public Object execute (String module, Object arg) {
		
		ModuleRequest request = new ModuleRequest(module, arg, reciverSocket.getInetAddress(), servantAddr);
		Callback callback = new ResultSaver();
		
		Invokation invokation = new Invokation(request, callback, results);
		invokation.start();
		
		try { invokation.join(); } catch (InterruptedException ignored) { }
		
		return ((ResultSaver) callback).result();
	}
	
	public void execute(String module, Object arg, Callback callback) {
		
		ModuleRequest request = new ModuleRequest(module, arg, reciverSocket.getInetAddress(), servantAddr);
		
		
		Invokation invokation = new Invokation(request, callback, results);
		invokation.start();
	}
	
	
}
