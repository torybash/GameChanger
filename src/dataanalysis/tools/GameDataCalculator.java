package dataanalysis.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import util.Utility;
import dataanalysis.core.DataTypes;
import dataanalysis.core.GameData;
import dataanalysis.core.LevelPlay;

public class GameDataCalculator {

	
	private static final int LOWEST_TICKS_ALLOWED = 10;
	private static final int MAX_ABS_SCORE = 10000;
	
        static boolean scoresEqual = true;
	static float lastScore = 0;
	
	public static ArrayList<GameData[]> getAcceptedGames(ArrayList<GameData[]> gameDatas) {
		int n = gameDatas.get(0).length;
		
		ArrayList<GameData[]> acceptedGameDatas = new ArrayList<GameData[]>();
		
//		for (int c = 0; c < n; c++) acceptedGameDatas.get(0)[c] = new ArrayList<GameData>();
		
		for (int g = 0; g < gameDatas.size(); g++) { //for each game		
			if (gameDatas.get(g)[0].gameTitle.contains("camelRace") 
					|| gameDatas.get(g)[0].gameTitle.contains("sokoban")
//					|| gameDatas.get(g)[0].gameTitle.contains("chase")
//					|| gameDatas.get(g)[0].gameTitle.contains("firestorms")
//					|| gameDatas.get(g)[0].gameTitle.contains("infection")
//					|| gameDatas.get(g)[0].gameTitle.contains("firecaster")
//					|| gameDatas.get(g)[0].gameTitle.contains("butterflies")
//					|| gameDatas.get(g)[0].gameTitle.contains("frogs")
				)
				continue;
			
			
			boolean acceptedGame = true; 
			// Per game
			for (int c = 0; c < n; c++) { //for each controller
				ArrayList<LevelPlay> levelsPlayed = gameDatas.get(g)[c].levelsPlayed; 
				if (hasDisqualified(levelsPlayed)
//						|| allScoresEqual(levelsPlayed, c, n)
//						|| allwaysLowTime(levelsPlayed, c, n)
				)
					acceptedGame = false;
			}
			
			if (acceptedGame){
				acceptedGameDatas.add(gameDatas.get(g));
			}
		}
		
		return acceptedGameDatas;
	}
	
	
	public static ArrayList<GameData[]> getAverageForEachGame(ArrayList<GameData[]> gameDatas) {
		ArrayList<GameData[]> result = new ArrayList<GameData[]>();
		for (int g = 0; g < gameDatas.size(); g++) {  //For each game
			ArrayList<GameData[]> singleInputList = new ArrayList<GameData[]>();
			singleInputList.add(gameDatas.get(g));
			
			GameData[] aveGameDatas = getAverageGameDatas(singleInputList, gameDatas.get(g)[0].gameTitle + " average");
			result.add(aveGameDatas);
		}
		return result;
	}
	
	
	public static GameData[] getAverageGameDatas(ArrayList<GameData[]> gameDatas, String name) {
		int n = gameDatas.get(0).length;
		int amountGames = gameDatas.size();
		int amountPlaythroughs = gameDatas.get(0)[0].levelsPlayed.size();
		
		GameData[] aveGameDatas = new GameData[n];
		
		float[][] allControllersGamePlaythroughMins = new float[amountGames][];
		float[][] allControllersGamePlaythroughMaxs = new float[amountGames][];
		float[][] allControllersGamePlaythroughAves = new float[amountGames][];
		float[][] allControllersGamePlaythroughStds = new float[amountGames][];
		for (int g = 0; g < amountGames; g++) {
			allControllersGamePlaythroughMins[g] = new float[amountPlaythroughs];
			allControllersGamePlaythroughMaxs[g] = new float[amountPlaythroughs];
			allControllersGamePlaythroughAves[g] = new float[amountPlaythroughs];
			allControllersGamePlaythroughStds[g] = new float[amountPlaythroughs];
			for (int i = 0; i < amountPlaythroughs; i++) {
				float min = Float.POSITIVE_INFINITY;
				float max = Float.NEGATIVE_INFINITY;
				float sum = 0, sumsq = 0, ave = 0, sd = 0;
				
				for (int c = 0; c < n; c++) { //for each controller
					LevelPlay lp = gameDatas.get(g)[c].levelsPlayed.get(i);
					if (lp.score > max) max = lp.score;
					if (lp.score < min) min = lp.score;
					sum += lp.score;
					sumsq += lp.score * lp.score;
				}
				ave = sum / n;
				double num = sumsq - (n * ave * ave);
		        if (num < 0) num = 0;
		        sd = (float) Math.sqrt(num / (n - 1));
				allControllersGamePlaythroughMins[g][i] = min;
				allControllersGamePlaythroughMaxs[g][i] = max;
				allControllersGamePlaythroughAves[g][i] = ave;
				allControllersGamePlaythroughStds[g][i] = sd;
			}
		}
		
		
		for (int c = 0; c < n; c++) { //for each controller
			GameData[] aveGds = new GameData[n];
//			System.out.println("---Controller: " + c);
			
			int count = amountGames * amountPlaythroughs;
			
			float min = Float.POSITIVE_INFINITY;
			float max = Float.NEGATIVE_INFINITY;
			float sum = 0;
			float sumsq = 0;
			float ave = 0;
			float sd = 0;
			float se = 0;
			float nsum = 0;
			float nsumsq = 0;
			float nave = 0;
			float nsd = 0;
			float nse = 0;
			float znsum = 0;
			float znave = 0;
			float sumtic = 0, sumsqtic = 0, avtic = 0, sdtic = 0, setic = 0;
			float winsum = 0;
			float wrate = 0;
			float wrsd = 0;
			float wrse = 0;
			float mmsum = 0, mmsumsq = 0, mmave = 0, mmsd = 0, mmse = 0, mmpecentile15p865 = 0, mmpecentile84p135 = 0;
			float median = 0, quartile1 = 0, quartile3 = 0;
			float actensum = 0;
			float acten = 0;
			float actse = 0;
			float nint = 0, naint = 0, nspr = 0, nspra = 0, nsprk = 0, nwals = 0;


			float[] scoreArray = new float[count];
			float[] mmScoreArray = new float[count];

			for (int g = 0; g < amountGames; g++) { //for each game	
				GameData gd = gameDatas.get(g)[c];
				
				int amountOfActions = 0;
				HashMap<String, Integer> actcounter = new HashMap<String, Integer>();
				actcounter.put("ACTION_NIL", 0);actcounter.put("ACTION_LEFT", 0);actcounter.put("ACTION_RIGHT", 0);
				actcounter.put("ACTION_UP", 0);actcounter.put("ACTION_DOWN", 0);actcounter.put("ACTION_USE", 0);
				
				
				float game_sum = 0;
				float game_sumsq = 0;
				float game_ave = 0;
				float game_sd = 0;
				float game_max = Float.NEGATIVE_INFINITY;
				float game_min = Float.POSITIVE_INFINITY;
				for (int i = 0; i < gd.levelsPlayed.size(); i++) {
					LevelPlay lp = gd.levelsPlayed.get(i);
					game_sum += lp.score;
					game_sumsq += lp.score * lp.score;
					sum += lp.score;
					nsum += lp.score > 0 ? 1 : 0;
					sumsq += lp.score * lp.score;
					nsumsq += (lp.score > 0 ? 1 : 0) * (lp.score > 0 ? 1 : 0);
					sumtic += lp.timesteps;
					sumsqtic += lp.timesteps * lp.timesteps;
					winsum += lp.won;
					
					nint += lp.numInteractions / (float)lp.timesteps;
					naint += lp.numAvatarInteractions / (float)lp.timesteps;
					nspr += lp.numSprites;
					nspra += lp.numSpritesAdded;
					nsprk += lp.numSpritesKilled;
					if (nwals <= 0) nwals += lp.numWalls;
					
					if (lp.score > max) max = lp.score;
					if (lp.score < min) min = lp.score;
					scoreArray[i + g * gd.levelsPlayed.size()] = lp.score;
					
					if (lp.score > game_max) game_max = lp.score;
					if (lp.score < game_min) game_min = lp.score;
										
					for (String act : lp.actionSeq) {
						if (act.contains("NIL")) continue;
						actcounter.put(act, actcounter.get(act) + 1);
						amountOfActions++;
					}
					
//			        ZNMean:
					float zNormalizeVal = lp.score - allControllersGamePlaythroughAves[g][i] == 0 ? 0 : (lp.score - allControllersGamePlaythroughAves[g][i]) / allControllersGamePlaythroughStds[g][i];
					znsum += zNormalizeVal;// * lp.score;
				
//			        MMMean:
		        	float maxMinNormalizedScore = allControllersGamePlaythroughMaxs[g][i] - allControllersGamePlaythroughMins[g][i]== 0 ? 0.5f : (lp.score - allControllersGamePlaythroughMins[g][i]) / (allControllersGamePlaythroughMaxs[g][i] - allControllersGamePlaythroughMins[g][i]);
		        	mmsum += maxMinNormalizedScore;
		        	mmsumsq += maxMinNormalizedScore * maxMinNormalizedScore;
		        	mmScoreArray[i + g * gd.levelsPlayed.size()] = maxMinNormalizedScore;			        	
				}
				
				game_ave = game_sum / (float)gd.levelsPlayed.size();
				double num = game_sumsq - (gd.levelsPlayed.size() * game_ave * game_ave);
		        if (num < 0)  num = 0;
		        game_sd = (float) Math.sqrt(num / (gd.levelsPlayed.size() - 1));
		        
		        
				float H = 0;
//				System.out.println("Probs for game: " + gd.gameTitle);
				for (String string : actcounter.keySet()) {
					float actionProb = actcounter.get(string) / (float)amountOfActions;
					H += actionProb == 0 ? 0 : actionProb * Utility.logOfBase(Utility.getAmountOfActionsInGame(gd.gameTitle), actionProb);
//					System.out.println("Probability of " + string + ": " + actionProb);
				}
				actensum += -H;
//				System.out.println("amount of actions: " + amountOfActions);
//				System.out.println("entropy: " + -H + ", count: " + count);
		   
			}
			
			Arrays.sort(scoreArray);
			if (scoreArray.length % 2 == 0){
				median = ((float)scoreArray[scoreArray.length/2] + (float)scoreArray[scoreArray.length/2 - 1])/2;
				quartile1 = ((float)scoreArray[scoreArray.length/4] + (float)scoreArray[scoreArray.length/4 - 1])/2;
				quartile3 = ((float)scoreArray[scoreArray.length*3/4] + (float)scoreArray[scoreArray.length*3/4 - 1])/2;
			}else{
				median = (float) scoreArray[scoreArray.length/2];
				quartile1 = (float) scoreArray[scoreArray.length/4];
				quartile3 = (float) scoreArray[scoreArray.length*3/4];
			}

			
			ave = sum / (float)count;
			avtic = sumtic / (float)count;
			nave = nsum / (float)count;
			wrate = winsum / (float)count;
			
			nint = nint / (float)count;
			naint = naint / (float)count;
			nspr = nspr / (float)count;
			nspra = nspr / (float)count;
			nsprk = nsprk / (float)count;
//			nwals = nwals / (float)count;
			
			double num = sumsq - (count * ave * ave);
	        if (num < 0) {
	            num = 0;
	        }
	        sd = (float) Math.sqrt(num / (count - 1));
	        se = sd / (float)Math.sqrt(count);
	       
	        wrse = (float) Math.sqrt(count * wrate * (1 - wrate)) / count;		//	        sqrt(np(1-p))
	        
			num = nsumsq - (count * nave * nave);
	        if (num < 0) {
	            num = 0;
	        }
	        nsd = (float) Math.sqrt(num / (count - 1));
	        nse = nsd / (float)Math.sqrt(count);
	        
			num = sumsqtic - (count * avtic * avtic);
	        if (num < 0) {
	            num = 0;
	        }
	        sdtic = (float) Math.sqrt(num / (count - 1));
	        setic = sdtic / (float)Math.sqrt(count);
	        
	        znave = znsum / (float)count;
	        mmave = mmsum / (float)count;
			num = mmsumsq - (count * mmave * mmave);
	        if (num < 0) {
	            num = 0;
	        }
	        mmsd = (float) Math.sqrt(num / (count - 1));
	        mmse =   mmsd / (float)Math.sqrt(count);
	        Arrays.sort(mmScoreArray);
			mmpecentile15p865 = (float) mmScoreArray[(int)(mmScoreArray.length * 0.15865)];
			mmpecentile84p135 = (float) mmScoreArray[(int)(mmScoreArray.length * 0.84135)];
	        
			acten = actensum / (float)amountGames; 
	        actse = (float) Math.sqrt(count * acten * (1 - acten)) / count;		//	        sqrt(np(1-p))

//			float H = 0;
////			System.out.println("Probs for try nr: " + lp.levelNumberTry);
//			for (String string : actcounter.keySet()) {
//				float actionProb = actcounter.get(string) / (float)amountOfActions;
//				H += actionProb == 0 ? 0 : actionProb * Utility.logOfBase(2, actionProb);
////				System.out.println("Probability of " + string + ": " + actionProb);
//			}
//			acten = -H;
			
			aveGds[c] = new GameData();
			aveGds[c].gameTitle = name + (amountGames > 1 ? (" -- amount of games: " + amountGames) : "") + " -- playthroughs: " + amountPlaythroughs;
			
			aveGds[c].gameValues.put(DataTypes.AVE, ave); 
			aveGds[c].gameValues.put(DataTypes.NAVE, nave); 
			aveGds[c].gameValues.put(DataTypes.ZNAVE, znave); 
			aveGds[c].gameValues.put(DataTypes.MMAVE, mmave); 
			aveGds[c].gameValues.put(DataTypes.MMSD, mmsd); 
			aveGds[c].gameValues.put(DataTypes.MMSE, mmse); 
			aveGds[c].gameValues.put(DataTypes.MEDI, median); 
			aveGds[c].gameValues.put(DataTypes.QUAR1, quartile1); 
			aveGds[c].gameValues.put(DataTypes.QUAR3, quartile3); 
			aveGds[c].gameValues.put(DataTypes.ERRLO, mmpecentile15p865); 
			aveGds[c].gameValues.put(DataTypes.ERRHI, mmpecentile84p135); 
			aveGds[c].gameValues.put(DataTypes.MAX, max); 
			aveGds[c].gameValues.put(DataTypes.MIN, min); 
			aveGds[c].gameValues.put(DataTypes.SD, sd); 
			aveGds[c].gameValues.put(DataTypes.SE, se); 
			aveGds[c].gameValues.put(DataTypes.WRATE, wrate); 
			aveGds[c].gameValues.put(DataTypes.WRSD, wrsd); 
			aveGds[c].gameValues.put(DataTypes.WRSE, wrse); 
			aveGds[c].gameValues.put(DataTypes.AVTIC, avtic); 
			aveGds[c].gameValues.put(DataTypes.SDTIC, sdtic); 
			aveGds[c].gameValues.put(DataTypes.SETIC, setic); 
			aveGds[c].gameValues.put(DataTypes.ACTEN, acten); 
			aveGds[c].gameValues.put(DataTypes.ACTSE, actse); 
			aveGds[c].gameValues.put(DataTypes.NINT, nint); 
			aveGds[c].gameValues.put(DataTypes.NAINT, naint); 
			aveGds[c].gameValues.put(DataTypes.NSPR, nspr); 
			aveGds[c].gameValues.put(DataTypes.NSPRA, nspra); 
			aveGds[c].gameValues.put(DataTypes.NSPRK, nsprk); 
			aveGds[c].gameValues.put(DataTypes.NWALS, nwals); 
	        
	        aveGameDatas[c] = aveGds[c];
		}
		
		return aveGameDatas;
	}
	
        
        
