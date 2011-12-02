class Singletone {
	
	private static Singletone instance;
	
	private Singletone() {
		System.out.println("Singletone Constructor();");
	}
	
	public synchronized static Singletone getInstance() {
		
		System.out.println("Singletone.getInstance");
		
		if (instance == null) {
			instance = new Singletone();
		}
		
		return instance;
	}
	
}

enum Singleenum {
	
	INSTANCE;
	
	private Singleenum() {
		System.out.println("Singleenum Constructor()");
	}
}


public class Main {
	
	public static void main(String args[]) {
		Singletone s1 = Singletone.getInstance();
		Singletone s2 = Singletone.getInstance();
		Singletone s3 = Singletone.getInstance();

		System.out.println(s1 == s2);
		System.out.println(s2 == s3);
		
		Singleenum e1 = Singleenum.INSTANCE;
		Singleenum e2 = Singleenum.INSTANCE;

		System.out.println(e1 == e2);
		
	}
}
