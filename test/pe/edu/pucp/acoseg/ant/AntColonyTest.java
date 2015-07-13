package pe.edu.pucp.acoseg.ant;

import static org.junit.Assert.assertEquals;

import isula.aco.AcoProblemSolver;
import isula.aco.exception.InvalidInputException;
import isula.image.util.ClusteredPixel;

import org.junit.Test;

import pe.edu.pucp.acoseg.ProblemConfiguration;
import pe.edu.pucp.acoseg.isula.AntForImageSegmentation;
import pe.edu.pucp.acoseg.isula.EnvironmentForImageSegmentation;
import pe.edu.pucp.acoseg.isula.ImageSegmentationAntColony;
import pe.edu.pucp.acoseg.isula.ImageSegmentationUpdatePheromoneMatrix;
import pe.edu.pucp.acoseg.test.TestDataGenerator;

public class AntColonyTest {

  // This comments are temporary, until stabilization
  @Test
  public void testGetBestAnt() throws InvalidInputException {
    ImageSegmentationAntColony antColony = TestDataGenerator
        .getDummyAntColony();
    EnvironmentForImageSegmentation dummyEnvironment = TestDataGenerator
        .getDummyEnvironment();
    AntForImageSegmentation bestAnt = (AntForImageSegmentation) antColony
        .getBestPerformingAnt(dummyEnvironment);
    AntForImageSegmentation antWithPerfectPartition = TestDataGenerator
        .getAntWithPerfectPartition();
    assertEquals(antWithPerfectPartition.getSolutionQuality(dummyEnvironment),
        bestAnt.getSolutionQuality(dummyEnvironment), 0.001);
  }

  @Test
  public void testRecordBestSolution() throws InvalidInputException {
    EnvironmentForImageSegmentation dummyEnvironment = TestDataGenerator
        .getDummyEnvironment();
    AcoProblemSolver<ClusteredPixel> dummySolver = new AcoProblemSolver<ClusteredPixel>();
    ImageSegmentationAntColony antColony = TestDataGenerator
        .getDummyAntColony();

    dummySolver.setAntColony(antColony);
    dummySolver.setEnvironment(dummyEnvironment);
    dummySolver.setBestSolutionQuality(TestDataGenerator
        .getAntWithTwistedPartition().getSolutionQuality(dummyEnvironment));

    dummySolver.updateBestSolution(dummyEnvironment);

    AntForImageSegmentation antWithPerfectPartition = TestDataGenerator
        .getAntWithPerfectPartition();
    assertEquals(antWithPerfectPartition.getSolutionQuality(dummyEnvironment),
        dummySolver.getBestSolutionQuality(), 0.001);
  }

  // TODO(cgavidia): This needs further work.
  @Test
  public void testDepositPheromoneInAntPath() throws Exception {
    AntForImageSegmentation antForTest = TestDataGenerator
        .getAntWithTwistedPartition();
    EnvironmentForImageSegmentation dummyEnvironment = TestDataGenerator
        .getDummyEnvironment();
    double contribution = 1 / antForTest.getSolutionQuality(dummyEnvironment);
    ImageSegmentationAntColony antColony = TestDataGenerator
        .getDummyAntColony();
    ImageSegmentationUpdatePheromoneMatrix updatePheromone = 
        new ImageSegmentationUpdatePheromoneMatrix();
    updatePheromone.setAntColony(antColony);
    updatePheromone.setEnvironment(dummyEnvironment);

    updatePheromone.applyDaemonAction(ProblemConfiguration.getInstance());

    double[][] pheromoneMatrix = dummyEnvironment.getPheromoneMatrix();

    for (int i = 0; i < pheromoneMatrix.length; i++) {
      for (int j = 0; j < pheromoneMatrix[0].length; j++) {
        if (j == 0) {
          if (contribution < ProblemConfiguration.getInstance()
              .getMinimumPheromoneValue()) {
            assertEquals(ProblemConfiguration.getInstance()
                .getMinimumPheromoneValue(), pheromoneMatrix[i][j], 0.001);
          } else if (contribution > ProblemConfiguration.getInstance()
              .getMaximumPheromoneValue()) {
            assertEquals(ProblemConfiguration.getInstance()
                .getMaximumPheromoneValue(), pheromoneMatrix[i][j], 0.001);
          } else {
            assertEquals(contribution, pheromoneMatrix[i][j], 0.001);
          }

        } else {
          assertEquals(ProblemConfiguration.getInstance()
              .getMaximumPheromoneValue(), pheromoneMatrix[i][j], 0.001);
        }
      }

    }
  }
}
