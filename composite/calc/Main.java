import java.util.ArrayList;
import java.util.List;

interface SubExpression {
	
	public Number value();						
	
	public void add(SubExpression expr); 
	public void sub(SubExpression expr);
	public SubExpression getSubExpression(int index);

}

class Expression implements SubExpression {
	
	private List<SubExpression> exprs;
	
	public Expression(SubExpression ... exprs) {
		this.exprs = new ArrayList<SubExpression>();
		for (SubExpression expr: exprs) {
			this.exprs.add(expr);
		}
	}
	
	@Override
	public void add(SubExpression expr) {
		exprs.add(expr);		
	}
	
	@Override
	public void sub(SubExpression expr) {
		if (expr instanceof IntegerValue) {
			exprs.add(new IntegerValue(-1*expr.value().intValue()));
		} else {
			exprs.add(new FloatValue(-1*expr.value().floatValue()));
		}
		
	}

	@Override
	public SubExpression getSubExpression(int index) {
		return exprs.get(index);
	}

	@Override
	public Number value() {
		Number result = new Float(0);
		
		for (SubExpression expr: exprs) {
			result = result.floatValue() + expr.value().floatValue();
		}
		
		return result;
	}
}

class FloatValue implements SubExpression {
	
	private Float value;
	
	public FloatValue(Float value) {
		this.value = value;
	}

	@Override
	public void add(SubExpression expr) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public SubExpression getSubExpression(int index) {
		throw new UnsupportedOperationException();	}

	@Override
	public void sub(SubExpression expr) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Number value() {
		return value;
	}
}

class IntegerValue implements SubExpression {
	
	private Integer value;
	
	public IntegerValue(Integer value) {
		this.value = value;
	}

	@Override
	public void add(SubExpression expr) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public SubExpression getSubExpression(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sub(SubExpression expr) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Number value() {
		return value;
	}
}

public class Main {

	public static void main(String[] args) {
		SubExpression expr = new Expression();

		SubExpression a = new Expression(new IntegerValue(3), new IntegerValue(-20));
		SubExpression b = new Expression(new IntegerValue(11), new IntegerValue(63));
		
		expr.add(new IntegerValue(20));
		expr.sub(a);
		expr.sub(b);
		
		System.out.println(expr.value());
	}
}
