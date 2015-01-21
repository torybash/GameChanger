package dataanalysis.fitness;

import java.util.ArrayList;
import java.util.Collections;

import util.Utility;

public class GoodToBadFitness implements Comparable{

	public int idx;
	public ArrayList<GameFitness> goodFitnessValues;
	public ArrayList<GameFitness> badFitnessValues;
	public double gtbFitness;
	
	private static final double GOOD_GAME_THRESHOLD = 7.5;
	
	
	public GoodToBadFitness(int idx, ArrayList<GameFitness> goodFitnessValues, ArrayList<GameFitness> badFitnessValues){
		this.idx = idx;
		this.goodFitnessValues = goodFitnessValues;
		this.badFitnessValues = badFitnessValues;
		this.gtbFitness = getGoodToBadFitness(goodFitnessValues, badFitnessValues);
	}
	
	
	

	
	public static double getGoodToBadFitness(ArrayList<GameFitness> goodFitnessValues, ArrayList<GameFitness> badFitnessValues){
		double fitness = 0;
		
		double gameRatio = badFitnessValues.size() / (float) goodFitnessValues.size();
		
		//METHOD 1
//		for (GameFitness goodGameFitness : goodFitnessValues) {
//			if (goodGameFitness.fitness > 0.0) fitness += 1 * gameRatio;
//			else if (goodGameFitness.fitness < 0.0) fitness -= 1 * gameRatio;
//		}
//		for (GameFitness badGameFitness : badFitnessValues) {
//			if (badGameFitness.fitness > 0.0) fitness -= 1;
//			else if (badGameFitness.fitness < 0.0) fitness += 1;
//		}
//		
		
		//METHOD 2
//		float goodGameTotalFitness = 0;
//		for (GameFitness goodGameFitness : goodFitnessValues) {
//			goodGameTotalFitness += 1+goodGameFitness.fitness * gameRatio;
//		}
//		float badGameTotalFitness = 0;
//		for (GameFitness badGameFitness : badFitnessValues) {
//			badGameTotalFitness += 1+badGameFitness.fitness;
//		}
//		fitness += goodGameTotalFitness - badGameTotalFitness;
//		System.out.println("goodGameTotalFitness: " + goodGameTotalFitness + ", badGameTotalFitness: " + badGameTotalFitness);
//		fitness += Utility.relDiff(goodGameTotalFitness, badGameTotalFitness);
		
//		fitness -= 1;
		
//		System.out.println(fitness);
		
		//METHOD 3
//		Collections.sort(goodFitnessValues);
//		for (GameFitness badGameFitness : badFitnessValues) {
//			if (badGameFitness.fitness < goodFitnessValues.get(goodFitnessValues.size()-1).fitness) fitness += 10;
//			
//			fitness += Utility.relDiff(goodFitnessValues.get(goodFitnessValues.size()-1).fitness, badGameFitness.fitness);
//		}
				
		//METHOD 4
		Collections.sort(goodFitnessValues);
		Collections.sort(badFitnessValues);
//		fitness += Utility.relDiff(goodFitnessValues.get(goodFitnessValues.size()-1).fitness+1, badFitnessValues.get(0).fitness+1);
		fitness += (goodFitnessValues.get(goodFitnessValues.size()-1).fitness - badFitnessValues.get(0).fitness);
		fitness -= 1;
		
		
//		fitness -= badFitnessValues.size() * goodFitnessValues.size() ;
		
		//METHOD 5 + 6
		for (GameFitness goodGameFitness : goodFitnessValues) {
			for (GameFitness badGameFitness : badFitnessValues) {
//				fitness += goodGameFitness.fitness - badGameFitness.fitness;
				
//				fitness += Utility.relDiff(goodGameFitness.fitness, badGameFitness.fitness);

//				if (goodGameFitness.fitness > badGameFitness.fitness) fitness += 1;
//				else if (goodGameFitness.fitness < badGameFitness.fitness) fitness -= 1 * gameRatio;
			}
		}
//		System.out.println(fitness+ " ----> " + (goodFitnessValues.size() *badFitnessValues.size() - fitness));
		return fitness;
	}
	
	
	
	@Override
	public int compareTo(Object o) {
		double otherGtbFitness = ((GoodToBadFitness) o).gtbFitness;			
		if (gtbFitness-otherGtbFitness == 0) return 0;
		return gtbFitness-otherGtbFitness > 0 ? -1 : 1;
	}

}
