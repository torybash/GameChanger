package dataanalysis.fitness;

public class GameFitness implements Comparable{

	public String gameTitle = "";
	public double fitness = 0;
	public double[] fitnessVals;
	public String[] fitnessValsString;
	
	public GameFitness(String gameTitle){
		this.gameTitle = gameTitle;
	}
	
	public GameFitness(String gameTitle, double fitness){
		this.gameTitle = gameTitle;
		this.fitness = fitness;
	}
	
	@Override
	public int compareTo(Object o) {
		double otherFitness = ((GameFitness) o).fitness;
		
		if (fitness-otherFitness == 0) return 0;
		return fitness-otherFitness > 0 ? -1 : 1;
	}

	public GameFitness copy() {
		GameFitness result = new GameFitness(gameTitle, fitness);
		if (fitnessVals != null) result.fitnessVals = fitnessVals.clone();
		if (fitnessValsString != null) result.fitnessValsString = fitnessValsString.clone();
		return result;
	}
}
