package demo;
import base.Highway;
import base.Truck;

import java.util.ArrayList;

public class HighwayDemo extends Highway
{
    int currentCapacity = 0;
    ArrayList <Truck> trucks = new ArrayList<>();

    @Override
    public boolean hasCapacity()
    {
        return currentCapacity < getCapacity();
    }

    @Override
    public boolean add(Truck truck)
    {
        if (currentCapacity < getCapacity())
        {
            trucks.add(truck);
            return true;
        }
        else
            return false;
    }

    @Override
    public void remove(Truck truck)
    {
        trucks.remove(truck);
    }
}
