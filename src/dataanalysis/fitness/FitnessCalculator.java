package dataanalysis.fitness;

import java.util.ArrayList;
import java.util.Arrays;

import util.Utility;
import dataanalysis.controller.Controller;
import dataanalysis.controller.Controller.ControllerType;
import dataanalysis.core.DataTypes;
import dataanalysis.core.GameData;


public class FitnessCalculator {

	private static double[] feature_weights;
	
	private static double[][][] ctrlmatrix_feature_weights;
	
	private static double[][][] matrix_feature_weights;
	
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

		
		int maxDataTypeCount = FeatureDataType.class.getEnumConstants().length;
		int dataTypeCount = 0;
		for (int i = 0; i < dataTypesToUse.length; i++) if (dataTypesToUse[i]) dataTypeCount++;
		int ctrlTypeCount = ControllerType.class.getEnumConstants().length;
		
		double fitness = 0;
		int parameterCount = 0;
		
		for (int t = 0; t < maxDataTypeCount; t++) {
			FeatureDataType typ = FeatureDataType.values()[t];
			if (typ.relDiffs){
				for (int c1 = 0; c1 < ctrlTypeCount; c1++) {
					for (int c2 = 0; c2 < ctrlTypeCount; c2++) {
						if (c1 >= c2) continue;
						parameterCount += dataTypeCount;
					}
				}
			}else{
				parameterCount += 1;
			}
			
		}
		
