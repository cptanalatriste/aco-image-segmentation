package pe.edu.pucp.acoseg.exper;

import isula.image.util.ImageComparator;

import pe.edu.pucp.acoseg.ProblemConfiguration;

import java.util.HashMap;
import java.util.logging.Logger;

public class TestSuiteForImageSegmentation {

    private static Logger logger = Logger
            .getLogger(TestSuiteForImageSegmentation.class.getName());

    private static final String WHITE_KEY = "WHITE";
    private static final String GREY_KEY = "GREY";
    private static final String CSF_KEY = "CSF";

    private HashMap<String, ImageComparator> comparisonMap = new HashMap<String, ImageComparator>();

    /**
     * Generates a new Test Suite.
     */
    public TestSuiteForImageSegmentation() {
        comparisonMap.put(CSF_KEY, new ImageComparator(CSF_KEY,
                ProblemConfiguration.INPUT_DIRECTORY + "csf_21130transverse1_64.gif",
                ProblemConfiguration.GRAYSCALE_POSITIVE_THRESHOLD));
        comparisonMap.put(GREY_KEY, new ImageComparator("Grey Matter",
                ProblemConfiguration.INPUT_DIRECTORY + "grey_20342transverse1_64.gif",
                ProblemConfiguration.GRAYSCALE_POSITIVE_THRESHOLD));
        comparisonMap.put(WHITE_KEY, new ImageComparator("White Matter",
                ProblemConfiguration.INPUT_DIRECTORY + "white_20358transverse1_64.gif",
                ProblemConfiguration.GRAYSCALE_POSITIVE_THRESHOLD));
    }

    /**
     * Executes the test suite and produces the report.
     *
     * @throws Exception In case of error.
     */
    public void executeReport() throws Exception {

        // TODO(cgavidia): It would be good to evaluate the behaviuor with
        // noise.
        logger.info("\n\nEXPERIMENT EXECUTION REPORT");

        for (ImageComparator comparator : comparisonMap.values()) {
            double maximumJci = 0;
            String maximumJciClusterFile = "";
            for (int i = 0; i < ProblemConfiguration.getInstance()
                    .getNumberOfClusters(); i++) {
                String currentFile = ProblemConfiguration.getInstance()
                        .getOutputDirectory()
                        + i
                        + "_"
                        + ProblemConfiguration.CLUSTER_IMAGE_FILE;

                String imageToValidate = ProblemConfiguration.getInstance()
                        .getOutputDirectory()
                        + i
                        + "_"
                        + ProblemConfiguration.CLUSTER_IMAGE_FILE;
                comparator.setImageToValidateFile(imageToValidate);

                logger.info("Comparing files. Reference image " + comparator.getReferenceImageFile() +
                        " against " + imageToValidate);
                comparator.executeComparison();
                if (comparator.getJaccardSimilarityIndex() > maximumJci) {
                    maximumJci = comparator.getJaccardSimilarityIndex();
                    maximumJciClusterFile = currentFile;
                }

            }
            comparator.setImageToValidateFile(maximumJciClusterFile);
        }

        logger.info(ProblemConfiguration.getInstance()
                .currentConfigurationAsString());

        for (ImageComparator comparator : comparisonMap.values()) {
            comparator.executeComparison();
            System.out.println(comparator.resultAsString());
        }

    }

    public double getJciForCsf() {
        return comparisonMap.get(CSF_KEY).getJaccardSimilarityIndex();
    }

    public double getJciForGreyMatter() {
        return comparisonMap.get(GREY_KEY).getJaccardSimilarityIndex();
    }

    public double getJciForWhiteMatter() {
        return comparisonMap.get(WHITE_KEY).getJaccardSimilarityIndex();
    }
}
