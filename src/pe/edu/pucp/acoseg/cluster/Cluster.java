package pe.edu.pucp.acoseg.cluster;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.acoseg.image.ClusteredPixel;

public class Cluster {

	private List<ClusteredPixel> pixels;
	private int clusterNumber;

	public Cluster(int clusterNumber) {
		this.clusterNumber = clusterNumber;
		this.pixels = new ArrayList<ClusteredPixel>();
	}

	public double getClusterMeanValue() {
		double greyScaleSum = 0.0;
		double clusterCounter = pixels.size();
		for (int i = 0; i < pixels.size(); i++) {
			ClusteredPixel clusteredPixel = pixels.get(i);
			greyScaleSum = greyScaleSum + clusteredPixel.getGreyScaleValue();
		}
		return clusterCounter != 0 ? greyScaleSum / clusterCounter : 0;
	}

	public List<ClusteredPixel> getPixels() {
		return pixels;
	}

	public void setPixels(List<ClusteredPixel> pixels) {
		this.pixels = pixels;
	}

	public int getClusterNumber() {
		return clusterNumber;
	}

}
