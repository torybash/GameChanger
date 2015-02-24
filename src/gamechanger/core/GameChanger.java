package gamechanger.core;

import java.util.ArrayList;
import java.util.Random;

import fastVGDL.ontology.avatar.FlakAvatar;
import fastVGDL.ontology.avatar.MovingAvatar;
import fastVGDL.ontology.avatar.oriented.OrientedAvatar;
import fastVGDL.ontology.avatar.oriented.ShootAvatar;
import fastVGDL.ontology.sprites.Flicker;
import fastVGDL.ontology.sprites.Immovable;
import fastVGDL.ontology.sprites.OrientedFlicker;
import fastVGDL.ontology.sprites.Passive;
import fastVGDL.ontology.sprites.Resource;
import fastVGDL.ontology.sprites.Spreader;
import fastVGDL.ontology.sprites.missile.Missile;
import fastVGDL.ontology.sprites.missile.RandomMissile;
import fastVGDL.ontology.sprites.npc.AlternateChaser;
import fastVGDL.ontology.sprites.npc.Chaser;
import fastVGDL.ontology.sprites.npc.Fleeing;
import fastVGDL.ontology.sprites.npc.RandomAltChaser;
import fastVGDL.ontology.sprites.npc.RandomNPC;
import fastVGDL.ontology.sprites.producer.Bomber;
import fastVGDL.ontology.sprites.producer.Portal;
import fastVGDL.ontology.sprites.producer.SpawnPoint;
import gamechanger.parsing.Element;
import gamechanger.parsing.Interaction;
import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Parser;
import gamechanger.parsing.Sprite;
import gamechanger.parsing.Termination;
import gamechanger.writer.Writer;

public class GameChanger {
	
	
	//	private static final Class [] possibleAvatarClass = {AimedAvatar.class, AimedFlakAvatar.class, FlakAvatar.class,
	//	HorizontalAvatar.class, InertialAvatar.class, MarioAvatar.class, MovingAvatar.class,
	//	NoisyRotatingFlippingAvatar.class, OrientedAvatar.class,
	//	RotatingFlippingAvatar.class,ShootAvatar.class, VerticalAvatar.class};
	
	//ONLY CONTAINS AVATAR SPRITES WITH IMPLEMENTATION (-AimedAvatar.class, AimedFlakAvatar.class, InertialAvatar.class, MarioAvatar.class, 
	//NoisyRotatingFlippingAvatar.class, RotatingFlippingAvatar.class, VerticalAvatar.class
//	static final Class [] possibleAvatarClasses = {FlakAvatar.class, HorizontalAvatar.class, MovingAvatar.class,
//	OrientedAvatar.class, ShootAvatar.class};
	
	static final Class [] possibleAvatarClasses = {FlakAvatar.class, MovingAvatar.class,
	OrientedAvatar.class, ShootAvatar.class};
	
	//private static final Class[] possibleSpriteClasses = {Conveyor.class, Door.class, Flicker.class,
	//Immovable.class, OrientedFlicker.class, Passive.class, ErraticMissile.class,
	//Missile.class, RandomMissile.class, Walker.class,
	//WalkerJumper.class,AlternateChaser.class, Chaser.class,
	//Fleeing.class, RandomAltChaser.class, RandomInertial.class, RandomNPC.class,
	//Bomber.class, Portal.class, SpawnPoint.class, SpriteProducer.class, Resource.class, Spreader.class};
	
	//ONLY CONTAINS SPRITES WITH IMPLEMENTATION (-Conveyor.class, Door.class, ErraticMissile.class, Walker.class,
	//WalkerJumper.class, RandomInertial.class, SpriteProducer.class
	static final Class[] possibleSpriteClasses = {Flicker.class,
            Immovable.class, OrientedFlicker.class, Passive.class, 
            Missile.class, RandomMissile.class, AlternateChaser.class, Chaser.class,
            Fleeing.class, RandomAltChaser.class, RandomNPC.class,
            Bomber.class, Portal.class, SpawnPoint.class, Resource.class, Spreader.class};
        
//    static final Class[] possiblePuzzleSpriteClasses = {Flicker.class,
//        Immovable.class, OrientedFlicker.class, Passive.class, 
//        Portal.class, Resource.class, Spreader.class};
    static final Class[] possiblePuzzleSpriteClasses = {
        Passive.class,
        Portal.class, Resource.class
    };
        static final Class [] possiblePuzzleAvatarClasses = {MovingAvatar.class};
	
	
	private static Random r = new Random();
	public static boolean haveAvatar = false;
	public static ArrayList<String> avatarNames;
	static ArrayList<Sprite> resourceSprites = new ArrayList<Sprite>();
	
