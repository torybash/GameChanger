package dataanalysis.controller;

import dataanalysis.controller.Controller.ControllerType;

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

	public static Controller[] getMainControllers() {
		Controller dontDieController = new Controller("Explorer", "dontDie", ControllerType.INTELLIGENT);
		Controller mctsController = new Controller("MCTS", "sampleMCTS", ControllerType.INTELLIGENT);
		Controller gaController = new Controller("GA", "sampleGA", ControllerType.INTELLIGENT);
		Controller rosController = new Controller("Onestep-S", "randomOneStep", ControllerType.SEMI_INTELLIGENT);
		Controller onestepController = new Controller("Onestep-H", "sampleonesteplookahead", ControllerType.SEMI_INTELLIGENT);
		Controller randomController = new Controller("Random", "random", ControllerType.RANDOM);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);
		
		Controller mctsishController = new Controller("MCTSish", "MCTSish", ControllerType.INTELLIGENT);
		
		return new Controller[]{dontDieController, rosController, randomController, doNothingController};
	}
}
