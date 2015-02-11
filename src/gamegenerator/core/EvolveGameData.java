package gamegenerator.core;

public class EvolveGameData implements Comparable{
	double fitness;
	String gameDesc;
	
	public EvolveGameData(double fitness, String gameDesc){
		this.fitness = fitness;
		this.gameDesc = gameDesc;
	}

	@Override
	public int compareTo(Object o) {
		double otherFitness = ((EvolveGameData)o).fitness;
		
		if (fitness-otherFitness == 0) return 0;
		return fitness-otherFitness > 0 ? -1 : 1;
	}
}
