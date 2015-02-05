package sokoban.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ParseSokoban3File {

	
	static Pattern patternGame = Pattern.compile(" \\*\\* Playing game [a-zA-Z0-9/\\.]+/([a-zA-Z0-9_]+)\\.txt, level [a-zA-Z0-9/\\.]+/[a-zA-Z0-9_]+_[a-z]*([0-9]*).txt \\(([0-9]+)/[0-9]+\\) \\*\\*");
	static Pattern patternResult = Pattern.compile("Result \\(1->win; 0->lose\\):([-]?[0-1]+), Score:([-]?[0-9]+\\.[0-9]+), timesteps:([0-9]+)");

	static String folderPath = "sokoban3_lvls/";
	
	public static void main(String[] args) {
		ParseSokoban3File.GetInstance().parseSokobanFile("Original.slc");
	}
	
	
	static ParseSokoban3File parseSokoban3File;
	
	public static ParseSokoban3File GetInstance (){
		if (parseSokoban3File == null) parseSokoban3File = new ParseSokoban3File();
		return parseSokoban3File;
	}
	
	public void parseSokobanFile(String filename){
		
		
		
		ArrayList<SokobanLevel> levels = new ArrayList<SokobanLevel>();
		int lvlIdx = -1;
		int rowIdx = 0;
        try{
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line = null;
            boolean newLevel = true;
            while ((line = in.readLine()) != null) {
            	if (line.contains("<Level ")){
            		newLevel = true;
            		lvlIdx++;
            		rowIdx = 0;
            		
        			int width = Integer.parseInt(line.split("Width=\"")[1].split("\"")[0]);
        			int height = Integer.parseInt(line.split("Height=\"")[1].split("\"")[0]);
        			
        			SokobanLevel lvl = new SokobanLevel(width, height, new String[height]);
            		
        			
        			levels.add(lvl);
            	}else if (line.contains("<L>")){
            		if (newLevel){
            			SokobanLevel lvl = levels.get(lvlIdx);
            			
            			String lvlRow = parseLine(line, lvl.width);
            			lvl.lvlString[rowIdx] = lvlRow;
            			levels.set(lvlIdx, lvl);
            			newLevel = false;
            			rowIdx++;
            		}else{
            			SokobanLevel lvl = levels.get(lvlIdx);
            			
            			String lvlRow = parseLine(line, lvl.width);
            			String newLevelString = levels.get(lvlIdx) + "\n" + lvlRow;
            			lvl.lvlString[rowIdx] = lvlRow;
            			levels.set(lvlIdx, lvl);
//            			levels.set(lvlIdx, newLevelString);
            			rowIdx++;
            		}
            	}
            	
            }
            in.close();
        }catch(Exception e)
        {
            System.out.println("Error reading the file " + filename + ": " + e.toString());
            e.printStackTrace();
        }
        
        int c = 0;
        for (SokobanLevel lvl : levels) {
//        	System.out.println();
//        	System.out.println("level");
//			System.out.println(string);
			createLevelFile(lvl.lvlString, c);
			c++;
		}
        
	}
	
	
	
	static String parseLine(String line, int width){
		
		String lvlRow = line.split("<L>")[1].split("</L>")[0];

		lvlRow = lvlRow.replace(".".charAt(0), "O".charAt(0));
		lvlRow = lvlRow.replace(" ".charAt(0), ".".charAt(0));
		lvlRow = lvlRow.replace("#".charAt(0), "w".charAt(0));
		
		lvlRow = lvlRow.replace("$".charAt(0), "*".charAt(0));
		lvlRow = lvlRow.replace("@".charAt(0), "A".charAt(0));
		
		if (lvlRow.length() < width){
			int orig_length = lvlRow.length();
			for (int i = 0; i < width - orig_length; i++) {
				lvlRow += ".";
			}
		}
		
		return lvlRow;
	}

	static void createLevelFile(String[] lvlString, int number){
        PrintWriter writer;
        String path = folderPath + "realsokoban" + "_lvl"+ number + ".txt";
        try {
            writer = new PrintWriter(path, "UTF-8");
            for (int i = 0; i < lvlString.length; i++) {
            	writer.println(lvlString[i]);
			}
            
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	}
	

//	public boolean parseLine(String line) {
//		Matcher matcher = patternGame.matcher(line);
//		while (matcher.find()) {
//
//			gameTitle = matcher.group(1);
//		    LevelPlay lp = new LevelPlay();
//		    levelsPlayed.add(lp);
//		    currLP = lp;
//		    
//			currLP.levelNumber = Integer.parseInt(matcher.group(2));
//			currLP.levelNumberTry = Integer.parseInt(matcher.group(3));
//			numberLevelsPlayed++;
//			
//			return true;
//		}
//	}
	
	
	public class SokobanLevel{
		public int width;
		public int height;
		public String[] lvlString;
		
		public SokobanLevel(int width, int height, String[] lvlString){
			this.width = width;
			this.height = height;
			this.lvlString = lvlString;
		}
		
		
		
	}
}
