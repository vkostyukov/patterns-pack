import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


final class Context {
	public final int x;
	public final int y;
	public final Color color;
	
	public Context(int x, int y, Color collor) {
		this.x = x;
		this.y = y;
		this.color = collor;
	}
}

interface Primitive {
	public void draw(Context context);
}

class Circle implements Primitive {
	private int radius;
	
	public Circle(int radius) {
		this.radius = radius;
	}

	@Override
	public void draw(Context context) { 
		System.out.println("Circle was painted!");
	}
}

class Square implements Primitive {
	private int height, width;
	
	public Square(int height, int width) {
		this.height = height;
		this.width = width;
	}

	@Override
	public void draw(Context context) {  
		System.out.println("Square was painted!");
	}
}

class Point implements Primitive {
	@Override
	public void draw(Context context) { 
        	System.out.println("Point was painted!");
	}
}

class Picture implements Primitive {
	private List<Primitive> childrens;
	
	public Picture(Primitive ...primitives) {
		this.childrens = new LinkedList<Primitive>();
		this.childrens.addAll(Arrays.asList(primitives));
	}

	@Override
	public void draw(Context context) {
		System.out.println("Picture was painted!");
		for (Primitive p: childrens) {
			p.draw(context);
		}
	}
}

abstract class PrimitiveFactory {
	
	private static Point onePoint;
	private static Map<Integer, Circle> circles;
	private static Map<Integer, Square> squares;
	
	static {
		circles = new HashMap<Integer, Circle>();						
		squares = new HashMap<Integer, Square>();
	}
	
	public static synchronized Picture createPicture(Primitive ... childrens) {
		return new Picture(childrens);
	}
	
	public static synchronized Circle createCircle(int radius) {
		if (circles.get(radius) == null) {
			circles.put(radius, new Circle(radius));
		}
		
		return circles.get(radius);
	}
	
	public static synchronized Square createSquare(int height, int width) {
		if (squares.get(height*10+width) == null) {
			squares.put(height*10+width, new Square(height, width));
		}
		
		return squares.get(height*10+width);
	}
	
	public static synchronized Point createPoint() {
		if (onePoint == null) {
			onePoint = new Point();
		}
		
		return onePoint;
	}
}

public class Main {
	public static void main(String[] args) {
		
		Primitive[] primitives = {
				PrimitiveFactory.createPoint(), 
				PrimitiveFactory.createCircle(10), 
				PrimitiveFactory.createSquare(20, 30),
				PrimitiveFactory.createCircle(20),
				PrimitiveFactory.createCircle(20),
				PrimitiveFactory.createPoint(),
				PrimitiveFactory.createSquare(20, 40),				
		};
		
		Picture picture = PrimitiveFactory.createPicture(primitives);
		Context context = new Context(10, 20, Color.BLUE);
		picture.draw(context);
	}
}
