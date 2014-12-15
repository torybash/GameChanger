package gamechanger.core;

import gamechanger.parsing.Interaction;
import gamechanger.parsing.Sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class InteractionsChanger {

	static Random r = new Random();
	
	private static final String[] possibleFunctions = {"attractGaze", "bounceForward", "collectResource", "killIfFromAbove",
		"killIfOtherHasMore", "pullWithIt", "teleportToExit", "wallStop", "changeResource", "cloneSprite", "flipDirection", 
		"killIfHasLess", "killIfHasMore", "killSprite", "reverseDirection", "spawnIfHasMore", "stepBack", "transformTo", 
		"turnAround", "undoAll", "wrapAround"};

	private static final HashMap<String, String[]> possibleFunctionParameters = new HashMap<String, String[]>();
	

	static{
		possibleFunctionParameters.put("attractGaze", new String[]{"scoreChange"});
		possibleFunctionParameters.put("bounceForward", new String[]{"scoreChange"});
		possibleFunctionParameters.put("collectResource", new String[]{"scoreChange"});
		possibleFunctionParameters.put("killIfFromAbove", new String[]{"scoreChange"});
		possibleFunctionParameters.put("killIfOtherHasMore", new String[]{"scoreChange", "resource", "limit"});
		possibleFunctionParameters.put("pullWithIt", new String[]{"scoreChange"});
		possibleFunctionParameters.put("teleportToExit", new String[]{"scoreChange"});
		possibleFunctionParameters.put("wallStop", new String[]{"scoreChange"});
		possibleFunctionParameters.put("changeResource", new String[]{"scoreChange", "resource", "value"});
		possibleFunctionParameters.put("cloneSprite", new String[]{"scoreChange"});
		possibleFunctionParameters.put("flipDirection", new String[]{"scoreChange"});
		possibleFunctionParameters.put("killIfHasLess", new String[]{"scoreChange", "resource", "limit"});
		possibleFunctionParameters.put("killIfHasMore", new String[]{"scoreChange", "resource", "limit"});
		possibleFunctionParameters.put("killSprite", new String[]{"scoreChange"});
		possibleFunctionParameters.put("reverseDirection", new String[]{"scoreChange"});
		possibleFunctionParameters.put("spawnIfHasMore", new String[]{"scoreChange", "stype", "resource", "limit"});
		possibleFunctionParameters.put("stepBack", new String[]{"scoreChange"});
		possibleFunctionParameters.put("transformTo", new String[]{"scoreChange", "stype"});
		possibleFunctionParameters.put("turnAround", new String[]{"scoreChange"});
		possibleFunctionParameters.put("undoAll", new String[]{"scoreChange"});
		possibleFunctionParameters.put("wrapAround", new String[]{"scoreChange"});
	}
	
	//Change chances
	static double chanceToMutateSprite1 = 0.33f; //and everthing else
	static double chanceToMutateSprite2 = 0.0f; //and effect+paramers
	static double chanceToMutateEffect = 0.0f; //and parameters
	static double chanceToMutateParameters = 0.0f;
	
	//Parameter chances
	static double chanceToHaveScoreChange = 0.3f;
	
	static void changeInteractions(ArrayList<Sprite> sprites, ArrayList<Interaction> interacts, int amountInteractions) {
		int currAmount = interacts.size();

		
		//Sprite set checks
		GameChanger.setAvatar(sprites);
		GameChanger.loadResourceSprites(sprites);
		
		//Remove interactions
		if (amountInteractions < interacts.size()){
			Integer[] interactIndices = new Integer[interacts.size() - amountInteractions];
			for (int i = 0; i < interactIndices.length; i++) {
				 int idx = GameChanger.range(0, currAmount-1);
				 
				 boolean valid = true;
				 for (int j = 0; j < interactIndices.length; j++) {
					 if (interactIndices[j] == null) continue;
					 if (idx == interactIndices[j]) valid = false;
				 }
				 if (valid) interactIndices[i] = idx;
				 else i--;
			}
			Arrays.sort(interactIndices, Collections.reverseOrder());
			for (int i = 0; i < interactIndices.length; i++) {
				interacts.remove((int)interactIndices[i]);
			}
		}
		
		//Change existing interactions
		int changes = 0;
		for (Interaction inter : interacts) {
			if (chanceToMutateSprite1 > r.nextFloat() || (changes == 0 && inter == interacts.get(interacts.size()-1))){ 
				//change sprite 1
				inter.sprite1 = GameChanger.getRandomSprite(sprites);

				//change sprite 2
				inter.sprite2 = getNewSprite2(inter.sprite1, sprites);
				
				//change function and parameters
				inter.function = getNewFunction(inter.sprite1, inter.sprite2, sprites);
				inter.parameters = getNewParameters(inter.sprite1, inter.sprite2, inter.function, sprites);
				changes++;
			}else if (chanceToMutateSprite2 > r.nextFloat()){ 
				//change sprite 2
				inter.sprite2 = getNewSprite2(inter.sprite1, sprites);
				
				//change function and parameters
				inter.function = getNewFunction(inter.sprite1, inter.sprite2, sprites);
				inter.parameters = getNewParameters(inter.sprite1, inter.sprite2, inter.function, sprites);
				changes++;
			}else if (chanceToMutateEffect > r.nextFloat()){ 
				//change function and parameters
				inter.function = getNewFunction(inter.sprite1, inter.sprite2, sprites);
				inter.parameters = getNewParameters(inter.sprite1, inter.sprite2, inter.function, sprites);
				changes++;
			}else if (chanceToMutateParameters > r.nextFloat()){ 
				//change paramters
				inter.parameters = getNewParameters(inter.sprite1, inter.sprite2, inter.function, sprites);
				changes++;
			}
		}
		
		//Add interactions
		int interactionsToMake = amountInteractions - interacts.size();
		for (int i = 0; i < interactionsToMake; i++) {
			Interaction inter = new Interaction();
			inter.sprite1 = GameChanger.getRandomSprite(sprites);
			inter.sprite2 = getNewSprite2(inter.sprite1, sprites);
			inter.function = getNewFunction(inter.sprite1, inter.sprite2, sprites);
			inter.parameters = getNewParameters(inter.sprite1, inter.sprite2, inter.function, sprites);
			interacts.add(inter);
		}
			
		
		
	}
	
	
	static String getNewSprite2(String sprite1, ArrayList<Sprite> sprites){
		String result = "";
		while (result.length() < 1 || (result.equals(GameChanger.avatarName) && sprite1.equals(GameChanger.avatarName))){
			int idx = GameChanger.range(0, sprites.size());
			if (idx == sprites.size()) result = "EOS";
			else result = sprites.get(idx).identifier;
		}
		return result;
	}
	
	static String getNewFunction(String sprite1, String sprite2, ArrayList<Sprite> sprites){
		String sprite1class = "";
		String sprite2class = "";
		for (Sprite sprite : sprites) {
			if (sprite1 == sprite.identifier) sprite1class = sprite.referenceClass;
			if (sprite2 == sprite.identifier) sprite2class = sprite.referenceClass;	
		}
		
		String func = "attractGaze";
		while (func.length() == 0 || (GameChanger.resourceSprites.size() == 0 && isResourceEffect(func)) || 
				(func.equals("collectResource") && !sprite1class.equals("Resource")) ||
				(func.equals("teleportToExit") && !sprite2class.equals("Portal")) ||
				(func.equals("wrapAround") && !sprite2.equals("EOS")) ||
				(func.equals("cloneSprite") && (GameChanger.isSpriteAvatar(sprite1, sprites) || GameChanger.isSpriteParent(sprite1, sprites))) ||
				(func.equals("attractGaze") && (!GameChanger.isOrientedSprite(sprite1, sprites) || !GameChanger.isOrientedSprite(sprite2, sprites)))  ||
				(isUnaryOrientationEffect(func) && !GameChanger.isOrientedSprite(sprite1, sprites))  ||
				(isBinaryEffect(func) && sprite2.equals("EOS"))){ 
			func = possibleFunctions[GameChanger.range(0, possibleFunctions.length-1)];
		}
		return func;
	}
//	unaryOrienationEffects = flipDirection reverseDirection turnAround
//	binaryOrientationEffects = attractGaze
	
	
	static boolean isBinaryEffect(String func) {
		if (func.equals("attractGaze") || func.equals("bounceForward") ||
				func.equals("killIfFromAbove") || func.equals("killIfOtherHasMore") ||
				func.equals("pullWithIt") || func.equals("collectResource") || func.equals("wallStop"))
				return true;
		return false;
	}










	private static HashMap<String,String> getNewParameters(String sprite1, String sprite2, String func, ArrayList<Sprite> sprites){
		HashMap<String,String> result = new HashMap<String,String>();
		String[] possibleParams = possibleFunctionParameters.get(func);
		
		boolean isNullResourceSpawnEffect = false;
		for (int i = 0; i < possibleParams.length; i++) {
			String paramType = possibleParams[i];
			switch (paramType) {
			case "scoreChange":
				if (r.nextFloat() > chanceToHaveScoreChange) continue;
				int scoreChange = GameChanger.range(-5, 10); //GameChanger.range(-1, 2);
				if (scoreChange == 0) continue;
				result.put(paramType, ""+scoreChange);
				break;
			case "stype":
				String spriteId = "";
				boolean validParameter = true;
				while (spriteId.length() == 0 || !validParameter){
					validParameter = true;
					if (GameChanger.haveAvatar){
						spriteId = sprites.get(GameChanger.range(0, sprites.size()-1)).identifier;
					}else{
						int idx = GameChanger.range(0, sprites.size());
						spriteId = idx == sprites.size() ? "avatar" : sprites.get(idx).identifier;
					}
					
					if (func.equals("transformTo")){
						if (GameChanger.isSpriteAvatar(sprite1, sprites) && !GameChanger.isSpriteAvatar(spriteId, sprites)) validParameter = false;
						if (!GameChanger.isSpriteAvatar(sprite1, sprites) && GameChanger.isSpriteAvatar(spriteId, sprites)) validParameter = false;
						if (GameChanger.isSpriteParent(spriteId, sprites)) validParameter = false;
					}else if (func.equals("spawnIfHasMore")){
						if (GameChanger.isSpriteAvatar(spriteId, sprites)) validParameter = false;
						if (GameChanger.isSpriteParent(spriteId, sprites)) validParameter = false;
					}
				}
				result.put(paramType, spriteId);
				break;
			case "resource":
				String resourceId = GameChanger.resourceSprites.size() == 0 ? "null" : GameChanger.resourceSprites.get(GameChanger.range(0, GameChanger.resourceSprites.size()-1)).identifier;
				if (resourceId.equals("null")) isNullResourceSpawnEffect = true;
				result.put(paramType, resourceId);
				break;
			case "limit":
				int limit = isNullResourceSpawnEffect ? 0 : GameChanger.range(0, 15); //GameChanger.range(0, 10);
				result.put(paramType, ""+limit);
				break;
			case "value":
				int value = GameChanger.range(-5, 5); //GameChanger.range(-2, 2);
				result.put(paramType, ""+value);
				break;
			default:
				throw new RuntimeException("Invalid switch input: " + possibleParams[i]);
			}
		}
		return result;
	}
	
	private static boolean isResourceEffect(String func){		
		if (func.equals("spawnIfHasMore")) return false; //spawnIfHasMore can be used with resource=null
		
		String[] params = possibleFunctionParameters.get(func);
		for (int i = 0; i < params.length; i++) {
			if (params[i].equals("resource")) return true;
		}
		
		
		return false;
	}
	
	private static boolean isUnaryOrientationEffect(String func){
		if (func.equals("flipDirection") || func.equals("reverseDirection") || func.equals("turnAround")) return true;
		return false;
	}
}
