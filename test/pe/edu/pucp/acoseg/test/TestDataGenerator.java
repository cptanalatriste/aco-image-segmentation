package pe.edu.pucp.acoseg.test;

import isula.image.util.ClusteredPixel;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.acoseg.isula.AntForImageSegmentation;
import pe.edu.pucp.acoseg.isula.EnvironmentForImageSegmentation;
import pe.edu.pucp.acoseg.isula.ImageSegmentationAntColony;

public class TestDataGenerator {

  public static final int CLUSTERS_FOR_TEST = 3;
  private static final double MAX_PHEROMONE_FOR_TEST = 1;
  private static final double MIN_PHEROMONE_FOR_TEST = 0.2;

 /* public static AntForImageSegmentation getAntWithPartialPartition() {
    AntForImageSegmentation antForTest = new AntForImageSegmentation(
        TestDataGenerator.getDummyImageMatrix().length
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
  }*/

  public static double[][] getDummyImageMatrix() {
    double[][] dummyImage = { { 85, 170, 255 }, { 85, 170, 255 },
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

  /* public static AntForImageSegmentation getAntWithPerfectPartition() {
   AntForImageSegmentation antForTest = new AntForImageSegmentation(
        TestDataGenerator.getDummyImageMatrix().length
            * TestDataGenerator.getDummyImageMatrix()[0].length,
        TestDataGenerator.CLUSTERS_FOR_TEST);
    antForTest.setCurrentIndex(0);
    for (ClusteredPixel clusteredPixel : getDummyPartition()) {
      antForTest.addAsignmentToSolution(clusteredPixel);
    }
    return antForTest;
  }*/

  /* public static AntForImageSegmentation getAntWithTwistedPartition() {
    AntForImageSegmentation antForTest = new AntForImageSegmentation(
        TestDataGenerator.getDummyImageMatrix().length
            * TestDataGenerator.getDummyImageMatrix()[0].length,
        TestDataGenerator.CLUSTERS_FOR_TEST);
    antForTest.setCurrentIndex(0);
    for (ClusteredPixel clusteredPixel : getDummyPartition()) {
      clusteredPixel.setCluster(0);
      antForTest.addAsignmentToSolution(clusteredPixel);
    }
    return antForTest;
  }*/

  /*public static ImageSegmentationAntColony getDummyAntColony() {
   EnvironmentForImageSegmentation environment = new EnvironmentForImageSegmentation(
        TestDataGenerator.getDummyImageMatrix(),
        TestDataGenerator.CLUSTERS_FOR_TEST);
    ImageSegmentationAntColony antColony = new ImageSegmentationAntColony(
        environment);
    List<AntForImageSegmentation> antColonyForTest = new ArrayList<AntForImageSegmentation>();
    AntForImageSegmentation antWithTwistedPartition = TestDataGenerator
        .getAntWithTwistedPartition();
    antColonyForTest.add(antWithTwistedPartition);
    AntForImageSegmentation antWithPerfectPartition = TestDataGenerator
        .getAntWithPerfectPartition();
    antColonyForTest.add(antWithPerfectPartition);
    antColony.setAntColony(antColonyForTest);
    return antColony;
  }*/

}
