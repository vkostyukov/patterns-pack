package icicle;

public class SyncDispatcher extends AbstractDispatcher implements Dispatcher,
		Runnable {

	public SyncDispatcher() {
		super();
	}

	@Override
	public void run() {

		for (;;) {
		
			ModuleRequest moduleRequest = requests.poll();

			if (moduleRequest != null) {

				Module module = modules.get(moduleRequest.module());
				if (module != null) {
					
					Execution execution = new Execution(moduleRequest, module, results);
					execution.start();
					
					try {
						execution.join();
					} catch (InterruptedException ignored) { 	}
					
				}
			}
		}
	}
}
