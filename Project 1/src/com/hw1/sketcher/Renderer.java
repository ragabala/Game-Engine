package com.hw1.sketcher;
import processing.core.PApplet;


/**
 * @author ragbalak
 * The Renderer is the main game loop that renders other game objects,
 * also responsible for the event handling and what happens to each game 
 * objects when such events occur.
 */
public class Renderer extends PApplet {

	Shape[] shapes;
	int stepRight,stepLeft,stepUp;

	@Override
	public void settings() {
		fullScreen();
	}

	@Override
	public void setup() {
		background(100);
		shapes = new Shape[2];
		// Design Logic : Shape[0] is the player - Dynamic
		// Shape[1..n] : Obstacles to the player
		shapes[0] = new Square(this, 80, new Color(0, 255, 255));
		shapes[0].render();
		
		shapes[1] = new Rectangle(this, 100, 80,new Color(0, 255, 0));
		shapes[1].render();
		
		stepRight=1;
		stepLeft=-1;
		stepUp=-1;
	}

	@Override
	public void draw(){
		background(100);
		setCommandManual();
		for (Shape shape : shapes) {
			shape.step();
			shape.render();
		}
		
		// logic for all shape collisions happens here
		// happens at O(n^2)
		
		detechCollision(shapes[0], shapes[1]);
		
	}

	/**
	 *  This method sets the text for helping the users know
	 *  what keypresses are available in the system for
	 *  interaction.
	 */
	public void setCommandManual() {
		fill(255,255,0);
		textSize(30);
		text("Left (->) | Right (<-) | Jump (Space)", width/2 - 200, height /2 + 200);
	}
	
	/**
	 * @param shape1
	 * @param shape2
	 * 
	 * Checks whether a collision has occured between the two Game objects that are passed.
	 */
	public void detechCollision(Shape shape1, Shape shape2) {		
		float x_dist = Math.abs(shape1.x - shape2.x);
		float y_dist = Math.abs(shape1.y - shape2.y);
		
		if(x_dist + 0.5 < (shape1.side_a + shape2.side_a)/2 &&
		   y_dist + 0.5 < (shape1.side_b + shape2.side_b)/2 ){

			textSize(30);
			fill(255, 0, 0);
			text("PRESS ENTER to RESTART!!", width/2 - 200, height /2 - 200);
			//stop execution     
			shape1.stop();shape2.stop();
		}
		
		
	}
	
	@Override
	public void keyPressed() {
		for (Shape shape : shapes) {
			
			switch (keyCode) {
			case RIGHT:
				shape.step(stepRight, 0);
				break;
			case LEFT:
				shape.step(stepLeft, 0);
				break;	
			case 10:
				shape.reset();
				break;
			case 32:
				shape.step(0,stepUp);
			default:
				break;
			}

		}

	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Renderer mySketch = new Renderer();
		PApplet.runSketch(processingArgs, mySketch);
	} 
}
