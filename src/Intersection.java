import java.util.ArrayList;

public class Intersection {

	private String label;
	private boolean maintenance;
	private int totalPass;
	private final ArrayList<Rail> neighborhood;
	private boolean visited;
	private int value;
	
	
	public Intersection(String label)
	{
		this.label=label;
		this.maintenance=false;
		this.totalPass=0;
		this.neighborhood = new ArrayList<Rail>();
		this.visited=false;
	}
	public int getValue()
	{
		return this.value;
	}
	public void setValue(int x)
	{
		this.value=x;
	}
	public boolean isVisited()
	{
		return this.visited;
	}
	public void setVisited(boolean x)
	{
		this.visited=x;
	}
	public String getLabe()
	{
		return this.label;
	}
	public boolean getMaintenance()
	{
		return this.maintenance;
	}
	public void setMaintenance(boolean x)
	{
		this.maintenance=x;
	}
	public int getTotalPass()
	{
		return this.totalPass;
	}
	public void setTotalPass()
	{
		this.totalPass++;
	}
	public void addNeighbor(Rail rail)
	{
		if(this.neighborhood.contains(rail))
			return;
		else
			this.neighborhood.add(rail);
	}
	public boolean containsNeighbor(Rail rail)
	{
		return this.neighborhood.contains(rail);
	}
	public void removeNeighbor(Rail rail)
	{
		if(this.neighborhood.contains(rail))
			this.neighborhood.remove(rail);
		else
			return;
	}
	public int getNeighborCount()
	{
		return this.neighborhood.size();
	}
	public Rail getNeighbor(int index)
	{
		return this.neighborhood.get(index);
	}
	public int hashCode()
	{
		return this.label.hashCode();
	}
	
	public boolean equals(Object other)
	{
		if(!(other instanceof Intersection))
			return false;
		
		Intersection in = (Intersection)other;
		return this.label.equals(in.label);
	}
	
	public ArrayList<Rail> getNeighbors()
	{
		return new ArrayList<Rail> (this.neighborhood);
	}
	public void printNeighbor()
	{
		for (Rail rail : neighborhood) {
			System.out.print(rail.getDst().getLabe()+" ");//rail.getSrc().getLabe()+" "+
		}
	}
}
