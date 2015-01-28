package levelgenerator.map;

public class Mapping {
	public char charID;
	public boolean isSingleton = false;
	public boolean isWall = false;
	public boolean isAvatar = false;
	
	public Mapping(char charID, boolean isSingleton){
		this.charID = charID;
		this.isSingleton = isSingleton;
		
		if (charID == "w".charAt(0)) isWall = true;
		if (charID == "A".charAt(0)){
			isAvatar = true;
			isSingleton = true;
		}
	}
	
	public String toString() {
		return "{Mapping: " + charID + " isSingleton: " + isSingleton + ", isWall: " + isWall + ", isAvatar: " + isAvatar +"}";
	}
}