        static Class[] avatarClasses = null;
        static Class[] spriteClasses = null;
	
        
        
        
    public static String changeGame(ArrayList[] elements, boolean changeSprites, boolean changeInteractions, boolean changeTerminations){
		resourceSprites.clear();
		
        avatarClasses = possibleAvatarClasses;
        spriteClasses = possibleSpriteClasses;
        
		ArrayList<Sprite> sprites = elements[0];
		ArrayList<Interaction> interacts = elements[1];
		ArrayList<LevelMapping> mappings = elements[2];
		ArrayList<Termination> terms = elements[3];
		
		setAvatar(sprites);
		
		//change values to allow removal/creation of sprites/interactions/terms
		int amountSprites = sprites.size(); // + range(-1,2); 
		int amountInteractions = interacts.size() + range(-2,2); if (amountInteractions < 1) amountInteractions = 1;
		int amountTerminations = terms.size();
				
		if (changeSprites) SpritesChanger.changeSprites(sprites, amountSprites);
		if (changeInteractions) InteractionsChanger.changeInteractions(sprites, interacts, amountInteractions);
		if (changeTerminations) TerminationsChanger.changeTerminations(sprites, terms, amountTerminations);
			
		return Writer.writeGameOutput(elements);
    }
        
	public static String changeGameByDesc(String gameDesc, boolean changeSprites, boolean changeInteractions, boolean changeTerminations){
		ArrayList[] elements = Parser.readGameDescByDesc(gameDesc);
		return changeGame(elements, changeSprites, changeInteractions, changeTerminations);
	}    
        
        
        
	/**
	 * Mutate a VGDL game description. 
	 * @return Mutated game description
	 */
	public static String changeGameByPath(String gamePath, boolean changeSprites, boolean changeInteractions, boolean changeTerminations){
		ArrayList[] elements = Parser.readGameDescByPath(gamePath);
		return changeGame(elements, changeSprites, changeInteractions, changeTerminations);
	}
	
	/**
	 * Generate a VGDL game description
	 * @return A VGDL game description
	 */
	public static ArrayList[] makeGame(int amountSprites, int amountInteractions, int amountTerminations){
		resourceSprites.clear();
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		ArrayList<Interaction> interacts = new ArrayList<Interaction>();
		ArrayList<LevelMapping> mappings = new ArrayList<LevelMapping>();
		ArrayList<Termination> terms = new ArrayList<Termination>();
		
		setAvatar(sprites);
		
		ArrayList[] elements = new ArrayList[4];
		elements[0] = sprites;
		elements[1] = interacts;
		elements[2] = mappings;
		elements[3] = terms;
		

		
		SpritesChanger.changeSprites(sprites, amountSprites);
		InteractionsChanger.changeInteractions(sprites, interacts, amountInteractions);
		TerminationsChanger.changeTerminations(sprites, terms, amountTerminations);
		LevelMappingMaker.makeMapping(mappings, sprites);
				
		return elements;
	}
        
	
	
    public static String makeArcadeGame(){
        avatarClasses = possibleAvatarClasses;
        spriteClasses = possibleSpriteClasses;
        
        InteractionsChanger.setFunctions(false);
        
		int amountSprites = range(4,8);
		int amountInteractions = range(4,10);
		int amountTerminations = range(2,2);
        ArrayList[] elements = makeGame(amountSprites, amountInteractions, amountTerminations);
        InteractionsChanger.makeEOSStepBacks(elements[0], elements[1]);
       
        return  Writer.writeGameOutput(elements);
    }

