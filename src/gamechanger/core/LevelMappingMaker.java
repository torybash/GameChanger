package gamechanger.core;

import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class LevelMappingMaker {

	static Random r = new Random();
	
	static double haveMappingChance = 1.0;
	
	
	public static void makeMapping(ArrayList<LevelMapping> mappings, ArrayList<Sprite> sprites) {	
		int c = 36;
		for (Sprite sprite : sprites) {		
			if (haveMappingChance > r.nextDouble() && !GameChanger.isSpriteAvatar(sprite.identifier, sprites)){
				if (c > 127) throw new RuntimeErrorException(null, "Too many sprites! Can't assign level mapping");
				if ((char) c == "A".charAt(0)) c++;
				
				LevelMapping mapping = new LevelMapping();
				mapping.charID = (char) c;
				
				mapping.references = new ArrayList<String>();
				mapping.references.add(sprite.identifier);
				c++;
				
				mappings.add(mapping);
			}
		}
		
	}
	
	
	public static String makeLevel(ArrayList<LevelMapping> mappings) {
		int width = 15;
		int height = 15;
		
		return makeLevel(mappings, width, height);
	}

	
	public static String makeLevel(ArrayList<LevelMapping> mappings, int width, int height) {
		String result = "";
		
		char[][] levelMap = makeCharMap(width, height);
		
		
		boolean hasOverridenDefaultAvatarMapping = false;
		boolean hasPutAvatar = false;
		for (LevelMapping lm : mappings) {			
			if (lm.charID == "A".charAt(0)){
				hasOverridenDefaultAvatarMapping = true;
				break;
			}
		}
		
		if (!hasOverridenDefaultAvatarMapping){
			LevelMapping avatarMapping = new LevelMapping();;
			avatarMapping.charID = "A".charAt(0);
			avatarMapping.references = new ArrayList<String>();
			avatarMapping.references.add(GameChanger.avatarNames.get(0));
			mappings.add(avatarMapping);
		}
		
		//wall mapping
//		LevelMapping wallMapping = new LevelMapping();;
//		wallMapping.charID = "w".charAt(0);
//		wallMapping.references = new ArrayList<String>();
//		wallMapping.references.add("wall");
//		mappings.add(wallMapping);
		
		int totalAmountPut = 0;
		for (LevelMapping lm : mappings) {
			int amount = GameChanger.range(1, 50);
			amount = 1 + (amount * amount * amount * amount)/250000; // 1 - 8
			int amountPut = 0;
			
			if (lm.charID == "w".charAt(0)) amount *= 5;
			
			boolean mappingContainsAvatar = false;
			for (String ref : lm.references) {
				for (String avaName : GameChanger.avatarNames) {
					if (ref.equals(avaName)){
						mappingContainsAvatar = true;
					}
				}

			}
			
			if (mappingContainsAvatar){
				if (hasPutAvatar) continue;
				hasPutAvatar = true;
				amount = 1;
			}
						
			int x = -1, y = -1;
			while (amountPut < amount && totalAmountPut < width * height){
				x = r.nextInt(width);
				y = r.nextInt(height);
				if (levelMap[x][y] == " ".charAt(0)){
					levelMap[x][y] = lm.charID;
					amountPut++;
					totalAmountPut++;
				}
			}
			
		}

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				result += levelMap[i][j];
			}
			result += "\n";
		}
		
		return result;
	}
	
	private static char[][] makeCharMap(int width, int height) {
		char[][] result = new char[width][];
		for (int i = 0; i < width; i++) {
			result[i] = new char[height];
			for (int j = 0; j < height; j++) {
				result[i][j] = " ".charAt(0);
			}
		}
		return result;
	}




	
}
