package pe.edu.pucp.acoseg.image;

public class ImageSegment {
	int yLeftBoundary = -1;
	int yRightBoundary = -1;
	int xTopBoundary = -1;
	int xBottomBoundary = -1;

	public ImageSegment(int yLeftBoundary, int yRightBoundary,
			int xTopBoundary, int xBottomBoundary) {
		super();
		this.yLeftBoundary = yLeftBoundary;
		this.yRightBoundary = yRightBoundary;
		this.xTopBoundary = xTopBoundary;
		this.xBottomBoundary = xBottomBoundary;
	}

	public int getyLeftBoundary() {
		return yLeftBoundary;
	}

	public int getyRightBoundary() {
		return yRightBoundary;
	}

	public int getxTopBoundary() {
		return xTopBoundary;
	}

	public int getxBottomBoundary() {
		return xBottomBoundary;
	}

}
