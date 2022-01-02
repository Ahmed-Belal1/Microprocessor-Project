
public class StationItem {
	public boolean busy;
	public String tag;
	public Instruction inst;
	public int remainingCycles;
	public float firstValue;
	public float secondValue;
	public String firstWaiting;
	public String secondWaiting;
	public int effectiveAdress;
	
	public StationItem(String tag, Instruction inst, int remainingCycles, int firstValue, int secondValue,
			String firstWaiting, String secondWaiting, int effectiveAdress) {
		super();
		this.busy=false;
		this.tag = tag;
		this.inst = inst;
		this.remainingCycles = remainingCycles;
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.firstWaiting = firstWaiting;
		this.secondWaiting = secondWaiting;
		this.effectiveAdress = effectiveAdress;
	}
	
	public StationItem() {
		super();
		this.busy=false;
		this.firstWaiting = "";
		this.secondWaiting = "";
		this.tag = "";
	}
	
	

}
