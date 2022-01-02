import java.util.ArrayList;

public class CommonBus {
	
	ArrayList<StationItem> bus;
	
	public CommonBus() {
		this.bus=new ArrayList<StationItem>();
	}
	
	
	public void updateTag(String tag,float data) {
		for (StationItem s:bus ) {
			if (s.firstWaiting.equals(tag)) {
				s.firstWaiting="";
				s.firstValue=data;
			}else if (s.secondWaiting.equals(tag)) {
				s.secondValue=data;
				s.secondWaiting="";
			}
			
			if (s.firstWaiting.equals("") && s.secondWaiting.equals("")) {
				bus.remove(s);
			}
		}
		
		
	}
	
	public void requestUpdate(StationItem s) {
		bus.add(s);
	}

}
