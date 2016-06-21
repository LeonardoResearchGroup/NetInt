package utilities;

public class HeapMessenger {
	private int hops;
	private int currentHop;
	private String message;
	private String log;
	private int nLog;
	
	public HeapMessenger() {
		log = "";
		nLog = 0;
	}

	public int getHops() {
		return hops;
	}

	public int getCurrentHop() {
		return currentHop;
	}

	public String getMessage() {
		return message;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public void setCurrentHope(int currentHop) {
		this.currentHop = currentHop;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addToLog(String msn){
		log = log.concat(nLog +": "+msn+"\n");
		nLog++;
	}

	public String getLog() {
		return log;
	}
	
	
	
	
	
}
