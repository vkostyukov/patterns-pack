package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProxyHelper {
	
	private ProxyGenerator generator;
	private Class<?> clazz;
	
	public ProxyHelper(Class<?> clazz) {
		this.clazz = clazz;
		this.generator = new ProxyGenerator(clazz);
	}
	
	public Object getProxy() {
		
		Object proxy = null;
		
		CharSequence code = generator.generate();
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(new File(
					generator.getDestinationPath())));
			bf.write(code.toString());
			bf.close();
		
			Runtime.getRuntime().exec("\"C:\\Program Files (x86)\\Java\\jdk1.6.0_21\\bin\\javac\" -cp " + System.getProperty("java.class.path") + " " + generator.getDestinationPath());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			proxy = Class.forName(generator.getProxyName()).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return proxy;
	}


}
