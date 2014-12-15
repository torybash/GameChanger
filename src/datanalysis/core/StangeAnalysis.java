package datanalysis.core;

import java.util.ArrayList;

import tools.IO;

public class StangeAnalysis {

//	private void findStrangeGames(String[] dataFolders, boolean[] controllersToCompareWith) {
//		int n = dataFolders.length;
//		
//		ArrayList<GameData>[] gameDatas = extractGameDatas(dataFolders);
//		
//		ArrayList<GameData>[] strangeGameDatas = new ArrayList[n];
//		for (int c = 0; c < n; c++)  strangeGameDatas[c] = new ArrayList<GameData>();
//
//		for (int g = 0; g < gameDatas[0].size(); g++) {			
//			float[] scoreArray = new float[n];
//			for (int c = 0; c < n; c++) {
//				scoreArray[c] = gameDatas[c].get(g).gameValues.get("ave");
//			}
//						
//			boolean acceptedGame = true; 
//			for (int c = 0; c < n; c++) { //for each controller
//				ArrayList<LevelPlay> levelsPlayed = gameDatas[c].get(g).levelsPlayed;
//				if (hasDisqualified(levelsPlayed) || allScoresEqual(levelsPlayed, c, n)){
//					acceptedGame = false;
//					break;
//				}
//			}
//			
//			if (acceptedGame){
//				boolean strangeGame = true;
//				for (int i = 0; i < controllersToCompareWith.length; i++) {
//					if (controllersToCompareWith[i] && scoreArray[n-1] <= scoreArray[i]){ strangeGame = false; break;}
//				}
//			
//				if (strangeGame){
//					for (int c = 0; c < n; c++) {
//						strangeGameDatas[c].add(gameDatas[c].get(g));
//					}
//				}
//			}
//		}
//		
//		compareStrangeGameDatas(strangeGameDatas, dataFolders, "generatedgames/", gameDatas[0].size(), controllersToCompareWith);	
//	}
//
//	private void findStrangeMutatedGames(String[] dataFolders, int numberMutations, boolean[] controllersToCompareWith) {
//		int n = dataFolders.length;
//		
//		ArrayList<GameData>[][] gameDatasList = extractMutatedGameDatas(dataFolders, numberMutations);
//		int numberGames = gameDatasList[0][0].size(); //amount of games
//		
//		ArrayList<GameData>[] strangeGameDatas = new ArrayList[n];
//		for (int c = 0; c < n; c++)  strangeGameDatas[c] = new ArrayList<GameData>();
//		for (int m = 0; m < numberMutations; m++) {
//			for (int g = 0; g < numberGames; g++) {
//				float[] scoreArray = new float[n];
//				for (int c = 0; c < n; c++) {
//					scoreArray[c] = gameDatasList[m][c].get(g).gameValues.get("ave");
//				}
//								
//				boolean acceptedGame = true; 
//				for (int c = 0; c < n; c++) { //for each controller
//					ArrayList<LevelPlay> levelsPlayed = gameDatasList[m][c].get(g).levelsPlayed;
//					if (hasDisqualified(levelsPlayed) || allScoresEqual(levelsPlayed, c, n)){
//						acceptedGame = false;
//						break;
//					}
//				}
//				
//				if (acceptedGame){
//					boolean strangeGame = true;
//					for (int i = 0; i < controllersToCompareWith.length; i++) {
//						if (controllersToCompareWith[i] && scoreArray[n-1] <= scoreArray[i]){ strangeGame = false; break;}
//					}
//				
//					if (strangeGame){
//						for (int c = 0; c < n; c++) {
//							strangeGameDatas[c].add(gameDatasList[m][c].get(g));
//						}
//					}
//				}
//			}
//		}
//
//		compareStrangeGameDatas(strangeGameDatas, dataFolders, "mutatedgames/", numberMutations * numberGames, controllersToCompareWith);	
//	}
//	
//	private void compareStrangeGameDatas(ArrayList<GameData>[] strangeGameDatas, String[] dataFolders, String gamesFolder, int origAmount, boolean[] controllersToCompareWith){
//		
//		String controllers = "[";
//		int amount = 0;
//		System.out.println(controllersToCompareWith.length);
//		for (int c = 0; c < dataFolders.length; c++) {
//			if (controllersToCompareWith.length <= c) break;
//			
//			if (controllersToCompareWith[c]){
//				if (amount > 0) controllers += " and ";
//				controllers += dataFolders[c].split("/")[1].split("\\d")[0];
//				amount++;
//			}
//		}
//		controllers += "]";
//		
//		System.out.println("Games where " + controllers + " have lower average than random. " + strangeGameDatas[0].size() + " games found out of " + origAmount);
//		System.out.println();
//		for (int i = 0; i < strangeGameDatas[0].size(); i++) {  //For each game
//			GameData[] gds = new GameData[dataFolders.length];			
//			for (int j = 0; j < dataFolders.length; j++) {				//For each controller
//				gds[j] = strangeGameDatas[j].get(i);
//			}
//			
//			//Get game description
//			String gameTitle = gds[0].gameTitle;
//			String[] gameDescLines = new IO().readFile(gamesFolder + gameTitle + ".txt");
//			String gameDesc = "";
//			for (int j = 0; j < gameDescLines.length; j++) gameDesc += gameDescLines[j] + "\n";
//			
//			compareGameDatas(gds, dataFolders);
//			System.out.println("Game description: ");
//			System.out.println(gameDesc);
//			System.out.println();
//		}
//	}
}
