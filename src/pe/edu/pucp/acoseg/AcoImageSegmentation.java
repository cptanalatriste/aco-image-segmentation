package pe.edu.pucp.acoseg;

import isula.aco.AcoProblemSolver;
import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.maxmin.StartPheromoneMatrixForMaxMin;
import isula.image.util.ClusteredPixel;
import isula.image.util.ImageFileHelper;
import pe.edu.pucp.acoseg.exper.TestSuiteForImageSegmentation;
import pe.edu.pucp.acoseg.isula.EnvironmentForImageSegmentation;
import pe.edu.pucp.acoseg.isula.ImageSegmentationAntColony;
import pe.edu.pucp.acoseg.isula.ImageSegmentationNodeSelection;
import pe.edu.pucp.acoseg.isula.ImageSegmentationUpdatePheromoneMatrix;
import pe.edu.pucp.acothres.AcoImageThresholding;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;



public class AcoImageSegmentation {

  private static Logger logger = Logger.getLogger(AcoImageSegmentation.class
      .getName());

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

    // TODO(cgavidia): We should implement a mechanism to add policies at Colony
    // Level.
    List<Ant<ClusteredPixel>> hive = antColony.getHive();
    for (Ant<ClusteredPixel> ant : hive) {
      ant.addPolicy(new ImageSegmentationNodeSelection());
    }

    problemSolver.solveProblem();
    ClusteredPixel[] bestPartition = problemSolver.getBestSolution();

    return bestPartition;
  }

  /**
   * Start the ACO-based segmentation process.
   * 
   * @param args
   *          Program arguments.
   */
  public static void main(String[] args) {
    logger.info("ACO FOR IMAGE SEGMENTATION");

    try {
      performSegmentation();
      new TestSuiteForImageSegmentation().executeReport();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Launches the segmentation process.
   * 
   * @return An instance of this class.
   * @throws IOException
   *           In case of reading/writing problems.
   * @throws Exception
   *           Application-level exceptions.
   */
  public static AcoImageSegmentation performSegmentation() throws IOException,
      Exception {
    String imageFile = ProblemConfiguration.INPUT_DIRECTORY
        + ProblemConfiguration.IMAGE_FILE;
    logger.info("Data file: " + imageFile);

    double[][] imageGraph = returnImageAsArray(imageFile);

    ClusteredPixel[] resultingPartition = null;
    AcoImageSegmentation acoImageSegmentation = null;
    acoImageSegmentation = new AcoImageSegmentation();
    resultingPartition = acoImageSegmentation.solveProblem(imageGraph);

    logger.info("Generating segmented image");
    int[][] segmentedImageAsMatrix = generateSegmentedImage(resultingPartition,
        (EnvironmentForImageSegmentation) acoImageSegmentation.problemSolver
            .getEnvironment());
    ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
        ProblemConfiguration.getInstance().getOutputDirectory()
            + ProblemConfiguration.OUTPUT_IMAGE_FILE);

    logger.info("Generating images per cluster");
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

    logger.info("Starting background filtering process");

    int[][] backgroundFilterMask = AcoImageThresholding
        .getSegmentedImageAsArray(imageFile, true);

    imageGraphAsInt = ImageFileHelper.applyFilter(imageGraphAsInt,
        backgroundFilterMask);
    logger.info("Generating filtered image");
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

  static int[][] generateSegmentedImage(
      ClusteredPixel[] resultingPartition,
      EnvironmentForImageSegmentation environment) {
    int[][] resultMatrix = new int[environment.getProblemGraph().length][environment
        .getProblemGraph()[0].length];

    for (ClusteredPixel clusteredPixel : resultingPartition) {
      int cluster = clusteredPixel.getCluster();
      if (cluster != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
        int numberOfClusters = environment.getNumberOfClusters();
        int greyScaleValue = (int) ((cluster + 1.0) 
            / numberOfClusters * ProblemConfiguration.GRAYSCALE_MAX_RANGE);
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
