package pe.edu.pucp.acoseg;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import pe.edu.pucp.acoseg.ant.AntColony;
import pe.edu.pucp.acoseg.ant.Environment;
import pe.edu.pucp.acoseg.exper.TestSuite;
import pe.edu.pucp.acoseg.image.ClusteredPixel;
import pe.edu.pucp.acoseg.image.ImageFileHelper;
import pe.edu.pucp.acothres.AcoImageThresholding;

public class ACOImageSegmentation {

  private static long startingComputingTime = System.nanoTime();

  private Environment environment;
  private AntColony antColony;
  private double computationTime;

  public ACOImageSegmentation(Environment environment) {
    this.environment = environment;
    this.antColony = new AntColony(environment);
  }

  private ClusteredPixel[] solveProblem() throws Exception {
    this.environment.initializePheromoneMatrix();
    int iteration = 0;
    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "STARTING ITERATIONS");
    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Number of iterations: "
        + ProblemConfiguration.getInstance().getMaxIterations());
    while (iteration < ProblemConfiguration.getInstance().getMaxIterations()) {
      System.out.println(ACOImageSegmentation.getComputingTimeAsString()
          + "Current iteration: " + iteration);
      this.antColony.clearAntSolutions();
      this.antColony
          .buildSolutions(ProblemConfiguration.DEPOSITE_PHEROMONE_ONLINE);
      System.out.println(ACOImageSegmentation.getComputingTimeAsString()
          + "UPDATING PHEROMONE TRAILS");
      if (!ProblemConfiguration.DEPOSITE_PHEROMONE_ONLINE) {
        this.antColony.depositPheromone();
      }
      this.environment.performEvaporation();
      this.antColony.recordBestSolution(iteration);
      iteration++;
    }
    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "EXECUTION FINISHED");
    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Best partition quality: " + antColony.getBestPartitionQuality());
    return antColony.getBestPartition();
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

  public static ACOImageSegmentation performSegmentation() throws IOException,
      Exception {
    String imageFile = ProblemConfiguration.INPUT_DIRECTORY
        + ProblemConfiguration.IMAGE_FILE;
    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Data file: " + imageFile);

    int[][] imageGraph = ImageFileHelper.getImageArrayFromFile(imageFile);

    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Starting background filtering process");

    int[][] backgroundFilterMask = AcoImageThresholding
        .getSegmentedImageAsArray(imageFile, true);

    imageGraph = ImageFileHelper.applyFilter(imageGraph, backgroundFilterMask);
    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Generating filtered image");
    ImageFileHelper.generateImageFromArray(imageGraph, ProblemConfiguration
        .getInstance().getOutputDirectory()
        + ProblemConfiguration.FILTERED_IMAGE_FILE);

    Environment environment = new Environment(imageGraph, ProblemConfiguration
        .getInstance().getNumberOfClusters());

    startingComputingTime = System.nanoTime();
    ClusteredPixel[] resultingPartition = null;
    ACOImageSegmentation acoImageSegmentation = null;
    if (ProblemConfiguration.USE_PHEROMONE_FOR_CLUSTERING) {
      acoImageSegmentation = new ACOImageSegmentation(environment);
      System.out.println(ACOImageSegmentation.getComputingTimeAsString()
          + "Starting computation at: " + new Date());
      resultingPartition = acoImageSegmentation.solveProblem();
    }

    // TODO(cgavidia): There should a method to get the number of
    // clústers automatically. Also, son preprocessing or postprocessing
    // would improve quality.

    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Finishing computation at: " + new Date());
    double computationTime = (double) (System.nanoTime() - startingComputingTime) / 1000000000.0;
    acoImageSegmentation.setComputationTime(computationTime);
    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Duration (in seconds): " + computationTime);

    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Generating segmented image");
    int[][] segmentedImageAsMatrix = generateSegmentedImage(resultingPartition,
        environment);
    ImageFileHelper.generateImageFromArray(segmentedImageAsMatrix,
        ProblemConfiguration.getInstance().getOutputDirectory()
            + ProblemConfiguration.OUTPUT_IMAGE_FILE);

    System.out.println(ACOImageSegmentation.getComputingTimeAsString()
        + "Generating images per cluster");
    for (int i = 0; i < ProblemConfiguration.getInstance()
        .getNumberOfClusters(); i++) {
      int[][] clusterImage = generateSegmentedImagePerCluster(i,
          resultingPartition, environment);
      ImageFileHelper.generateImageFromArray(clusterImage, ProblemConfiguration
          .getInstance().getOutputDirectory()
          + i
          + "_"
          + ProblemConfiguration.CLUSTER_IMAGE_FILE);

    }
    return acoImageSegmentation;
  }

  private static int[][] generateSegmentedImagePerCluster(int clusterNumber,
      ClusteredPixel[] resultingPartition, Environment environment) {

    int[][] resultMatrix = new int[environment.getImageGraph().length][environment
        .getImageGraph()[0].length];

    int pixelCounter = 0;
    for (int i = 0; i < environment.getImageGraph().length; i++) {
      for (int j = 0; j < environment.getImageGraph()[0].length; j++) {
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
      ClusteredPixel[] resultingPartition, Environment environment) {
    int[][] resultMatrix = new int[environment.getImageGraph().length][environment
        .getImageGraph()[0].length];

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

  public static String getComputingTimeAsString() {
    double computingTime = (double) (System.nanoTime() - startingComputingTime) / 1000000000.0;
    return new DecimalFormat("##.##").format(computingTime) + ":	";
  }

  private void setComputationTime(double computationTime) {
    this.computationTime = computationTime;
  }

  public AntColony getAntColony() {
    return antColony;
  }

  public double getComputationTime() {
    return computationTime;
  }
}
