package pe.edu.pucp.acoseg;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pe.edu.pucp.acoseg.ant.Environment;
import pe.edu.pucp.acoseg.test.TestDataGenerator;

public class ACOImageSegmentationTest {

	@Test
	public void testGenerateSegmentedImage() {
		int[][] segmentedImage = ACOImageSegmentation.generateSegmentedImage(
				TestDataGenerator.getDummyPartition(), new Environment(
						TestDataGenerator.getDummyImageMatrix(),
						TestDataGenerator.CLUSTERS_FOR_TEST));

		for (int i = 0; i < TestDataGenerator.getDummyImageMatrix().length; i++) {
			for (int j = 0; j < TestDataGenerator.getDummyImageMatrix()[0].length; j++) {
				assertEquals("Error in pixel (" + i + ", " + j + ")",
						TestDataGenerator.getDummyImageMatrix()[i][j],
						segmentedImage[i][j]);
			}
		}
	}

}
