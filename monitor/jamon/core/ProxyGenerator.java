package core;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ProxyGenerator {

	private Class<?> clazz;

	public ProxyGenerator(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public String getProxyName() {
		return clazz.getName() + "Proxy";
	}

	public String getDestinationPath() {
		String cp = System.getProperty("java.class.path");
		String pkg = clazz.getPackage().getName();

		for (String dir : pkg.split(" . ")) {
			cp += File.separator + dir;
		}

		return cp + File.separator + clazz.getSimpleName() + "Proxy.java";
	}

	public CharSequence generate() {

		StringBuilder sb = new StringBuilder();
		sb.append(clazz.getPackage() + ";");
		sb.append("\n");
		sb.append("public class " + clazz.getSimpleName() + "Proxy extends "
				+ clazz.getName());
		
		Class<?>[] infs = clazz.getInterfaces();
		if (infs.length > 0) sb.append(" implements ");
		for (Class<?> inf: infs) {
			sb.append(inf.getName());
		}
		sb.append(" {\n");
		
		sb.append("\n");
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getModifiers() == Modifier.PUBLIC) {
				sb.append("public ");
				sb.append(method.getReturnType().getCanonicalName() + " ");
				sb.append(method.getName());

				sb.append("(");

				Class<?>[] args = method.getParameterTypes();
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i].getCanonicalName() + " arg" + i
							+ (i == (args.length - 1) ? "" : ", "));
				}

				sb.append(")");

				sb.append(" {");
				sb.append("\n");
				sb.append("core.MethodRequest mr = new core.MethodRequest(\"" + method.getName() + "\""); //, arg0);\n");
				
				for (int i = 0; i < args.length; i++) {
					sb.append(", arg" + i);
				}

				sb.append(");\n");
				
				if (method.getReturnType().getCanonicalName().equals("void")) {
					sb.append("dispatcher.dispatch(mr);\n");
				} else {
					sb.append("return dispatcher.dispatch(mr);\n");
				}
				sb.append("}");

				sb.append("\n");
			}
		}

		sb.append("\n");
		sb.append("}");
		return sb.toString();
	}

}
