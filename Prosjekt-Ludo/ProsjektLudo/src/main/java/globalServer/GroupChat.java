package globalServer;

public class GroupChat {

	private int ID;
	private String name;
	
	public GroupChat(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}
	
	public int returnID() {
		return ID;
	}
	
	public String returnName() {
		return name;
	}
}
