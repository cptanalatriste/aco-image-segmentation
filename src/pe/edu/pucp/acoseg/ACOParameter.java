package pe.edu.pucp.acoseg;

public enum ACOParameter {
	
	HEURISTIC_IMPORTANCE("heuristicImportance"), 
	BEST_CHOICE_PROBABILITY("bestChoiceProbability"), 
	EVAPORATION("evaporation"), 
	CONTIGUITY_MEASSURE_PARAM("contiguityMeassureParam"), 
	MAXIMUM_PHEROMONE_VALUE("maximumPheromoneValue"), 
	NUMBER_OF_CLUSTERS("numberOfClusters"), 
	NUMBER_OF_ANTS("numberOfAnts"), 
	MAX_ITERATIONS("maxIterations");
	
	private String name;
	
	private ACOParameter(String name){
		this.name= name;
	}

	public String getName() {
		return name;
	}
}
