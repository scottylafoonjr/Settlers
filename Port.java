
public class Port {
	
	public int joint1;
	public int joint2;
	ResourceCardType type = null;
	
	
	public Port(int joint1, int joint2){
		this.joint1 = joint1;
		this.joint2 = joint2;
	}
	
	
	public Port(int joint1, int joint2, ResourceCardType type){
		this.joint1 = joint1;
		this.joint2 = joint2;
		this.type = type;
	}
	

}
