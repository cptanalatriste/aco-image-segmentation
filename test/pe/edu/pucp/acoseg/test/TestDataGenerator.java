package pe.edu.pucp.acoseg.test;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.acoseg.ant.Ant;
import pe.edu.pucp.acoseg.ant.AntColony;
import pe.edu.pucp.acoseg.ant.Environment;
import pe.edu.pucp.acoseg.image.ClusteredPixel;

public class TestDataGenerator {

	public static final int CLUSTERS_FOR_TEST = 3;
	private static final double MAX_PHEROMONE_FOR_TEST = 1;
	private static final double MIN_PHEROMONE_FOR_TEST = 0.2;

	public static Ant getAntWithPartialPartition() {
		Ant antForTest = new Ant(TestDataGenerator.getDummyImageMatrix().length
				* TestDataGenerator.getDummyImageMatrix()[0].length,
				TestDataGenerator.CLUSTERS_FOR_TEST);
		antForTest.setCurrentIndex(0);
		antForTest.addAsignmentToSolution(new ClusteredPixel(0, 0,
				TestDataGenerator.getDummyImageMatrix(), 0));
		antForTest.addAsignmentToSolution(new ClusteredPixel(0, 1,
				TestDataGenerator.getDummyImageMatrix(), 1));
		antForTest.addAsignmentToSolution(new ClusteredPixel(0, 2,
				TestDataGenerator.getDummyImageMatrix(), 2));
		antForTest.addAsignmentToSolution(new ClusteredPixel(1, 0,
				TestDataGenerator.getDummyImageMatrix(), 0));
		return antForTest;
	}

	public static int[][] getDummyImageMatrix() {
		int[][] dummyImage = { { 85, 170, 255 }, { 85, 170, 255 },
				{ 85, 170, 255 } };
		return dummyImage;
	}

	public static ClusteredPixel[] getDummyPartition() {
		ClusteredPixel[] dummyPartition = new ClusteredPixel[9];
		dummyPartition[0] = new ClusteredPixel(0, 0, getDummyImageMatrix(), 0);
		dummyPartition[1] = new ClusteredPixel(0, 1, getDummyImageMatrix(), 1);
		dummyPartition[2] = new ClusteredPixel(0, 2, getDummyImageMatrix(), 2);
		dummyPartition[3] = new ClusteredPixel(1, 0, getDummyImageMatrix(), 0);
		dummyPartition[4] = new ClusteredPixel(1, 1, getDummyImageMatrix(), 1);
		dummyPartition[5] = new ClusteredPixel(1, 2, getDummyImageMatrix(), 2);
		dummyPartition[6] = new ClusteredPixel(2, 0, getDummyImageMatrix(), 0);
		dummyPartition[7] = new ClusteredPixel(2, 1, getDummyImageMatrix(), 1);
		dummyPartition[8] = new ClusteredPixel(2, 2, getDummyImageMatrix(), 2);

		return dummyPartition;
	}

	public static double[][] getDummyPheromoneMatrix() {
		double[][] pheromoneMatrix = new double[getDummyPartition().length][CLUSTERS_FOR_TEST];
		for (int i = 0; i < getDummyPartition().length; i++) {
			ClusteredPixel pixel = getDummyPartition()[i];
			for (int j = 0; j < CLUSTERS_FOR_TEST; j++) {
				if (j == pixel.getCluster()) {
					pheromoneMatrix[i][j] = MAX_PHEROMONE_FOR_TEST;
				} else {
					pheromoneMatrix[i][j] = MIN_PHEROMONE_FOR_TEST;

				}
			}
		}
		return pheromoneMatrix;
	}

	public static Ant getAntWithPerfectPartition() {
		Ant antForTest = new Ant(TestDataGenerator.getDummyImageMatrix().length
				* TestDataGenerator.getDummyImageMatrix()[0].length,
				TestDataGenerator.CLUSTERS_FOR_TEST);
		antForTest.setCurrentIndex(0);
		for (ClusteredPixel clusteredPixel : getDummyPartition()) {
			antForTest.addAsignmentToSolution(clusteredPixel);
		}
		return antForTest;
	}

	public static Ant getAntWithTwistedPartition() {
		Ant antForTest = new Ant(TestDataGenerator.getDummyImageMatrix().length
				* TestDataGenerator.getDummyImageMatrix()[0].length,
				TestDataGenerator.CLUSTERS_FOR_TEST);
		antForTest.setCurrentIndex(0);
		for (ClusteredPixel clusteredPixel : getDummyPartition()) {
			clusteredPixel.setCluster(0);
			antForTest.addAsignmentToSolution(clusteredPixel);
		}
		return antForTest;
	}

	public static AntColony getDummyAntColony() {
		Environment environment = new Environment(
				TestDataGenerator.getDummyImageMatrix(),
				TestDataGenerator.CLUSTERS_FOR_TEST);
		AntColony antColony = new AntColony(environment);
		List<Ant> antColonyForTest = new ArrayList<Ant>();
		Ant antWithTwistedPartition = TestDataGenerator
				.getAntWithTwistedPartition();
		antColonyForTest.add(antWithTwistedPartition);
		Ant antWithPerfectPartition = TestDataGenerator
				.getAntWithPerfectPartition();
		antColonyForTest.add(antWithPerfectPartition);
		antColony.setAntColony(antColonyForTest);
		return antColony;
	}

}
