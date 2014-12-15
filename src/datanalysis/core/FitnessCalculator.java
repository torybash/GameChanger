package datanalysis.core;

import util.Utility;


public class FitnessCalculator {

	private static double[] feature_weights;
	
	private static int EXPLORER = 0;
	private static int MCTS = 1;
	private static int GA = 2;
	private static int ONESTEP_S = 3;
	private static int ONESTEP_H = 4;
	private static int RANDOM = 5;
	private static int DONOTHING = 6;
	
	private static boolean VERBOSE = false;

	private static enum ControllerTypes{
		EXPLORER,
		MCTS,
		GA,
		ONESTEP_S,
		ONESTEP_H,
		RANDOM,
		DONOTHING,
	}

	
	public static GameFitness calculateGameFitness(GameData[] gds) {		
		GameFitness gf = new GameFitness();
		gf.gameTitle = gds[0].gameTitle;
		gf.fitness = calculateFitness(gds);
		
		return gf;
	}

	public static float calculateFitness(GameData[] gds) {
		float fitness = 0;
		float[] fitnessValues = new float[FitnessType.class.getEnumConstants().length];
		int parameters = FitnessType.class.getEnumConstants().length;
		
		if (VERBOSE){System.out.println("------------------------");
			System.out.println("Calculating fitness for "  + gds[0].gameTitle.split("--")[0]);}
		
		
		//SCORE
		float intelligentScore = Math.max(gds[EXPLORER].gameValues.get(DataTypes.AVE) - gds[EXPLORER].gameValues.get(DataTypes.WRATE), gds[MCTS].gameValues.get(DataTypes.AVE) - gds[MCTS].gameValues.get(DataTypes.WRATE));
		float semiIntelligentScore = gds[ONESTEP_S].gameValues.get(DataTypes.AVE) - gds[ONESTEP_S].gameValues.get(DataTypes.WRATE);
		float stupidScore = Math.max(gds[RANDOM].gameValues.get(DataTypes.AVE) - gds[RANDOM].gameValues.get(DataTypes.WRATE), gds[DONOTHING].gameValues.get(DataTypes.AVE) - gds[DONOTHING].gameValues.get(DataTypes.WRATE));
		float randomScore = gds[RANDOM].gameValues.get(DataTypes.AVE) - gds[RANDOM].gameValues.get(DataTypes.WRATE);
		float doNothinScore = gds[DONOTHING].gameValues.get(DataTypes.AVE) - gds[DONOTHING].gameValues.get(DataTypes.WRATE);
		float highestScoreSD = 0;
		for (int c = 0; c < gds.length; c++) {
			float sd = gds[c].gameValues.get(DataTypes.SD);
			if (sd > highestScoreSD) highestScoreSD = sd;
		}
		
		
		if (intelligentScore > 0){
			if (intelligentScore > stupidScore)
	//			fitnessValues[FitnessType.S_INTELLIGENT_OVER_STUPID.ordinal()] = 1;
				fitnessValues[FitnessType.S_INTELLIGENT_OVER_STUPID.ordinal()] = Math.min(Utility.relDiff(intelligentScore, stupidScore), 1) ;
			
			if (intelligentScore > semiIntelligentScore)
	//			fitnessValues[FitnessType.S_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = 1;
				fitnessValues[FitnessType.S_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = Math.min(Utility.relDiff(intelligentScore, semiIntelligentScore), 1) ;
			
			if (semiIntelligentScore > stupidScore)
	//			fitnessValues[FitnessType.S_SEMIINTELLIGENT_OVER_STUPID.ordinal()] = 1;
				fitnessValues[FitnessType.S_SEMIINTELLIGENT_OVER_STUPID.ordinal()] = Math.min(Utility.relDiff(semiIntelligentScore, stupidScore), 1) ;
			
			if (randomScore > doNothinScore)
	//			fitnessValues[FitnessType.S_RANDOM_OVER_DONOTHING.ordinal()] = 1;
				fitnessValues[FitnessType.S_RANDOM_OVER_DONOTHING.ordinal()] = Math.min(Utility.relDiff(randomScore, doNothinScore), 1) ;
			
			if (highestScoreSD > 0)
				fitnessValues[FitnessType.S_SD_OVER_ZERO.ordinal()] = 1;
		}
		
		if (VERBOSE){System.out.println("Intel score: " + intelligentScore + ", semiIntelligentScore: " + semiIntelligentScore + ", stupidScore: " + stupidScore);
			System.out.println("Random score: " + randomScore + ", doNothin score: " + doNothinScore + ", highestScoreSD: " + highestScoreSD);}
		
		//WINRATE
		float intelligentWR = Math.max(gds[EXPLORER].gameValues.get(DataTypes.WRATE), gds[MCTS].gameValues.get(DataTypes.WRATE));
		float semiIntelligentWR = gds[ONESTEP_S].gameValues.get(DataTypes.WRATE);
		float stupidWR = Math.max(gds[RANDOM].gameValues.get(DataTypes.WRATE), gds[DONOTHING].gameValues.get(DataTypes.WRATE));
		boolean disqualified = false;
		float highestWRSD = 0;
		for (int c = 0; c < gds.length; c++) {
			float wr = gds[c].gameValues.get(DataTypes.WRATE);
			float se = gds[c].gameValues.get(DataTypes.WRSE);
			if (se > highestWRSD) highestWRSD = se;
			if (wr < 0) disqualified = true;
		}
		
		boolean gameWithoutWinning = false;
		if (highestWRSD > 0)
			fitnessValues[FitnessType.WR_SD_OVER_ZERO.ordinal()] = 1;
		else{
			for (int c = 0; c < gds.length; c++) {
				float wr = gds[c].gameValues.get(DataTypes.WRATE);
				if (wr == 0){
					if (c == gds.length-1) gameWithoutWinning = true; 
				}else{
					break;
				}
			}
		}
		
		if (!gameWithoutWinning){
			if (intelligentWR > stupidWR)
	//			fitnessValues[FitnessType.WR_INTELLIGENT_OVER_STUPID.ordinal()] = 1;
				fitnessValues[FitnessType.WR_INTELLIGENT_OVER_STUPID.ordinal()] = Math.min(Utility.relDiff(intelligentWR, stupidWR), 1) ;
			
			if (intelligentWR > semiIntelligentWR)
	//			fitnessValues[FitnessType.WR_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = 1;
				fitnessValues[FitnessType.WR_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = Math.min(Utility.relDiff(intelligentWR, semiIntelligentWR), 1) ;
			
			if (semiIntelligentWR > stupidWR)
	//			fitnessValues[FitnessType.WR_SEMIINTELLIGENT_EQOVER_STUPID.ordinal()] = 1;
				fitnessValues[FitnessType.WR_SEMIINTELLIGENT_OVER_STUPID.ordinal()] = Math.min(Utility.relDiff(semiIntelligentWR, stupidWR), 1) ;
		}
		
		if (VERBOSE){System.out.println("intelligentWR: " + intelligentWR + ", semiIntelligentWR: " + semiIntelligentWR + ", stupidWR: " + stupidWR);
			System.out.println("highestWRSD: " + highestWRSD);}

		
		//TICKS
		float intelligentTicks = Math.max(gds[EXPLORER].gameValues.get(DataTypes.AVTIC), gds[MCTS].gameValues.get(DataTypes.AVTIC));
		float semiIntelligentTicks = gds[ONESTEP_S].gameValues.get(DataTypes.AVTIC);
		float randomTicks = gds[RANDOM].gameValues.get(DataTypes.AVTIC);
		float highestTicksAve = 0;
		float highestTicksSD = 0;
		for (int c = 0; c < gds.length; c++) {
			float avtic = gds[c].gameValues.get(DataTypes.AVTIC);
			float sd = gds[c].gameValues.get(DataTypes.SDTIC);
			if (sd > highestTicksSD) highestTicksSD = sd;
			if (avtic > highestTicksAve) highestTicksAve = avtic;
		}
		
		if (intelligentTicks > randomTicks)
//			fitnessValues[FitnessType.TIC_INTELLIGENT_OVER_RANDOM.ordinal()] = 1;
			fitnessValues[FitnessType.TIC_INTELLIGENT_OVER_RANDOM.ordinal()] = Math.min(Utility.relDiff(intelligentTicks, randomTicks), 1) ;

		
		if (intelligentTicks > semiIntelligentTicks)
//			fitnessValues[FitnessType.TIC_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = 1;
			fitnessValues[FitnessType.TIC_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = Math.min(Utility.relDiff(intelligentTicks, semiIntelligentTicks), 1) ;

		
		if (semiIntelligentTicks > randomTicks)
//			fitnessValues[FitnessType.TIC_SEMIINTELLIGENT_OVER_RANDOM.ordinal()] = 1;
			fitnessValues[FitnessType.TIC_SEMIINTELLIGENT_OVER_RANDOM.ordinal()] = Math.min(Utility.relDiff(semiIntelligentTicks, randomTicks), 1) ;

		
		if (highestTicksSD > 0)
			fitnessValues[FitnessType.TIC_SD_OVER_ZERO.ordinal()] = 1;
		
		if (intelligentTicks > 100)
			fitnessValues[FitnessType.TIC_INTELLIGENT_NOT_TOO_LOW.ordinal()] = intelligentTicks > 150 ? 1 : (intelligentTicks - 100)/50f;
		
		if (VERBOSE){
			System.out.println("intelligentTicks: " + intelligentTicks + ", semiIntelligentTicks: " + semiIntelligentTicks + ", random Ticks: " + gds[RANDOM].gameValues.get(DataTypes.AVTIC));
			System.out.println("highestTicksSD: " + highestTicksSD + ", highestTicksAve: " + highestTicksAve);
			
			System.out.println("Fitness values:");
		}
		for (int i = 0; i < fitnessValues.length; i++) {
			fitness += fitnessValues[i] * feature_weights[i];
			if (VERBOSE) System.out.printf("%-35s\t%f\n", FitnessType.values()[i], fitnessValues[i]);
//			System.out.println("Fitness values:\t" + FitnessType.values()[i] + ":\t" + fitnessValues[i]);
		}
		if (VERBOSE) System.out.printf("%-35s\t%f\n", "...TOTAL FITNESS", fitness);
		
		if (gameWithoutWinning){
			parameters -= 4;
		}
		
//		fitness = fitness / (float)parameters;
		
		if (disqualified) fitness = -1;
		
		return fitness;
	}

	
	
	public enum FitnessType{
		S_INTELLIGENT_OVER_STUPID,
		S_INTELLIGENT_OVER_SEMIINTELLIGENT,
		S_SEMIINTELLIGENT_OVER_STUPID,
		S_RANDOM_OVER_DONOTHING,
		S_SD_OVER_ZERO,
		WR_INTELLIGENT_OVER_STUPID,
		WR_INTELLIGENT_OVER_SEMIINTELLIGENT,
		WR_SEMIINTELLIGENT_OVER_STUPID,
		WR_SD_OVER_ZERO,
		TIC_INTELLIGENT_OVER_RANDOM,
		TIC_INTELLIGENT_OVER_SEMIINTELLIGENT,
		TIC_SEMIINTELLIGENT_OVER_RANDOM,
		TIC_SD_OVER_ZERO,
		TIC_INTELLIGENT_NOT_TOO_LOW,
//		ENT_SEMIINTELLIGENT_OVER_EXPLORER
	}


	public static void setWeights(double[] featureWeights) {
		if (featureWeights == null){
			feature_weights = new double[FitnessType.class.getEnumConstants().length];
			for (int i = 0; i < FitnessType.class.getEnumConstants().length; i++) {
				feature_weights[i] = 1f;
			}
		}else{
			feature_weights = featureWeights.clone();
		}
	}
	
	
}


