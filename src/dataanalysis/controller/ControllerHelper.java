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
		Controller explorerController = new Controller("Explorer", "Explorer", ControllerType.INTELLIGENT);
		Controller mctsController = new Controller("MCTS", "sampleMCTS", ControllerType.INTELLIGENT);
		Controller olMctsController = new Controller("MCTS", "sampleOLMCTS", ControllerType.INTELLIGENT);
		Controller gaController = new Controller("GA", "sampleGA", ControllerType.INTELLIGENT);
		Controller rosController = new Controller("Onestep-S", "randomOneStep", ControllerType.SEMI_INTELLIGENT);
		Controller onestepController = new Controller("Onestep-H", "sampleonesteplookahead", ControllerType.SEMI_INTELLIGENT);
		Controller randomController = new Controller("Random", "random", ControllerType.RANDOM);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);
		
		Controller mctsishController = new Controller("DeepSearch", "MCTSish", ControllerType.INTELLIGENT);
		
		return new Controller[]{dontDieController, mctsishController, olMctsController, rosController, randomController, doNothingController};
	}

	public static Controller[] getMainFourControllers() {
		Controller dontDieController = new Controller("Explorer", "dontDie", ControllerType.INTELLIGENT);
		Controller rosController = new Controller("Onestep-S", "randomOneStep", ControllerType.SEMI_INTELLIGENT);
		Controller randomController = new Controller("Random", "random", ControllerType.RANDOM);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);
		
		Controller mctsishController = new Controller("MCTSish", "MCTSish", ControllerType.INTELLIGENT);
		
		return new Controller[]{dontDieController, rosController, randomController, doNothingController};
	}
	
	public static Controller[] getOldControllers() {
		Controller dontDieController = new Controller("Explorer", "dontDie", ControllerType.INTELLIGENT);
		Controller mctsController = new Controller("MCTS", "mcts", ControllerType.INTELLIGENT);
		Controller gaController = new Controller("GA", "ga", ControllerType.INTELLIGENT);
		Controller rosController = new Controller("Onestep-S", "randomonestep", ControllerType.SEMI_INTELLIGENT);
		Controller onestepController = new Controller("Onestep-H", "onestep", ControllerType.SEMI_INTELLIGENT);
		Controller randomController = new Controller("Random", "random", ControllerType.RANDOM);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);
		
		Controller mctsishController = new Controller("MCTSish", "MCTSish", ControllerType.INTELLIGENT);
		
		return new Controller[]{dontDieController, mctsController, gaController, rosController, onestepController, randomController, doNothingController};
	}

	public static Controller[] getOtherFourControllers() {
		Controller rosController = new Controller("Onestep-S", "randomOneStep", ControllerType.SEMI_INTELLIGENT);
		Controller randomController = new Controller("Random", "random", ControllerType.RANDOM);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);
		
		Controller mctsishController = new Controller("DeepSearch", "MCTSish", ControllerType.INTELLIGENT);
		
		return new Controller[]{mctsishController, rosController, randomController, doNothingController};

	}

	public static Controller[] getTheTwoControllers() {
		Controller mctsishController = new Controller("DeepSearch", "MCTSish", ControllerType.INTELLIGENT);
		Controller doNothingController = new Controller("Do Nothing", "doNothing", ControllerType.DO_NOTHING);

		return new Controller[]{mctsishController, doNothingController};
	}
}
