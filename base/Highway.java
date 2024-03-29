package base;

// Highways are unidirectional

public abstract class Highway
{

	private Hub start, end;
	private int maxSpeed, capacity;

	public void setHubs(Hub s, Hub e)
	{
		start = s;
		end = e;
		start.add(this); // add it to the list of highways connected to that hub
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int mspeed) {
		maxSpeed = mspeed;
	}

	public void setCapacity(int cap) {
		capacity = cap;
	}
	
	// returns true if Highway is not full
	// i.e. number of trucks is below capacity
	abstract public boolean hasCapacity();
	
	// add fails if already at capacity
	abstract public boolean add(Truck truck);


	abstract public void remove(Truck truck);
		
	public Hub getStart() {
		return start;
	}

	public Hub getEnd() {
		return end;
	}

	public void draw(Display disp)
	{
		// draws a line from start to end
		disp.drawLine(start.getLoc(), end.getLoc());
	}
	
	protected int getCapacity() {
		return capacity;
	}
}
	
