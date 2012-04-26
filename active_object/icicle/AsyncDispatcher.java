package icicle;


public class AsyncDispatcher extends AbstractDispatcher implements Dispatcher, Runnable {

	public AsyncDispatcher() {
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
				}
			}			
			
		}
		
	}

}
