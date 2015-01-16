package dataanalysis.fitness;

public class GameFitness implements Comparable{

	public String gameTitle = "";
	public double fitness = 0;
	public double[] fitnessVals;
	
	public GameFitness(String gameTitle){
		this.gameTitle = gameTitle;
	}
	
	@Override
	public int compareTo(Object o) {
		double otherFitness = ((GameFitness) o).fitness;
		
		if (fitness-otherFitness == 0) return 0;
		return fitness-otherFitness > 0 ? -1 : 1;
	}
}
