abstract class Cell {

	public static final int OPENED = 0;
	public static final int CLOSED = 1;
	
	protected int status;

	protected int left, top;

	public Cell(int left, int top) {
		super();
		
		this.left = left;
		this.top = top;
		this.status = Cell.CLOSED;
		
	}

	public void open() {
		this.status = Cell.OPENED;
	}

	public int getLeft() {
		return left;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getStatus() {
		return status;
	}

	public abstract int getPoints();	
}

class Empty extends Cell {
	
	public Empty(int left, int top) {
		super(left, top);
	}

	@Override
	public int getPoints() {
		return 10;
	}
}

class Mine extends Cell {
	
	public Mine(int left, int top) {
		super(left, top);
	}

	@Override
	public int getPoints() {
		return 100; 
	}
}

class EmptyProxy extends Cell {
	private Empty proxy;

	public EmptyProxy(int left, int top) {
		super(left, top);
		this.proxy = null;
	}

	@Override
	public void open() {
		if (proxy == null) {
			proxy = new Empty(left, top);
		}
		
		proxy.open();
	}

	@Override
	public int getLeft() {
		if (proxy == null) {
			return left;
		} else {
			return proxy.getLeft();
		}
	
	}
	
	@Override
	public int getTop() {
		if (proxy == null) {
			return top;
		} else {
			return proxy.getTop();
		}
	}

	@Override
	public int getStatus() {
		if (proxy == null) {
			return status;
		} else {
			return proxy.getStatus();
		}
	}

	@Override
	public int getPoints() {
		if (proxy == null) {
			return 10;
		} else {
			return proxy.getPoints();
		}
		
	}
}

class MineProxy extends Cell {
	private Mine proxy;

	public MineProxy(int left, int top) {
		super(left, top);
		
		this.proxy = null;
	}
	
	@Override
	public void open() {
		if (proxy == null) {
			proxy = new Mine(left, top);
		}
		
		proxy.open();
	}

	@Override
	public int getLeft() {
		if (proxy == null) {
			return left;
		} else {
			return proxy.getLeft();
		}
	
	}
	
	@Override
	public int getTop() {
		if (proxy == null) {
			return top;
		} else {
			return proxy.getTop();
		}
	}

	@Override
	public int getStatus() {
		if (proxy == null) {
			return status;
		} else {
			return proxy.getStatus();
		}
	}

	@Override
	public int getPoints() {
		if (proxy == null) {
			return 100;
		} else {
			return proxy.getPoints();
		}
		
	}
}

public class Main {
	public static void main(String[] args) {
		Cell cells[][] = new Cell[4][4];
		
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				
				if (i+j % 2 == 0) {
					cells[i][j] = new MineProxy(i, j);
				} else {
					cells[i][j] = new EmptyProxy(i, j);
				}
			}
		}
		
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				cells[i][j].open();
				System.out.println("Opening cell: " + cells[i][j].getPoints());
			}
		}
	}
}
