package gamechanger.core;

import gamechanger.parsing.Interaction;
import gamechanger.parsing.Sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class SpritesChanger {
	
	private static Random r = new Random();
	
	private static final HashMap<String, String[]> possibeSpriteParameters = new HashMap<String, String[]>();
	static{
		possibeSpriteParameters.put("FlakAvatar", 		new String[]{"img", "cooldown", "speed", "stype", "ammo"});
		possibeSpriteParameters.put("HorizontalAvatar", new String[]{"img", "cooldown", "speed"});
		possibeSpriteParameters.put("MovingAvatar", 	new String[]{"img", "cooldown", "speed"});
		possibeSpriteParameters.put("OrientedAvatar", 	new String[]{"img", "cooldown", "speed"});
		possibeSpriteParameters.put("ShootAvatar", 		new String[]{"img", "cooldown", "speed", "stype", "ammo"});
			
		possibeSpriteParameters.put("Flicker",			new String[]{"img", "singleton", "limit"});
		possibeSpriteParameters.put("Immovable", 		new String[]{"img", "singleton"});
		possibeSpriteParameters.put("Door", 			new String[]{"img", "singleton"});
		possibeSpriteParameters.put("OrientedFlicker", 	new String[]{"img", "singleton", "orientation", "limit"});
		possibeSpriteParameters.put("Passive", 			new String[]{"img", "singleton"});
		possibeSpriteParameters.put("Missile",	 		new String[]{"img", "singleton", "cooldown", "speed", "orientation"});
		possibeSpriteParameters.put("RandomMissile", 	new String[]{"img", "singleton", "cooldown", "speed"});
		possibeSpriteParameters.put("AlternateChaser", 	new String[]{"img", "singleton", "cooldown", "speed", "stype1", "stype2", "fleeing"});
		possibeSpriteParameters.put("Chaser", 			new String[]{"img", "singleton", "cooldown", "speed", "stype", "fleeing"});
		possibeSpriteParameters.put("Fleeing", 			new String[]{"img", "singleton", "cooldown", "speed", "stype", "fleeing"});
		possibeSpriteParameters.put("RandomAltChaser", 	new String[]{"img", "singleton", "cooldown", "speed", "stype1", "stype2", "fleeing", "epsilon"});
		possibeSpriteParameters.put("RandomNPC", 		new String[]{"img", "singleton", "cooldown", "speed"});
		possibeSpriteParameters.put("Bomber", 			new String[]{"img", "singleton", "cooldown", "speed", "orientation", "stype", "prob", "total"});
		possibeSpriteParameters.put("Portal", 			new String[]{"img", "singleton", "stype"});
		possibeSpriteParameters.put("SpawnPoint", 		new String[]{"img", "singleton", "cooldown", "stype", "prob", "total"});
		possibeSpriteParameters.put("Resource", 		new String[]{"img", "singleton", "value", "limit"});
		possibeSpriteParameters.put("Spreader", 		new String[]{"img", "singleton", "stype", "limit", "spreadprob"});
	}
	
	
	
	//Changes chances
	static double chanceToMutateSpriteClass = 0.25;
	static double chanceToMutateSpriteParameters = 0.0;
	
	//Parameter chances
	static double singletonChance = 0.1;
	static double cooldownChance = 0.25;
	static double speedChance = 0.1;
	static double ammoChance = 0.15;
	static double probChance = 0.8;
	static double totalChance = 0.2;
	static double fleeingChance = 0.3;
	static double spreadprobChance = 0.3;
	
	
	static void changeSprites(ArrayList<Sprite> sprites, int amountSprites){
				
		//Remove existing sprites
		int spritesToRemove = sprites.size() - amountSprites;
		if (spritesToRemove > 0){
			Integer[] spriteIndices = new Integer[spritesToRemove];
			for (int i = 0; i < spriteIndices.length; i++) {
				 int idx = GameChanger.range(0, sprites.size()-1);
				 
				 boolean valid = true;
				 for (int j = 0; j < spriteIndices.length; j++) {
					 if (spriteIndices[j] == null) continue;
					 if (idx == spriteIndices[j]) valid = false;
				 }
				 if (valid) spriteIndices[i] = idx;
				 else i--;
			}
			Arrays.sort(spriteIndices, Collections.reverseOrder());
			for (int i = 0; i < spriteIndices.length; i++) {
				sprites.remove((int)spriteIndices[i]);
			}
		}
		
		
		//Change existing sprites
		int changes = 0;
		for (Sprite sprite : sprites) {
			if (sprite.identifier.equals("wall")) continue;
			if (chanceToMutateSpriteClass > r.nextDouble() || (changes == 0 && sprite == sprites.get(sprites.size()-1))){ 
				if (sprite.referenceClass.length() > 0){
					sprite.referenceClass = getNewSpriteClass(sprite, sprites);
					sprite.parameters = getNewSpriteParameters(sprite, sprites);
					changes++;
				}else{
					sprite.parameters = getNewSpriteParameters(sprite, sprites);
					changes++;
				}
			}else if (chanceToMutateSpriteParameters > r.nextDouble()){
				sprite.parameters = getNewSpriteParameters(sprite, sprites);
				changes++;
			}
		}
		
		
		//Add new sprites
		boolean addedAvatar = GameChanger.haveAvatar;
		int spritesToMake = amountSprites - sprites.size();
		Sprite[] newSprites = new Sprite[spritesToMake];
		for (int i = 0; i < spritesToMake; i++) {
			Sprite s = new Sprite();
			if (!addedAvatar){
				s.identifier = "avatar"; 
				s.referenceClass = getNewSpriteClass(s, sprites);
				addedAvatar = true;
			}else{
				s.identifier = "gen" + i;
				s.referenceClass = getNewSpriteClass(s, sprites);
			}
			sprites.add(s);
			newSprites[i] = s;
		}
		for (int i = 0; i < spritesToMake; i++) {
			Sprite s = newSprites[i];
			s.parameters = getNewSpriteParameters(s, sprites);
		}
	}
	
	
	private static String getNewSpriteClass(Sprite sprite, ArrayList<Sprite> sprites) {
		String newClass = "";
		
		if (GameChanger.isSpriteAvatar(sprite.identifier, sprites)){
			int idx = GameChanger.range(0, GameChanger.possibleAvatarClass.length-1);
			newClass = GameChanger.possibleAvatarClass[idx].getSimpleName();
		}else{
			int idx = GameChanger.range(0, GameChanger.possibleSpriteClasses.length-1);
			newClass = GameChanger.possibleSpriteClasses[idx].getSimpleName();
		}		
		return newClass;
	}


	

	
	private static HashMap<String, String> getNewSpriteParameters(Sprite sprite, ArrayList<Sprite> sprites) {
		HashMap<String, String> parameters = new HashMap<String, String>();

		String spriteClass = sprite.referenceClass;
		if (spriteClass.equals("")){
			if (sprite.parent == null) return parameters;
			spriteClass = sprite.parent.referenceClass;
		}
		if (spriteClass.equals("")){
			if (sprite.parent.parent == null) return parameters;
			spriteClass = sprite.parent.parent.referenceClass;
		}
		
		String[] possibleParames = possibeSpriteParameters.get(spriteClass);
		String spriteId = "";
		
		for (int i = 0; i < possibleParames.length; i++) {
			String paramType = possibleParames[i];
			switch (paramType) {
			case "img":
				if (GameChanger.isSpriteAvatar(sprite.identifier, sprites)){
					parameters.put(paramType, avatarImage);
				}else{
					parameters.put(paramType, getRandomImg());
				}
				break;
			case "singleton":
				if (singletonChance > r.nextDouble()){
					parameters.put(paramType, "TRUE");
				}
				break;
			case "cooldown":
				if (cooldownChance > r.nextDouble()){
					parameters.put(paramType, "" + GameChanger.range(1, 10));
				}
				break;
			case "speed":
				if (speedChance > r.nextDouble()){
					double speed = GameChanger.range(10, 99) * 0.01; 
					speed = speed * speed; //0.01-0.98 (and preference for lower speeds)
					parameters.put(paramType, "" + speed);
				}
				break;
			case "orientation":
				parameters.put(paramType, getRandomOrientation());
				break;
			case "stype":
				spriteId = "";
				while (spriteId.length() == 0 || spriteId.equals(sprite.identifier) || isChildOfSprite(sprite, spriteId, sprites)){
					if (sprite.referenceClass.equals("Chaser") || sprite.referenceClass.equals("Fleeing") || sprite.referenceClass.equals("Portal")){
						spriteId = GameChanger.getRandomSprite(sprites);
					}else{ //SpawnPoints, bombers, spreaders and shootavatars/flakavatars
						spriteId = GameChanger.getRandomSpriteNotAvatar(sprites);
					}
				}
				parameters.put(paramType, spriteId);
				break;
			case "stype1":
				spriteId = "";
				while (spriteId.length() == 0 || spriteId.equals(sprite.identifier)){
					spriteId = GameChanger.getRandomSprite(sprites);
				}
				parameters.put(paramType, spriteId);
				break;
			case "stype2":
				spriteId = "";
				while (spriteId.length() == 0 || spriteId.equals(sprite.identifier)){
					spriteId = GameChanger.getRandomSprite(sprites);
				}
				parameters.put(paramType, spriteId);
				break;
			case "ammo":
				GameChanger.loadResourceSprites(sprites);
				if (ammoChance > r.nextDouble() && GameChanger.resourceSprites.size() > 0){
					int idx = GameChanger.range(0, GameChanger.resourceSprites.size()-1);
					parameters.put(paramType, GameChanger.resourceSprites.get(idx).identifier);
				}
				break;
			case "prob":
				if (probChance > r.nextDouble()){
					double prob = GameChanger.range(7, 99) * 0.01; 
					prob = prob * prob; //~0.005-0.98 (and preference for lower speeds)
					parameters.put(paramType, "" + prob);
				}
				break;
			case "total":
				if (totalChance > r.nextDouble()){
					parameters.put(paramType, "" + GameChanger.range(1,30));
				}
				break;
			case "value":
				parameters.put(paramType, "" + GameChanger.range(0,5));
				break;
			case "fleeing":
				if (fleeingChance > r.nextDouble()){
					parameters.put(paramType, "TRUE");
				}
				break;
			case "epsilon":
				parameters.put(paramType, "" + r.nextDouble());
				break;
			case "limit":
				if (sprite.referenceClass.equals("Resource")){
					parameters.put(paramType, "" + GameChanger.range(1, 20));
				}else{ //flickers
					parameters.put(paramType, "" + GameChanger.range(1, 50));
				}
				break;
			case "spreadprob":
				if (spreadprobChance > r.nextDouble()){
					double spreadprob = GameChanger.range(7, 99) * 0.01; 
					spreadprob = spreadprob * spreadprob; //~0.005-0.98 (and preference for lower speeds)
					parameters.put(paramType, "" + spreadprob);
				}
				break;
			default:
				throw new RuntimeErrorException(null);
			}
		}
		
		return parameters;
	}



	private static boolean isChildOfSprite(Sprite sprite, String otherSpriteId, ArrayList<Sprite> sprites) {
		
		for (Sprite other : sprites) {
			if (other.identifier.equals(otherSpriteId) && other.parent != null){
				if (other.parent.identifier.equals(sprite.identifier)){
					return true;
				}else if (other.parent.parent != null && other.parent.parent.identifier.equals(sprite.identifier)){
					return true;
				}
			}
		}
		
		return false;
	}



	static String[] possibleOrientations = new String[]{"UP","DOWN","LEFT","RIGHT"};
	private static String getRandomOrientation() {
		int idx = GameChanger.range(0, possibleOrientations.length-1);
		return possibleOrientations[idx];
	}

	static String avatarImage = "avatar";
	static String[] possibleImages = new String[]{"alien", "base", "bee", "bomb", "boulder", "box", "bullet",
		"butterfly", "camel", "carcass", "city", "cocoon", "diamond", "dirt", "door", "explosion", "fire", "flower",
		"forest", "frog", "ghost", "goal", "gold", "hell", "hole", "honey", "key", "log", "mana", "marsh", "missile",
		"monster", "mushroom", "pellet", "portal", "powerpill", "shovel", "spaceship", "sword", "truck", "virus",
		"wall", "water", "zombie"};
	
	private static String getRandomImg() {
		int idx = GameChanger.range(0, possibleImages.length-1);	
		return possibleImages[idx];
	}
	
}