    public static void getGoodToBadValues(ArrayList<GameData[]> acceptedGameDatas1, ArrayList<GameData[]> acceptedGameDatas2) {
        
        float[] values1 = getGoodToBadDistribution(acceptedGameDatas1);
        float[] values2 = getGoodToBadDistribution(acceptedGameDatas2);
        
        
        System.out.println("------THE FIRST------");
        for (int i = 0; i < values1.length; i++) {
            System.out.println(values1[i]);
        }
        
        System.out.println("------THE SECOND------");
        for (int i = 0; i < values2.length; i++) {
            System.out.println(values2[i]);
        }
    }

    
    private static float[] getGoodToBadDistribution(ArrayList<GameData[]> gameDatas){
        float[] values = new float[gameDatas.size() * gameDatas.get(0)[0].levelsPlayed.size()];
        int c = 0;
        for (GameData[] gds : gameDatas) {
            GameData goodCtrlGameData = gds[0];
            GameData badCtrlGameData = gds[gds.length-2];
            
            
            for (int l = 0; l < goodCtrlGameData.levelsPlayed.size(); l++) {
                LevelPlay goodCtrlLp = goodCtrlGameData.levelsPlayed.get(l);
                LevelPlay badCtrlLp = badCtrlGameData.levelsPlayed.get(l);
                
                System.out.println("goodCtrlLp.score, badCtrlLp.score: " + goodCtrlLp.score +", " + badCtrlLp.score + ", Utility.relDiff(goodCtrlLp.score, badCtrlLp.score): " + Utility.relDiff(goodCtrlLp.score, badCtrlLp.score));
                
                values[c++] = Utility.relDiff(goodCtrlLp.score, badCtrlLp.score);
            }
        }
        return values;
    }