		double[] fitnessVals = new double[parameterCount];
		
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
				double ctrlVal = gds[c].gameValues.get(typ.dataType); //highestValues[t][ctrl.type.id()];
				if (ctrlVal > highestValues[t][ctrl.type.id()]) highestValues[t][ctrl.type.id()] = ctrlVal;
			}
		}
		
		if (VERBOSE) System.out.println(gds[0].gameTitle);
		
		int cnt = 0;
		for (int t = 0; t < maxDataTypeCount; t++) {
			if (!dataTypesToUse[t]) continue;
			FeatureDataType typ = FeatureDataType.values()[t];
			
			double val = 0;
			
			if (typ.relDiffs){
				for (int ct1 = 0; ct1 < ctrlTypeCount; ct1++) {
					for (int ct2 = 0; ct2 < ctrlTypeCount; ct2++) {				
						if (ct1 >= ct2) continue;
	
						val = Utility.relDiff(highestValues[t][ct1], highestValues[t][ct2]); // / 2.0 + 0.5;
						
						fitness += val * ctrlmatrix_feature_weights[t][ct1][ct2];		

						if (VERBOSE) System.out.println(ControllerType.values()[ct1] + " > " + ControllerType.values()[ct2] + ", DataType: " + typ + ", relDiff: " + val + 
								", Highest val 1: " + highestValues[t][ct1] + ", Highest val 2: " + highestValues[t][ct2] + ", ctrlmatrix_feature_weights[ct1][ct2][t]: " + ctrlmatrix_feature_weights[t][ct1][ct2]);
					}
				}
			}else{
				val = highestValues[t][ControllerType.INTELLIGENT.id()];
				fitness += val * ctrlmatrix_feature_weights[t][0][0];
			}
			
			fitnessVals[cnt++] = val;
		}
		if (VERBOSE) System.out.println("---Total fitness:\t" + fitness);
		if (VERBOSE) System.out.println("parameterCount: " + parameterCount);
		
		fitness /= parameterCount;
		
		gf.fitness = fitness;
		gf.fitnessVals = fitnessVals;
		                
		return gf;
	}

	private static void makeCSV(double[] fitnessVals) {
		int dataTypeCount = FeatureDataType.class.getEnumConstants().length;
		int ctrlTypeCount = ControllerType.class.getEnumConstants().length;
		
		String topString = "";
		String mainString = "";
		
		int cnt = 0;
		
		for (int ct1 = 0; ct1 < ctrlTypeCount; ct1++) {
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

            for (int c1 = 0; c1 < matrixWeights.length; c1++) {
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
		REL_SCORE(DataTypes.AVE, true),
		REL_SCORE_SD(DataTypes.SD, true),
//		MMSCORE(DataTypes.MMAVE, false),
//		MMSCORE_SD(DataTypes.MMSE),
		QUAR1(DataTypes.QUAR1, true),
		QUAR3(DataTypes.QUAR3, true),
		REL_WR(DataTypes.WRATE, true),
//		REL_WR_SD(DataTypes.WRSE, true),
		REL_TICKS(DataTypes.AVTIC, true),
		REL_TICKS_SD(DataTypes.SDTIC, true),
		REL_MAX(DataTypes.MAX, true),
		REL_MEDI(DataTypes.MEDI, true),
//		REL_MIN(DataTypes.MIN, true),
		MMSCORE(DataTypes.MMAVE, false),
		WR(DataTypes.WRATE, false),
		;
		
		 private String dataType;
		 private boolean relDiffs;
		 
		 private FeatureDataType(String dataType, boolean relDiffs) {
		   this.dataType = dataType;
		   this.relDiffs = relDiffs;
		 }
		 
		 public String typ() {
		   return dataType;
		 }
		 
		 public boolean isRelDiffs() {
		   return relDiffs;
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
//	
//	
//	
//	public enum MatrixWeightType{
//		SCORE,
//		WRATE,
//		TICKS,
//		ACTEN,
//	}
	
	
	public static void setWeights(double[] featureWeights, boolean[] dataTypesToUse){
		int count = 0;
		if (ctrlmatrix_feature_weights == null){
			ctrlmatrix_feature_weights = new double[FeatureDataType.class.getEnumConstants().length][][];
			
			for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
				ctrlmatrix_feature_weights[t] = new double[ControllerType.class.getEnumConstants().length][];
				outerloop:
				for (int c1 = 0; c1 < ControllerType.class.getEnumConstants().length; c1++) {
					ctrlmatrix_feature_weights[t][c1] = new double[ControllerType.class.getEnumConstants().length];
					for (int c2 = 0; c2 < ControllerType.class.getEnumConstants().length; c2++) {
						if (c1 >= c2 || !dataTypesToUse[t]) continue;
						if (FeatureDataType.values()[t].relDiffs){
							if (featureWeights != null){
								ctrlmatrix_feature_weights[t][c1][c2] = featureWeights[count++];
							}else{
								ctrlmatrix_feature_weights[t][c1][c2] = 1;
							}
						}else{
							ctrlmatrix_feature_weights[t][0][0] = featureWeights[count++];
							break outerloop;
						}
					}
				}
			}
			return;
		}else{
			for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
				if (!dataTypesToUse[t]) continue;
				
				if (FeatureDataType.values()[t].relDiffs){
					for (int c1 = 0; c1 < ControllerType.class.getEnumConstants().length; c1++) {
						for (int c2 = 0; c2 < ControllerType.class.getEnumConstants().length; c2++) {
							if (c1 >= c2) continue;
							ctrlmatrix_feature_weights[t][c1][c2] = featureWeights[count++];
						}
					}
				}else{
					
					ctrlmatrix_feature_weights[t][0][0] = featureWeights[count++];
				}
			}
		}
		
	}
	
	public static void setWeights(double[][][] ctrlMatrixFeatureWeights){
		if (ctrlMatrixFeatureWeights == null){
			ctrlmatrix_feature_weights = new double[FeatureDataType.class.getEnumConstants().length][][];
			for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
				ctrlmatrix_feature_weights[t] = new double[ControllerType.class.getEnumConstants().length][];
				for (int c1 = 0; c1 < ControllerType.class.getEnumConstants().length; c1++) {
					ctrlmatrix_feature_weights[t][c1] = new double[ControllerType.class.getEnumConstants().length];
					for (int c2 = 0; c2 < ControllerType.class.getEnumConstants().length; c2++) {
						if (c1 >= c2) continue;
						ctrlmatrix_feature_weights[t][c1][c2] = 1;
					}
				}
			}
		}else{
			ctrlmatrix_feature_weights = ctrlMatrixFeatureWeights;
		}
	}
	
}


