package pe.edu.pucp.acoseg;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.maxmin.StartPheromoneMatrixForMaxMin;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import pe.edu.pucp.acoseg.exper.TestSuite;
import pe.edu.pucp.acoseg.image.ClusteredPixel;
import pe.edu.pucp.acoseg.image.ImageFileHelper;
import pe.edu.pucp.acoseg.isula.EnvironmentForImageSegmentation;
import pe.edu.pucp.acoseg.isula.ImageSegmentationAntColony;
import pe.edu.pucp.acoseg.isula.ImageSegmentationNodeSelection;
import pe.edu.pucp.acoseg.isula.ImageSegmentationUpdatePheromoneMatrix;
import pe.edu.pucp.acothres.AcoImageThresholding;

public class AcoImageSegmentation {

  private AcoProblemSolver<ClusteredPixel> problemSolver;

  private ClusteredPixel[] solveProblem(double[][] imageGraph) throws Exception {
    ConfigurationProvider configurationProvider = ProblemConfiguration
        .getInstance();
    problemSolver = new AcoProblemSolver<ClusteredPixel>();
    EnvironmentForImageSegmentation environment = new EnvironmentForImageSegmentation(
        imageGraph, ProblemConfiguration.getInstance().getNumberOfClusters());

    // TODO(cgavidia): Maybe the constructor should take the Configuration
    // Provider.
    ImageSegmentationAntColony antColony = new ImageSegmentationAntColony(
        configurationProvider.getNumberOfAnts(),
        environment.getNumberOfClusters());
    antColony.buildColony(environment);
    problemSolver.setConfigurationProvider(configurationProvider);
    problemSolver.setEnvironment(environment);
    problemSolver.setAntColony(antColony);

    problemSolver
        .addDaemonAction(new StartPheromoneMatrixForMaxMin<ClusteredPixel>());
    problemSolver.addDaemonAction(new ImageSegmentationUpdatePheromoneMatrix());

    List<Ant<ClusteredPixel>> hive = antColony.getHive();
    for (Ant<ClusteredPixel> ant : hive) {
      ant.addPolicy(new ImageSegmentationNodeSelection());
    }

    problemSolver.solveProblem();
    ClusteredPixel[] bestPartition = problemSolver.getBestSolution();

    return bestPartition;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("ACO FOR IMAGE SEGMENTATION");
    System.out.println("=============================");

    try {

      performSegmentation();
      new TestSuite().executeReport();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static AcoImageSegmentation performSegmentation() throws IOException,
      Exception {
    String imageFile = ProblemConfiguration.INPUT_DIRECTORY
        + ProblemConfiguration.IMAGE_FILE;
    System.out.println("Data file: " + imageFile);

    double[][] imageGraph = returnImageAsArray(imageFile);

    ClusteredPixel[] resultingPartition = null;
    AcoImageSegmentation acoImageSegmentation = null;
    acoImageSegmentation = new AcoImageSegmentation();
    System.out.println("Starting computation at: " + new Date());
    resultingPartition = acoImageSegmentation.solveProblem(imageGraph);

    System.out.println("Generating segmented image");
    int[][] segmentedImageAsMatrix = generateSegmentedImage(resultingPartition,
        (EnvironmentForImageSegmentation) acoImageSegmentation.problemSolver
            .getEnvironment());
    ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
        ProblemConfiguration.getInstance().getOutputDirectory()
            + ProblemConfiguration.OUTPUT_IMAGE_FILE);

    System.out.println("Generating images per cluster");
    for (int i = 0; i < ProblemConfiguration.getInstance()
        .getNumberOfClusters(); i++) {
      int[][] clusterImage = generateSegmentedImagePerCluster(i,
          resultingPartition,
          acoImageSegmentation.problemSolver.getEnvironment());
      ImageFileHelper.generateImageFromArray(clusterImage, ProblemConfiguration
          .getInstance().getOutputDirectory()
          + i
          + "_"
          + ProblemConfiguration.CLUSTER_IMAGE_FILE);

    }
    return acoImageSegmentation;
  }

  private static double[][] returnImageAsArray(String imageFile)
      throws IOException, Exception {
    int[][] imageGraphAsInt = ImageFileHelper.getImageArrayFromFile(imageFile);

    System.out.println("Starting background filtering process");

    int[][] backgroundFilterMask = AcoImageThresholding
        .getSegmentedImageAsArray(imageFile, true);

    imageGraphAsInt = ImageFileHelper.applyFilter(imageGraphAsInt,
        backgroundFilterMask);
    System.out.println("Generating filtered image");
    ImageFileHelper.generateImageFromArray(imageGraphAsInt,
        ProblemConfiguration.getInstance().getOutputDirectory()
            + ProblemConfiguration.FILTERED_IMAGE_FILE);

    // TODO(cgavidia): Simple hack to support the type. It should be a generic
    // instead.
    double[][] imageGraph = new double[imageGraphAsInt.length][imageGraphAsInt[0].length];
    for (int i = 0; i < imageGraphAsInt.length; i++) {
      for (int j = 0; j < imageGraphAsInt[0].length; j++) {
        imageGraph[i][j] = imageGraphAsInt[i][j];
      }
    }

    return imageGraph;
  }

  private static int[][] generateSegmentedImagePerCluster(int clusterNumber,
      ClusteredPixel[] resultingPartition, Environment environment) {

    int[][] resultMatrix = new int[environment.getProblemGraph().length][environment
        .getProblemGraph()[0].length];

    int pixelCounter = 0;
    for (int i = 0; i < environment.getProblemGraph().length; i++) {
      for (int j = 0; j < environment.getProblemGraph()[0].length; j++) {
        int greyscaleValue = ProblemConfiguration.GRAYSCALE_MIN_RANGE;
        if (resultingPartition[pixelCounter].getCluster() == clusterNumber) {
          greyscaleValue = ProblemConfiguration.GRAYSCALE_MAX_RANGE / 2;
        }
        resultMatrix[i][j] = greyscaleValue;
        pixelCounter++;
      }
    }
    return resultMatrix;
  }

  public static int[][] generateSegmentedImage(
      ClusteredPixel[] resultingPartition,
      EnvironmentForImageSegmentation environment) {
    int[][] resultMatrix = new int[environment.getProblemGraph().length][environment
        .getProblemGraph()[0].length];

    for (ClusteredPixel clusteredPixel : resultingPartition) {
      int cluster = clusteredPixel.getCluster();
      if (cluster != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
        int numberOfClusters = environment.getNumberOfClusters();
        int greyScaleValue = (int) ((cluster + 1.0) / numberOfClusters * ProblemConfiguration.GRAYSCALE_MAX_RANGE);
        resultMatrix[clusteredPixel.getxCoordinate()][clusteredPixel
            .getyCoordinate()] = greyScaleValue;
      } else {
        resultMatrix[clusteredPixel.getxCoordinate()][clusteredPixel
            .getyCoordinate()] = ProblemConfiguration.GRAYSCALE_MIN_RANGE;
      }

    }

    return resultMatrix;
  }

  public AcoProblemSolver<ClusteredPixel> getProblemSolver() {
    return problemSolver;
  }

}
