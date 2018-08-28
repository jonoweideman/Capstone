package aim4.map;

public class PedestrianLight {
	
	PedestrianSpawnPoint psp1;
	PedestrianSpawnPoint psp2;
	boolean green;
	
	public PedestrianLight(PedestrianSpawnPoint psp1, PedestrianSpawnPoint psp2, boolean green) {
		this.psp1 = psp1;
		this.psp2 = psp2;
		this.green = green;
	}
}