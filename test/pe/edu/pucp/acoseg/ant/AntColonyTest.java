package pe.edu.pucp.acoseg.ant;

import static org.junit.Assert.assertEquals;

import isula.aco.AcoProblemSolver;
import isula.aco.exception.InvalidInputException;
import isula.image.util.ClusteredPixel;

import org.junit.Test;

import pe.edu.pucp.acoseg.isula.AntForImageSegmentation;
import pe.edu.pucp.acoseg.isula.EnvironmentForImageSegmentation;
import pe.edu.pucp.acoseg.isula.ImageSegmentationAntColony;
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
    AcoProblemSolver<ClusteredPixel, EnvironmentForImageSegmentation> dummySolver 
        = new AcoProblemSolver<ClusteredPixel, EnvironmentForImageSegmentation>();
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
}
