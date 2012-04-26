package icicle;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

public class ModuleResult implements Serializable {
	
	private static final long serialVersionUID = 4843194074331318562L;
	
	private InetAddress from;
	private InetAddress to;
	
	private UUID uuid;

	private Object result;
	
	public ModuleResult(Object result, InetAddress from, InetAddress to, UUID uuid) {
		this.result = result;
		
		this.from = from;
		this.to = to;
		
		this.uuid = uuid;
	}

	public Object result() {
		return result;
	}
	
	public InetAddress from() {
		return from;
	}
	
	public InetAddress to() {
		return to;
	}
	
	public UUID uuid() {
		return uuid;
	}

}
