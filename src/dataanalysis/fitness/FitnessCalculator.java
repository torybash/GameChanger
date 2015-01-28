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
	
	private static final int INITIAL_FITNESS = 0;

	
	public static GameFitness calculateGameFitness(GameData[] gds, Controller[] controllers, boolean matrixApproach) {		
		GameFitness gf =  calculateFitness(gds, controllers);
		
		return gf;
	}

	private static GameFitness calculateFitness(GameData[] gds, Controller[] controllers) {
		GameFitness gf = new GameFitness(gds[0].gameTitle);

		
		int dataTypeCount = FeatureDataType.class.getEnumConstants().length;
		int ctrlTypeCount = ControllerType.class.getEnumConstants().length;
		
		double fitness = 0;
		int parameterCount = 0;
		for (int c1 = 0; c1 < ctrlTypeCount; c1++) {
			for (int c2 = 0; c2 < ctrlTypeCount; c2++) {
				if (c1 >= c2) continue;
				parameterCount += dataTypeCount;
			}
		}
		
		double[] fitnessVals = new double[parameterCount];
		
		
		double[][] highestValues = new double[ctrlTypeCount][];
		for (int i = 0; i < ctrlTypeCount; i++){
			highestValues[i] = new double[dataTypeCount];
			for (int j = 0; j < dataTypeCount; j++) {
				highestValues[i][j] = Double.NEGATIVE_INFINITY;
			}
		}
		
		for (int c = 0; c < controllers.length; c++) {
			Controller ctrl = controllers[c];
			for (int t = 0; t < dataTypeCount; t++) {
				FeatureDataType typ = FeatureDataType.values()[t];
				double ctrlVal = gds[c].gameValues.get(typ.dataType); //highestValues[t][ctrl.type.id()];
				if (ctrlVal > highestValues[ctrl.type.id()][t]) highestValues[ctrl.type.id()][t] = ctrlVal;
			}
		}
		
		if (VERBOSE) System.out.println(gds[0].gameTitle);
		
		int cnt = 0;
		for (int ct1 = 0; ct1 < ctrlTypeCount; ct1++) {
			for (int ct2 = 0; ct2 < ctrlTypeCount; ct2++) {				
				if (ct1 >= ct2) continue;

				
				for (int t = 0; t < dataTypeCount; t++) {
					FeatureDataType typ = FeatureDataType.values()[t];
					double score = Utility.relDiff(highestValues[ct1][t], highestValues[ct2][t]); // / 2.0 + 0.5;
//					double score = Utility.divDiff(highestValues[ct1][t], highestValues[ct2][t]);
					fitnessVals[cnt++] = score;
					fitness += score * ctrlmatrix_feature_weights[ct1][ct2][t];		
					
					
					if (VERBOSE) System.out.println(ControllerType.values()[ct1] + " > " + ControllerType.values()[ct2] + ", DataType: " + typ + ", relDiff: " + score + 
							", Highest val 1: " + highestValues[ct1][t] + ", Highest val 2: " + highestValues[ct2][t] + ", ctrlmatrix_feature_weights[ct1][ct2][t]: " + ctrlmatrix_feature_weights[ct1][ct2][t]);
				}
			}
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

	private static float calculateFitnessUsingMatrix(GameData[] gds) {
		int n = gds.length;
		float fitness = INITIAL_FITNESS;
		
		for (int c2 = 0; c2 < n; c2++) {
			for (int c1 = 0; c1 < c2; c1++) {
				fitness += Utility.relDiff(gds[c1].gameValues.get(DataTypes.AVE), gds[c2].gameValues.get(DataTypes.AVE)) * matrix_feature_weights[MatrixWeightType.SCORE.ordinal()][c2][c1];				
				fitness += Utility.relDiff(gds[c1].gameValues.get(DataTypes.WRATE), gds[c2].gameValues.get(DataTypes.WRATE)) * matrix_feature_weights[MatrixWeightType.WRATE.ordinal()][c2][c1];				
				fitness += Utility.relDiff(gds[c1].gameValues.get(DataTypes.AVTIC), gds[c2].gameValues.get(DataTypes.AVTIC)) * matrix_feature_weights[MatrixWeightType.TICKS.ordinal()][c2][c1];				
				fitness += Utility.relDiff(gds[c1].gameValues.get(DataTypes.ACTEN), gds[c2].gameValues.get(DataTypes.ACTEN)) * matrix_feature_weights[MatrixWeightType.ACTEN.ordinal()][c2][c1];				
			}
		}
		
		return fitness;
	}
	
	public static ArrayList<GameFitness> getFitnessForEachGame(ArrayList<GameData[]> gameDatas, Controller[] controllers, boolean matrixApproach) {
		int n = gameDatas.get(0).length;
		ArrayList<GameFitness> fitnessValues = new ArrayList<GameFitness>();
		for (int g = 0; g < gameDatas.size(); g++) {  //For each game
			GameFitness gf = calculateGameFitness(gameDatas.get(g), controllers, matrixApproach);
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
	
	
//	public static float calculateFitness(GameData[] gds, Controller[] controllers) {
//		float fitness = INITIAL_FITNESS;
//		float[] fitnessValues = new float[FitnessType.class.getEnumConstants().length];
//		int parameters = FitnessType.class.getEnumConstants().length;
//		
//		if (VERBOSE){System.out.println("------------------------");
//			System.out.println("Calculating fitness for "  + gds[0].gameTitle.split("--")[0]);}
//		
//		
//		
//		float[] highestScoreAves = new float[ControllerType.class.getEnumConstants().length];
//		float[] highestWRAves = new float[ControllerType.class.getEnumConstants().length];
//		float[] highestTicksAves = new float[ControllerType.class.getEnumConstants().length];
//		for (int c = 0; c < highestScoreAves.length; c++){
//			highestScoreAves[c] = Float.NEGATIVE_INFINITY;
//			highestWRAves[c] = Float.NEGATIVE_INFINITY;
//			highestTicksAves[c] = Float.NEGATIVE_INFINITY;
//		}
//		float highestIntlScoreSD = Float.NEGATIVE_INFINITY;
//		float highestIntlWRSD = Float.NEGATIVE_INFINITY;
//		float highestIntlTicksSD = Float.NEGATIVE_INFINITY;
//		
//		for (int c = 0; c < controllers.length; c++) {
//			float ctrlScoreAve = gds[c].gameValues.get(DataTypes.AVE);
//			float ctrlScoreSD = gds[c].gameValues.get(DataTypes.SD);
//			float ctrlWR = gds[c].gameValues.get(DataTypes.WRATE);
//			float ctrlWRSD = gds[c].gameValues.get(DataTypes.WRSD);
//			float ctrlTicksAve = gds[c].gameValues.get(DataTypes.AVTIC);
//			float ctrlTicksSD = gds[c].gameValues.get(DataTypes.SDTIC);
//			int ctrlTypeId = controllers[c].type.ordinal();
//			if (ctrlScoreAve > highestScoreAves[ctrlTypeId]) highestScoreAves[ctrlTypeId] = ctrlScoreAve;
//			if (ctrlScoreSD > highestIntlScoreSD) highestIntlScoreSD = ctrlScoreSD;
//			if (ctrlWR > highestWRAves[ctrlTypeId]) highestWRAves[ctrlTypeId] = ctrlWR;
//			if (ctrlWRSD > highestIntlWRSD) highestIntlWRSD = ctrlWRSD;
//			if (ctrlTicksAve > highestTicksAves[ctrlTypeId]) highestTicksAves[ctrlTypeId] = ctrlTicksAve;
//			if (ctrlTicksSD > highestIntlTicksSD) highestIntlTicksSD = ctrlTicksSD;
//		}
//		
//		//SCORE
//		fitnessValues[FitnessType.S_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = Utility.relDiff(highestScoreAves[ControllerType.INTELLIGENT.id()], highestScoreAves[ControllerType.SEMI_INTELLIGENT.id()]);
//		fitnessValues[FitnessType.S_INTELLIGENT_OVER_RANDOM.ordinal()] = Utility.relDiff(highestScoreAves[ControllerType.INTELLIGENT.id()], highestScoreAves[ControllerType.RANDOM.id()]) ;
//		fitnessValues[FitnessType.S_INTELLIGENT_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestScoreAves[ControllerType.INTELLIGENT.id()], highestScoreAves[ControllerType.DO_NOTHING.id()]) ;
//
//		fitnessValues[FitnessType.S_SEMIINTELLIGENT_OVER_RANDOM.ordinal()] = Utility.relDiff(highestScoreAves[ControllerType.SEMI_INTELLIGENT.id()], highestScoreAves[ControllerType.RANDOM.id()]);
//		fitnessValues[FitnessType.S_SEMIINTELLIGENT_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestScoreAves[ControllerType.SEMI_INTELLIGENT.id()], highestScoreAves[ControllerType.DO_NOTHING.id()]);
//	
//		fitnessValues[FitnessType.S_RANDOM_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestScoreAves[ControllerType.RANDOM.id()], highestScoreAves[ControllerType.DO_NOTHING.id()]);
//		
////		fitnessValues[FitnessType.S_INTELLIGENT_VAL.ordinal()] = Utility.relDiff(highestScoreAves[ControllerType.INTELLIGENT.id()], 0);
////		fitnessValues[FitnessType.S_INTELLIGENT_SD_VAL.ordinal()] = Utility.relDiff(highestIntlScoreSD, 0);
//		
//
//		//WINRATE
//		fitnessValues[FitnessType.WR_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = Utility.relDiff(highestWRAves[ControllerType.INTELLIGENT.id()], highestWRAves[ControllerType.SEMI_INTELLIGENT.id()]);
//		fitnessValues[FitnessType.WR_INTELLIGENT_OVER_RANDOM.ordinal()] = Utility.relDiff(highestWRAves[ControllerType.INTELLIGENT.id()], highestWRAves[ControllerType.RANDOM.id()]);
//		fitnessValues[FitnessType.WR_INTELLIGENT_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestWRAves[ControllerType.INTELLIGENT.id()], highestWRAves[ControllerType.DO_NOTHING.id()]);
//		
//		fitnessValues[FitnessType.WR_SEMIINTELLIGENT_OVER_RANDOM.ordinal()] = Utility.relDiff(highestWRAves[ControllerType.SEMI_INTELLIGENT.id()], highestWRAves[ControllerType.RANDOM.id()]);		
//		fitnessValues[FitnessType.WR_SEMIINTELLIGENT_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestWRAves[ControllerType.SEMI_INTELLIGENT.id()], highestWRAves[ControllerType.DO_NOTHING.id()]);
//		
//		fitnessValues[FitnessType.WR_RANDOM_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestWRAves[ControllerType.RANDOM.id()], highestWRAves[ControllerType.DO_NOTHING.id()]);
//		
////		fitnessValues[FitnessType.WR_INTELLIGENT_VAL.ordinal()] = Utility.relDiff(highestWRAves[ControllerType.INTELLIGENT.id()], 0);
////		fitnessValues[FitnessType.WR_INTELLIGENT_SD_VAL.ordinal()] = Utility.relDiff(highestIntlWRSD, 0);
//		
//		
//		//TICKS
//		fitnessValues[FitnessType.TIC_INTELLIGENT_OVER_SEMIINTELLIGENT.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.INTELLIGENT.id()], highestTicksAves[ControllerType.SEMI_INTELLIGENT.id()]);
//		fitnessValues[FitnessType.TIC_INTELLIGENT_OVER_RANDOM.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.INTELLIGENT.id()], highestTicksAves[ControllerType.RANDOM.id()]);
//		fitnessValues[FitnessType.TIC_INTELLIGENT_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.INTELLIGENT.id()], highestTicksAves[ControllerType.DO_NOTHING.id()]);
//
//		fitnessValues[FitnessType.TIC_SEMIINTELLIGENT_OVER_RANDOM.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.SEMI_INTELLIGENT.id()], highestTicksAves[ControllerType.RANDOM.id()]);
//		fitnessValues[FitnessType.TIC_SEMIINTELLIGENT_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.SEMI_INTELLIGENT.id()], highestTicksAves[ControllerType.DO_NOTHING.id()]);
//
//		fitnessValues[FitnessType.TIC_RANDOM_OVER_DONOTHING.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.RANDOM.id()], highestTicksAves[ControllerType.DO_NOTHING.id()]);
//
////		fitnessValues[FitnessType.TIC_INTELLIGENT_VAL.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.INTELLIGENT.id()], 0);
////		fitnessValues[FitnessType.TIC_INTELLIGENT_SD_VAL.ordinal()] = Utility.relDiff(highestTicksAves[ControllerType.INTELLIGENT.id()], 0);
//		
//
//		if (VERBOSE) System.out.println("Fitness values:");				
//		for (int i = 0; i < fitnessValues.length; i++) {
//			fitness += fitnessValues[i] * feature_weights[i];
//			if (VERBOSE){ System.out.printf("%-35s\t%f\n", FitnessType.values()[i], fitnessValues[i]);
//			System.out.println("Fitness values:\t" + FitnessType.values()[i] + ":\t" + fitnessValues[i]);}
//		}
//		if (VERBOSE){
//			System.out.printf("%-35s\t%f\n", "...TOTAL FITNESS", fitness);
//			System.out.println("highestScoreAves: " + Arrays.toString(highestScoreAves));
//			System.out.println("highestWRAves: " + Arrays.toString(highestWRAves));
//			System.out.println("highestTicksAves: " + Arrays.toString(highestWRAves));
//		}
//		
//
//
//		
////		if (gameWithoutWinning){
////			parameters -= 4;
////		}
//		
//		fitness = fitness / (float)parameters;
//		
////		if (disqualified) fitness = -1;
//		
//		String maString = "";
//		maString += gds[0].gameTitle.split("--")[0] + ",";
//		for (int i = 0; i < fitnessValues.length; i++) {
//			maString += fitnessValues[i] +",";
//		}
//		maString += "generated"; //(fitness > 0) ? "designed" : "generated";
//		if (!hasPrintedTop){
//			String maTopString = "";
//			maTopString += "game,";
//			for (int i = 0; i < fitnessValues.length; i++) {
//				maTopString += FitnessType.values()[i] + ",";
//			}
//			maTopString += "class";
//			System.out.println(maTopString);
//			hasPrintedTop = true;
//		}
//		System.out.println(maString);
//
//		
//		return fitness;
//	}

	private static boolean hasPrintedTop = false;
	
	
	
	
	public enum FeatureDataType{
		SCORE(DataTypes.AVE),
//		SCORE_SD(DataTypes.SD),
		MMSCORE(DataTypes.MMAVE),
//		SCORE_SD(DataTypes.MMSE),
//		ERR_HI(DataTypes.ERRHI),
//		ERR_LO(DataTypes.ERRLO),
		WR(DataTypes.WRATE),
//		WR_SD(DataTypes.WRSE),
		TICKS(DataTypes.AVTIC),
//		TICKS_SD(DataTypes.SDTIC)
		;
		
		 private String dataType;
		 
		 private FeatureDataType(String dataType) {
		   this.dataType = dataType;
		 }
		 
		 public String typ() {
		   return dataType;
		 }
	}
	
	public enum FitnessType{
		S_INTELLIGENT_OVER_SEMIINTELLIGENT,
		S_INTELLIGENT_OVER_RANDOM,
		S_INTELLIGENT_OVER_DONOTHING,
		S_SEMIINTELLIGENT_OVER_RANDOM,
		S_SEMIINTELLIGENT_OVER_DONOTHING,
		S_RANDOM_OVER_DONOTHING,
//		S_INTELLIGENT_VAL,
//		S_INTELLIGENT_SD_VAL,
		WR_INTELLIGENT_OVER_SEMIINTELLIGENT,
		WR_INTELLIGENT_OVER_RANDOM,
		WR_INTELLIGENT_OVER_DONOTHING,
		WR_SEMIINTELLIGENT_OVER_RANDOM,
		WR_SEMIINTELLIGENT_OVER_DONOTHING,
		WR_RANDOM_OVER_DONOTHING,
//		WR_INTELLIGENT_VAL,
//		WR_INTELLIGENT_SD_VAL,
		TIC_INTELLIGENT_OVER_SEMIINTELLIGENT,
		TIC_INTELLIGENT_OVER_RANDOM,
		TIC_INTELLIGENT_OVER_DONOTHING,
		TIC_SEMIINTELLIGENT_OVER_RANDOM,
		TIC_SEMIINTELLIGENT_OVER_DONOTHING,
		TIC_RANDOM_OVER_DONOTHING,
//		TIC_INTELLIGENT_VAL,
//		TIC_INTELLIGENT_SD_VAL,
//		ENT_SEMIINTELLIGENT_OVER_EXPLORER
	}
	
	
	
	public enum MatrixWeightType{
		SCORE,
		WRATE,
		TICKS,
		ACTEN,
	}
	
	
	public static void setWeights(double[] featureWeights){
		int count = 0;
		
		if (featureWeights == null){
			ctrlmatrix_feature_weights = new double[ControllerType.class.getEnumConstants().length][][];
			for (int c1 = 0; c1 < ControllerType.class.getEnumConstants().length; c1++) {
				ctrlmatrix_feature_weights[c1] = new double[ControllerType.class.getEnumConstants().length][];
				for (int c2 = 0; c2 < ControllerType.class.getEnumConstants().length; c2++) {
					if (c1 >= c2) continue;
					ctrlmatrix_feature_weights[c1][c2] = new double[FeatureDataType.class.getEnumConstants().length];
					for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
						ctrlmatrix_feature_weights[c1][c2][t] = 1;
					}
				}
			}
			return;
		}
		
		if (ctrlmatrix_feature_weights == null){
			ctrlmatrix_feature_weights = new double[ControllerType.class.getEnumConstants().length][][];
			for (int c1 = 0; c1 < ControllerType.class.getEnumConstants().length; c1++) {
				ctrlmatrix_feature_weights[c1] = new double[ControllerType.class.getEnumConstants().length][];
				for (int c2 = 0; c2 < ControllerType.class.getEnumConstants().length; c2++) {
					if (c1 >= c2) continue;
					ctrlmatrix_feature_weights[c1][c2] = new double[FeatureDataType.class.getEnumConstants().length];
					for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
						ctrlmatrix_feature_weights[c1][c2][t] = featureWeights[count++];
					}
				}
			}
		}else{
			for (int c1 = 0; c1 < ControllerType.class.getEnumConstants().length; c1++) {
				for (int c2 = 0; c2 < ControllerType.class.getEnumConstants().length; c2++) {
					if (c1 >= c2) continue;
					for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
						ctrlmatrix_feature_weights[c1][c2][t] = featureWeights[count++];
					}
				}
			}
		}
		
	}
	
	public static void setCtrlMatrixWeights(double[][][] ctrlMatrixFeatureWeights){
		
		if (ctrlMatrixFeatureWeights == null){
			ctrlmatrix_feature_weights = new double[ControllerType.class.getEnumConstants().length][][];
			for (int c1 = 0; c1 < ControllerType.class.getEnumConstants().length; c1++) {
				ctrlmatrix_feature_weights[c1] = new double[ControllerType.class.getEnumConstants().length][];
				for (int c2 = 0; c2 < ControllerType.class.getEnumConstants().length; c2++) {
					ctrlmatrix_feature_weights[c1][c2] = new double[FeatureDataType.class.getEnumConstants().length];
					for (int t = 0; t < FeatureDataType.class.getEnumConstants().length; t++) {
						ctrlmatrix_feature_weights[c1][c2][t] = 1;
					}
				}
			}
		}else{
			ctrlmatrix_feature_weights = ctrlMatrixFeatureWeights;
		}
	}
	
}


