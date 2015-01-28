package levelgenerator.map;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelMap {

	public char[][] map;
	public HashMap<Mapping, Integer> spriteMappings;
	
	
	public LevelMap(){
		
	}
	
	public LevelMap(char[][] map){
		this.map = map;
	}


	public void calculateSpriteMappings(ArrayList<Mapping> mappings) {
		
		
		spriteMappings = new HashMap<Mapping, Integer>();
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (i == 0 || i == map.length-1) continue;
				if (j == 0 || j == map[i].length-1) continue;
				
				char ch = map[i][j];
				Mapping mapping = null;
				for (Mapping mp : mappings) {
					if (ch == mp.charID){
						mapping = mp;
						break;
					}
				}
				if (mapping == null) continue;
				int amount = 0;
				if (spriteMappings.get(mapping) != null) amount = spriteMappings.get(mapping);
				spriteMappings.put(mapping, amount+1);
			}
		}
		
	}
	
	
	
}
