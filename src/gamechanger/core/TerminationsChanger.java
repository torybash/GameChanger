package gamechanger.core;

import gamechanger.parsing.Sprite;
import gamechanger.parsing.Termination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class TerminationsChanger {

	static Random r = new Random();
	
	static String[] possibleTerminationClasses = new String[]{"SpriteCounter", "MultiSpriteCounter"};
	private static final HashMap<String, String[]> possibeTerminationParameters = new HashMap<String, String[]>();
	static {
		possibeTerminationParameters.put("SpriteCounter", new String[]{"stype", "limit", "win"});
		possibeTerminationParameters.put("MultiSpriteCounter", new String[]{"stype1", "stype2", "limit", "win"});
	}
	
	
	static double changeTerminationClassChance = 0.2;
	static double changeTerminationParametersChance = 0.4;
	static void changeTerminations(ArrayList<Sprite> sprites, ArrayList<Termination> terms, int amountTerminations) {
		
		//Change existing terminations
		for (Termination term : terms) {
			if (changeTerminationClassChance > r.nextDouble()){
				term.term = getRandomTerminationClass(sprites);
				term.parameters = getRandomTerminationParameters(term, sprites, term.parameters.get("win").equalsIgnoreCase("TRUE"));
			}else if (changeTerminationParametersChance > r.nextDouble()){
				term.parameters = getRandomTerminationParameters(term, sprites, term.parameters.get("win").equalsIgnoreCase("TRUE"));
			}
		}
		
		//Make new terminations
		int terminationsToMake = amountTerminations - terms.size();
		for (int i = 0; i < terminationsToMake; i++) {
			Termination term = new Termination();
			term.term = getRandomTerminationClass(sprites);
			term.parameters = getRandomTerminationParameters(term, sprites, i%2==0);
			terms.add(term);
		}
	}

	static double loseTerminationIsWithAvatarChance = 0.9;
	static double haveType2Chance = 0.9;
	static HashMap<String, String> getRandomTerminationParameters(Termination term, ArrayList<Sprite> sprites, boolean win) {
		HashMap<String, String> result = new HashMap<String, String>();

		String[] possibleParams = possibeTerminationParameters.get(term.term);
		
		boolean existTermination = false;
		if (haveType2Chance <= r.nextDouble() && term.term.equals("MultiSpriteCounter")){
			existTermination = true;
		};
		
		for (int i = 0; i < possibleParams.length; i++) {
			String paramType = possibleParams[i];
			switch (paramType) {
			case "stype":
				if (!win && loseTerminationIsWithAvatarChance > r.nextDouble()){
					result.put(paramType, GameChanger.avatarName);
				}else{
					result.put(paramType, GameChanger.getRandomSprite(sprites));
				}
				break;
			case "stype1":
				if (!win && !existTermination && loseTerminationIsWithAvatarChance > r.nextDouble()){
					result.put(paramType, GameChanger.avatarName);
				}else{
					result.put(paramType, GameChanger.getRandomSprite(sprites));
				}
				break;
			case "stype2":
				if (haveType2Chance > r.nextDouble()){
					result.put(paramType, GameChanger.getRandomSprite(sprites));
				}
				break;
			case "limit":
				if (existTermination){
					result.put(paramType,"1"); // win/lose when a certain sprite exists
				}else{
					result.put(paramType,"0"); // win/lose when all of certain sprite type are removed
				}
				
				break;
			case "win":
				if (win){
					result.put(paramType, "TRUE");
				}else{
					result.put(paramType, "FALSE");
				}
				break;
			default:
				throw new RuntimeErrorException(null);
			}
		}
		return result;
	}

	static String getRandomTerminationClass(ArrayList<Sprite> sprites) {
		int idx = GameChanger.range(0, possibleTerminationClasses.length-1);
		return possibleTerminationClasses[idx];
	}
}
