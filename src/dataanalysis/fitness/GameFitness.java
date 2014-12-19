package dataanalysis.fitness;

public class GameFitness implements Comparable{

	public String gameTitle = "";
	public float fitness = 0;
	
	
	
	@Override
	public int compareTo(Object o) {
		float otherFitness = ((GameFitness) o).fitness;
		
		if (fitness-otherFitness == 0) return 0;
		return fitness-otherFitness > 0 ? -1 : 1;
	}
}
