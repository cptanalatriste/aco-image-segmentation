package pe.edu.pucp.acoseg.exper;

import pe.edu.pucp.acoseg.AcoImageSegmentation;
import pe.edu.pucp.acoseg.AcoParameter;
import pe.edu.pucp.acoseg.ProblemConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class ExperimentDriver {
  
  private static Logger logger = Logger.getLogger(ExperimentDriver.class.getName());

  /**
   * Starts the execution of parameter experimentation.
   * 
   * @param args Program arguments.
   */
  public static void main(String[] args) {
    logger.info("ACO FOR IMAGE SEGMENTATION");

    try {
      // executeExperiment(ACOParameter.BEST_CHOICE_PROBABILITY, 1, -0.1,
      // 5);
      // executeExperiment(ACOParameter.EVAPORATION, 1, -0.1, 5);
      // executeExperiment(ACOParameter.CONTIGUITY_MEASSURE_PARAM, 1,
      // -0.1,
      // 5);
      // executeExperiment(ACOParameter.MAXIMUM_PHEROMONE_VALUE,
      // 1.0 / 30 + 0.005, -0.005, 5);
      // executeExperiment(ACOParameter.HEURISTIC_IMPORTANCE, 0, -0.5, 5);

      executeExperiment(AcoParameter.NUMBER_OF_CLUSTERS, 14, -2, 5);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Performs experimentation on an specific parameter.
   * 
   * @param acoParameter Parameter to modify in the experiment.
   * @param seedValue Initial value.
   * @param step Quantity to increase.
   * @param runs Number of increases.
   * @throws IOException In case of file reading issues.
   * @throws Exception Other errors.
   */
  public static void executeExperiment(AcoParameter acoParameter,
      double seedValue, double step, int runs) throws IOException, Exception {
    ProblemConfiguration configuration = ProblemConfiguration.getInstance();
    PrintWriter printWriter = new PrintWriter(acoParameter.getName()
        + "Experiments.txt");
    String currentOutputDirectory = configuration.getOutputDirectory();
    logger.info("INITIAL SETTINGS");
    printWriter.println(configuration.currentConfigurationAsString());
    printWriter.println("\n***Experimental Data****\n");
    configuration.setParameter(acoParameter, seedValue);
    for (int i = 0; i < runs; i++) {
      double currentParameterValue = configuration.getParameter(acoParameter);
      configuration.setParameter(acoParameter, currentParameterValue + step);
      String outputDirectoryForExperiment = currentOutputDirectory
          + acoParameter.getName() + "_"
          + configuration.getParameter(acoParameter) + "/";
      configuration.setOutputDirectory(outputDirectoryForExperiment);
      File newDirectory = new File(outputDirectoryForExperiment);
      if (!newDirectory.exists()) {
        newDirectory.mkdir();
      }

      AcoImageSegmentation acoImageSegmentation = AcoImageSegmentation
          .performSegmentation();
      TestSuiteForImageSegmentation testSuite = new TestSuiteForImageSegmentation();
      testSuite.executeReport();
      String reportLine = " ****" + acoParameter.getName() + ": "
          + configuration.getParameter(acoParameter) + ", Execution time:"
          + ", White JCI: " + testSuite.getJciForWhiteMatter() + ", Grey JCI: "
          + testSuite.getJciForGreyMatter() + ", CLF JCI: "
          + testSuite.getJciForCsf() + ", Partition Quality: "
          + acoImageSegmentation.getProblemSolver().getBestSolutionAsString()
          + "****";
      logger.info(reportLine);
      printWriter.println(reportLine);
    }
    printWriter.close();
  }
}
