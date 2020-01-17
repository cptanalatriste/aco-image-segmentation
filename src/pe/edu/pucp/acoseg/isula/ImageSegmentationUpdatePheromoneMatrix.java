package pe.edu.pucp.acoseg.isula;

import isula.aco.Ant;
import isula.aco.algorithms.maxmin.MaxMinConfigurationProvider;
import isula.aco.algorithms.maxmin.UpdatePheromoneMatrixForMaxMin;
import isula.image.util.ClusteredPixel;
import pe.edu.pucp.acoseg.ProblemConfiguration;

public class ImageSegmentationUpdatePheromoneMatrix
        extends
        UpdatePheromoneMatrixForMaxMin<ClusteredPixel, EnvironmentForImageSegmentation> {

    @Override
    protected double getNewPheromoneValue(
            Ant<ClusteredPixel, EnvironmentForImageSegmentation> bestAnt,
            int positionInSolution, ClusteredPixel solutionComponent,
            MaxMinConfigurationProvider configurationProvider) {

        EnvironmentForImageSegmentation environment = getEnvironment();
        double contribution = 1 / bestAnt.getSolutionCost(environment);
        double[][] pheromoneMatrix = environment.getPheromoneMatrix();
        double[][] problemGraph = environment.getProblemRepresentation();

        double newValue = -1;

        // TODO(cgavidia): Another reason to not accept direct manipulation of the
        // matrix
        if (solutionComponent.getCluster() != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
            int rowIndex = solutionComponent.getxCoordinate() * problemGraph[0].length + solutionComponent.getyCoordinate();
            int columnIndex = solutionComponent.getCluster();

            newValue = pheromoneMatrix[rowIndex][columnIndex] * ProblemConfiguration.EXTRA_WEIGHT + contribution;
        }


        return newValue;
    }

}
