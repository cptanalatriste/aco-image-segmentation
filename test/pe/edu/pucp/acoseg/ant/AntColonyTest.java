package pe.edu.pucp.acoseg.ant;

import static org.junit.Assert.*;

import org.junit.Test;

import pe.edu.pucp.acoseg.ProblemConfiguration;
import pe.edu.pucp.acoseg.test.TestDataGenerator;

public class AntColonyTest {

	@Test
	public void testGetBestAnt() {

		AntColony antColony = TestDataGenerator.getDummyAntColony();
		Ant bestAnt = antColony.getBestAnt();
		Ant antWithPerfectPartition = TestDataGenerator
				.getAntWithPerfectPartition();
		assertEquals(
				antWithPerfectPartition.getPartitionQuality(TestDataGenerator
						.getDummyImageMatrix()),
				bestAnt.getPartitionQuality(TestDataGenerator
						.getDummyImageMatrix()), 0.001);
	}

	@Test
	public void testRecordBestSolution() {
		AntColony antColony = TestDataGenerator.getDummyAntColony();
		antColony.setBestPartitionQuality(TestDataGenerator
				.getAntWithTwistedPartition().getPartitionQuality(
						TestDataGenerator.getDummyImageMatrix()));
		antColony.recordBestSolution(0);
		Ant antWithPerfectPartition = TestDataGenerator
				.getAntWithPerfectPartition();
		assertEquals(
				antWithPerfectPartition.getPartitionQuality(TestDataGenerator
						.getDummyImageMatrix()),
				antColony.getBestPartitionQuality(), 0.001);
	}

	@Test
	public void testDepositPheromoneInAntPath() throws Exception {
		Ant antForTest = TestDataGenerator.getAntWithTwistedPartition();
		double contribution = 1 / antForTest
				.getPartitionQuality(TestDataGenerator.getDummyImageMatrix());
		AntColony antColony = TestDataGenerator.getDummyAntColony();
		antColony.depositPheromoneInAntPath(antForTest);

		double[][] pheromoneMatrix = antColony.getEnvironment()
				.getPheromoneTrails();

		for (int i = 0; i < pheromoneMatrix.length; i++) {
			for (int j = 0; j < pheromoneMatrix[0].length; j++) {
				if (j == 0) {
					if (contribution < ProblemConfiguration.getInstance()
							.getMinimumPheromoneValue()) {
						assertEquals(ProblemConfiguration.getInstance()
								.getMinimumPheromoneValue(),
								pheromoneMatrix[i][j], 0.001);
					} else if (contribution > ProblemConfiguration
							.getInstance().getMaximumPheromoneValue()) {
						assertEquals(ProblemConfiguration.getInstance()
								.getMaximumPheromoneValue(),
								pheromoneMatrix[i][j], 0.001);
					} else {
						assertEquals(contribution, pheromoneMatrix[i][j], 0.001);
					}
				} else {
					assertEquals(0, pheromoneMatrix[i][j], 0.001);
				}
			}

		}
	}
}
