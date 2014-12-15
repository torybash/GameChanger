package datanalysis.core;

import java.util.ArrayList;

import util.Utility;

public class GoodToBadFitness implements Comparable{

	public int idx;
	public ArrayList<GameFitness> goodFitnessValues;
	public ArrayList<GameFitness> badFitnessValues;
	public double gtbFitness;
	
	public GoodToBadFitness(int idx, ArrayList<GameFitness> goodFitnessValues, ArrayList<GameFitness> badFitnessValues){
		this.idx = idx;
		this.goodFitnessValues = goodFitnessValues;
		this.badFitnessValues = badFitnessValues;
		this.gtbFitness = getGoodToBadFitness(goodFitnessValues, badFitnessValues);
	}
	
	
	
	@Override
	public int compareTo(Object o) {
//		float bestGoodToBad = goodFitnessValues.get(0).fitness - badFitnessValues.get(0).fitness;
		
//		float otherBestGoodToBad = ((GoodToBadFitness) o).goodFitnessValues.get(0).fitness - ((GoodToBadFitness) o).badFitnessValues.get(0).fitness;
		double otherGtbFitness = ((GoodToBadFitness) o).gtbFitness;	
		
		
		if (gtbFitness-otherGtbFitness == 0) return 0;
		return gtbFitness-otherGtbFitness > 0 ? -1 : 1;
	}
	
	public static double getGoodToBadFitness(ArrayList<GameFitness> goodFitnessValues, ArrayList<GameFitness> badFitnessValues){
		double fitness = 0;
		
		for (GameFitness goodGameFitness : goodFitnessValues) {
			for (GameFitness badGameFitness : badFitnessValues) {
				fitness += Utility.relDiff(goodGameFitness.fitness, badGameFitness.fitness);
//				System.out.println(Utility.relDiff(goodGameFitness.fitness, badGameFitness.fitness));
//				if (goodGameFitness.fitness > badGameFitness.fitness) fitness += 1;
//				else if (goodGameFitness.fitness < badGameFitness.fitness) fitness -= 1;
			}
		}
		return fitness;
	}
}
