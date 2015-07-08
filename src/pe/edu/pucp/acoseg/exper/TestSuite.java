package pe.edu.pucp.acoseg.exper;

import java.util.HashMap;

import pe.edu.pucp.acoseg.ProblemConfiguration;

public class TestSuite {

	private static final String WHITE_KEY = "WHITE";
	private static final String GREY_KEY = "GREY";
	private static final String CSF_KEY = "CSF";

	private HashMap<String, ImageComparator> comparisonMap = new HashMap<String, ImageComparator>();

	public TestSuite() {
		comparisonMap.put(CSF_KEY, new ImageComparator(CSF_KEY,
				ProblemConfiguration.INPUT_DIRECTORY
						+ "csf_21130transverse1_64.gif",
				ProblemConfiguration.GRAYSCALE_POSITIVE_THRESHOLD));
		comparisonMap.put(GREY_KEY, new ImageComparator("Grey Matter",
				ProblemConfiguration.INPUT_DIRECTORY
						+ "grey_20342transverse1_64.gif",
				ProblemConfiguration.GRAYSCALE_POSITIVE_THRESHOLD));
		comparisonMap.put(WHITE_KEY, new ImageComparator("White Matter",
				ProblemConfiguration.INPUT_DIRECTORY
						+ "white_20358transverse1_64.gif",
				ProblemConfiguration.GRAYSCALE_POSITIVE_THRESHOLD));
	}

	public void executeReport() throws Exception {

		// TODO(cgavidia): It would be good to evaluate the behaviuor with
		// noise.
		System.out.println("\n\nEXPERIMENT EXECUTION REPORT");
		System.out.println("===============================");

		for (ImageComparator comparator : comparisonMap.values()) {
			double maximumJCI = 0;
			String maximumJCIClusterFile = "";
			for (int i = 0; i < ProblemConfiguration.getInstance()
					.getNumberOfClusters(); i++) {
				String currentFile = ProblemConfiguration.getInstance()
						.getOutputDirectory()
						+ i
						+ "_"
						+ ProblemConfiguration.CLUSTER_IMAGE_FILE;
				comparator.setImageToValidateFile(ProblemConfiguration
						.getInstance().getOutputDirectory()
						+ i
						+ "_"
						+ ProblemConfiguration.CLUSTER_IMAGE_FILE);
				comparator.executeComparison();
				if (comparator.getJaccardSimilarityIndex() > maximumJCI) {
					maximumJCI = comparator.getJaccardSimilarityIndex();
					maximumJCIClusterFile = currentFile;
				}

			}
			comparator.setImageToValidateFile(maximumJCIClusterFile);
		}

		System.out.println(ProblemConfiguration.getInstance()
				.currentConfigurationAsString());

		for (ImageComparator comparator : comparisonMap.values()) {
			comparator.executeComparison();
			System.out.println(comparator.resultAsString());
		}

	}

	public double getJCIForCSF() {
		return comparisonMap.get(CSF_KEY).getJaccardSimilarityIndex();
	}

	public double getJCIForGreyMatter() {
		return comparisonMap.get(GREY_KEY).getJaccardSimilarityIndex();
	}

	public double getJCIForWhiteMatter() {
		return comparisonMap.get(WHITE_KEY).getJaccardSimilarityIndex();
	}
}
