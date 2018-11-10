package EventManager.types;

import com.hw3.sketcher.GameObject;

import EventManager.Event;

public class CharacterCollisionEvent extends Event {
	
	// This Event can have two intrested objects
	// One is the Game objects that collides
	// Other one is the Game object that is collided with
	GameObject collider;
	GameObject collided;
	
	public CharacterCollisionEvent(GameObject collider, GameObject collided) {
		// TODO Auto-generated constructor stub
		super(Event.Type.CHARACTER_COLLSION);
		this.collider = collider;
		this.collided = collided;
	}

}
