package dataanalysis.controller;

public class Controller {
	public String name;
	public String folderName;
	public ControllerType type;
	
	public String dataFolder;
	
	public Controller(String name, String folderName, ControllerType type){
		this.name = name;
		this.folderName = folderName;
		this.type = type;
	}
	
	public static enum ControllerType{
		INTELLIGENT(0),
//		SEMI_INTELLIGENT(1),
//		STUPID(2);
		RANDOM(1),
		DO_NOTHING(2);
//		
		 private int id;
 
		 private ControllerType(int id) {
		   this.id = id;
		 }
		 
		 public int id() {
		   return id;
		 }
	}

	public Controller copy() {
		return new Controller(name, folderName, type);
	}
}
