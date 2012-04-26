package icicle;

import java.io.Serializable;

public interface Callback extends Serializable {
	
	public void callback(Object returnObject);
	
}