	private static  boolean allScoresEqual(ArrayList<LevelPlay> levelsPlayed, int ctrlIdx, int n) {
		if (ctrlIdx == 0) scoresEqual = true;
		else if (!scoresEqual) return false;
		
		for (int i = 0; i < levelsPlayed.size(); i++) {
			LevelPlay lp = levelsPlayed.get(i);
			if ((i != 0 || ctrlIdx != 0) && lp.score != lastScore){
				scoresEqual = false;
				return false;
			}
			lastScore = lp.score;
		}
		if (ctrlIdx == n-1 && scoresEqual) return true;
		return false;
	}
	
	static boolean allwaysLowTime = true;
	private static  boolean allwaysLowTime(ArrayList<LevelPlay> levelsPlayed, int ctrlIdx, int n) {
		if (ctrlIdx == 0) allwaysLowTime = true;
		
		for (int i = 0; i < levelsPlayed.size(); i++) {
			LevelPlay lp = levelsPlayed.get(i);
			if (lp.timesteps > LOWEST_TICKS_ALLOWED) allwaysLowTime = false;
		}
		
		if (ctrlIdx == n-1 && allwaysLowTime) return true;
		return false;
	}

	private static boolean hasDisqualified(ArrayList<LevelPlay> levelsPlayed) {
		for (LevelPlay lp : levelsPlayed) {
			if (lp.won < 0) return true;
		}
		return false;
	}
	
	private static boolean scoresAbove(ArrayList<LevelPlay> levelsPlayed, float maxAbsScore) { 		
		for (LevelPlay lp : levelsPlayed) {
			if (Math.abs(lp.score) > maxAbsScore) return true;
		}
		return false;
	}


}
