package dataanalysis.fitness;

import java.util.ArrayList;

import util.Utility;
import dataanalysis.controller.Controller;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.core.DataTypes;
import dataanalysis.core.GameData;
import dataanalysis.core.LevelPlay;


public class FitnessCalculator {

	private static double[] feature_weights;
	
	private static boolean VERBOSE = false;
	
	
	public static GameFitness calculateGameFitness(GameData[] gds, Controller[] controllers, boolean[] dataTypesToUse) {		
		boolean[] dataTypes = dataTypesToUse;
		if (dataTypesToUse == null){ 
			dataTypes = new boolean[FeatureDataType.class.getEnumConstants().length];
			for (int i = 0; i < dataTypes.length; i++) dataTypes[i] = true;
		}
		GameFitness gf =  calculateFitness(gds, controllers, dataTypes);
		
		return gf;
	}

	private static GameFitness calculateFitness(GameData[] gds, Controller[] controllers, boolean[] dataTypesToUse) {
		GameFitness gf = new GameFitness(gds[0].gameTitle);
		
		
		boolean hasLowTicksWin = false;
		
		int maxDataTypeCount = FeatureDataType.class.getEnumConstants().length;
		int ctrlTypeCount = ControllerType.class.getEnumConstants().length;
		
		double fitness = 0;
		int parameterCount = getFeatureCount();
				
		double[] fitnessVals = new double[parameterCount];
		String[] fitnessValsString = new String[parameterCount];
		
		double[][] highestValues = new double[maxDataTypeCount][];
		for (int t = 0; t < maxDataTypeCount; t++) {
			highestValues[t] = new double[ctrlTypeCount];
			for (int c = 0; c < ctrlTypeCount; c++){
				highestValues[t][c] = Double.NEGATIVE_INFINITY;
			}
		}
		
		for (int c = 0; c < controllers.length; c++) {
			Controller ctrl = controllers[c];
			for (int t = 0; t < maxDataTypeCount; t++) {
				FeatureDataType typ = FeatureDataType.values()[t];
				if (typ.dataType == null) continue;
				double ctrlVal = gds[c].gameValues.get(typ.dataType); //highestValues[t][ctrl.type.id()];
				if (ctrlVal > highestValues[t][ctrl.type.id()]) highestValues[t][ctrl.type.id()] = ctrlVal;
			}
			
			for (LevelPlay lp: gds[c].levelsPlayed) {
				if (lp.won == 1 && lp.timesteps < 50) hasLowTicksWin = true;
			}
		}
		
		if (VERBOSE) System.out.println(gds[0].gameTitle);
		
//		System.out.println("game: " + gds[0].gameTitle + ", hasLowTicksWin: " + hasLowTicksWin);
		
		int cnt = 0;
		for (int t = 0; t < maxDataTypeCount; t++) {
			if (!dataTypesToUse[t]) continue;
			FeatureDataType typ = FeatureDataType.values()[t];
			
			double val = 0;
						
			if (typ.relDiffs && typ.ctrlType == null){
				for (int ct1 = 0; ct1 < 1; ct1++) {
					for (int ct2 = 0; ct2 < ctrlTypeCount; ct2++) {				
						if (ct1 >= ct2) continue;
	
						val = Utility.relDiff(highestValues[t][ct1], highestValues[t][ct2]); // / 2.0 + 0.5;
						
						fitness += val * feature_weights[cnt];		
						
						if (VERBOSE) System.out.println(ControllerType.values()[ct1] + " > " + ControllerType.values()[ct2] + ", DataType: " + typ + ", relDiff: " + val + 
								", Highest val 1: " + highestValues[t][ct1] + ", Highest val 2: " + highestValues[t][ct2] + ", feature_weights[cnt]: " + feature_weights[cnt]);
						fitnessVals[cnt] = val;
						fitnessValsString[cnt] = "reldiff(" + String.format("%.3f", highestValues[t][ct1]) + ", " + String.format("%.3f", highestValues[t][ct2]) + ") = " + val;
						cnt++;
					}
				}
			}else if (typ.relDiffs){ //compare controller 0 with a specific other
				if (typ.dataType.equals(DataTypes.ACTEN)){ //special case for entropy comparison
					//f(int => random) = 1
					//f(int=0.5, random=1) = 0
					//f(int=0, random=1) = -1
					if (highestValues[t][0] >= highestValues[t][typ.ctrlTyp().id()]){
						val = 1;
					}else{
						val = 2*(0.5 + highestValues[t][0] - highestValues[t][typ.ctrlTyp().id()]);
					}
					
				}else{
					val = Utility.relDiff(highestValues[t][0], highestValues[t][typ.ctrlTyp().id()]); 
				}
				
				fitness += val * feature_weights[cnt];
				fitnessVals[cnt] = val;
				fitnessValsString[cnt] = "reldiff(" + String.format("%.3f", highestValues[t][0]) + ", " + String.format("%.3f", highestValues[t][typ.ctrlTyp().id()]) + ") = " + val;
				cnt++;
			}else{ //just use value as fitness (or special cases)
				if (typ == FeatureDataType.HAS_LOW_TICKS){
					val = hasLowTicksWin ? -1 : 1;
				}else if (typ.dataType == DataTypes.AVTIC){
					val = (highestValues[t][ControllerType.INTELLIGENT.id()] > 50) ? 1 : -1;
				}else{
					val = highestValues[t][typ.ctrlTyp().id()];
				}
				
				
				fitness += val * feature_weights[cnt];
				fitnessVals[cnt] = val;
				fitnessValsString[cnt] = "valueOf(" + String.format("%.3f", highestValues[t][typ.ctrlTyp().id()]) + ") = " + val;
				cnt++;
			}			
		}
		if (VERBOSE) System.out.println("---Total fitness:\t" + fitness);
		if (VERBOSE) System.out.println("parameterCount: " + parameterCount);
		
		fitness /= parameterCount;
		
		gf.fitness = fitness;
		gf.fitnessVals = fitnessVals;
		gf.fitnessValsString = fitnessValsString;
		                
		return gf;
	}

