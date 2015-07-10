package pe.edu.pucp.acoseg.isula;

import isula.aco.Ant;
import isula.aco.ConfigurationProvider;
import isula.aco.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.pucp.acoseg.ProblemConfiguration;
import pe.edu.pucp.acoseg.cluster.Cluster;
import pe.edu.pucp.acoseg.image.ClusteredPixel;

public class AntForImageSegmentation extends Ant<ClusteredPixel> {

  private int currentXPosition = 0;
  private int currentYPosition = 0;
  private int numberOfClusters;
  private double partitionQuality = -1;

  private Map<Integer, Cluster> clusterMap = new HashMap<Integer, Cluster>();

  /**
   * Creates a new Ant for Image Segmentation.
   * 
   * @param environment
   *          Environment for the ants to traverse.
   * @param numberOfClusters
   *          Number of clusters for the segmentation.
   */
  public AntForImageSegmentation(EnvironmentForImageSegmentation environment,
      int numberOfClusters) {
    this.setSolution(new ClusteredPixel[environment.getNumberOfPixels()]);
    this.numberOfClusters = numberOfClusters;

    for (int i = -1; i < numberOfClusters; i++) {
      clusterMap.put(i, new Cluster(i));
    }
  }

  @Override
  public void visitNode(ClusteredPixel visitedNode) {
    super.visitNode(visitedNode);
    clusterMap.get(visitedNode.getCluster()).getPixels().add(visitedNode);
  }

  @Override
  public void clear() {
    super.clear();

    currentXPosition = 0;
    currentYPosition = 0;

    for (int i = 0; i < numberOfClusters; i++) {
      clusterMap.get(i).setPixels(new ArrayList<ClusteredPixel>());
    }
  }

  @Override
  public void selectNextNode(Environment environment,
      ConfigurationProvider configurationProvider) {
    double[][] problemGraph = environment.getProblemGraph();

    if (problemGraph[currentXPosition][currentYPosition] != ProblemConfiguration.ABSENT_PIXEL_FLAG) {
      super.selectNextNode(environment, configurationProvider);
    } else {
      this.visitNode(new ClusteredPixel(currentXPosition, currentYPosition,
          problemGraph, ProblemConfiguration.ABSENT_PIXEL_CLUSTER));
    }

    currentYPosition += 1;
    if (currentYPosition == problemGraph[0].length) {
      currentYPosition = 0;
      currentXPosition += 1;
    }
  }

  @Override
  public boolean isSolutionReady(Environment environment) {
    // TODO(cgavidia): This can be a parametrized class
    EnvironmentForImageSegmentation env = (EnvironmentForImageSegmentation) environment;

    return getCurrentIndex() == env.getNumberOfPixels();
  }

  @Override
  public Double getHeuristicValue(ClusteredPixel solutionComponent,
      Integer positionInSolution, Environment environment) {

    double[][] problemGraph = environment.getProblemGraph();

    double euclideanDistance = Math.abs(solutionComponent.getGreyScaleValue()
        - getClusterMeanValue(solutionComponent.getCluster()));
    // Minor change, let's see how it goes
    double contiguityMeasure = getContiguityMeasure(solutionComponent,
        problemGraph);
    return euclideanDistance
        + ProblemConfiguration.getInstance().getContiguityMeassureParam()
        * contiguityMeasure;
  }

  private double getContiguityMeasure(ClusteredPixel solutionComponent,
      double[][] problemGraph) {
    // TODO(cgavidia): Contiguity measure taken from: A contiguity-enhanced
    // k-means clustering algorithm for unsupervised multispectral image
    // segmentation

    double neighboursWithDifferentClass = 0;

    List<ClusteredPixel> neighbours = solutionComponent.getNeighbourhood(
        getSolution(), problemGraph);
    for (ClusteredPixel neighbour : neighbours) {
      if (neighbour.getCluster() != solutionComponent.getCluster()) {
        neighboursWithDifferentClass++;
      }
    }
    return neighboursWithDifferentClass;
  }

  public double getClusterMeanValue(int cluster) {
    return clusterMap.get(cluster).getClusterMeanValue();
  }

  @Override
  public List<ClusteredPixel> getNeighbourhood(Environment environment) {
    List<ClusteredPixel> neighbourhood = new ArrayList<ClusteredPixel>();

    for (int cluster = 0; cluster < numberOfClusters; cluster++) {
      ClusteredPixel currentPixel = new ClusteredPixel(currentXPosition,
          currentYPosition, environment.getProblemGraph(), cluster);
      neighbourhood.add(currentPixel);
    }

    return neighbourhood;
  }

  @Override
  public Double getPheromoneTrailValue(ClusteredPixel solutionComponent,
      Integer positionInSolution, Environment environment) {
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    double[][] problemGraph = environment.getProblemGraph();

    double pheromoneTrailValue = pheromoneMatrix[solutionComponent
        .getxCoordinate()
        * problemGraph[0].length
        + solutionComponent.getyCoordinate()][solutionComponent.getCluster()]
        + ProblemConfiguration.DELTA;
    return pheromoneTrailValue;
  }

  @Override
  public void setPheromoneTrailValue(ClusteredPixel solutionComponent,
      Environment environment, Double value) {
    double[][] pheromoneMatrix = environment.getPheromoneMatrix();
    double[][] problemGraph = environment.getProblemGraph();

    if (solutionComponent.getCluster() != ProblemConfiguration.ABSENT_PIXEL_CLUSTER) {
      pheromoneMatrix[solutionComponent.getxCoordinate()
          * problemGraph[0].length + solutionComponent.getyCoordinate()][solutionComponent
          .getCluster()] = value;

    }
  }

  @Override
  public double getSolutionQuality(Environment environment) {

    if (partitionQuality < 0) {
      partitionQuality = 0.0;
      // TODO(cgavidia): This MUST be optimized. The Cluster clas would
      // be a great help
      for (int cluster = 0; cluster < numberOfClusters; cluster++) {
        List<ClusteredPixel> pixelsFromCluster = clusterMap.get(cluster)
            .getPixels();
        int clusterCounter = pixelsFromCluster.size();
        double clusterQuality = 0;
        for (int pixel = 0; pixel < pixelsFromCluster.size(); pixel++) {
          clusterQuality += getHeuristicValue(pixelsFromCluster.get(pixel), -1,
              environment);
        }
        partitionQuality += clusterCounter != 0 ? clusterQuality
            / clusterCounter : 0;
      }
    }
    return partitionQuality;
  }

}
