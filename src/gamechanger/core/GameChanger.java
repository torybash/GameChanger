package gamechanger.core;

import gamechanger.parsing.Interaction;
import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Parser;
import gamechanger.parsing.Sprite;
import gamechanger.parsing.Termination;
import gamechanger.writer.Writer;

import java.util.ArrayList;
import java.util.Random;

import ontology.avatar.FlakAvatar;
import ontology.avatar.HorizontalAvatar;
import ontology.avatar.MovingAvatar;
import ontology.avatar.NoisyRotatingFlippingAvatar;
import ontology.avatar.RotatingFlippingAvatar;
import ontology.avatar.VerticalAvatar;
import ontology.avatar.oriented.AimedAvatar;
import ontology.avatar.oriented.AimedFlakAvatar;
import ontology.avatar.oriented.InertialAvatar;
import ontology.avatar.oriented.MarioAvatar;
import ontology.avatar.oriented.OrientedAvatar;
import ontology.avatar.oriented.ShootAvatar;
import ontology.sprites.Conveyor;
import ontology.sprites.Door;
import ontology.sprites.Flicker;
import ontology.sprites.Immovable;
import ontology.sprites.OrientedFlicker;
import ontology.sprites.Passive;
import ontology.sprites.Resource;
import ontology.sprites.Spreader;
import ontology.sprites.missile.ErraticMissile;
import ontology.sprites.missile.Missile;
import ontology.sprites.missile.RandomMissile;
import ontology.sprites.missile.Walker;
import ontology.sprites.missile.WalkerJumper;
import ontology.sprites.npc.AlternateChaser;
import ontology.sprites.npc.Chaser;
import ontology.sprites.npc.Fleeing;
import ontology.sprites.npc.RandomAltChaser;
import ontology.sprites.npc.RandomInertial;
import ontology.sprites.npc.RandomNPC;
import ontology.sprites.producer.Bomber;
import ontology.sprites.producer.Portal;
import ontology.sprites.producer.SpawnPoint;
import ontology.sprites.producer.SpriteProducer;

public class GameChanger {

	AimedAvatar aim;
//	
	private final Class [] possibleAvatarClass = {AimedAvatar.class, AimedFlakAvatar.class, FlakAvatar.class,
			HorizontalAvatar.class, InertialAvatar.class, MarioAvatar.class, MovingAvatar.class,
			NoisyRotatingFlippingAvatar.class, OrientedAvatar.class,
			RotatingFlippingAvatar.class,ShootAvatar.class, VerticalAvatar.class};
	
	private final Class[] possibleSpriteClasses = {Conveyor.class, Door.class, Flicker.class,
			Immovable.class, OrientedFlicker.class, Passive.class, ErraticMissile.class,
			Missile.class, RandomMissile.class, Walker.class,
			WalkerJumper.class,AlternateChaser.class, Chaser.class,
			Fleeing.class, RandomAltChaser.class, RandomInertial.class, RandomNPC.class,
			Bomber.class, Portal.class, SpawnPoint.class, SpriteProducer.class, Resource.class, Spreader.class};
	
	public GameChanger(){}

	
	private Parser p = new Parser();
	private Writer w = new Writer();
	
	public void changeGame(String game_desc){
		ArrayList[] elements = p.readGameOutput(game_desc);
		
		ArrayList<Sprite> sprites = elements[0];
		ArrayList<Interaction> interacts = elements[1];
		ArrayList<LevelMapping> mappings = elements[2];
		ArrayList<Termination> terms = elements[3];
		
		changeSprites(sprites);
		
		
		w.writeGameOutput(elements);
	}
	
	
	void changeSprites(ArrayList<Sprite> sprites){
		Random r = new Random();
		
		//Change class
		for (Sprite sprite : sprites) {
			if (sprite.identifier.equals("avatar")){
				sprite.referenceClass = ""+possibleAvatarClass[r.nextInt(possibleAvatarClass.length)].getSimpleName();
			}else{
				sprite.referenceClass = ""+possibleSpriteClasses[r.nextInt(possibleSpriteClasses.length)].getSimpleName();
			}
		}
		
		
	}
}
