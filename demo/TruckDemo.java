package demo;

import base.Highway;
import base.Hub;
import base.Truck;

public class TruckDemo extends Truck
{
    Highway highway = null;
    int currentTime = 0;
    static int count = 0;

    @Override
    public Hub getLastHub()
    {
        // null when the Truck has just started moving
        return this.highway.getStart();
    }

    @Override
    // the truck is notified that is has entered the highway through a
    // call to its enter(Highway) method
    public void enter(Highway hwy)
    {
        while (!hwy.add(this))
        {
            this.setLoc(hwy.getStart().getLoc());
            this.highway = hwy;
        }
    }

    @Override
    // called every deltaT time to update its status/position
    // If less than startTime, does nothing
    // If at a hub, tries to move on to next highway in its route; don't do anything, Hub will do.
    // If on a road/highway, moves towards next Hub
    // If at dest Hub, moves towards dest; but truck need not handle this right?
    protected void update(int deltaT)
    {
        // update its location
        System.out.println("Call to update");
        /*
        Case 1: if currentTime < this.startTime then do nothing
        Case 2: If at a hub, tries to move on to next highway in its route: no need to handle this
        Case 3: If on a road/highway, moves towards next Hub: needs to be handled
        Case 4: If at dest Hub, moves towards dest: no need to handle this

        increment currentTime by deltaT on each call
        update position as x = speed * deltaT * some trig stuff
         */
    }


    // derived classes should generate unique name for each instance
    @Override
    public String getTruckName()
    {
        count += 1;
        return super.getName() + "IMT2019043" + Integer.toString(count);
    }
    // IMT2019043
}