	private static int getFeatureCount() {
		int paramCnt = 0;
		for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
			FeatureDataType typ = FeatureDataType.values()[t];
			if (typ.relDiffs && typ.ctrlType == null){
				for (int c2 = 1; c2 < ControllerType.class.getEnumConstants().length; c2++) {
					paramCnt += 1;
				}
				
			}else{
				paramCnt += 1;
			}
		}
		return paramCnt;
	}

	private static void makeCSV(double[] fitnessVals) {
		int dataTypeCount = FeatureDataType.class.getEnumConstants().length;
		int ctrlTypeCount = ControllerType.class.getEnumConstants().length;
		
//		String topString = "";
		String mainString = "";
		
		int cnt = 0;
		
		for (int ct1 = 0; ct1 < 1; ct1++) {
			for (int ct2 = 0; ct2 < ctrlTypeCount; ct2++) {				
				if (ct1 >= ct2) continue;
				for (int t = 0; t < dataTypeCount; t++) {
					mainString += fitnessVals[cnt++] + ",";
				}
			}
		}
		
		System.out.println(mainString);
	}
	
	public static ArrayList<GameFitness> getFitnessForEachGame(ArrayList<GameData[]> gameDatas, Controller[] controllers, boolean[] dataTypesToUse) {
		ArrayList<GameFitness> fitnessValues = new ArrayList<GameFitness>();
		for (int g = 0; g < gameDatas.size(); g++) {  //For each game
			GameFitness gf = calculateGameFitness(gameDatas.get(g), controllers, dataTypesToUse);
			fitnessValues.add(gf);
		}		
		return fitnessValues;
	}

	public static double[] matrixWeightsToArray(double[][][] matrixWeights){
        ArrayList<Double> list = new ArrayList<Double>();

        for (int c1 = 0; c1 < 1; c1++) {
            for (int c2 = 0; c2 < matrixWeights[c1].length; c2++) {
                if (c1 >= c2) continue;
                for (int t = 0; t < matrixWeights[c1][c2].length; t++) {
                    list.add(matrixWeights[c1][c2][t]);
                }
            }
        }
        double[] result = new double[list.size()];
        for (int i = 0; i < list.size(); i++) result[i] = list.get(i);
        return result;
	}
	
	
	
	
	public enum FeatureDataType{
		REL_SCORE(DataTypes.AVE, true, ControllerType.DO_NOTHING),
		REL_WR(DataTypes.WRATE, true, ControllerType.DO_NOTHING),
//		MMSCORE_SD(DataTypes.MMSE),
//		REL_QUAR1(DataTypes.QUAR1, true, null),
//		REL_MEDI(DataTypes.MEDI, true, null),
//		REL_QUAR3(DataTypes.QUAR3, true, null),
//		REL_WR(DataTypes.WRATE, true),
//		REL_TICKS(DataTypes.AVTIC, true, null),
//		REL_TICKS_SD(DataTypes.SDTIC, true, null),
//		REL_MIN(DataTypes.MIN, true, null),
//		REL_MAX(DataTypes.MAX, true, null),
		
//		REL_SCORE_SD(DataTypes.SD, true, ControllerType.DO_NOTHING),
		REL_WR_SD(DataTypes.WRSE, true, ControllerType.DO_NOTHING),     //<-- compares first controller with doNothing
		
		REL_ACTEN(DataTypes.ACTEN, true, ControllerType.RANDOM),
		
//		TICKS(DataTypes.AVTIC, false, ControllerType.INTELLIGENT),
		
		HAS_LOW_TICKS(null, false, ControllerType.INTELLIGENT),

		

//		INT_MMSCORE(DataTypes.MMAVE, false, ControllerType.INTELLIGENT),
//		INT_WR(DataTypes.WRATE, false, ControllerType.INTELLIGENT),
//		RND_MMSCORE(DataTypes.MMAVE, false, ControllerType.RANDOM),
//		RND_WR(DataTypes.WRATE, false, ControllerType.RANDOM),
//		DN_MMSCORE(DataTypes.MMAVE, false, ControllerType.DO_NOTHING),
		
		;
		
		 private String dataType;
		 private boolean relDiffs;
		 private ControllerType ctrlType;
		 
		 private FeatureDataType(String dataType, boolean relDiffs, ControllerType ctrlType) {
		   this.dataType = dataType;
		   this.relDiffs = relDiffs;
		   this.ctrlType = ctrlType;
		 }
		 
		 public String typ() {
		   return dataType;
		 }
		 
		 public boolean isRelDiffs() {
		   return relDiffs;
		 }
		 
		 public ControllerType ctrlTyp(){
			 return ctrlType;
		 }
		 
	}
	
