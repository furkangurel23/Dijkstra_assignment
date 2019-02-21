import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Dijsktra {

	private final ArrayList<Intersection> inters;
    private final ArrayList<Rail> rails;
    private Set<Intersection> settledNodes;
    private Set<Intersection> unSettledNodes;
    private Map<Intersection, Intersection> predecessors;
    private Map<Intersection, Double> distance;
    
   
    
    
    public Dijsktra(Graph graph)
    {
    	this.inters=new ArrayList<Intersection>(graph.getInter());
    	this.rails = new ArrayList<Rail>(graph.getRails());
    	
    	
    }
    public void execute(Intersection src, double speed, double swtchTime)
    {
    	
    	
    	
    	settledNodes = new HashSet<Intersection>();
    	unSettledNodes = new HashSet<Intersection>();
    	distance = new HashMap<Intersection, Double>();
    	predecessors = new HashMap<Intersection, Intersection>();
    	if(!src.getMaintenance())
    		distance.put(src, 0.0);
    	else
    	{
    		System.out.println("The source is under maintenance!");
    		return; //System.exit(0);
    	}
    	
    	unSettledNodes.add(src);
    	while(unSettledNodes.size()>0)
    	{
    		Intersection node = getMinimum(unSettledNodes);
    		settledNodes.add(node);
    		unSettledNodes.remove(node);
    		findMinimalDistance(node,speed, swtchTime);
    	}
    }
     private Rail findEdge(Intersection src, Intersection dst)
     {
    	 for (Rail rail : rails) {
    		 if(!rail.isBroken())
    		 {
    			 if(rail.getSrc().equals(src) && rail.getDst().equals(dst))
    			 {
    				 
    				 return rail;
    			 }
    					
    		 }
			
		}
    	 return null;
     }
     private void findMinimalDistance(Intersection node, double speed, double swtchTime)
     {
    	 ArrayList<Intersection> adjNodes = getNeighbors(node);
    	 for(Intersection target:adjNodes)
    	 {
    		 
    		 if(!target.getMaintenance()&& !node.getMaintenance() && findEdge(node,target)!=null && findEdge(node,target).isSwtc())
    		 {
    			 if(getShortestDistance(target)>getShortestDistance(node)+getDistance(node,target))
        		 {
        			 distance.put(target, getShortestDistance(node)+getDistance(node,target));
        			 predecessors.put(target,node);
        			 unSettledNodes.add(target);
        		 }
    			 
    		 }
    		 
    		 if(!target.getMaintenance() && !node.getMaintenance() && findEdge(node,target)!=null && !findEdge(node,target).isSwtc())
    		 {
    			 if(getShortestDistance(target)>getShortestDistance(node)+getDistance(node,target)+swtchTime/60*speed)
        		 {
        			 distance.put(target, getShortestDistance(node)+getDistance(node,target)+swtchTime/60*speed);
        			 predecessors.put(target,node);
        			 unSettledNodes.add(target);
        		 }
    			 
    		 }
    		 
    	 }
     }
     private double getDistance(Intersection node, Intersection target)
     {
    	 for (Rail edge : rails) {
			if(edge.getSrc().equals(node) && edge.getDst().equals(target))
			{
				return edge.getWeight();
			}
		}
    	 throw new RuntimeException("Should not happen");
     }
     private ArrayList<Intersection> getNeighbors(Intersection node)
     {
    	 ArrayList<Intersection> neighbors = new ArrayList<Intersection>();
    	 for(Rail edge: rails)
    	 {
    		 if(edge.getSrc().equals(node) && !isSettled(edge.getDst()))
    		 {
    			 neighbors.add(edge.getDst());
    		 }
    	 }
    	 return neighbors;
     }
     private Intersection getMinimum(Set<Intersection> vertexes)
     {
    	 Intersection min = null;
    	 for(Intersection vertex: vertexes)
    	 {
    		 if(min ==null)
    		 {
    			 min = vertex;
    			 
    		 }
    		 else
    		 {
    			 if(getShortestDistance(vertex)<getShortestDistance(min))
    			 {
    				 min = vertex;
    			 }
    		 }
    	 }
    	 return min;
    	 
     }
     private boolean isSettled(Intersection vertex) {
         return settledNodes.contains(vertex);
     }

     private double getShortestDistance(Intersection destination) {
         Double d = distance.get(destination);
         if (d == null) {
             return Double.MAX_VALUE;
         } else {
             return d;
         }
     }
     
     public LinkedList<Intersection> getPath(Intersection target) {
         LinkedList<Intersection> path = new LinkedList<Intersection>();
         Intersection step = target;
         // check if a path exists
         if (predecessors.get(step) == null) {
             return null;
         }
         path.add(step);
         while (predecessors.get(step) != null) {
             step = predecessors.get(step);
             path.add(step);
         }
         // Put it into the correct order
         Collections.reverse(path);
         return path;
     }
     
     public Graph switchControl(Graph gr, LinkedList<Intersection> path)
     {
    	 int k =0;
    	 for(int i =0; i<path.size()-1;i++)
			{
				if(gr.findRailS(path.get(i).getLabe(), path.get(i+1).getLabe()).isSwtc())
				{
					continue;
				}
				else if(!gr.findRailS(path.get(i).getLabe(), path.get(i+1).getLabe()).isSwtc())
				{
					k++;
					for(int j=0;j<path.get(i).getNeighborCount();j++)
					{
						if(path.get(i).getNeighbor(j).isSwtc())
						{
							path.get(i).getNeighbor(j).setSwtc(false);
						}			
					}
					gr.findRailS(path.get(i).getLabe(), path.get(i+1).getLabe()).setSwtc(true);
				}
			}
    	 System.out.println("	Total # of switch changes: "+k);
    	 return gr;
     }
     
     public void totalTime(Intersection a,double speed)
     { 	 
    	 System.out.printf("	Time (in min): %.3f",(this.distance.get(a))/speed*60);
    	 System.out.println();
     }
     
     public void PrintPath(Graph gr,LinkedList<Intersection> path)
     {
    	 
    	 for (Intersection vertex : path) {
    		 gr.getInterNode(vertex).setTotalPass();
    		 System.out.print(vertex.getLabe()+" ");
	     }
    	 System.out.println();
     }
}
