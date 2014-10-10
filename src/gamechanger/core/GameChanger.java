package gamechanger.core;

import gamechanger.parsing.Interaction;
import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Parser;
import gamechanger.parsing.Sprite;
import gamechanger.parsing.Termination;
import gamechanger.writer.Writer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.management.RuntimeErrorException;

import ontology.avatar.FlakAvatar;
import ontology.avatar.HorizontalAvatar;
import ontology.avatar.MovingAvatar;
import ontology.avatar.oriented.OrientedAvatar;
import ontology.avatar.oriented.ShootAvatar;
import ontology.sprites.Flicker;
import ontology.sprites.Immovable;
import ontology.sprites.OrientedFlicker;
import ontology.sprites.Passive;
import ontology.sprites.Resource;
import ontology.sprites.Spreader;
import ontology.sprites.missile.Missile;
import ontology.sprites.missile.RandomMissile;
import ontology.sprites.npc.AlternateChaser;
import ontology.sprites.npc.Chaser;
import ontology.sprites.npc.Fleeing;
import ontology.sprites.npc.RandomAltChaser;
import ontology.sprites.npc.RandomNPC;
import ontology.sprites.producer.Bomber;
import ontology.sprites.producer.Portal;
import ontology.sprites.producer.SpawnPoint;

public class GameChanger {
	
	
	//	private static final Class [] possibleAvatarClass = {AimedAvatar.class, AimedFlakAvatar.class, FlakAvatar.class,
	//	HorizontalAvatar.class, InertialAvatar.class, MarioAvatar.class, MovingAvatar.class,
	//	NoisyRotatingFlippingAvatar.class, OrientedAvatar.class,
	//	RotatingFlippingAvatar.class,ShootAvatar.class, VerticalAvatar.class};
	
	//ONLY CONTAINS SPRITES WITH CORRECT IMPLEMENTATION (-AimedAvatar.class, AimedFlakAvatar.class, InertialAvatar.class, MarioAvatar.class, 
	//NoisyRotatingFlippingAvatar.class, RotatingFlippingAvatar.class, VerticalAvatar.class
	static final Class [] possibleAvatarClass = {FlakAvatar.class, HorizontalAvatar.class, MovingAvatar.class,
	OrientedAvatar.class, ShootAvatar.class};
	
	//private static final Class[] possibleSpriteClasses = {Conveyor.class, Door.class, Flicker.class,
	//Immovable.class, OrientedFlicker.class, Passive.class, ErraticMissile.class,
	//Missile.class, RandomMissile.class, Walker.class,
	//WalkerJumper.class,AlternateChaser.class, Chaser.class,
	//Fleeing.class, RandomAltChaser.class, RandomInertial.class, RandomNPC.class,
	//Bomber.class, Portal.class, SpawnPoint.class, SpriteProducer.class, Resource.class, Spreader.class};
	
	//ONLY CONTAINS SPRITES WITH CORRECT IMPLEMENTATION (-Conveyor.class, Door.class, ErraticMissile.class, Walker.class,
	//WalkerJumper.class, RandomInertial.class, SpriteProducer.class
	static final Class[] possibleSpriteClasses = {Flicker.class,
	Immovable.class, OrientedFlicker.class, Passive.class, 
	Missile.class, RandomMissile.class, AlternateChaser.class, Chaser.class,
	Fleeing.class, RandomAltChaser.class, RandomNPC.class,
	Bomber.class, Portal.class, SpawnPoint.class, Resource.class, Spreader.class};
	
	
	

	
	
	
	
	private static Random r = new Random();
	static boolean haveAvatar = false;
	static String avatarName = "avatar";
	static ArrayList<Sprite> resourceSprites = new ArrayList<Sprite>();
	
		
	public static String changeGame(String game_desc, boolean changeSprites, boolean changeInteractions, boolean changeTerminations){
		haveAvatar = false;
		avatarName = "avatar";
		resourceSprites.clear();
		
		ArrayList[] elements = Parser.readGameOutput(game_desc);
		
		ArrayList<Sprite> sprites = elements[0];
		ArrayList<Interaction> interacts = elements[1];
		ArrayList<LevelMapping> mappings = elements[2];
		ArrayList<Termination> terms = elements[3];
				
		if (changeSprites) SpritesChanger.changeSprites(sprites, sprites.size());
		if (changeInteractions) InteractionsChanger.changeInteractions(sprites, interacts, interacts.size());
		if (changeTerminations) TerminationsChanger.changeTerminations(sprites, terms, terms.size());
			
		return Writer.writeGameOutput(elements);
	}
	
