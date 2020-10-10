package demo;

import base.*;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class HubDemo extends Hub
{
    Queue <Truck> queue = new PriorityBlockingQueue<>();

    public HubDemo(Location loc)
    {
        super(loc);
    }

    @Override
    public boolean add(Truck truck)
    {
        if (queue.size() < getCapacity())
        {
            queue.add(truck);
            return true;
        }
        else
            return false;
    }

    @Override
    protected void remove(Truck truck)
    {
        queue.remove(truck);
    }

    @Override
    // we do not want to send it to where it came from
    public Highway getNextHighway(Hub last, Hub dest)
            // this function has been tested
    {
        // have to implement some algorithm here, sad :(
        // we can safely assume that the graph is connected
        Set <Hub> visited = new LinkedHashSet<Hub>();
        visited = depthFirstTraversal(NetworkDemo.graph, last);

        /*for (Hub hub: visited)
            System.out.println(hub.getLoc());*/

        /*it is guaranteed our our dest Hub will be in the visited sequence.
        The 0th element in visited will be last Hub. The 1st element will be the next node we need to take. So, return
        the highway between 0th and 1st.
         */
        // converting LinkedHashSet to List
        List<Hub> listVisited = new ArrayList<Hub>(visited);
        Hub currentDest = listVisited.get(1);

        return getConnectingHighway(last, currentDest);

    }

    /*Set<Hub> depthFirstTraversal(Graph graph, Hub root)
    {
        Set<Hub> visited = new LinkedHashSet<Hub>();
        Stack<Hub> stack = new Stack <Hub>();
        stack.push(root);
        while (!stack.isEmpty())
        {
            Hub node = stack.pop();
            if (!visited.contains(node))
            {
                visited.add(node);
                for (Node v : graph.getAdjNodes(node))
                {
                    stack.push(v.hub);
                }
            }
        }
        return visited;
    }*/


    @Override
    protected void processQ(int deltaT)
    {
        // queue is the Queue object
        /*
        Two situations:
        Hub has to send the truck to the next hub.
        OR, Hub has to send the truck to the final station
         */
        System.out.println("Call to processQ");
        for (Truck truck: queue)
        {
            // if Truck is at the final hub then put it on the final road and remove it from the queue
            // write code for this
            if (true)
            {

            }
            /*if Truck is at an intermediate Hub then call getNextHighway and try to put it on that highway if capacity
            permits and remove it from the queue. For getNextHighway, src is "this" hub and dest needs to be found out.
            */
            else
            {
                Hub destHub = Network.getNearestHub(truck.getDest());
                Highway highway = getNextHighway(this, destHub);

                if (highway.add(truck))
                {
                    System.out.println("Truck sent to highway");
                    this.remove(truck); // remove it from the queue
                }
                else
                    System.out.println("Highway is full");
            }
        }

    }

    public Highway getConnectingHighway(Hub src, Hub dest)
            // function has been tested
    {
        for (Highway highway: NetworkDemo.getAllHighways())
        {
            if (highway.getStart() == src && highway.getEnd() == dest)
                return highway;
        }
        return null;
    }
};

