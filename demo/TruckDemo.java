package demo;

import base.*;

public class TruckDemo extends Truck
{
    Highway highway = null; // if between src to srchub or dest to desthub
    int currentTime = 0;
    static int count = 0;
    private boolean flag = false; // on src to srchub road

    static int margin = 50;

    @Override
    public Hub getLastHub()
    {
        if(highway != null)
            return highway.getStart();
            //if on source-network link
        else if(!flag)
            return null;
            //from last hub to dest
        else
            return Network.getNearestHub(this.getDest());
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
    protected synchronized void update(int deltaT)
    {
        currentTime += deltaT; // increment the time
        //if before start time
        if (currentTime < this.getStartTime()) return;

        Location startloc, endloc;

        //if location is getHubNearestToLoc(dest) - set highway to null
        int v;
        double m, sin, cos;
        //if inside the network
        if(highway != null)
        {
            //stationary
            //if(this.getLoc() == highway.getEnd().getLoc())
            if (sameSpot(this.getLoc(), highway.getEnd().getLoc()))
            {
                //if(this.getLoc() == Network.getNearestHub(this.getDest()).getLoc())
                if (sameSpot(this.getLoc(), Network.getNearestHub(this.getDest()).getLoc()))
                highway = null;

            return;
            }
            //on highway or exiting hub
            else
                {
                v = highway.getMaxSpeed();
                m = (highway.getStart().getLoc().getY() - highway.getEnd().getLoc().getY()) / (double)(highway.getStart().getLoc().getX() - highway.getEnd().getLoc().getX());
                startloc = highway.getStart().getLoc();
                    /*m = (this.getLoc().getY() - highway.getEnd().getLoc().getY()) / (double)(this.getLoc().getX() - highway.getEnd().getLoc().getX());
                    startloc = this.getLoc();*/
                endloc = highway.getEnd().getLoc();
            }
        }

        //network-station link
        else
            {
            //set some speed for network-station link
            v = 50;
            //start to network
            if(!flag)
            {
                m = (this.getSource().getY() - Network.getNearestHub(this.getSource()).getLoc().getY()) / (double) (this.getSource().getX() - Network.getNearestHub(this.getSource()).getLoc().getX());
                startloc = this.getSource();
                endloc = Network.getNearestHub(this.getSource()).getLoc();
            }
            //network to end
            else
                {
                //if(this.getLoc() == this.getDest())
                    if (sameSpot(this.getLoc(), this.getDest()))
                    return;

                m = (this.getDest().getY() - Network.getNearestHub(this.getDest()).getLoc().getY()) / (double) (this.getDest().getX() - Network.getNearestHub(this.getDest()).getLoc().getX());
                startloc = Network.getNearestHub(this.getDest()).getLoc();
                endloc = this.getDest();
            }
        }
        sin = (endloc.getY() < startloc.getY() ? -1 : 1) * (m / Math.pow(1 + (m * m),0.5));
        cos = (endloc.getX() < startloc.getX() ? -1 : 1) * (1 / Math.pow(1 + (m * m),0.5));
        this.setLoc(new Location(this.getLoc().getX() + (int)(v * (deltaT/300) * cos),this.getLoc().getY() + (int)(v * (deltaT/300) * sin)));
    }

    private boolean sameSpot(Location l1, Location l2)
    {
        if((l1.getY() >= l2.getY() + margin) && (l1.getY() <= l2.getY() + margin) && (l1.getX() >= l2.getX() + margin) && (l1.getX() <= l2.getX() + margin))
            return true;

        return false;
    }



    // derived classes should generate unique name for each instance
    @Override
    public String getTruckName()
    {
        return super.getTruckName() + 19043;
    }
    // IMT2019043
}
