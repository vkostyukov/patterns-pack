class Student {
	
	String firstname;
	private String secondname;

	private String faculty;
	private int course;
	private int rate;
	
	public static class StudentBuilder {
		
		private String firstname;
		private String lastname;
		
		private String faculty = "UNKNOWN";
		private int course = 1;
		private int rate = 0;
		
		public StudentBuilder(String firstname, String secondname) {
			this.firstname = firstname;
			this.lastname = secondname;
		}
		
		public StudentBuilder faculty(String val) {
			this.faculty = val;
			return this;
		}
		
		public StudentBuilder course(int val) {
			this.course = val;
			return this;
		}
		
		public StudentBuilder rate(int val) {
			this.rate = val;
			return this;
		}
		
		public Student build() {
			return new Student(this);
		}
		
	}
	
	private Student(StudentBuilder builder) {
		this.firstname = builder.firstname;
		this.secondname = builder.lastname;
		this.faculty = builder.faculty;
		this.course = builder.course;
		this.rate = builder.rate;
	}

	@Override
	public String toString() {
		return firstname + " " + secondname + " course: " + course + ", faculty: " + faculty + ", rate:" + rate;
	}

}

public class Main {
	
	public static void main(String[] args) {
		
		Student student1 = new Student.StudentBuilder("Jhon", "Lenon").course(2).faculty("IT").rate(10).build();
		Student student2 = new Student.StudentBuilder("Howard", "Volovec").build();
		Student student3 = new Student.StudentBuilder("Cooper", "Sheldon").faculty("Physics").build();
		
		System.out.println(student1);
		System.out.println(student2);
		System.out.println(student3);
		
	}
}
