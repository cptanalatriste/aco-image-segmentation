package pe.edu.pucp.acoseg.isula;

import isula.aco.ConfigurationProvider;
import isula.aco.Environment;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;

import java.util.List;
import java.util.Random;

import pe.edu.pucp.acoseg.image.ClusteredPixel;

public class ImageSegmentationNodeSelection extends
    PseudoRandomNodeSelection<ClusteredPixel> {

  @Override
  protected void doIfNoNodeWasSelected(Environment environment,
      ConfigurationProvider configuration) {

    List<ClusteredPixel> neighbours = getAnt().getNeighbourhood(environment);
    ClusteredPixel imagePixel = neighbours.get(new Random().nextInt(neighbours
        .size()));

    getAnt().visitNode(imagePixel);
  }

}
