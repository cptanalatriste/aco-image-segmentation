package pe.edu.pucp.acoseg.isula;

import isula.aco.Environment;
import isula.aco.exception.InvalidInputException;

public class EnvironmentForImageSegmentation extends Environment {

  private int numberOfClusters;
  private int numberOfPixels;

  /**
   * Generates a new environment for problem execution.
   * 
   * @param problemGraph
   *          The image to be segmented.
   * @param numberOfClusters
   *          Clusters to consider.
   * @throws InvalidInputException
   *           In case configuration is invalid.
   */
  public EnvironmentForImageSegmentation(double[][] problemGraph,
      int numberOfClusters) throws InvalidInputException {
    super(problemGraph);
    this.numberOfClusters = numberOfClusters;
    this.numberOfPixels = problemGraph.length * problemGraph[0].length;

    // TODO(cgavidia): Ugly hack. We need to fix that.
    this.setPheromoneMatrix(this.createPheromoneMatrix());
  }

  @Override
  protected double[][] createPheromoneMatrix() {
    return new double[numberOfPixels][numberOfClusters];
  }

  public int getNumberOfClusters() {
    return numberOfClusters;
  }

  public int getNumberOfPixels() {
    return numberOfPixels;
  }

}
