package base;

import java.util.ArrayList;

public abstract class Hub extends Thread
{
	private int deltaT = 500; // value being overwritten by function
	private final Location loc;
	private int capacity;
	private final ArrayList<Highway> hwys; // it has the out-bound highways; our adjacency list also has out-bound ones

	public Hub(Location loc)
	{
		this.loc = new Location(loc);
		hwys = new ArrayList<>();
	}

	public Location getLoc() {
		return new Location(loc);
	} // object of object?

	public void add(Highway hwy) {
		hwys.add(hwy);
	}
	
	public void setCapacity(int cap) {
		capacity = cap;
	}
	
	public void setDeltaT(int dt) {
		deltaT = dt;
	}
	
	// add a Truck to the queue of the Hub.
	// returns false if the Q is full and add fails
	abstract public boolean add(Truck truck);

	protected abstract void remove(Truck truck);

	// provides routing information
	abstract public Highway getNextHighway(Hub last, Hub dest); // dest is the final dest
	
	// draws a circle at its location. Override if needed; what ???
	public void draw(Display disp) {
		disp.drawCircle(loc, 5);
	}

	
	public void run()
	{  // code to be run as new thread
		// sets timer and calls processQ after each timestep deltaT
		for(int i=0; i < 1000; i++)
		{
			try
			{
				Thread.sleep(deltaT);
				processQ(deltaT);
			} catch (Exception e)
			{

			}
		}
	}
	
	public ArrayList<Highway> getHighways() {
		return new ArrayList<>(hwys);
	}

	// to be implemented in derived classes. Called at each time step
	abstract protected void processQ(int deltaT);

	protected int getCapacity() {
		return capacity;
	}
	

}
