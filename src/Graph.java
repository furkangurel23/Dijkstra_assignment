import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {

	private final HashMap<String,Intersection> intersections;
	private final HashMap<Integer, Rail> rails;
	private HashMap<Intersection, ArrayList<Rail>> adj;
	
	private ArrayList<Rail> rails2;
	
	public Graph()
	{
		this.intersections = new HashMap<String, Intersection>();
		this.rails = new HashMap<Integer, Rail>();
		
		this.adj=new HashMap<Intersection,ArrayList<Rail>>();
		this.rails2= new ArrayList<Rail>();
	}
	public Graph(ArrayList<Intersection> intersections)
	{
		this.intersections = new HashMap<String, Intersection>();
		this.rails = new HashMap<Integer, Rail>();
		
		this.rails2= new ArrayList<Rail>();
		
		for (Intersection intersection : intersections) {
			this.intersections.put(intersection.getLabe(), intersection);
		}
	}
	
	public boolean addRail(Intersection src, Intersection dst, double weight)
	{
		if(src.equals(dst))
			return false;
		Rail r = new Rail(src,dst,weight);
		if(this.rails.containsKey(r.hashCode()))
			return false;
		else if(src.containsNeighbor(r) || dst.containsNeighbor(r))
			return false;
		/*else if(this.intersections.get(src).containsNeighbor(r) || this.intersections.get(dst).containsNeighbor(r))
			return false;*/
		this.rails.put(r.hashCode(), r);
		
		/*this.intersections.get(src).addNeighbor(r);
		this.intersections.get(dst).addNeighbor(r);*/
		src.addNeighbor(r);
		//dst.addNeighbor(r);
		return true;
	}
	
	
	
	public boolean containsRail(Rail r)
	{
		if(r.getSrc()==null || r.getDst()==null)
			return false;
		return this.rails.containsKey(r.hashCode());
	}
	
	
	public Rail removeRail(Rail r)
	{
		r.getSrc().removeNeighbor(r);
		r.getDst().removeNeighbor(r);
		return this.rails.remove(r.hashCode());
	}
	
	public Rail findRailS(String i1,String i2)
	{
		
		if(this.rails.containsKey((i1+i2).hashCode()))
		{
			return this.rails.get((i1+i2).hashCode());
			//System.out.println(this.rails.get((i1+i2).hashCode()).isSwtc());
		}
		return null;
	}
	
	public boolean containsIntersection(Intersection i)
	{
		return this.intersections.get(i.getLabe()) !=null;
	}
	
	public Intersection getIntersection(String label)
	{
		return this.intersections.get(label);
	}
	
	public boolean addIntersection2(Intersection in)
	{
		if(this.intersections.containsValue(in))
			return false;
		this.intersections.put(in.getLabe(), in);
		return true;
	}
	public Intersection getInterNode(Intersection in)
	{
		if(this.intersections.containsKey(in.getLabe()))
			return this.intersections.get(in.getLabe());
		return null;
	}
	public Intersection removeIntersection(String label)
	{
		Intersection in = this.intersections.remove(label);
		while(in.getNeighborCount()>0)
		{
			this.removeRail(in.getNeighbor(0));
		}
		return in;
	}
	public Set<Intersection> getInter()
	{
		return new HashSet<Intersection>(this.intersections.values());
	}
	public Set<Rail> getRails()
	{
		return new HashSet<Rail>(this.rails.values());
	}
	
	public void maintain(String label)
	{
		if(this.intersections.containsKey(label))
			this.intersections.get(label).setMaintenance(true);
	}
	public void service(String label)
	{
		if(this.intersections.containsKey(label))
			this.intersections.get(label).setMaintenance(false);
	}
	public void breakRail(String a, String b)
	{
		if(this.rails.containsKey((a+b).hashCode()))
			this.rails.get((a+b).hashCode()).setBroken(true);
	}
	public void repair(String a, String b)
	{
		if(this.rails.containsKey((a+b).hashCode()))
			this.rails.get((a+b).hashCode()).setBroken(false);
	}
	public void addInterCommand(String a)
	{
		Intersection yeni = new Intersection(a);
		this.addIntersection2(yeni);
	}
	public void Link(String src, HashMap<String, Double> station, String dst)
	{
		Intersection source = this.intersections.get(src);
		for(String st: station.keySet())
		{
			this.addRail(source, this.intersections.get(st), station.get(st));
			this.addRail(this.intersections.get(st), source, station.get(st));
			if(st.equals(dst))
			{
				findRailS(src,dst).setSwtc(true);
			}
		}
	}
	public void ListRoute(String name)
	{
		Intersection source = this.intersections.get(name);
		ArrayList<String> ngh = new ArrayList<String>();
		for(Rail rl: source.getNeighbors())
		{
			//if(!rl.isBroken())
			//{
				ngh.add(rl.getDst().getLabe());
			//}
		}
		Collections.sort(ngh,String.CASE_INSENSITIVE_ORDER);
		System.out.print("	Routes from "+name+": ");
		for (String string : ngh) {
			System.out.print(string+" ");
		}
		System.out.println();
	}
	public void ListMaintance()
	{
		System.out.print("	Intersections under maintenance: ");
		ArrayList<String> st = new ArrayList<String>(this.intersections.keySet());
		Collections.sort(st);
		for (String string : st) {
			if(this.intersections.get(string).getMaintenance())
			{
				System.out.print(this.intersections.get(string).getLabe()+" ");
			}
		}
		System.out.println();
	}
	public void ListActiveRails()
	{
		System.out.print("	Active rails: ");
		
		ArrayList<Rail> qw = new ArrayList<Rail>(this.rails.values());
		int N = qw.size();
		for(int i=0;i<N;i++)
		{
			int min =i;
			for(int j =i+1;j<N;j++)
			{
				//&& 0>qw.get(j).getDst().getLabe().compareTo(qw.get(min).getDst().getLabe())
				if(0>qw.get(j).getSrc().getLabe().compareTo(qw.get(min).getSrc().getLabe()) )
					min=j;
				else if(0==qw.get(j).getSrc().getLabe().compareTo(qw.get(min).getSrc().getLabe())&& 0>qw.get(j).getDst().getLabe().compareTo(qw.get(min).getDst().getLabe()))
					min = j;
			}
			Rail swap = qw.get(i);
			qw.set(i, qw.get(min));
			qw.set(min, swap);
		}
		for (Rail rail : qw) {
			if(rail.isSwtc())
				System.out.print(rail.getSrc().getLabe()+">"+rail.getDst().getLabe()+" ");
		}
		System.out.println();
	}
	public void ListBrokenRails()
	{
		System.out.print("	Broken rails: ");
		ArrayList<Rail> qw = new ArrayList<Rail>(this.rails.values());
		int N = qw.size();
		for(int i=0;i<N;i++)
		{
			int min =i;
			for(int j =i+1;j<N;j++)
			{
				//&& 0>qw.get(j).getDst().getLabe().compareTo(qw.get(min).getDst().getLabe())
				if(0>qw.get(j).getSrc().getLabe().compareTo(qw.get(min).getSrc().getLabe()) )
					min=j;
				else if(0==qw.get(j).getSrc().getLabe().compareTo(qw.get(min).getSrc().getLabe())&& 0>qw.get(j).getDst().getLabe().compareTo(qw.get(min).getDst().getLabe()))
					min = j;
			}
			Rail swap = qw.get(i);
			qw.set(i, qw.get(min));
			qw.set(min, swap);
		}
		for (Rail rail : qw) {
			if(rail.isBroken())
				System.out.print(rail.getSrc().getLabe()+">"+rail.getDst().getLabe()+" ");
		}
		System.out.println();
	}
	public void ListCrossTime()
	{
		System.out.print("\t# of cross times: ");
		ArrayList<String> st = new ArrayList<String>(this.intersections.keySet());
		Collections.sort(st);
		for (String string : st) {
			if(this.intersections.get(string).getTotalPass()>0)
			{
				System.out.print(this.intersections.get(string).getLabe()+":"+this.intersections.get(string).getTotalPass()+" ");
			}
		}
		System.out.println();
	}
	
	public void printInter()
	{
		for(String st : this.intersections.keySet())
		{
			Intersection in = this.intersections.get(st);
			System.out.println(in.getLabe());
		}
	}
	public void printRa()
	{
		System.out.println(this.rails.size());
		for(int i : this.rails.keySet())
		{
			Rail rl = this.rails.get(i);
			System.out.println(rl.getSrc().getLabe()+" "+rl.getDst().getLabe()+" "+rl.getWeight()+" "+rl.isSwtc());
		}
		/*for(Rail rl1: this.rails.values())
		{
			System.out.println(rl1.getSrc().getLabe()+" "+rl1.getDst().getLabe());
		}*/
		/*for (Rail rl : rails2) {
			System.out.println(rl.getSrc().getLabe()+" "+rl.getDst().getLabe());
		}*/
	}
}
