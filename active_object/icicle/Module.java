package icicle;

public interface Module {
	
	public static final Object EMPTY_RESULT = null; 

	public Object execute(Object arg);
	
}
