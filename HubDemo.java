package demo;

import base.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class HubDemo extends Hub
{
    Queue<Truck> queue = new LinkedList<>(); // use normal queue here
    private static ArrayList<Hub> hubs = new ArrayList<>();
    private static ArrayList<Highway> highways = new ArrayList<>();
    private static AllPairsSP GraphObj;

    public HubDemo(Location loc)
    {
        super(loc);
        hubs.add(this);
        /*for (Highway highway: this.getHighways())
        {
            if (!highways.contains(highway))
                highways.add(highway);
        }*/
    }

    @Override // this wasn't an abstract function but still we are overloading
    public void add(Highway hwy)
    {
        super.add(hwy);

        if(!highways.contains(hwy))
            highways.add(hwy);

        initGraph();
    }


    public void test()
    {
        System.out.println(getNextHighway(hubs.get(6), hubs.get(5)).getStart().getLoc() + " " + getNextHighway(hubs.get(6), hubs.get(5)).getEnd().getLoc() + " " +
                getNextHighway(hubs.get(6), hubs.get(5)).getMaxSpeed());
    }

    @Override
    public boolean add(Truck truck)
    {
        if (queue.size() < getCapacity())
        {
            queue.add(truck);
            return true;
        } else
            return false;
    }

    @Override
    protected void remove(Truck truck)
    {
        queue.remove(truck);
    }

    /*@Override
    // we do not want to send it to where it came from
    public Highway getNextHighway(Hub last, Hub dest)
            // this function has been tested
    {
        // have to implement some algorithm here, sad :(
        // we can safely assume that the graph is connected
        Set <Hub> visited = new LinkedHashSet<Hub>();
        visited = depthFirstTraversal(NetworkDemo.graph, last);

        for (Hub hub: visited)
            System.out.println(hub.getLoc());

        it is guaranteed our our dest Hub will be in the visited sequence.
        The 0th element in visited will be last Hub. The 1st element will be the next node we need to take. So, return
        the highway between 0th and 1st.

        // converting LinkedHashSet to List
        List<Hub> listVisited = new ArrayList<Hub>(visited);
        Hub currentDest = listVisited.get(1);

        return getConnectingHighway(last, currentDest);

    }*/

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

    //next node in the shortest path from src to dest after src
    @Override
    public Highway getNextHighway(Hub from, Hub dest)
    {
        int index1=hubs.indexOf(from),index2=hubs.indexOf(dest);
        int next_index = AllPairsSP.constructPath(index1,index2).get(1);
        return getConnector(hubs.get(index1),hubs.get(next_index));
    }

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
        for (Truck truck : queue)
        {
            // if Truck is at the final hub then put it on the final road and remove it from the queue
            // To put it on the final road, update the location of the truck
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
                } else
                    System.out.println("Highway is full");
            }
        }

    }


    /*public Highway getConnectingHighway(Hub src, Hub dest)
            // function has been tested
    {
        for (Highway highway: NetworkDemo.getAllHighways())
        {
            if (highway.getStart() == src && highway.getEnd() == dest)
                return highway;
        }
        return null;
    }*/

    public static boolean isAdjacent(Hub hub1, Hub hub2)
    {
        for(Highway hwy : highways)
        {
            if(hwy.getStart() == hub1 && hwy.getEnd() == hub2)
                return true;
        }
        return false;
    }


    public static Highway getConnector(Hub hub1, Hub hub2)
    {
        for(Highway hwy : highways)
        {
            if(hwy.getStart() == hub1 && hwy.getEnd() == hub2)
                return hwy;
        }
        return null;
    }

    /*@Override
    protected void processQ(int deltaT)
    {
        //process truckQueue - if last hub before station, send it towards station
        for(Truck truck : queue)
        {
            //get dest hub
            Hub result=null;
            int minima = Integer.MAX_VALUE;
            for(Hub h : hubs)
            {
                if(h.getLoc().distSqrd(truck.getDest()) < minima)
                {
                    minima = h.getLoc().distSqrd(truck.getDest());
                    result = h;
                }
            }
            //final hub before station
            if(this == result)
            {
                continue;
            }
            //intermediate hub - if entry to highway is successful
            truck.enter(this.getNextHighway(this,result));
            if(truck.getLastHub() == this) queue.remove(truck);
        }
    }*/


    public static void initGraph()
    {
        int V = hubs.size();
        int [][] Graph = new int[V][V];
        for(int i=0; i<V; i++)
        {
            for (int j = 0; j <V; j++)
            {
                int INF = (int) 1e7;
                if(i==j) Graph[i][j] = 0;
                else if(HubDemo.isAdjacent(hubs.get(i),hubs.get(j)))
                {
                    Highway highway = getConnector(hubs.get(i),hubs.get(j));
                    double length = Math.sqrt(Math.pow(highway.getStart().getLoc().getX() - highway.getEnd().getLoc().getX(), 2) +
                            Math.pow(highway.getStart().getLoc().getY() - highway.getEnd().getLoc().getY(), 2));
                    Graph[i][j] = (int) length;
                }
                else Graph[i][j] = INF;
            }
        }
        GraphObj = new AllPairsSP(V,Graph);
    }

}

class AllPairsSP
{

    static final int MAXN = 1000;

    // Infinite value for array
    static int INF = (int) 1e7;

    static int[][] dis;
    static int[][] Next;
    static int V;

    AllPairsSP(int V, int[][] graph)
    {
        AllPairsSP.V = V;
        dis = new int[V][V];
        Next = new int[V][V];
        AllPairsSP.initialise(V,graph);
        AllPairsSP.floydWarshall(V);
    }

    // Initializing the distance and Next array
    static void initialise(int V, int[][] graph)
    {
        for (int i = 0; i < V; i++)
        {
            for (int j = 0; j < V; j++)
            {
                dis[i][j] = graph[i][j];

                // No edge between node
                // i and j
                if (graph[i][j] == INF)
                    Next[i][j] = -1;
                else
                    Next[i][j] = j;
            }
        }
    }

    static Vector<Integer> constructPath(int u, int v)
    {

        // If there's no path between
        // node u and v, simply return
        // an empty array
        if (Next[u][v] == -1)
            return null;

        // Storing the path in a vector
        Vector<Integer> path = new Vector<>();
        path.add(u);

        while (u != v)
        {
            u = Next[u][v];
            path.add(u);
        }
        return path;
    }

    static void floydWarshall(int V)
    {
        for (int k = 0; k < V; k++)
        {
            for (int i = 0; i < V; i++)
            {
                for (int j = 0; j < V; j++)
                {

                    // We cannot travel through
                    // edge that doesn't exist
                    if (dis[i][k] == INF ||
                            dis[k][j] == INF)
                        continue;

                    if (dis[i][j] > dis[i][k] +
                            dis[k][j]) {
                        dis[i][j] = dis[i][k] +
                                dis[k][j];
                        Next[i][j] = Next[i][k];
                    }
                }
            }
        }
    }
}




