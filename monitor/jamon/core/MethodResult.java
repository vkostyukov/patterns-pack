package core;

public class MethodResult implements Comparable<MethodResult> {
	
	private final String uuid;
	private Object value;
	
	public MethodResult(String uuid, Object value) {
		this.uuid = uuid;
		this.value = value;
	}

	public Object value() {
		return value;
	}

	public String uid() {
		return uuid;
	}

	@Override
	public int compareTo(MethodResult o) {
		return 0;
	}

}
