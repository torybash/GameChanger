package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import datanalysis.core.GameData;

public class Utility {

	public static double logOfBase(int base, float num) {
	    return Math.log(num) / Math.log(base);
	}

	public static double relDiff(double d1, double d2){		
		double max = Math.max(d1, d2);
		if (max == 0) return 0;		
		double result = Math.abs(d1-d2)/max;
		if (result > 1) result = 1;
		else if (result < -1) result = -1;
		return result;
	}
	
	public static float relDiff(float f1, float f2){	
		float max = Math.max(f1, f2);
		if (max == 0) return 0;		
		float result = Math.abs(f1-f2)/max;
		if (result > 1) result = 1;
		else if (result < -1) result = -1;
		return result;
	}
	
	public static int getAmountOfActionsInGame(String gameTitle) {
		return 0;
		
//		String gameDesc = "";
//		
//		if (gameTitle.contains("mutation")){
//			gameDesc = "mutatedgames/" + gameTitle + ".txt";
//		}else if (gameTitle.contains("gen")){
//			gameDesc = "generatedgames/" + gameTitle + ".txt";
//		}else{
//			//Examples
//			gameDesc = "../gvgai/examples/gridphysics/" + gameTitle + ".txt";
//		}
//		String avatarClass = null;
//		BufferedReader br;
//		try {
//			br = new BufferedReader(new FileReader(gameDesc));
//			String line;
//			while ((line = br.readLine()) != null) {
//				if (line.contains("Avatar")){
//					String formatedLine = line.replaceAll("\t","    ");
//					formatedLine = formatedLine.trim();
//					formatedLine = formatedLine.replaceAll("[\\s&&[^\\n]]+", " ").replaceAll("(?m)^\\s|\\s$", "");
//			        String pieces[] = formatedLine.split(" ");
//			        avatarClass = pieces[2];
//			        
//				}else if (line.contains("InteractionSet") || line.contains("TerminationSet") || line.contains("LevelMapping")) break;
//			}
//		}  catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		if (avatarClass == null){
//			return 4; //moving avatar
//		}else{
//	        switch (avatarClass) {
//			case "MovingAvatar":
//				return 4;
//			case "FlakAvatar":
//				return 3;
//			case "ShootAvatar":
//				return 5;
//			case "OrientedAvatar":
//				return 4;
//			case "HorizontalAvatar":
//				return 2;
//			default:
//				return -1;
//			}
//		}
	}
}
