package gamechanger.core;

import gamechanger.parsing.LevelMapping;
import gamechanger.parsing.Sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class LevelMappingMaker {

	static Random r = new Random();
	
	static double haveMappingChance = 0.9;
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
		String result = "";
		
		char[][] levelMap = new char[width][];
		for (int i = 0; i < width; i++) {
			levelMap[i] = new char[height];
			for (int j = 0; j < height; j++) {
				levelMap[i][j] = " ".charAt(0);
			}
		}
		
		LevelMapping avatarMapping = new LevelMapping();;
		avatarMapping.charID = "A".charAt(0);
		avatarMapping.references = new ArrayList<String>();
		avatarMapping.references.add(GameChanger.avatarName);
		mappings.add(avatarMapping);
		
		for (LevelMapping mapping : mappings) {
			int amount = GameChanger.range(1, 50);
			amount = 1 + (amount * amount * amount * amount)/1000000; // 1 - 8
			int amountPut = 0;
			if (mapping.charID == "A".charAt(0)) amount = 1;
			
			boolean foundRoom = false;
			int x = -1, y = -1;
			while (amount > amountPut){
				x = r.nextInt(width);
				y = r.nextInt(height);
				if (levelMap[x][y] == " ".charAt(0)){
					levelMap[x][y] = mapping.charID;
					amountPut++;
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

	
	public static void main(String[] args) {
		
		char[] chars = new char[200];
		
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) i;
		}
		
		for (int i = 0; i < chars.length; i++) {
			System.out.println(i + ": " + chars[i]);
		}
	}
	
}
