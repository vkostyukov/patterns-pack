import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

interface SubExpression {
	
	public Number value();	 
	public void add(SubExpression expr); 
	public void sub(SubExpression expr); 
		
	public void accept(SubExpressionVisitor visitor);
}

interface SubExpressionVisitor extends Cloneable {
	
	public void visit(FloatValue self);
	public void visit(IntegerValue self);
	public void visit(Expression self);
	
	public SubExpressionVisitor createSecondaryVisitor();
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
	public void sub(SubExpression expr) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Number value() {
		return value;
	}

	@Override
	public void accept(SubExpressionVisitor visitor) {
		visitor.visit(this);
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
	public void sub(SubExpression expr) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Number value() {
		return value;
	}

	@Override
	public void accept(SubExpressionVisitor visitor) {
		visitor.visit(this);
	}
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
	public Number value() {
		Number result = new Float(0);
		
		for (SubExpression expr: exprs) {
			result = result.floatValue() + expr.value().floatValue();
		}
		
		return result;
	}

	@Override
	public void accept(SubExpressionVisitor visitor) {		
		for (SubExpression expr: exprs) {
			expr.accept(visitor);
		}
		
		visitor.visit(this);
	}

}

class ValueVisitor implements SubExpressionVisitor {
	
	private Number value;
	private Stack<ValueVisitor> secondaryVisitors;	
	
	public ValueVisitor() {
		this.value = new Float(0);
		this.secondaryVisitors = new Stack<ValueVisitor>();
	}

	@Override
	public void visit(FloatValue self) {
		value = value.floatValue() + self.value().floatValue();
	}

	@Override
	public void visit(IntegerValue self) {
		value = value.floatValue() + self.value().floatValue();
	}

	@Override
	public void visit(Expression self) {
		
	}
	
	@Override
	public SubExpressionVisitor createSecondaryVisitor() {
		ValueVisitor visitor = new ValueVisitor();
		secondaryVisitors.push(visitor);
		return visitor;
	}
	
	public Number getValue() {
		return value;
	}
}

class ValueSqrVisitor implements SubExpressionVisitor {
	
	private Number value;
	
	public ValueSqrVisitor() {
		this.value = new Float(0);
	}

	@Override
	public void visit(FloatValue self) {
		value = value.floatValue() + self.value().floatValue()*self.value().floatValue();
	}

	@Override
	public void visit(IntegerValue self) {
		value = value.floatValue() + self.value().floatValue()*self.value().floatValue();
	}

	@Override
	public void visit(Expression self) {
		
	}
	
	@Override
	public SubExpressionVisitor createSecondaryVisitor() {
		return null;
	}
	
	public Number getValue() {
		return value;
	}
}

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {
		SubExpression expr = new Expression();

		SubExpression a = new Expression(new IntegerValue(5), new IntegerValue(-2));
		SubExpression b = new Expression(new IntegerValue(11), new IntegerValue(6));
		
		expr.add(new IntegerValue(20));
		expr.sub(a);
		expr.sub(b);
		
		ValueVisitor visitor1 = new ValueVisitor();
		expr.accept(visitor1);
		
		ValueSqrVisitor visitor2 = new ValueSqrVisitor();
		expr.accept(visitor2);
		
		System.out.println(visitor1.getValue());
		System.out.println(visitor2.getValue());

	}
}
