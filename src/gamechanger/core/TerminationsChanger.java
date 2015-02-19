package gamechanger.core;

import gamechanger.parsing.Sprite;
import gamechanger.parsing.Termination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		possibeTerminationParameters.put("Timeout", new String[]{"limit", "win"});
	}
	
	//Change chances
	static double changeTerminationClassChance = 0.25;
	static double changeTerminationParametersChance = 0.0;
	
	static void changeTerminations(ArrayList<Sprite> sprites, ArrayList<Termination> terms, int amountTerminations) {
		
		//Remove existing terminations
		int termsToRemove = terms.size() - amountTerminations;
		if (termsToRemove > 0){
			Integer[] termsIndices = new Integer[termsToRemove];
			for (int i = 0; i < termsIndices.length; i++) {
				 int idx = GameChanger.range(0, terms.size()-1);
				 
				 boolean valid = true;
				 for (int j = 0; j < termsIndices.length; j++) {
					 if (termsIndices[j] == null) continue;
					 if (idx == termsIndices[j]) valid = false;
				 }
				 if (valid) termsIndices[i] = idx;
				 else i--;
			}
			Arrays.sort(termsIndices, Collections.reverseOrder());
			for (int i = 0; i < termsIndices.length; i++) {
				terms.remove((int)termsIndices[i]);
			}
		}
		
		//Change existing terminations
		for (Termination term : terms) {
			if (changeTerminationClassChance > r.nextDouble()){
				term.term = getRandomTerminationClass(sprites);
				term.parameters = getRandomTerminationParameters(term, sprites, terms, term.parameters.get("win").equalsIgnoreCase("TRUE"));
			}else if (changeTerminationParametersChance > r.nextDouble()){
				term.parameters = getRandomTerminationParameters(term, sprites, terms, term.parameters.get("win").equalsIgnoreCase("TRUE"));
			}
		}
		
		//Make new terminations
		int terminationsToMake = amountTerminations - terms.size();
		for (int i = 0; i < terminationsToMake; i++) {
			Termination term = new Termination();
			term.term = getRandomTerminationClass(sprites);
			term.parameters = getRandomTerminationParameters(term, sprites, terms, i%2==0);
			terms.add(term);
		}
	}

	static double winTerminationIsWithAvatarChance = 0.1;
	static double loseTerminationIsWithAvatarChance = 0.9;
	static double multiCounterIsExistCheck = 0.8;
	static double haveType2Chance = 0.5;
	static HashMap<String, String> getRandomTerminationParameters(Termination term, ArrayList<Sprite> sprites, ArrayList<Termination> terms, boolean win) {
		HashMap<String, String> result = new HashMap<String, String>();

		

		
		String[] possibleParams = possibeTerminationParameters.get(term.term);
		
		boolean existTermination = false;
		if (r.nextDouble() < multiCounterIsExistCheck && term.term.equals("MultiSpriteCounter")){
			existTermination = true;
		}
		
		ArrayList<String> forbiddenStyps = new ArrayList<String>();
		for (Termination t : terms) {
			if (t!=term){
				if (t.parameters.get("stype") != null) forbiddenStyps.add(t.parameters.get("stype"));
				if (t.parameters.get("stype1") != null) forbiddenStyps.add(t.parameters.get("stype1"));
				if (t.parameters.get("stype2") != null) forbiddenStyps.add(t.parameters.get("stype2"));
			}
		}
		
		boolean avatarUsed = false;
		for (String string : forbiddenStyps) {
			if (string.equals("avatar")){
				avatarUsed = true;
				break;
			}
		}

		
//		System.out.println("making terms .. sprites: " + sprites);
//		System.out.println("terms: " + terms);
//		System.out.println("This term: " + term);
//		System.out.println("Forbidden from choosin types: "+ forbiddenStyps);
//		System.out.println("Avatar used? " + avatarUsed);
		
		for (int i = 0; i < possibleParams.length; i++) {
			String paramType = possibleParams[i];
			switch (paramType) {
			case "stype":
				
				if (avatarUsed){
					result.put(paramType, getRandomSprite(sprites, forbiddenStyps));
				}else{
					if (win && r.nextDouble() < winTerminationIsWithAvatarChance){
						result.put(paramType, GameChanger.avatarNames.get(0));
						forbiddenStyps.add(GameChanger.avatarNames.get(0));
					}else if (!win && r.nextDouble() < loseTerminationIsWithAvatarChance){
						result.put(paramType, GameChanger.avatarNames.get(0));
						forbiddenStyps.add(GameChanger.avatarNames.get(0));
					}else{
						forbiddenStyps.add("avatar");
						result.put(paramType, getRandomSprite(sprites, forbiddenStyps));
					}
				}
				
				break;
			case "stype1":
				String styp1res = "";

				if (avatarUsed){
					styp1res = getRandomSprite(sprites, forbiddenStyps);
				}else{
					if (win && r.nextDouble() < winTerminationIsWithAvatarChance){
						styp1res = GameChanger.avatarNames.get(0);
					}else if (!win && r.nextDouble() < loseTerminationIsWithAvatarChance){
						styp1res = GameChanger.avatarNames.get(0);
					}else{
						forbiddenStyps.add("avatar");
						styp1res = getRandomSprite(sprites, forbiddenStyps);
					}
				}
				result.put(paramType, styp1res);
				forbiddenStyps.add(styp1res);
				break;
			case "stype2":
				if (haveType2Chance <= r.nextDouble() && !existTermination){
					result.put(paramType, getRandomSprite(sprites, forbiddenStyps));
				}else{
					result.put(paramType, "null");
				}
				break;
			case "limit":
				if (term.term.equals("Timeout")){
					result.put(paramType, ""+GameChanger.range(500, 2000));
				}else{
					if (existTermination){
						result.put(paramType,"1"); // win/lose when a certain sprite exists
					}else{
						result.put(paramType,"0"); // win/lose when all of certain sprite type are removed
					}
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

	private static String getRandomSprite(ArrayList<Sprite> sprites, ArrayList<String> forbiddenStyps) {

		int idx = GameChanger.range(0, sprites.size()-1);
		boolean isGood = false;
		String newSpriteName = sprites.get(idx).identifier;
		while (!isGood){
			isGood = true;
			for (String styp : forbiddenStyps) {
				if (styp != null && styp.equals(newSpriteName)){
					idx = GameChanger.range(0, sprites.size()-1);
					newSpriteName = sprites.get(idx).identifier;
					isGood = false;
					break;
				}
			}
		}
		return newSpriteName;
	}


	static String getRandomTerminationClass(ArrayList<Sprite> sprites) {
		int idx = GameChanger.range(0, possibleTerminationClasses.length-1);
		return possibleTerminationClasses[idx];
	}
}
