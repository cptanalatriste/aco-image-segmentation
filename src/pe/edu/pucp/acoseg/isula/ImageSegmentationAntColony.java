package pe.edu.pucp.acoseg.isula;

import isula.aco.Ant;
import isula.aco.AntColony;
import isula.aco.Environment;
import isula.image.util.ClusteredPixel;

//TODO(cgavidia): If we made the Ant class also a parameter?
public class ImageSegmentationAntColony extends AntColony<ClusteredPixel> {

  private int numberOfClusters;

  public ImageSegmentationAntColony(int numberOfAnts, int numberOfClusters) {
    super(numberOfAnts);
    this.numberOfClusters = numberOfClusters;
  }

  @Override
  protected Ant<ClusteredPixel> createAnt(Environment environment) {
    // TODO(cgavidia): This can be also a parameter class.
    EnvironmentForImageSegmentation currentEnvironment = 
        (EnvironmentForImageSegmentation) environment;

    return new AntForImageSegmentation(currentEnvironment, numberOfClusters);
  }

}
