package pe.edu.pucp.acoseg.ant;

import pe.edu.pucp.acoseg.ACOImageSegmentation;
import pe.edu.pucp.acoseg.ProblemConfiguration;

public class Environment {

	private int numberOfClusters;
	private int numberOfPixels;

	private int[][] imageGraph;
	private double pheromoneTrails[][] = null;
	private ProblemConfiguration problemConfiguration;

	public Environment(int[][] imageGraph, int numberOfClusters) {
		super();
		this.numberOfClusters = numberOfClusters;
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Number of Clusters: " + numberOfClusters);
		this.imageGraph = imageGraph;
		this.numberOfPixels = imageGraph.length * imageGraph[0].length;
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Number of Píxels: " + numberOfPixels);
		this.pheromoneTrails = new double[numberOfPixels][numberOfClusters];
	}

	public void initializePheromoneMatrix() {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "INITIALIZING PHEROMONE MATRIX");
		double initialPheromoneValue = ProblemConfiguration.INITIAL_PHEROMONE_VALUE;
		if (ProblemConfiguration.MMAS_PHEROMONE_UPDATE) {
			initialPheromoneValue = ProblemConfiguration.getInstance()
					.getMaximumPheromoneValue();
		}

		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Initial pheromone value: " + initialPheromoneValue);
		for (int i = 0; i < numberOfPixels; i++) {
			for (int j = 0; j < numberOfClusters; j++) {
				pheromoneTrails[i][j] = initialPheromoneValue;
			}
		}
	}

	public void performEvaporation() {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Performing evaporation on all edges");
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Evaporation ratio: "
				+ ProblemConfiguration.getInstance().getEvaporation());
		for (int i = 0; i < numberOfPixels; i++) {
			for (int j = 0; j < numberOfClusters; j++) {
				double newValue = pheromoneTrails[i][j]
						* ProblemConfiguration.getInstance().getEvaporation();
				if (ProblemConfiguration.MMAS_PHEROMONE_UPDATE
						&& newValue < ProblemConfiguration.getInstance()
								.getMinimumPheromoneValue()) {
					newValue = ProblemConfiguration.getInstance()
							.getMinimumPheromoneValue();
				}
				pheromoneTrails[i][j] = newValue;
			}
		}
	}

	public int[][] getImageGraph() {
		return imageGraph;
	}

	public double[][] getPheromoneTrails() {
		return pheromoneTrails;
	}

	public int getNumberOfClusters() {
		return numberOfClusters;
	}

	public int getNumberOfPixels() {
		return numberOfPixels;
	}

	public ProblemConfiguration getProblemConfiguration() {
		return problemConfiguration;
	}

}
