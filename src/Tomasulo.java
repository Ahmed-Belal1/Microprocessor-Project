import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Tomasulo {
	public float[] memory;
	public HashMap<String, Integer> latencies;
	public Queue<Instruction> instructions;
	public ArrayList<Instruction> traceInstructionTable;
	public RegisterData[] registerFile;
	public StationItem[] loadBuffer;
	public StationItem[] storeBuffer;
	public StationItem[] additionStation;
	public StationItem[] multiplicationStation;
	public int loadcount;
	public int storecount;
	public int addcount;
	public int mulcount;
	public CommonBus bus;

	public Tomasulo() {
		this.memory = new float[2048];
		this.latencies = new HashMap<String, Integer>();
		this.instructions = new LinkedList<Instruction>();
		this.traceInstructionTable = new ArrayList<Instruction>();
		this.registerFile = new RegisterData[33];
		this.loadBuffer = new StationItem[3];
		this.storeBuffer = new StationItem[3];
		this.additionStation = new StationItem[3];
		this.multiplicationStation = new StationItem[3];
		this.bus = new CommonBus();
		this.loadcount = 0;
		this.storecount = 0;
		this.addcount = 0;
		this.mulcount = 0;
	}

	public void issue(int cycle) {
		Instruction instruction = this.instructions.peek();
		switch (instruction.opcode) {
		case "ADD": {
			if (this.addcount < 3) {
				instruction.issueCycle = cycle;
				this.instructions.remove();
				this.addcount++;
				StationItem item = new StationItem();
				item.inst = instruction;
				item.tag = "A" + addcount;
				item.remainingCycles = this.latencies.get("ADD");

				if (this.registerFile[instruction.rs].tag == "0") {
					item.firstValue = this.registerFile[instruction.rs].data;
				} else {
					item.firstWaiting = this.registerFile[instruction.rs].tag;
				}

				if (this.registerFile[instruction.rt].tag == "0") {
					item.secondValue = this.registerFile[instruction.rt].data;
				} else {
					item.secondWaiting = this.registerFile[instruction.rt].tag;
				}

				if (!item.firstWaiting.equals("") || !item.secondWaiting.equals("")) {
					this.bus.requestUpdate(item);
				}

				this.registerFile[instruction.rd].tag = item.tag;
				item.busy = true;
				for (int i = 0; i < this.additionStation.length; i++) {
					if (this.additionStation[i] == null || !this.additionStation[i].busy) {
						this.additionStation[i] = item;
						break;
					}
				}
			}
			break;
		}
		case "SUB": {
			if (this.addcount < 3) {
				this.instructions.remove();
				instruction.issueCycle = cycle;
				this.addcount++;
				StationItem item = new StationItem();
				item.inst = instruction;
				item.tag = "A" + addcount;
				item.remainingCycles = this.latencies.get("SUB");
				if (this.registerFile[instruction.rs].tag == "0") {
					item.firstValue = this.registerFile[instruction.rs].data;
				} else {
					item.firstWaiting = this.registerFile[instruction.rs].tag;

				}
				if (this.registerFile[instruction.rt].tag == "0") {
					item.secondValue = this.registerFile[instruction.rt].data;
				} else {
					item.secondWaiting = this.registerFile[instruction.rt].tag;

				}
				if (!item.firstWaiting.equals("") || !item.secondWaiting.equals("")) {
					this.bus.requestUpdate(item);
				}

				this.registerFile[instruction.rd].tag = item.tag;
				item.busy = true;
				for (int i = 0; i < this.additionStation.length; i++) {
					if (this.additionStation[i] == null || !this.additionStation[i].busy) {
						this.additionStation[i] = item;
						break;
					}
				}
			}
			break;
		}
		case "MUL": {
			if (this.mulcount < 3) {
				this.instructions.remove();
				instruction.issueCycle = cycle;
				this.mulcount++;
				StationItem item = new StationItem();
				item.inst = instruction;
				item.tag = "M" + mulcount;
				item.remainingCycles = this.latencies.get("MUL");
				if (this.registerFile[instruction.rs].tag == "0") {
					item.firstValue = this.registerFile[instruction.rs].data;
				} else {
					item.firstWaiting = this.registerFile[instruction.rs].tag;
				}
				if (this.registerFile[instruction.rt].tag == "0") {
					item.secondValue = this.registerFile[instruction.rt].data;
				} else {
					item.secondWaiting = this.registerFile[instruction.rt].tag;
				}

				if (!item.firstWaiting.equals("") || !item.secondWaiting.equals("")) {
					this.bus.requestUpdate(item);
				}

				this.registerFile[instruction.rd].tag = item.tag;
				item.busy = true;
				for (int i = 0; i < this.multiplicationStation.length; i++) {
					if (this.multiplicationStation[i] == null || !this.multiplicationStation[i].busy) {
						this.multiplicationStation[i] = item;
						break;
					}

				}
				break;
			}
		}
		case "DIV": {
			if (this.mulcount < 3) {
				this.instructions.remove();
				instruction.issueCycle = cycle;
				this.mulcount++;
				StationItem item = new StationItem();
				item.inst = instruction;
				item.tag = "M" + mulcount;
				item.remainingCycles = this.latencies.get("DIV");
				if (this.registerFile[instruction.rs].tag == "0") {
					item.firstValue = this.registerFile[instruction.rs].data;
				} else {
					item.firstWaiting = this.registerFile[instruction.rs].tag;
				}
				if (this.registerFile[instruction.rt].tag == "0") {
					item.secondValue = this.registerFile[instruction.rt].data;
				} else {
					item.secondWaiting = this.registerFile[instruction.rt].tag;
				}

				if (!item.firstWaiting.equals("") || !item.secondWaiting.equals("")) {
					this.bus.requestUpdate(item);
				}

				this.registerFile[instruction.rd].tag = item.tag;
				item.busy = true;
				for (int i = 0; i < this.multiplicationStation.length; i++) {
					if (this.multiplicationStation[i] == null || !this.multiplicationStation[i].busy) {
						this.multiplicationStation[i] = item;
						break;
					}

				}
				break;
			}
			break;
		}

		case "LD": {
			if (this.loadcount < 3) {
				this.instructions.remove();
				instruction.issueCycle = cycle;
				this.loadcount++;
				StationItem item = new StationItem();
				item.inst = instruction;
				item.tag = "L" + this.loadcount;
				item.remainingCycles = this.latencies.get("LD");
				item.effectiveAdress = instruction.effectiveAddress;

				this.registerFile[instruction.rd].tag = item.tag;

				item.busy = true;
				for (int i = 0; i < this.loadBuffer.length; i++) {
					if (this.loadBuffer[i] == null || !this.loadBuffer[i].busy) {
						this.loadBuffer[i] = item;
						break;
					}

				}

			}
			break;
		}
		case "SD": {
			if (this.storecount < 3) {

				this.instructions.remove();
				instruction.issueCycle = cycle;
				this.storecount++;
				StationItem item = new StationItem();
				item.inst = instruction;
				item.tag = "S" + this.storecount;
				item.remainingCycles = this.latencies.get("SD");
				item.effectiveAdress = instruction.effectiveAddress;

				if (this.registerFile[instruction.rt].tag == "0") {
					item.firstValue = this.registerFile[instruction.rt].data;
				} else {
					item.firstWaiting = this.registerFile[instruction.rt].tag;
					this.bus.requestUpdate(item);
				}

				item.busy = true;
				for (int i = 0; i < this.storeBuffer.length; i++) {
					if (this.storeBuffer[i] == null || !this.storeBuffer[i].busy) {
						this.storeBuffer[i] = item;
						break;
					}

				}
			}
			break;
		}
		}
	}

	public void execute(int cycle) {

		for (int i = 0; i < this.additionStation.length; i++) {
			if (this.additionStation[i].busy && this.additionStation[i].firstWaiting.equals("")
					&& this.additionStation[i].secondWaiting.equals("")) {
				this.additionStation[i].remainingCycles--;
				if (this.additionStation[i].inst.startExecute == 0) {
					this.additionStation[i].inst.startExecute = cycle;
				}
				if (this.additionStation[i].remainingCycles == 0 && this.additionStation[i].inst.endExecute == 0) {
					this.additionStation[i].inst.endExecute = cycle;
				}
			}
		}

		for (int i = 0; i < this.multiplicationStation.length; i++) {
			if (this.multiplicationStation[i].busy && this.multiplicationStation[i].firstWaiting.equals("")
					&& this.multiplicationStation[i].secondWaiting.equals("")) {
				this.multiplicationStation[i].remainingCycles--;
				if (this.multiplicationStation[i].inst.startExecute == 0) {
					this.multiplicationStation[i].inst.startExecute = cycle;
				}
				if (this.multiplicationStation[i].remainingCycles == 0
						&& this.multiplicationStation[i].inst.endExecute == 0) {
					this.multiplicationStation[i].inst.endExecute = cycle;
				}
			}
		}

		for (int i = 0; i < this.loadBuffer.length; i++) {
			if (this.loadBuffer[i].busy) {
				this.loadBuffer[i].remainingCycles--;

				if (this.loadBuffer[i].inst.startExecute == 0) {
					this.loadBuffer[i].inst.startExecute = cycle;
				}
				if (this.loadBuffer[i].remainingCycles == 0 && this.loadBuffer[i].inst.endExecute == 0) {
					this.loadBuffer[i].inst.endExecute = cycle;
				}
			}
		}
		
		for (int i=0;i<this.storeBuffer.length;i++) {
			if (this.storeBuffer[i].busy&& this.storeBuffer[i].firstWaiting.equals("")) {
				this.storeBuffer[i].remainingCycles--;
				if (this.storeBuffer[i].inst.startExecute == 0) {
					this.storeBuffer[i].inst.startExecute = cycle;
				}
				if (this.storeBuffer[i].remainingCycles == 0 && this.storeBuffer[i].inst.endExecute == 0) {
					this.storeBuffer[i].inst.endExecute = cycle;
				}
			}
		}

	}
	
	public void writeBack(int cycle) {
		
		for (int i = 0; i < this.additionStation.length; i++) {
			if (this.additionStation[i].busy && this.additionStation[i].remainingCycles<0) {
				float res=0;
				float v1= this.additionStation[i].firstValue;
				float v2= this.additionStation[i].secondValue;
				this.additionStation[i].inst.writebackCycle=cycle;
				if (this.additionStation[i].inst.opcode.equals("ADD")) {
					res =v1+v2;	
				}else {
					res=v1-v2;
				}
				int regdest= this.additionStation[i].inst.rd;
				if(registerFile[regdest].tag.equals(this.additionStation[i].tag)) {
					registerFile[regdest].data=res;
					registerFile[regdest].tag="0";
				}
				bus.updateTag(this.additionStation[i].tag, res);
				this.additionStation[i].busy=false;
				this.addcount--;
			}
		}
		
		for (int i = 0; i < this.multiplicationStation.length; i++) {
			if (this.multiplicationStation[i].busy && this.multiplicationStation[i].remainingCycles<0) {
				float res=0;
				float v1= this.multiplicationStation[i].firstValue;
				float v2= this.multiplicationStation[i].secondValue;
				this.multiplicationStation[i].inst.writebackCycle=cycle;
				if (this.multiplicationStation[i].inst.opcode.equals("MUL")) {
					res =v1*v2;	
				}else {
					res=v1/v2;
				}
				int regdest= this.multiplicationStation[i].inst.rd;
				if(registerFile[regdest].tag.equals(this.multiplicationStation[i].tag)) {
					registerFile[regdest].data=res;
					registerFile[regdest].tag="0";
				}
				bus.updateTag(this.multiplicationStation[i].tag, res);
				this.multiplicationStation[i].busy=false;
				this.mulcount--;
			}
		}
		
		for(int i=0;i<this.loadBuffer.length;i++) {
			if (this.loadBuffer[i].busy && this.loadBuffer[i].remainingCycles<0) {
				int regdest= this.loadBuffer[i].inst.rd;
				float res= this.memory[this.loadBuffer[i].effectiveAdress];
				this.loadBuffer[i].inst.writebackCycle=cycle;
				if(registerFile[regdest].tag.equals(this.loadBuffer[i].tag)) {
					registerFile[regdest].data=res;
					registerFile[regdest].tag="0";
				}
				bus.updateTag(this.loadBuffer[i].tag, res);
				this.loadBuffer[i].busy=false;
				this.loadcount--;
			}
		}
		
		for(int i=0;i<this.storeBuffer.length;i++) {
			if (this.storeBuffer[i].busy && this.storeBuffer[i].remainingCycles<0) {
				int regdest= this.storeBuffer[i].inst.rt;
				this.storeBuffer[i].inst.writebackCycle=cycle;
				float res= this.registerFile[regdest].data;
				this.memory[this.storeBuffer[i].effectiveAdress]=res;
				this.storeBuffer[i].busy=false;
				this.loadcount--;
			}
		}

	}

	public void runProgram(String instructionsFile, String latencyFile) throws IOException {
		parse(instructionsFile, latencyFile);

		int cycle = 0;

		while (!this.instructions.isEmpty() && (!(this.loadcount == 0) || !(this.addcount == 0) || !(this.mulcount == 0)
				|| !(this.storecount == 0))) {

			cycle++;
			issue(cycle);
			execute(cycle);
			writeBack(cycle);
			

		}
	}

	// method parsing the instruction file and the latency file
	public void parse(String instructionsFile, String latencyFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(latencyFile));
		String st;
		while ((st = br.readLine()) != null) {
			String[] line = st.split(" ");
			this.latencies.put(line[0], Integer.parseInt(line[1]));
		}

		br = new BufferedReader(new FileReader(instructionsFile));
		String st1;
		while ((st1 = br.readLine()) != null) {
			String[] line = st.split(" ");
			switch (line[0]) {
			case "ADD": {
				Instruction inst = new Instruction("ADD", this.latencies.get("ADD"),
						Integer.parseInt(line[3].substring(1)), Integer.parseInt(line[2].substring(1)),
						Integer.parseInt(line[1].substring(1)), -1);
				this.instructions.add(inst);
				this.traceInstructionTable.add(inst);
				break;
			}
			case "SUB": {
				Instruction inst = new Instruction("SUB", this.latencies.get("SUB"),
						Integer.parseInt(line[3].substring(1)), Integer.parseInt(line[2].substring(1)),
						Integer.parseInt(line[1].substring(1)), -1);
				this.instructions.add(inst);
				this.traceInstructionTable.add(inst);
				break;
			}
			case "MUL": {
				Instruction inst = new Instruction("MUL", this.latencies.get("MUL"),
						Integer.parseInt(line[3].substring(1)), Integer.parseInt(line[2].substring(1)),
						Integer.parseInt(line[1].substring(1)), -1);
				this.instructions.add(inst);
				this.traceInstructionTable.add(inst);

				break;
			}
			case "DIV": {
				Instruction inst = new Instruction("DIV", this.latencies.get("DIV"),
						Integer.parseInt(line[3].substring(1)), Integer.parseInt(line[2].substring(1)),
						Integer.parseInt(line[1].substring(1)), -1);
				this.instructions.add(inst);
				this.traceInstructionTable.add(inst);
				break;
			}
			case "LD": {
				Instruction inst = new Instruction("LD", this.latencies.get("LD"), -1, -1,
						Integer.parseInt(line[1].substring(1)), Integer.parseInt(line[2]));
				this.instructions.add(inst);
				this.traceInstructionTable.add(inst);
				break;
			}
			case "SD": {
				Instruction inst = new Instruction("SD", this.latencies.get("SD"),
						Integer.parseInt(line[1].substring(1)), -1, -1, Integer.parseInt(line[2]));
				this.instructions.add(inst);
				this.traceInstructionTable.add(inst);
				break;
			}
			default: {
				break;
			}
			}

		}
	}

	public static void main(String[] args) {

	}
}
