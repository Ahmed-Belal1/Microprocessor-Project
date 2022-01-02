
public class Instruction {
	public String opcode;
	public int latency;
	public int rt;// src register 2
	public int rs;// src register 1
	public int rd;// destination register
	public int effectiveAddress;// effective address if load or store	
	public int issueCycle ;
	public int startExecute;
	public int endExecute;
	public int writebackCycle;
	public Instruction(String opcode, int latency, int rt, int rs, int rd, int effectiveAddress) {
		super();
		this.opcode = opcode;
		this.latency = latency;
		this.rt = rt;
		this.rs = rs;
		this.rd = rd;
		this.effectiveAddress = effectiveAddress;
		this.issueCycle=0 ;
		this.startExecute=0;
		this.endExecute=0;
		this.writebackCycle=0;
	}

	public String toString()
	{
		String out = "OPCODE >> " + this.opcode + "-- RT >> " + this.rt + "-- RS >> " + this.rs +
				"-- RD >>" + this.rd + "-- EFFECTIVE ADDRESS >>" + this.effectiveAddress;

		return out;
	}
	
}
