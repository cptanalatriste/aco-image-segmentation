# aco-image-segmentation
A Java Program to identify segments on an image using an Ant Colony optimization algorithm. It is a two-phase procedure:

* First, the cerebrum is extracted using also an Ant Colony algorithm, as described here: https://github.com/cptanalatriste/aco-image-thresholding
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
The implemented process has the following characteristics:
* The components of the solution are Clustered Pixels, that is, each pixel of the image will be assigned to one of three clusters.
* The Ant class avaible in Isula was extended to support this image clustering class. The ants for this problems have memory of the current position in the image, as well as a Map for Cluster storage. Since the calculation of the Solution Quality is expensive, it was also added as an instance variable.
* These Ants  are built so they only consider Cerebrum pixels. That information was provided but the previous binary thresholding algorithm.
* The Max-Min Ant System policies available in Isula were reused with minor customization. This includes pheromone update and node selection.

The results 
-----------
We use an MR Brain image as an input. This was provided by the BrainWeb Database: http://brainweb.bic.mni.mcgill.ca/brainweb/

![Original Image](https://github.com/cptanalatriste/aco-image-segmentation/blob/master/src/inputImg/19952transverse2_64.gif?raw=true)

The program extracts three segments from the image: Brain Matter, White Matter and CLF. Here's the segment corresponding to the white matter:

![Segments](https://raw.githubusercontent.com/cptanalatriste/aco-image-segmentation/master/outputImg/2_19952transverse2_64_cluster.bmp)

How to use this code
--------------------
The code uploaded to this GitHub Repository corresponds to a Java Project developed on the Eclipse IDE. You should be able to import it as an existing project to your current workspace.

**This project depends on the Isula Framework**.  You need to download first the Isula Framework Project available on this Github Repository: https://github.com/cptanalatriste/isula

This project also depends on an ACO-based procedure for image thresholding. The `aco-image-thresholding` project that contains it is available on this Github Repository: https://github.com/cptanalatriste/aco-image-thresholding 

Keep in mind that several file and folder locations were configured on the `ProblemConfiguration.java` file. You need to set values according to your environment in order to avoid a `FileNotFoundException`. 

More about Isula
----------------
Visit the Isula Framework site: http://cptanalatriste.github.io/isula/

Review the Isula JavaDoc: http://cptanalatriste.github.io/isula/doc/

Questions, issues or support?
----------------------------
Feel free to contact me at carlos.gavidia@pucp.edu.pe.
