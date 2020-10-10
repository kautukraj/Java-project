import java.util.ArrayList;
import java.util.Scanner;

import base.*;
import demo.FactoryDemo;
import demo.HighwayDemo;
import demo.HubDemo;

public class DemoDriver
{

	private static int fIndex = 0; // for cycling through the factories
	private static final ArrayList <Factory> factories = new ArrayList<>();

	public static void main(String[] args)
	{

		// replace the following by explicit calls to different factories
		factories.add(new FactoryDemo());
		//factories.add(new FactoryDemo());
		//factories.add(new FactoryDemo());
		//factories.add(new FactoryDemo());
		//factories.add(new FactoryDemo());
		//factories.add(new FactoryDemo());

		// replace by appropriate display; not my headache, Sir has to do this
		Display disp = new TextDisplay();

		Network net = factories.get(0).createNetwork(); // network of all the factories will be the same

		// create network elements and trucks based on input
		// Cycle through factories and create hubs/highways/trucks using
		// different factories
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		String[] tokens = s.split(" ");
		final int animateTimeStep = Integer.parseInt(tokens[0]); // millisecs
		final int displayTimeStep = Integer.parseInt(tokens[1]); // millisecs

		// read in hubs
		ArrayList<Hub> hubs = new ArrayList<>();
		int numHubs = Integer.parseInt(sc.nextLine());
		for (int i = 0; i < numHubs; i++)
		{
			s = sc.nextLine();
			tokens = s.split(" ");
			Hub hub = nextFactory().createHub(new Location(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
			hub.setCapacity(Integer.parseInt(tokens[2]));
			hub.setDeltaT(animateTimeStep);
			hubs.add(hub);
			net.add(hub);
		}

		// read in highways
		int numHwys = Integer.parseInt(sc.nextLine());
		for (int i = 0; i < numHwys; i++)
		{
			s = sc.nextLine();
			tokens = s.split(" ");
			Highway hwy = nextFactory().createHighway();
			hwy.setHubs(hubs.get(Integer.parseInt(tokens[0])), hubs.get(Integer.parseInt(tokens[1])));
			hwy.setCapacity(Integer.parseInt(tokens[2]));
			hwy.setMaxSpeed(Integer.parseInt(tokens[3]));
			net.add(hwy);
		}

		// read in trucks - create 2 trucks for each input line; but why???
		int numTrucks = Integer.parseInt(sc.nextLine());
		for (int i = 0; i < numTrucks; i++)
		{
			s = sc.nextLine();
			tokens = s.split(" ");
			Truck truck = nextFactory().createTruck();
			// truck moves from start location to nearest hub; start and end point is arbitrary
			truck.setSourceDest(new Location(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])),
					new Location(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
			truck.setStartTime(Integer.parseInt(tokens[4])); // in millisecs
			truck.setDeltaT(animateTimeStep);
			net.add(truck);

			truck = nextFactory().createTruck();
			truck.setSourceDest(new Location(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])),
					new Location(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
			truck.setStartTime(Integer.parseInt(tokens[4])); // in millisecs
			truck.setDeltaT(animateTimeStep);
			net.add(truck);
		}
		sc.close(); // closing the Scanner object

		//net.start(); // start the simulation

		// call net.redisplay periodically. Display runs on a different thread
		Thread t = new Thread() {
			public void run() {
				try {
					for (int i = 0; i < 2000; i++) {
						net.redisplay(disp); // call net.redisplay periodically
						Thread.sleep(displayTimeStep);
					}
				} catch (Exception e) {

				}
			}
		};
		//t.start();

		//HubDemo hub = new HubDemo(new Location(0, 0));
		//System.out.println(hubs.get(1).getLoc());
		//System.out.println(hub.getNextHighway(hubs.get(5), hubs.get(1)).getMaxSpeed() + " " + hub.getNextHighway(hubs.get(5), hubs.get(1)).getEnd().getLoc());

	}

	private static Factory nextFactory()
	{
		int numFactories = factories.size();
		int newIndex = fIndex % numFactories;
		fIndex++;
		return factories.get(newIndex);
	}
}
