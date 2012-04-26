package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;

public abstract class CachingMonitor implements Monitor {

	public static class CachingMonitorDispatcher extends Thread implements
			MonitorDispatcher {

		private Queue<MethodRequest> requests;
		private Queue<MethodResult> results;
		private Class<?> dispatchedClass;
		
		private boolean active;

		public CachingMonitorDispatcher(Class<?> clazz) {
			requests = new LinkedList<MethodRequest>();
			results = new LinkedList<MethodResult>();
			dispatchedClass = clazz;
		}
		
		@Override
		public synchronized Object dispatch(MethodRequest request) {

			requests.offer(request);
			MethodResult result = results.peek();

			for (; result == null || !result.uid().equals(request.uuid()); result = results
					.peek()) {
				//System.out.println("wait");
			}

			return result.value();

		}

		@Override
		public void run() {
			try {
				for (;active; sleep(10)) {
					MethodRequest request = requests.poll();

					// System.out.println(dispatchedClass);

					//System.out.println(request);
					
					if (request == null)
						continue;

					Method[] methods = dispatchedClass.getDeclaredMethods();

					NEXT_METHOD: for (Method method : methods) {
						if (method.getName() == request.methodName()) {
							Class<?>[] type = method.getParameterTypes();
							Object[] args = request.args();
							for (int i = 0; i < request.args().length; i++) {
								try {
									if (type[i].cast(args[i]) == null) {
										continue NEXT_METHOD;
									}
								} catch (ClassCastException ex) {
									continue NEXT_METHOD;
								}
							}

							try {
								Object value = method.invoke(
										dispatchedClass.newInstance(), args);
								MethodResult result = request
										.createMethodResult(value);
								results.offer(result);
								//System.out.println("INVOKED " + method);
							} catch (InstantiationException e) {
								e.printStackTrace();
							}
							break;
						}
					}

				}
			} catch (InterruptedException e) {
				System.out.println(e.toString());
			} catch (IllegalArgumentException e) {
				System.out.println(e.toString());
			} catch (IllegalAccessException e) {
				System.out.println(e.toString());
			} catch (InvocationTargetException e) {
				System.out.println(e.toString());
			}
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			if (active == true) start();
			this.active = active;
		}
	}

	protected CachingMonitorDispatcher dispatcher;

	public CachingMonitor() {
		
	}

	public void activate() {
		dispatcher = new CachingMonitorDispatcher(this.getClass()
				.getSuperclass());

		dispatcher.setActive(true);
	}

	public void deactivate() {
		dispatcher.setActive(false);
	}
	
}
