package pe.edu.pucp.acoseg;

public class ProblemConfiguration {
  // TODO(cgavidia) This need huge refactoring

  private static final ProblemConfiguration INSTANCE = new ProblemConfiguration();

  // Credits:
  // https://www.eecs.berkeley.edu/Research/Projects/CS/vision/bsds/BSDS300/html/dataset/images/gray/296059.html
  // http://brainweb.bic.mni.mcgill.ca/brainweb/anatomic_normal_20.html
  public static final String INPUT_DIRECTORY = "C:/Users/V144615/Documents/GitHub/ACOImageSegmentationWithIsula/inputImg/";

  public static final String IMAGE_FILE = "19952transverse2_64.gif";
  public static final String OUTPUT_IMAGE_FILE = IMAGE_FILE.substring(0,
      IMAGE_FILE.lastIndexOf('.')) + "_output.bmp";
  public static final String PHEROMONE_IMAGE_FILE = "pheromone.bmp";
  public static final String ORIGINAL_IMAGE_FILE = IMAGE_FILE.substring(0,
      IMAGE_FILE.lastIndexOf('.')) + "_original.bmp";
  public static final String FILTERED_IMAGE_FILE = IMAGE_FILE.substring(0,
      IMAGE_FILE.lastIndexOf('.')) + "_filtered.bmp";
  public static final String CLUSTER_IMAGE_FILE = IMAGE_FILE.substring(0,
      IMAGE_FILE.lastIndexOf('.')) + "_cluster.bmp";

  // This features were disabled as it affects the quality of solutuion.
  public static final boolean ONLY_BEST_CAN_UPDATE = true;
  public static boolean MMAS_PHEROMONE_UPDATE = true;
  public static final boolean ALLOW_VISITED_PIXELS = false;
  public static final boolean DEPOSITE_PHEROMONE_ONLINE = false;
  public static final boolean RANDOMIZE_BEFORE_BUILD = false;

  // Max-Min Ant System Pheromone parameters
  // TODO(cgavidia): This will be calibrated later

  // This are values from the original paper
  // TODO(cgavidia): Calibrate later
  public static final int NUMBER_OF_STEPS = 15;
  public static final int PHEROMONE_IMPORTANCE = 1;

  public static final double EXTRA_WEIGHT = 0.6;

  public static final int COST_FUNCTION_PARAMETER_A = 5000;
  public static final int COST_FUNCTION_PARAMETER_B = 10;

  public static final double INITIAL_PHEROMONE_VALUE = Float.MIN_VALUE;

  public static final boolean USE_PHEROMONE_FOR_CLUSTERING = true;
  public static final boolean USE_GREYSCALE_FOR_CLUSTERING = true;

  public static final double DELTA = Float.MIN_VALUE;

  public static final int GRAYSCALE_MIN_RANGE = 0;
  public static final int GRAYSCALE_MAX_RANGE = 255;
  public static final int GRAYSCALE_POSITIVE_THRESHOLD = 120;

  public static final int ABSENT_PIXEL_FLAG = -1;
  public static final int ABSENT_PIXEL_CLUSTER = -1;

  public static final int GRAYSCALE_DELTA = 10;

  public String currentConfigurationAsString() {
    String result = " Input file: " + IMAGE_FILE + "\n";
    result = result + " Evaporation parameter: " + evaporation + "\n";
    result = result + " Number of steps: " + NUMBER_OF_STEPS + "\n";
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
    result = result + " Number of Clústers: " + numberOfClusters + "\n";

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

  private String outputDirectory = "C:/Users/V144615/Documents/GitHub/ACOImageSegmentationWithIsula/outputImg/";

  private ProblemConfiguration() {

  }

  public double getParameter(ACOParameter parameter) throws Exception {
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

  public void setParameter(ACOParameter parameter, double parameterValue)
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

}
