package pe.edu.pucp.acoseg.image;

public class PosibleAssignment {
	private ClusteredPixel imagePixel;
	private double probability;
	private double heuristicTimesPheromone;
	private double heuristicValue;
	private double pheromoneTrailValue;

	public PosibleAssignment(ClusteredPixel imagePixel, double heuristicValue,
			double pheromoneTrailValue, double heuristicTimesPheromone,
			double probability) {
		super();
		this.imagePixel = imagePixel;
		this.heuristicTimesPheromone = heuristicTimesPheromone;
		this.probability = probability;
		this.heuristicValue = heuristicValue;
		this.pheromoneTrailValue = pheromoneTrailValue;
	}

	public double getHeuristicTimesPheromone() {
		return heuristicTimesPheromone;
	}

	public void setHeuristicTimesPheromone(double heuristicTimesPheromone) {
		this.heuristicTimesPheromone = heuristicTimesPheromone;
	}

	public ClusteredPixel getImagePixel() {
		return imagePixel;
	}

	public void setImagePixel(ClusteredPixel imagePixel) {
		this.imagePixel = imagePixel;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String toString() {
		return "Pixel (" + imagePixel.getxCoordinate() + ", "
				+ imagePixel.getyCoordinate() + ")  to cluster "
				+ imagePixel.getCluster() + " with probability " + probability
				+ ", heuristic value " + heuristicValue
				+ " and pheromone value " + pheromoneTrailValue;
	}

}