//	public enum FitnessType{
//		S_INTELLIGENT_OVER_SEMIINTELLIGENT,
//		S_INTELLIGENT_OVER_RANDOM,
//		S_INTELLIGENT_OVER_DONOTHING,
//		S_SEMIINTELLIGENT_OVER_RANDOM,
//		S_SEMIINTELLIGENT_OVER_DONOTHING,
//		S_RANDOM_OVER_DONOTHING,
////		S_INTELLIGENT_VAL,
////		S_INTELLIGENT_SD_VAL,
//		WR_INTELLIGENT_OVER_SEMIINTELLIGENT,
//		WR_INTELLIGENT_OVER_RANDOM,
//		WR_INTELLIGENT_OVER_DONOTHING,
//		WR_SEMIINTELLIGENT_OVER_RANDOM,
//		WR_SEMIINTELLIGENT_OVER_DONOTHING,
//		WR_RANDOM_OVER_DONOTHING,
////		WR_INTELLIGENT_VAL,
////		WR_INTELLIGENT_SD_VAL,
//		TIC_INTELLIGENT_OVER_SEMIINTELLIGENT,
//		TIC_INTELLIGENT_OVER_RANDOM,
//		TIC_INTELLIGENT_OVER_DONOTHING,
//		TIC_SEMIINTELLIGENT_OVER_RANDOM,
//		TIC_SEMIINTELLIGENT_OVER_DONOTHING,
//		TIC_RANDOM_OVER_DONOTHING,
////		TIC_INTELLIGENT_VAL,
////		TIC_INTELLIGENT_SD_VAL,
////		ENT_SEMIINTELLIGENT_OVER_EXPLORER
//	}

	
	
	public static void setWeights(double[] featureWeights){
		if (featureWeights == null){
			feature_weights = new double[getFeatureCount()];
			for (int i = 0; i < feature_weights.length; i++) feature_weights[i] = 1;
		}else{
			feature_weights = featureWeights.clone();
		}
	}
	
	
	public static void setWeights(double[] featureWeights, boolean[] dataTypesToUse){
		System.out.println("setWeights(double[] featureWeights, boolean[] dataTypesToUse) CURRENTLY NOT POSSIBLE");
	}
	
	

	public static String getFitnessTopString(String seperator) {
		String topString = "";
		for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
			FeatureDataType typ = FeatureDataType.values()[t];
			if (typ.relDiffs && typ.ctrlType == null){
				for (int c2 = 1; c2 < ControllerType.class.getEnumConstants().length; c2++) {
					topString += "relDiff(" + ControllerType.values()[0] + ", " + ControllerType.values()[c2] + ")." + typ.dataType;
					topString += seperator;
				}
				
			}else if (typ.relDiffs){
				topString += "relDiff(" + ControllerType.values()[0] + ", " + ControllerType.values()[typ.ctrlType.id()] + ")." + typ.dataType;
				topString += seperator;
			}else{
				topString += "valueOf(" + ControllerType.values()[typ.ctrlType.id()] + ")." + typ.dataType;
				topString += seperator;
			}
		}
		return topString;
	}
	
}


