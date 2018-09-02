package aim4.map;

import java.awt.geom.Point2D;

public class PedestrianSpawnPoint {
	
	// Location of the spawning point
	private Point2D location;
	// Destination of this spawning point
	private PedestrianSpawnPoint destinationSpawnPoint;
	private String crosswalk;
	
	public PedestrianSpawnPoint(Point2D location, PedestrianSpawnPoint destinationSpawnPoint) {
		this.location = location;
		this.destinationSpawnPoint = destinationSpawnPoint;
	}
	
	public Point2D getLocation() {
		return location;
	}
	
	public Point2D getDestination() {
		return location;
	}
	
}