    public static String makePuzzleGame(){
        avatarClasses = possiblePuzzleAvatarClasses;
        spriteClasses = possiblePuzzleSpriteClasses;
        
        InteractionsChanger.setFunctions(true);
        
		int amountSprites = range(3,4);
		int amountInteractions = range(8,14);
		int amountTerminations = range(1,1);
		ArrayList[] elements = makeGame(amountSprites, amountInteractions, amountTerminations);
        InteractionsChanger.makeWallStepBacks(elements[0], elements[1]);

        return  Writer.writeGameOutput(elements);
    }
        
        
    public static void setSpriteClasses(){
        avatarClasses = possiblePuzzleAvatarClasses;
        spriteClasses = possiblePuzzleSpriteClasses;
    }
	
	public static String makeLevel(String game_desc){
		ArrayList[] elements = Parser.readGameOutputString(game_desc);
		ArrayList<Sprite> sprites = elements[0];
		setAvatar(sprites);
		if (avatarNames.size() == 0) avatarNames.add("avatar");
		ArrayList<LevelMapping> mappings = elements[2];
		String level = LevelMappingMaker.makeLevel(mappings);

		return level;
	}
	

	public static boolean setAvatar(ArrayList<Sprite> sprites) {
		avatarNames = new ArrayList<String>();
		haveAvatar = false;
		for (Sprite s: sprites) {
			for (int i = 0; i < avatarClasses.length; i++) {
				String className = avatarClasses[i].getSimpleName();
				if (s.referenceClass.equals(className) ||
					(s.parent != null && s.parent.referenceClass.equals(className)) ||
					(s.parent != null && s.parent.parent != null && s.parent.parent.referenceClass.equals(className))
					)
				{
					avatarNames.add(s.identifier);
                    haveAvatar = true;
				}				
			}
		}
		if (avatarNames.size() == 0) avatarNames.add("avatar");
		return haveAvatar;
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
		for (String avaName: avatarNames) {
			if (spriteId.equals(avaName)) return true;
		
			for (Sprite s: sprites) {
				if (s.identifier.equals(spriteId)){
					if (s.parent != null && s.parent.identifier.equals(avaName)) return true;
					if (s.parent != null && s.parent.parent != null && s.parent.parent.identifier.equals(avaName)) return true;
				}
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
	
	/**Random int. Both values included
	 */
	public static int range(int from, int to){
		if (from > to) throw new IllegalArgumentException("To value can't be smaller than from");		
		int result = r.nextInt(to-from+1) + from;
		return result;
	}

	public static String crossOverGame(String gameDesc1, String gameDesc2) {
		ArrayList[] elements1 = Parser.readGameDescByDesc(gameDesc1);
		ArrayList[] elements2 = Parser.readGameDescByDesc(gameDesc2);

		ArrayList[] retElements = new ArrayList[4];
		
		retElements[0] = elements1[0];
		retElements[2] = elements1[2];
		retElements[3] = elements1[3];
		
		ArrayList<Interaction> game1Inters = elements1[1];
		ArrayList<Interaction> game2Inters = elements2[1];
		
		int maxSize = Math.max(game1Inters.size(), game2Inters.size());
		int minSize = Math.min(game1Inters.size(), game2Inters.size());
		int size = (maxSize - minSize == 0) ? (maxSize) : (r.nextInt(maxSize - minSize) + minSize);
		
		ArrayList<Interaction> retGameInters = new ArrayList<Interaction>(size);

		for (int i = 0; i < size; i++) {
			Interaction inter = null;
			if (r.nextFloat() > 0.5f){
				if (i < game1Inters.size()){
					inter = game1Inters.get(i);
				}else{
					inter = game2Inters.get(i);
				}
			}else{
				if (i < game2Inters.size()){
					inter = game2Inters.get(i);
				}else{
					inter = game1Inters.get(i);
				}
			}
			retGameInters.add(inter);
		}
		
		retElements[0] = elements1[0];
		retElements[1] = retGameInters;
		retElements[2] = elements1[2];
		retElements[3] = elements1[3];
		
		return Writer.writeGameOutput(retElements);
	}

}
