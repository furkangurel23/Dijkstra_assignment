
public class Rail {

	private Intersection src,dst;
	private boolean swtc;
	private boolean broken;
	private double weight;
	private boolean visited;
	
	public Rail(Intersection src, Intersection dst, double weight)
	{
		this.src=src;
		this.dst=dst;
		this.weight=weight;
		this.swtc=false;
		this.broken=false;
	}
	public boolean isSwtc() {
		return swtc;
	}
	public void setSwtc(boolean swtc) {
		this.swtc = swtc;
	}
	
	public Intersection getSrc() {
		return src;
	}
	public void setSrc(Intersection src) {
		this.src = src;
	}
	
	public Intersection getDst() {
		return dst;
	}
	public void setDst(Intersection dst) {
		this.dst = dst;
	}
	
	public boolean isBroken() {
		return broken;
	}
	public void setBroken(boolean broken) {
		this.broken = broken;
	}
	
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public int hashCode()
	{
		return (this.src.getLabe()+this.dst.getLabe()).hashCode();
	}
}
