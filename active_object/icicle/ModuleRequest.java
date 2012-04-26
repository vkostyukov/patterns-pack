package icicle;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

public class ModuleRequest implements Serializable {
	
	private static final long serialVersionUID = -4737996921537862655L;

	private InetAddress from;
	private InetAddress to;
	
	private String module;
	private Object arg;
	
	private UUID uuid;
	
	public ModuleRequest(String module, Object arg, InetAddress from, InetAddress to) {
		this.module = module;
		this.arg = arg;
		
		this.from = from;
		this.to = to;
		
		this.uuid = UUID.randomUUID();
	}

	public String module() {
		return module;
	}

	public Object arg() {
		return arg;
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
	
	public ModuleResult createModuleResult(Object result) {
		return new ModuleResult(result, to, from, uuid);
	}
}
