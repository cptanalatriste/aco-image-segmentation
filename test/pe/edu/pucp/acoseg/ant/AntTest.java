package pe.edu.pucp.acoseg.ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import isula.aco.exception.InvalidInputException;
import isula.image.util.ClusteredPixel;

import org.junit.Test;

import pe.edu.pucp.acoseg.ProblemConfiguration;
import pe.edu.pucp.acoseg.isula.AntForImageSegmentation;
import pe.edu.pucp.acoseg.isula.ImageSegmentationNodeSelection;
import pe.edu.pucp.acoseg.test.TestDataGenerator;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class AntTest {

  // TODO(cgavidia): This comments are temporarl, until stabilitization
  @Test
  public void testGetContiguityMeasure() throws InvalidInputException {

    ClusteredPixel pixelToEvaluate = new ClusteredPixel(1, 1,
        TestDataGenerator.getDummyImageMatrix(), 0);
    double expectedContiguity = 2.0 / 4.0;
    AntForImageSegmentation antForTest = TestDataGenerator
        .getAntWithPartialPartition();
    assertEquals(
        expectedContiguity,
        antForTest.getContiguityMeasure(pixelToEvaluate,
            TestDataGenerator.getDummyImageMatrix()), 0.001);

    pixelToEvaluate = new ClusteredPixel(1, 1,
        TestDataGenerator.getDummyImageMatrix(), 1);
    expectedContiguity = 1.0 / 4.0;
    assertEquals(
        expectedContiguity,
        antForTest.getContiguityMeasure(pixelToEvaluate,
            TestDataGenerator.getDummyImageMatrix()), 0.001);
  }

  @Test
  public void testGetClusterMeanValue() throws InvalidInputException {
    AntForImageSegmentation antForTest = TestDataGenerator
        .getAntWithPartialPartition();
    assertEquals(85.0, antForTest.getClusterMeanValue(0), 0.001);
    assertEquals(255.0, antForTest.getClusterMeanValue(2), 0.001);
  }

  @Test
  public void testGetHeuristicValue() throws InvalidInputException {
    AntForImageSegmentation antForTest = TestDataGenerator
        .getAntWithPartialPartition();
    ClusteredPixel pixelToEvaluate = new ClusteredPixel(1, 1,
        TestDataGenerator.getDummyImageMatrix(), 0);
    double expectedHeuristicValue = 85.0 + ProblemConfiguration.getInstance()
        .getContiguityMeassureParam() * 0.5;
    assertEquals(
        expectedHeuristicValue,
        antForTest.getHeuristicValue(pixelToEvaluate, -1,
            TestDataGenerator.getDummyEnvironment()), 0.001);
  }

  @Test
  public void testGetPartitionQuality() throws InvalidInputException {

    AntForImageSegmentation antForTest = TestDataGenerator
        .getAntWithPerfectPartition();
    double perfectCluster = antForTest.getSolutionQuality(TestDataGenerator
        .getDummyEnvironment());

    AntForImageSegmentation drunkAnt = TestDataGenerator
        .getAntWithTwistedPartition();
    double messyCluster = drunkAnt.getSolutionQuality(TestDataGenerator
        .getDummyEnvironment());

    assertTrue("Optimal value: " + perfectCluster
        + " should be grater than messy one: " + messyCluster,
        perfectCluster < messyCluster);
  }

  @Test
  public void testGetProbabilities() throws InvalidInputException {
    AntForImageSegmentation antForTest = TestDataGenerator
        .getAntWithPartialPartition();
    antForTest.setCurrentXPosition(1);
    antForTest.setCurrentYPosition(1);

    ImageSegmentationNodeSelection nodeSelection = new ImageSegmentationNodeSelection();
    nodeSelection.setAnt(antForTest);

    HashMap<ClusteredPixel, Double> probabilities = nodeSelection
        .getComponentsWithProbabilities(
            TestDataGenerator.getDummyEnvironment(),
            ProblemConfiguration.getInstance());

    assertEquals(TestDataGenerator.CLUSTERS_FOR_TEST, probabilities.size());

    double clusterZeroProb = -1;
    double clusterOneProb = -1;
    double clusterTwoProb = -1;

    Iterator<Entry<ClusteredPixel, Double>> componentWithProbabilitiesIterator = probabilities
        .entrySet().iterator();
    while (componentWithProbabilitiesIterator.hasNext()) {
      Entry<ClusteredPixel, Double> componentWithProbability = componentWithProbabilitiesIterator
          .next();

      ClusteredPixel component = componentWithProbability.getKey();

      if (component.getCluster() == 0) {
        clusterZeroProb = componentWithProbability.getValue();
      } else if (component.getCluster() == 1) {
        clusterOneProb = componentWithProbability.getValue();
      } else if (component.getCluster() == 2) {
        clusterTwoProb = componentWithProbability.getValue();
      }

    }

    assertTrue(
        "Probability for Cluster 1 ->" + clusterOneProb
            + " is not greater than Probability for Cluster 2 -> "
            + clusterTwoProb, clusterOneProb > clusterTwoProb);

    assertTrue("Probability for Cluster 1 ->" + clusterOneProb
        + " is not greater than Probability for Cluster 0 -> "
        + clusterZeroProb, clusterOneProb > clusterZeroProb);

  }

  @Test
  public void testMaximumValueAssignment() throws InvalidInputException {
    AntForImageSegmentation antForTest = TestDataGenerator
        .getAntWithPartialPartition();
    antForTest.setCurrentXPosition(1);
    antForTest.setCurrentYPosition(1);

    ImageSegmentationNodeSelection nodeSelection = new ImageSegmentationNodeSelection();
    nodeSelection.setAnt(antForTest);

    HashMap<ClusteredPixel, Double> probabilities = nodeSelection
        .getComponentsWithProbabilities(
            TestDataGenerator.getDummyEnvironment(),
            ProblemConfiguration.getInstance());

    ClusteredPixel assignment = nodeSelection.getMostConvenient(probabilities);
    assertEquals(1, assignment.getCluster());

  }
}
