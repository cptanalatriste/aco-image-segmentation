package pe.edu.pucp.acoseg;

import static org.junit.Assert.assertEquals;

import isula.aco.exception.InvalidInputException;

import org.junit.Test;

import pe.edu.pucp.acoseg.isula.EnvironmentForImageSegmentation;
import pe.edu.pucp.acoseg.test.TestDataGenerator;



public class AcoImageSegmentationTest {

  @Test
  public void testGenerateSegmentedImage() throws InvalidInputException {
    int[][] segmentedImage = AcoImageSegmentation.generateSegmentedImage(
        TestDataGenerator.getDummyPartition(),
        new EnvironmentForImageSegmentation(TestDataGenerator
            .getDummyImageMatrix(), TestDataGenerator.CLUSTERS_FOR_TEST));

    for (int i = 0; i < TestDataGenerator.getDummyImageMatrix().length; i++) {
      for (int j = 0; j < TestDataGenerator.getDummyImageMatrix()[0].length; j++) {
        assertEquals("Error in pixel (" + i + ", " + j + ")",
            (int) TestDataGenerator.getDummyImageMatrix()[i][j],
            (int) segmentedImage[i][j]);
      }
    }
  }

}
