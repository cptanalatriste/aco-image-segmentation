package pe.edu.pucp.acoseg.isula;

import isula.aco.Ant;
import isula.aco.AntColony;
import isula.image.util.ClusteredPixel;

//TODO(cgavidia): If we made the Ant class also a parameter?
public class ImageSegmentationAntColony extends
    AntColony<ClusteredPixel, EnvironmentForImageSegmentation> {

  private int numberOfClusters;

  public ImageSegmentationAntColony(int numberOfAnts, int numberOfClusters) {
    super(numberOfAnts);
    this.numberOfClusters = numberOfClusters;
  }

  @Override
  protected Ant<ClusteredPixel, EnvironmentForImageSegmentation> createAnt(
      EnvironmentForImageSegmentation currentEnvironment) {
    return new AntForImageSegmentation(currentEnvironment, numberOfClusters);
  }

}
