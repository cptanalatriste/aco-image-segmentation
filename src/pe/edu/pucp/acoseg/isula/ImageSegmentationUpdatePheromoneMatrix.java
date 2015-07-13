package pe.edu.pucp.acoseg.isula;

import isula.aco.Ant;
import isula.aco.Environment;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;
import isula.aco.algorithms.maxmin.UpdatePheromoneMatrixForMaxMin;
import isula.image.util.ClusteredPixel;
import pe.edu.pucp.acoseg.ProblemConfiguration;

public class ImageSegmentationUpdatePheromoneMatrix extends
    UpdatePheromoneMatrixForMaxMin<ClusteredPixel> {

  @Override
  protected double getNewPheromoneValue(Ant<ClusteredPixel> bestAnt,
      int positionInSolution, ClusteredPixel solutionComponent,
      MaxMinConfigurationProvider configurationProvider) {

    Environment environment = getEnvironment();
    double contribution = 1 / bestAnt.getSolutionQuality(environment);
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    double[][] problemGraph = environment.getProblemGraph();

    double newValue = -1;

    // TODO(cgavidia): Another reason to not accept direct manipulation of the
    // matrix
    try {
      if (solutionComponent.getCluster() != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
        newValue = pheromoneMatrix[solutionComponent.getxCoordinate()
            * problemGraph[0].length + solutionComponent.getyCoordinate()][solutionComponent
            .getCluster()] * ProblemConfiguration.EXTRA_WEIGHT + contribution;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return newValue;
  }

}
