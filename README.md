# aco-image-segmentation
A Java Program to identify segments on an image using an Ant Colony optimization algorithm. It is a two-phase procedure:

* First, the cerebrum is extrated using also an Ant Colony algorithm., as described here: https://github.com/cptanalatriste/aco-image-thresholding
* Then, a clustering procedure is applied to the cerebrum.


The Ant-Colony Algorithm
------------------------
The Ant Colony algorithm used to extract segments of the image is based on Max-Min Ant System, as was proposed by Salima Ouadfel in the paper "Unsupervised image segmentation using a colony of cooperating ants".  To implement it, we used the Isula Framework:

```java
    ConfigurationProvider configurationProvider = ProblemConfiguration
        .getInstance();
    EnvironmentForImageSegmentation environment = new EnvironmentForImageSegmentation(
        imageGraph, ProblemConfiguration.getInstance().getNumberOfClusters());

    ImageSegmentationAntColony antColony = new ImageSegmentationAntColony(
        configurationProvider.getNumberOfAnts(),
        environment.getNumberOfClusters());
    antColony.buildColony(environment);

    problemSolver = new AcoProblemSolver<ClusteredPixel>();

    problemSolver.setConfigurationProvider(configurationProvider);
    problemSolver.setEnvironment(environment);
    problemSolver.setAntColony(antColony);

    problemSolver.addDaemonActions(
        new StartPheromoneMatrixForMaxMin<ClusteredPixel>(),
        new ImageSegmentationUpdatePheromoneMatrix());
    antColony.addAntPolicies(new ImageSegmentationNodeSelection());

    problemSolver.solveProblem();
    ClusteredPixel[] bestPartition = problemSolver.getBestSolution();
```
