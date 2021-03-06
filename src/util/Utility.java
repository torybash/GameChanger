package util;

import java.util.Random;


public class Utility {

	public static double logOfBase(int base, float num) {
	    return Math.log(num) / Math.log(base);
	}

	public static double relDiff(double d1, double d2){		
		double division = Math.max(Math.abs(d1), Math.abs(d2));
		
		if (division == 0) return 0;
		return (d1-d2)/division;
//		double division = Math.max(Math.abs(d1), Math.abs(d2));
//		double result = 0;
//		if (d1 > d2){
//			result = Math.abs(d1-d2)/division;
//		}else if (d1 < d2){
//			result = -Math.abs(d1-d2)/division;
//		}
//		if (result > 1) result = 1; if (result < -1) result = -1;
//		return result;
	}
	
	public static float relDiff(float f1, float f2){
		float division = Math.max(Math.abs(f1), Math.abs(f2));
		if (division == 0) return 0;
		return (f1-f2)/division;
//		float division = Math.max(Math.abs(f1), Math.abs(f2));
//		float result = 0;
//		if (f1 > f2){
//			result = Math.abs(f1-f2)/division;
//		}else if (f1 < f2){
//			result = -Math.abs(f1-f2)/division;
//		}
//		if (result > 1) result = 1; if (result < -1) result = -1;
//		return result;
	}
	
	public static int getAmountOfActionsInGame(String gameTitle) {
		return 5;
		
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

	public static double divDiff(double d1, double d2) {
		double result = 0;
		boolean differentSign = (d1 >= 0 ? (d2 >= 0 ? false : true) : (d2 < 0 ? true : false));
//		if (Math.abs(d1) > Math.abs(d2)){
//			result = 1-Math.abs(d2)/Math.abs(d1);
//			if (d1 < 0) result *= -1;
//		}else if (Math.abs(d1) < Math.abs(d2)){
//			result = Math.abs(d1)/Math.abs(d2) - 1;
//			if (d2 < 0) result *= -1;
//		}
//		
//		
		
		result = d2/d1;
		
		 System.out.println("intermediate result: " + result + ", differentSign: " + differentSign);

		if (result > 1) result = 1; if (result < -1) result = -1;
			
		return result;
	}

}
