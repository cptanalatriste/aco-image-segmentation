package pe.edu.pucp.acoseg.isula;

import isula.aco.ConfigurationProvider;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.image.util.ClusteredPixel;

import java.util.List;
import java.util.Random;

public class ImageSegmentationNodeSelection extends
    PseudoRandomNodeSelection<ClusteredPixel, EnvironmentForImageSegmentation> {

  @Override
  protected boolean doIfNoNodeWasSelected(
      EnvironmentForImageSegmentation environment,
      ConfigurationProvider configuration) {

    List<ClusteredPixel> neighbours = getAnt().getNeighbourhood(environment);
    ClusteredPixel imagePixel = neighbours.get(new Random().nextInt(neighbours
        .size()));

    getAnt().visitNode(imagePixel);
    return true;
  }

}
