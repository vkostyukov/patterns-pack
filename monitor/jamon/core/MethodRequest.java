package core;

import java.io.Serializable;
import java.util.UUID;

public final class MethodRequest implements Serializable {
	
	private static final long serialVersionUID = 8686365736884946219L;

	private final String uuid;
	private final String methodName;
	private final Object args[];
	
	public MethodRequest(String methodName, Object ... args) {
		this.args = args;
		this.methodName = methodName;
		
		this.uuid = UUID.randomUUID().toString();
	}
	
	public String methodName() {
		return methodName;
	}
	
	public Object[] args() {
		return args;
	}
	
	public String uuid() {
		return uuid;
	}
	
	public MethodResult createMethodResult(Object value) {
		return new MethodResult(uuid, value);
	}
	
}
