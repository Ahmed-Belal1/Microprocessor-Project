import java.util.ArrayList;

public class CommonBus {

	ArrayList<StationItem> bus;

	public CommonBus() {
		this.bus = new ArrayList<StationItem>();
	}

	public void updateTag(String tag, float data) {
		for (int i = 0; i < bus.size(); i++) {
			StationItem s = bus.get(i);
			if (s.firstWaiting.equals(tag)) {
				s.firstWaiting = "";
				s.firstValue = data;
			}
			if (s.secondWaiting.equals(tag)) {
				s.secondValue = data;
				s.secondWaiting = "";
			}

			if (s.firstWaiting.equals("") && s.secondWaiting.equals("")) {
				bus.remove(s);
				i--;
			}

		}

	}

	public void requestUpdate(StationItem s) {
		bus.add(s);
	}

}
