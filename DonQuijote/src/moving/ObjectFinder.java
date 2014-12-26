package moving;

import ai.Memory;
import sensors.IRSample;
import sensors.IRSensor;

public class ObjectFinder {
	
	private Moving move;
	private Memory memory;
	private int currentRotation;
	private IRSensor iR;
	private IRSample nearestTarget;
	
	public ObjectFinder(Moving move, Memory memory) {
		this.move = move;
		this.memory = memory;
		currentRotation = 0;
		iR = new IRSensor();
	}
	
	public int findNearestObject() {
		if (nearestTarget == null) {
			resetNearest();
			rotateToFindNearest();
			memory.analyzeData();
		}
		rotateTowardsNearest();
		return nearestTarget.getNearestDist();
	}
	
	private void resetNearest() {
		memory.resetMemory();
	}
	
	private void rotateToFindNearest() {
		for	(int i = 0; i < 360 / 5; i++) {
			move.rotateLeft(5);
			addSampleToMemory(iR.measureDistance(), i);
		}
	}
	
	private void addSampleToMemory(int distance, int i) {
		memory.addValues(distance, i);
	}
	
	private void rotateTowardsNearest() {
		nearestTarget = memory.getDirection();
		int rotation = nearestTarget.getNearestDegree() - currentRotation;
		if (rotation > 0 && rotation < 180) {
			move.rotateLeft(rotation);
		} else if (rotation > 0){
			move.rotateRight(360 - rotation);
		} else {
			move.rotateRight(rotation);
		}
	}

}
