package icicle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractDispatcher implements Dispatcher, Runnable {

	public static class Execution extends Thread {

		private Queue<ModuleResult> results;
		private Module module;
		private ModuleRequest request;

		public Execution(ModuleRequest requets, Module module,
				Queue<ModuleResult> results) {
			this.results = results;
			this.module = module;
			this.request = requets;
		}

		@Override
		public void run() {
			Object result = module.execute(request.arg());
			results.offer(request.createModuleResult(result));
		}
	}

	public static class RequestReception extends Thread {

		private Queue<ModuleRequest> requests;
		private Socket fromClient;

		public RequestReception(Queue<ModuleRequest> activations,
				Socket fromClient) {
			this.requests = activations;
			this.fromClient = fromClient;
		}

		@Override
		public void run() {

			try {
				ObjectInputStream ois = new ObjectInputStream(
						fromClient.getInputStream());
				ModuleRequest req = (ModuleRequest) ois.readObject();

				requests.offer(req);

				ois.close();
				fromClient.close();

			} catch (IOException ignored) {
				System.err.println(ignored.toString());
			} catch (ClassNotFoundException ignored) {
				System.err.println(ignored.toString());
			}

		}

	}

	public static class RequestsReciver extends Thread {

		private Queue<ModuleRequest> requests;

		public RequestsReciver(Queue<ModuleRequest> activations) {
			this.requests = activations;
		}

		@Override
		public void run() {

			try {
				ServerSocket socket = new ServerSocket(10000);
				for (;;) {
					Socket fromClient = socket.accept();
					RequestReception session = new RequestReception(requests,
							fromClient);
					session.start();
				}
			} catch (IOException ignored) {
			}
		}
	}

	public static class ResultsSender extends Thread {

		private Queue<ModuleResult> results;

		public ResultsSender(Queue<ModuleResult> results) {
			this.results = results;
		}

		@Override
		public void run() {

			for (;;) {
				ModuleResult moduleResult = results.poll();

				if (moduleResult != null) {
					try {
						
						Socket invoker = new Socket(moduleResult.to(), 10001);
						ObjectOutputStream oos = new ObjectOutputStream(
								invoker.getOutputStream());
						oos.flush();
						oos.writeObject(moduleResult);
						oos.flush();
						oos.close();

					} catch (IOException e) {
					}

				}
			}
		}

	}

	protected Thread reciver;
	protected Thread self;
	protected Thread sender;

	protected Queue<ModuleRequest> requests;
	protected Queue<ModuleResult> results;

	protected Hashtable<String, Module> modules;

	public AbstractDispatcher() {
		this.modules = new Hashtable<String, Module>();
		this.requests = new ConcurrentLinkedQueue<ModuleRequest>();
		this.results = new ConcurrentLinkedQueue<ModuleResult>();

		this.self = new Thread(this);
		this.reciver = new RequestsReciver(requests);
		this.sender = new ResultsSender(results);
	}

	@Override
	public void add(Module module) {
		String moduleName = module.getClass().getSimpleName();
		modules.put(moduleName, module);
	}

	@Override
	public void remove(Module module) {
		String moduleName = module.getClass().getSimpleName();
		modules.remove(moduleName);
	}

	@Override
	public void activate() {
		reciver.start();
		self.start();
		sender.start();
	}

	@Override
	public void deactivate() {

	}

	@Override
	public void waitForDeactivate() {
		try {
			self.join();
		} catch (InterruptedException ignored) { }
	}

}