	public static String makeGame(){
		haveAvatar = false;
		avatarName = "avatar";
		resourceSprites.clear();
		
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		ArrayList<Interaction> interacts = new ArrayList<Interaction>();
		ArrayList<LevelMapping> mappings = new ArrayList<LevelMapping>();
		ArrayList<Termination> terms = new ArrayList<Termination>();
		
		ArrayList[] elements = new ArrayList[4];
		elements[0] = sprites;
		elements[1] = interacts;
		elements[2] = mappings;
		elements[3] = terms;
		
		int amountSprites = range(3,25);
		int amountInteractions = range(3,25);
		int amountTerminations = range(1,2);
		
//		int amountSprites = range(3,10);
//		int amountInteractions = range(3,10);
//		int amountTerminations = range(1,2);
		
		SpritesChanger.changeSprites(sprites, amountSprites);
		InteractionsChanger.changeInteractions(sprites, interacts, amountInteractions);
		TerminationsChanger.changeTerminations(sprites, terms, amountTerminations);
		LevelMappingMaker.makeMapping(mappings, sprites);
		
		return Writer.writeGameOutput(elements);
	}
	
	public static String makeLevel(String game_desc){
		
		ArrayList[] elements = Parser.readGameOutputString(game_desc);
		ArrayList<LevelMapping> mappings = elements[2];
		String level = LevelMappingMaker.makeLevel(mappings);
				
		

		
//		System.out.println("----------");
//		for (LevelMapping levelMapping : mappings) {
//			System.out.println("Levelmapping Char: " +levelMapping.charID + " > " + levelMapping.references);
//		}
//		System.out.println("game_desc:");
//		System.out.println(game_desc);
//		System.out.println("----------");
//		System.out.println("----------");
//		System.out.println(level);
		return level;
	}
	

	static boolean haveAvatar(ArrayList<Sprite> sprites) {
		for (Sprite s: sprites) {
			for (int i = 0; i < possibleAvatarClass.length; i++) {
				String className = possibleAvatarClass[i].getSimpleName();
				if (s.referenceClass.equals(className)){
					avatarName = s.identifier;
					haveAvatar = true;
					return true;
				}
			}
		}
		return false;
	}

	static void loadResourceSprites(ArrayList<Sprite> sprites) {
		resourceSprites.clear();
		for (Sprite s: sprites) {
			if (s.referenceClass.equals("Resource")){
				resourceSprites.add(s);
			}
		}	
	}

	static String getRandomSprite(ArrayList<Sprite> sprites){
		int idx = range(0, sprites.size()-1);
		return sprites.get(idx).identifier;
	}
	
	static String getRandomSpriteNotAvatar(ArrayList<Sprite> sprites){
		String spriteId = "";
		while(spriteId.length() == 0 || isSpriteAvatar(spriteId, sprites)){
			spriteId = getRandomSprite(sprites);
		}
		return spriteId;
	}
	
	static boolean isOrientedSprite(String spriteId, ArrayList<Sprite> sprites){
		for (Sprite s: sprites) {
			if (s.identifier.equals(spriteId)){
				if (s.referenceClass.equals("OrientedAvatar") || s.referenceClass.equals("ShootAvatar") || s.referenceClass.equals("Missile") ||
						s.referenceClass.equals("RandomMissile") || s.referenceClass.equals("AlternateChaser") || s.referenceClass.equals("Chaser") || 
						s.referenceClass.equals("Fleeing") || s.referenceClass.equals("RandomAltChaser") || s.referenceClass.equals("RandomNPC") || 
						s.referenceClass.equals("Bomber")) return true;
				else return false;
			}
		}
		return false;
	}  
	
	static boolean isSpriteAvatar(String spriteId, ArrayList<Sprite> sprites){
		if (spriteId.equals(avatarName)) return true;
		for (Sprite s: sprites) {
			if (s.identifier.equals(spriteId)){
				if (s.parent != null && s.parent.identifier.equals(avatarName)) return true;
				if (s.parent != null && s.parent.parent != null && s.parent.parent.identifier.equals(avatarName)) return true;
			}
		}
		return false;
	}
	
	
	static boolean isSpriteParent(String spriteId, ArrayList<Sprite> sprites){
		for (Sprite s: sprites) {
			if (s.parent != null && s.parent.identifier.equals(spriteId)) return true;
		}
		return false;
	}
	
	/**both value included
	 */
	static int range(int from, int to){
		if (from > to) throw new IllegalArgumentException("To value can't be smaller than from");		
		int result = r.nextInt(to-from+1) + from;
		return result;
	}
	
	
}
