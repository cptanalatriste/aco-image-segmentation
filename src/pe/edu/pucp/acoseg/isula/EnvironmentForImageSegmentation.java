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
    this.setPheromoneMatrix(this.createPheromoneMatrixForSegmentation(
        this.numberOfPixels, this.numberOfClusters));
  }

  protected double[][] createPheromoneMatrixForSegmentation(int numberOfPixels,
      int numberOfClusters) {
    return new double[numberOfPixels][numberOfClusters];
  }

  public int getNumberOfClusters() {
    return numberOfClusters;
  }

  public int getNumberOfPixels() {
    return numberOfPixels;
  }

  @Override
  protected double[][] createPheromoneMatrix() {
    // Not needed. The pheromone matrix is created by the
    // createPheromoneMatrixForSegmentation method.
    return null;
  }

  @Override
  public String toString() {
    return super.toString() + "\n Number of pixels: "
        + this.getNumberOfPixels() + "\n Number of Cluters: "
        + this.getNumberOfClusters();
  }

}
