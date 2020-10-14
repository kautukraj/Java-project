package demo;

import base.*;

public class TruckDemo extends Truck
{
    Highway highway = null; // if between src to srchub or dest to desthub
    int currentTime = 0;
    static int count = 0;
    private boolean flag = false; // on src to srchub road

    @Override
    public Hub getLastHub()
    {
        // returns null when the Truck has just started moving
        return this.highway.getStart();
    }


    @Override
    // the truck is notified that is has entered the highway through a
    // call to its enter(Highway) method
    public void enter(Highway hwy)
    {
        if(hwy.add(this)) // if that highway has the capacity to accommodate the truck
            highway = hwy; // update the state of the truck

        flag = true; // indicates that the truck has entered the network of Hubs and Highways
    }

    /*@Override
    // called every deltaT time to update its status/position
    // If less than startTime, does nothing
    // If at a hub, tries to move on to next highway in its route; don't do anything, Hub will do.
    // If on a road/highway, moves towards next Hub
    // If at dest Hub, moves towards dest; but truck need not handle this right?
    protected void update(int deltaT)
    {
        // update its location
        System.out.println("Call to update");

        //Case 1: if currentTime < this.startTime then do nothing
        //Case 2: If at a hub, tries to move on to next highway in its route: no need to handle this
        //Case 3: If on a road/highway, moves towards next Hub: needs to be handled
        //Case 4: If at dest Hub, moves towards dest station
        //Case 5: If at src station, then move towards nearest hub

        //increment currentTime by deltaT on each call
        //update position as x = speed * deltaT * some trig stuff

    }*/

    @Override
    protected void update(int deltaT)
    {
        // keep track of the time. currentTime = 0 and increment it everytime
        // if currentTime < this.startTime then do nothing
        
        currentTime += deltaT; // increment the time
        if (currentTime < this.getStartTime())
        {
            // do nothing
            return;
        }
        else
        {
            int v;
            double m, sin, cos;

            if (highway != null) // if inside the network
            {
                // stationary
                if (this.getLoc() == highway.getEnd().getLoc())
                {
                    if (this.getLoc() == Network.getNearestHub(this.getDest()).getLoc()) // it is at Hub nearest to dest
                        highway = null; // exiting the network to get onto road to dest station
                    return;
                }

                // on highway or exiting hub
                else
                {
                    v = highway.getMaxSpeed();
                    m = (highway.getStart().getLoc().getY() - highway.getEnd().getLoc().getY()) /
                            (double) (highway.getStart().getLoc().getX() - highway.getEnd().getLoc().getX());
                    // m = y2 - y1 / x2 - x1
                }
            }

            // network-station link, ie, on a road
            else
            {
                //set some speed for network-station link as Sir said
                v = 50;

                if (!flag) // start to network
                {
                    m = (this.getSource().getY() - Network.getNearestHub(this.getSource()).getLoc().getY()) /
                            (double) (this.getSource().getX() - Network.getNearestHub(this.getSource()).getLoc().getX());
                    // should be ulta right ??? but still works
                } else // network to end
                {
                    m = (this.getDest().getY() - Network.getNearestHub(this.getDest()).getLoc().getY()) /
                            (double) (this.getDest().getX() - Network.getNearestHub(this.getDest()).getLoc().getX());
                }
            }

            sin = m / Math.pow(1 + (m * m), 0.5); // should be +
            cos = 1 / Math.pow(1 + (m * m), 0.5); // should be +
            this.setLoc(new Location(this.getLoc().getX() + (int) (v * deltaT * cos),
                    this.getLoc().getY() + (int) (v * deltaT * sin)));
        }
    }



    // derived classes should generate unique name for each instance
    @Override
    public String getTruckName()
    {
        count += 1;
        return super.getName() + "-IMT2019043";
    }
    // IMT2019043
}
