package dataanalysis.controller;

public class ControllerHelper {
	public static void setControllerDataFolders(Controller[] controllers, String folder, String data){
		
		for (int i = 0; i < controllers.length; i++) {
			Controller ctrl = controllers[i];
			ctrl.dataFolder = folder + ctrl.folderName + data;
		}
	}
	
	public static Controller[] copyControllers(Controller[] controllers){
		Controller[] result = new Controller[controllers.length];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = controllers[i].copy();
		}
		
		
		
		return result;
		
	}
}
