abstract class Validator {
	public void validate(String str) throws IllegalArgumentException {
		if (str == null || str.equals("") || !isValid(str)) {
			throw new IllegalArgumentException("Argument " + str + ", not valid for " + this.getClass().getName());
		}
	}
	
	public abstract boolean isValid(String str);
}

class PhoneValidator extends Validator {
	@Override
	public boolean isValid(String str) {
		return str.matches("\\+[0-9]-[0-9]{3}-[0-9]{3}-[0-9]{4}");
	}
}

class IpValidator extends Validator {
	@Override
	public boolean isValid(String str) {
		return str.matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+");
	}
}

class EmailValidator extends Validator {
	@Override
	public boolean isValid(String str) {
		return str.matches("\\w+@\\w+\\.(com|ru|us|org)");
	}
}

public class Main {
	
	public static void main(String args[]) {
		
		Validator validators[] = new Validator[] { new PhoneValidator(), new IpValidator(), new EmailValidator() };		
		String strings[] = new String[] {"+7-982-123-1233", "192.168.1.222", "name@gmail.com","+7-1232-123-22", "192.454.288.2", "name@domain.hero"};
		
		for (String str: strings) {
			for (Validator val: validators) {
				try {
					val.validate(str);
					System.out.println("Argument " + str + ", valid for " + val.getClass().getName());
				} catch (IllegalArgumentException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}
}
