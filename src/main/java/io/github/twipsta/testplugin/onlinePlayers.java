package io.github.twipsta.testplugin;

public class onlinePlayers {
	String name;
	Long logonTime;
	Double totalCredits;
	Long lastTopUp;
	Long lastOnline;
	
	public onlinePlayers() {

	}

	public onlinePlayers(String name, long logonTime, double totalCredits, long lastTopUp, long lastOnline) {
		this.name = name;
		this.logonTime = logonTime;
		this.totalCredits = totalCredits;
		this.lastTopUp = lastTopUp;
		this.lastOnline = lastOnline;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLastTopUp(long lastTopUp) {
		this.lastTopUp = lastTopUp;
	}
	
	public void setLastOnline(long lastOnline) {
		this.lastOnline = lastOnline;
	}
	
	public void setTotalCredits(double totalCredits) {
		this.totalCredits = totalCredits;
	}
	
	public long getSessionTime() {
		return this.logonTime;
	}
	
}
