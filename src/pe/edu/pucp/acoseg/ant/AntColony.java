package pe.edu.pucp.acoseg.ant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pe.edu.pucp.acoseg.ACOImageSegmentation;
import pe.edu.pucp.acoseg.ProblemConfiguration;
import pe.edu.pucp.acoseg.image.ClusteredPixel;

public class AntColony {

	private List<Ant> antColony;
	private int numberOfAnts;
	private Environment environment;

	private ClusteredPixel bestPartition[];
	public double bestPartitionQuality = -1.0;
	public int bestPartitionIteration = -1;

	public AntColony(Environment environment) {
		this.environment = environment;
		// Ant Ant per every pixel
		this.numberOfAnts = ProblemConfiguration.getInstance()
				.getNumberOfAnts();
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Number of Ants in Colony: " + numberOfAnts);
		this.antColony = new ArrayList<Ant>(numberOfAnts);
		for (int j = 0; j < numberOfAnts; j++) {
			antColony.add(new Ant(environment.getNumberOfPixels(), environment
					.getNumberOfClusters()));
		}
	}

	public void buildSolutions(boolean depositPheromone) throws Exception {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "BUILDING ANT SOLUTIONS");

		// TODO(cgavidia): We need to pick ants randomly
		if (ProblemConfiguration.RANDOMIZE_BEFORE_BUILD) {
			Collections.shuffle(antColony);
		}

		int antCounter = 0;
		for (Ant ant : antColony) {
			System.out.println(ACOImageSegmentation.getComputingTimeAsString()
					+ "Ant " + antCounter + " is building a partition ...");
			for (int i = 0; i < environment.getImageGraph().length; i++) {
				for (int j = 0; j < environment.getImageGraph()[0].length; j++) {
					ClusteredPixel nextPixel = null;
					if (environment.getImageGraph()[i][j] != ProblemConfiguration.ABSENT_PIXEL_FLAG) {
						nextPixel = ant.selectedPixelAssignment(i, j,
								environment.getPheromoneTrails(),
								environment.getImageGraph());
						if (nextPixel == null) {
							throw new Exception(
									"No pixel was selected, for ant with path: "
											+ ant.pathAsString());
						}
					} else {
						nextPixel = new ClusteredPixel(i, j,
								environment.getImageGraph(),
								ProblemConfiguration.ABSENT_PIXEL_CLUSTER);
					}
					ant.addAsignmentToSolution(nextPixel);

				}
			}
			if (depositPheromone) {
				depositPheromoneInAntPath(ant);
			}
			antCounter++;
			// TODO(cgavidia): Local search is also omitted. No recording of
			// best solutions either.
		}
	}

	public void clearAntSolutions() {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "CLEARING ANT SOLUTIONS");
		for (Ant ant : antColony) {
			ant.clear();
			ant.setCurrentIndex(0);
		}
	}

	public void depositPheromone() throws Exception {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Depositing pheromone");

		if (ProblemConfiguration.ONLY_BEST_CAN_UPDATE) {
			System.out.println(ACOImageSegmentation.getComputingTimeAsString()
					+ "Depositing pheromone on Best Ant trail.");
			Ant bestAnt = getBestAnt();
			depositPheromoneInAntPath(bestAnt);
		} else {
			for (Ant ant : antColony) {
				depositPheromoneInAntPath(ant);
			}
		}
	}

	public void depositPheromoneInAntPath(Ant ant) throws Exception {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Starting  depositPheromoneInAntPath.");

		double contribution = 1 / ant.getPartitionQuality(environment
				.getImageGraph());

		for (int i = 0; i < environment.getNumberOfPixels(); i++) {
			ClusteredPixel imagePixel = ant.getPartition()[i];
			if (imagePixel.getCluster() != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
				double newValue = environment.getPheromoneTrails()[imagePixel
						.getxCoordinate()
						* environment.getImageGraph()[0].length
						+ imagePixel.getyCoordinate()][imagePixel.getCluster()]
						* ProblemConfiguration.EXTRA_WEIGHT + contribution;
				if (ProblemConfiguration.MMAS_PHEROMONE_UPDATE
						&& newValue < ProblemConfiguration.getInstance()
								.getMinimumPheromoneValue()) {
					newValue = ProblemConfiguration.getInstance()
							.getMinimumPheromoneValue();
				} else if (ProblemConfiguration.MMAS_PHEROMONE_UPDATE
						&& newValue > ProblemConfiguration.getInstance()
								.getMaximumPheromoneValue()) {
					newValue = ProblemConfiguration.getInstance()
							.getMaximumPheromoneValue();
				}
				if (Double.isNaN(newValue)) {
					throw new Exception(
							"Invalid feromone final value. Original value: "
									+ environment.getPheromoneTrails()[imagePixel
											.getxCoordinate()][imagePixel
											.getyCoordinate()]
									+ ". Contribution: " + contribution);
				}
				environment.getPheromoneTrails()[imagePixel.getxCoordinate()
						* environment.getImageGraph()[0].length
						+ imagePixel.getyCoordinate()][imagePixel.getCluster()] = newValue;
			}
		}
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Ending  depositPheromoneInAntPath.");
	}

	public Ant getBestAnt() {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Starting  getBestAnt.");
		Ant bestAnt = antColony.get(0);
		for (Ant ant : antColony) {
			if (ant.getPartitionQuality(environment.getImageGraph()) < bestAnt
					.getPartitionQuality(environment.getImageGraph())) {
				bestAnt = ant;
			}
		}
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Ending  getBestAnt.");
		return bestAnt;

	}

	public void recordBestSolution(int iterationCounter) {
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "GETTING BEST SOLUTION FOUND");
		Ant bestAnt = getBestAnt();

		// TODO(cgavidia): Again, some CPU cicles can be saved here.
		double partitionQuality = bestAnt.getPartitionQuality(environment
				.getImageGraph());
		if (bestPartition == null || bestPartitionQuality > partitionQuality) {
			bestPartition = bestAnt.getPartition().clone();
			bestPartitionQuality = partitionQuality;
			bestPartitionIteration = iterationCounter;
		}
		System.out.println(ACOImageSegmentation.getComputingTimeAsString()
				+ "Best solution so far > Quality: " + bestPartitionQuality);
	}

	public double getBestPartitionQuality() {
		return bestPartitionQuality;
	}

	public ClusteredPixel[] getBestPartition() {
		return bestPartition;
	}

	public void setBestPartitionQuality(double bestPartitionQuality) {
		this.bestPartitionQuality = bestPartitionQuality;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setAntColony(List<Ant> antColony) {
		this.antColony = antColony;
	}

	public int getBestPartitionIteration() {
		return bestPartitionIteration;
	}

	public void setBestPartitionIteration(int bestPartitionIteration) {
		this.bestPartitionIteration = bestPartitionIteration;
	}

}
