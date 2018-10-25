package com.hw2.sketcher;

public class GameObjectFactory {

	
	public static GameObject format(String gameObjectString) {
		String[] vals = gameObjectString.split("~");
		GameObject object = null;
		if(vals[0].equals("PLAYER")) {
			
		}
		else if(vals[0].equals("PLATFORM")) {
			
		}
		else if(vals[0].equals("FLOOR")) {
			
		}
		
			
		
		
		return object;
	}
	
	
	
}
