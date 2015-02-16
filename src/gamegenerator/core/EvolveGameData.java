package gamegenerator.core;

import dataanalysis.fitness.GameFitness;

public class EvolveGameData implements Comparable{
	GameFitness gf;
	String gameDesc;
	
	public EvolveGameData(GameFitness gf, String gameDesc){
		this.gf = gf;
		this.gameDesc = gameDesc;
	}

	@Override
	public int compareTo(Object o) {
		double otherFitness = ((EvolveGameData)o).gf.fitness;
		
		if (gf.fitness-otherFitness == 0) return 0;
		return gf.fitness-otherFitness > 0 ? -1 : 1;
	}

	public EvolveGameData copy() {
		return new EvolveGameData(gf.copy(), gameDesc);
	}
}
