package pe.edu.pucp.acoseg.exper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import pe.edu.pucp.acoseg.ACOImageSegmentation;
import pe.edu.pucp.acoseg.ACOParameter;
import pe.edu.pucp.acoseg.ProblemConfiguration;

public class ExperimentDriver {

	public static void main(String[] args) {
		System.out.println("ACO FOR IMAGE SEGMENTATION");
		System.out.println("=============================");

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

			executeExperiment(ACOParameter.NUMBER_OF_CLUSTERS, 14, -2, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void executeExperiment(ACOParameter acoParameter,
			double seedValue, double step, int runs) throws IOException,
			Exception {
		ProblemConfiguration configuration = ProblemConfiguration.getInstance();
		PrintWriter printWriter = new PrintWriter(acoParameter.getName()
				+ "Experiments.txt");
		String currentOutputDirectory = configuration.getOutputDirectory();
		printWriter.println("INITIAL SETTINGS");
		printWriter.println(configuration.currentConfigurationAsString());
		printWriter.println("\n***Experimental Data****\n");
		configuration.setParameter(acoParameter, seedValue);
		for (int i = 0; i < runs; i++) {
			double currentParameterValue = configuration
					.getParameter(acoParameter);
			configuration.setParameter(acoParameter, currentParameterValue
					+ step);
			String outputDirectoryForExperiment = currentOutputDirectory
					+ acoParameter.getName() + "_"
					+ configuration.getParameter(acoParameter) + "/";
			configuration.setOutputDirectory(outputDirectoryForExperiment);
			File newDirectory = new File(outputDirectoryForExperiment);
			if (!newDirectory.exists()) {
				newDirectory.mkdir();
			}

			ACOImageSegmentation acoImageSegmentation = ACOImageSegmentation
					.performSegmentation();
			TestSuite testSuite = new TestSuite();
			testSuite.executeReport();
			String reportLine = " ****"
					+ acoParameter.getName()
					+ ": "
					+ configuration.getParameter(acoParameter)
					+ ", Execution time:"
					+ acoImageSegmentation.getComputationTime()
					+ ", White JCI: "
					+ testSuite.getJCIForWhiteMatter()
					+ ", Grey JCI: "
					+ testSuite.getJCIForGreyMatter()
					+ ", CLF JCI: "
					+ testSuite.getJCIForCSF()
					+ ", Partition Quality: "
					+ acoImageSegmentation.getAntColony()
							.getBestPartitionQuality() + "****";
			System.out.print(reportLine);
			printWriter.println(reportLine);
		}
		printWriter.close();
	}
}
