package levelgenerator.core;

import levelgenerator.map.LevelMap;

public class Main {

    
//    Survived GameInfo 0, won: 1 , timesteps: 76 , score: 0.0 , action count: 76
//wwwwwwww
//wM1Mw 0w
//w0M M1Mw
//w0MwwMMw
//wKwM1 1w
//w1GMAMMw
//wwwwwwww

	
	public static void main(String[] args) {
		LevelGenerator lg = new LevelGenerator("bait");	
//		lg.groundChar = ".".charAt(0);
		lg.groundChar = " ".charAt(0);
		lg.makeWall = true;
		lg.generateLevel(8, 7, new LevelMap());
		
//		lg.generateLevel(8, 7, lg.getCharMap(	"wwwwwwww\n"+
//												"w.w....w\n"+
//												"w.w.w*.w\n"+
//												"ww.*A..w\n"+
//												"w...Owww\n"+
//												"wO*...Ow\n"+
//												"wwwwwwww"));
	}
}
