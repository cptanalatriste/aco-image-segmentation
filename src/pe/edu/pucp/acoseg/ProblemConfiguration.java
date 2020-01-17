package pe.edu.pucp.acoseg;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;

public class ProblemConfiguration implements ConfigurationProvider,
    MaxMinConfigurationProvider, AcsConfigurationProvider {
  // TODO(cgavidia) This need huge refactoring

  private static final ProblemConfiguration INSTANCE = new ProblemConfiguration();

  // Credits:
  // https://www.eecs.berkeley.edu/Research/Projects/CS/vision/bsds/BSDS300/html/dataset/images/gray/296059.html
  // http://brainweb.bic.mni.mcgill.ca/brainweb/anatomic_normal_20.html
  public static final String INPUT_DIRECTORY = "inputImg/";

  static final String IMAGE_FILE = "19952transverse2_64.gif";
  static final String OUTPUT_IMAGE_FILE = IMAGE_FILE.substring(0,
      IMAGE_FILE.lastIndexOf('.'))
      + "_output.bmp";
  static final String FILTERED_IMAGE_FILE = IMAGE_FILE.substring(0,
      IMAGE_FILE.lastIndexOf('.'))
      + "_filtered.bmp";
  public static final String CLUSTER_IMAGE_FILE = IMAGE_FILE.substring(0,
      IMAGE_FILE.lastIndexOf('.')) + "_cluster.bmp";

  // Max-Min Ant System Pheromone parameters
  // TODO(cgavidia): This will be calibrated later

  // This are values from the original paper
  // TODO(cgavidia): Calibrate later
  private static final int PHEROMONE_IMPORTANCE = 1;
  public static final double EXTRA_WEIGHT = 0.6;
  private static final int COST_FUNCTION_PARAMETER_A = 5000;
  private static final int COST_FUNCTION_PARAMETER_B = 10;
  private static final double INITIAL_PHEROMONE_VALUE = Float.MIN_VALUE;
  public static final double DELTA = Float.MIN_VALUE;
  public static final int GRAYSCALE_MIN_RANGE = 0;
  public static final int GRAYSCALE_MAX_RANGE = 255;
  public static final int GRAYSCALE_POSITIVE_THRESHOLD = 120;
  public static final int ABSENT_PIXEL_FLAG = -1;
  public static final int ABSENT_PIXEL_CLUSTER = -1;
  public static final int GRAYSCALE_DELTA = 10;

  /**
   *  A String representation of the configuration.
   * 
   * @return Configuration as an String.
   */
  public String currentConfigurationAsString() {
    String result = " Input file: " + IMAGE_FILE + "\n";
    result = result + " Evaporation parameter: " + evaporation + "\n";
    result = result + " Number of iterations: " + maxIterations + "\n";
    result = result + " Pheromone importance: " + PHEROMONE_IMPORTANCE + "\n";
    result = result + " Heuristic importance: " + heuristicImportance + "\n";
    result = result + " Extra weight: " + EXTRA_WEIGHT + "\n";
    result = result + " Parameter A for Cost Function: "
        + COST_FUNCTION_PARAMETER_A + "\n";
    result = result + " Parameter B for Cost Function: "
        + COST_FUNCTION_PARAMETER_B + "\n";
    result = result + " Initial Pheromone Value: " + INITIAL_PHEROMONE_VALUE
        + "\n";
    result = result + " Number of Clusters: " + numberOfClusters + "\n";

    return result;
  }

  // This HAVE TO BE NEGATIVEA, because of the nature of the expression.
  private double heuristicImportance = -1;
  private double bestChoiceProbability = 0.9;
  private double evaporation = 0.9;
  private double contiguityMeassureParam = 0.9;
  private double maximumPheromoneValue = 1.0 / 30;
  private double minimumPheromoneValue = maximumPheromoneValue / 5;

  private int numberOfClusters = 3;

  private int numberOfAnts = 10;
  private int maxIterations = 5;

  private String outputDirectory = "/home/carlos/";

  private ProblemConfiguration() {

  }

  /**
   * Returns the value of a specific parameter.
   * 
   * @param parameter Enum corresponding the parameter.
   * @return Parameter value.
   * @throws Exception In case of unsupported parameter.
   */
  public double getParameter(AcoParameter parameter) throws Exception {
    double result = 0.0;
    switch (parameter) {
      case HEURISTIC_IMPORTANCE:
        result = this.getHeuristicImportance();
        break;
      case BEST_CHOICE_PROBABILITY:
        result = this.getBestChoiceProbability();
        break;
      case EVAPORATION:
        result = this.getEvaporation();
        break;
      case CONTIGUITY_MEASSURE_PARAM:
        result = this.getContiguityMeassureParam();
        break;
      case MAXIMUM_PHEROMONE_VALUE:
        result = this.getMaximumPheromoneValue();
        break;
      case NUMBER_OF_CLUSTERS:
        result = this.getNumberOfClusters();
        break;
      case NUMBER_OF_ANTS:
        result = this.getNumberOfAnts();
        break;
      case MAX_ITERATIONS:
        result = this.getMaxIterations();
        break;
      default:
        throw new Exception("Parameter is not supported");
    }
    return result;
  }

  /**
   * Configures an specific parameter.
   * 
   * @param parameter Enum representing the parameter.
   * @param parameterValue Value to set.
   * @throws Exception In case of unsupported parameter.
   */
  public void setParameter(AcoParameter parameter, double parameterValue)
      throws Exception {
    switch (parameter) {
      case HEURISTIC_IMPORTANCE:
        this.setHeuristicImportance(parameterValue);
        break;
      case BEST_CHOICE_PROBABILITY:
        this.setBestChoiceProbability(parameterValue);
        break;
      case EVAPORATION:
        this.setEvaporation(parameterValue);
        break;
      case CONTIGUITY_MEASSURE_PARAM:
        this.setContiguityMeassureParam(parameterValue);
        break;
      case MAXIMUM_PHEROMONE_VALUE:
        this.setMaximumPheromoneValue(parameterValue);
        // ONLY FOR EXPERIMENT PURPOSES
        this.setMinimumPheromoneValue(parameterValue / 5);
        break;
      case NUMBER_OF_CLUSTERS:
        this.setNumberOfClusters((int) parameterValue);
        break;
      case NUMBER_OF_ANTS:
        this.setNumberOfAnts((int) parameterValue);
        break;
      case MAX_ITERATIONS:
        this.setMaxIterations((int) parameterValue);  
        break;
      default:
        throw new Exception("Parameter is not supported");
    }
  }

  public double getHeuristicImportance() {
    return heuristicImportance;
  }

  public void setHeuristicImportance(double heuristicImportance) {
    this.heuristicImportance = heuristicImportance;
  }

  public double getBestChoiceProbability() {
    return bestChoiceProbability;
  }

  public void setBestChoiceProbability(double bestChoiceProbability) {
    this.bestChoiceProbability = bestChoiceProbability;
  }

  public double getEvaporation() {
    return evaporation;
  }

  public double getContiguityMeassureParam() {
    return contiguityMeassureParam;
  }

  public void setContiguityMeassureParam(double contiguityMeassureParam) {
    this.contiguityMeassureParam = contiguityMeassureParam;
  }

  public void setEvaporation(double evaporation) {
    this.evaporation = evaporation;
  }

  public int getNumberOfClusters() {
    return numberOfClusters;
  }

  public void setNumberOfClusters(int numberOfClusters) {
    this.numberOfClusters = numberOfClusters;
  }

  public int getNumberOfAnts() {
    return numberOfAnts;
  }

  public void setNumberOfAnts(int numberOfAnts) {
    this.numberOfAnts = numberOfAnts;
  }

  public double getMaximumPheromoneValue() {
    return maximumPheromoneValue;
  }

  public double getMinimumPheromoneValue() {
    return minimumPheromoneValue;
  }

  public void setMinimumPheromoneValue(double minimumPheromoneValue) {
    this.minimumPheromoneValue = minimumPheromoneValue;
  }

  public void setMaximumPheromoneValue(double maximumPheromoneValue) {
    this.maximumPheromoneValue = maximumPheromoneValue;
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public String getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public void setMaxIterations(int maxIterations) {
    this.maxIterations = maxIterations;
  }

  public static ProblemConfiguration getInstance() {
    return INSTANCE;
  }

  @Override
  public double getEvaporationRatio() {
    return evaporation;
  }

  @Override
  public int getNumberOfIterations() {
    return maxIterations;
  }

  @Override
  public double getInitialPheromoneValue() {
    // I don't think this is used for this implementation.
    return minimumPheromoneValue;
  }

  @Override
  public double getPheromoneImportance() {
    return PHEROMONE_IMPORTANCE;
  }

}
