package com.ragabala.sketcher;
import processing.core.PApplet;

public class Renderer extends PApplet {

	Shape[] shapes;
	int stepRight,stepLeft;

	@Override
	public void settings() {
		size(1024, 1024);
	}

	@Override
	public void setup() {
		background(100);
		shapes = new Shape[2];
		shapes[0] = new Square(this, 80);
		shapes[0].init_color(new Color(0, 255, 255));
		shapes[0].render();
		
		shapes[1] = new Rectangle(this, 100, 80);
		shapes[1].init_color(new Color(0, 255, 0));
		shapes[1].render();
		
		stepRight=1;
		stepLeft=-1;
	}

	@Override
	public void draw(){
		background(100);
		for (Shape shape : shapes) {
			shape.step();
			shape.render();
		}
		
		// logic for all shape collisions happens here
		// happens at O(n^2)
		
		detechCollision(shapes[0], shapes[1]);
		
	}

	public void detechCollision(Shape shape1, Shape shape2) {		
		float x_dist = Math.abs(shape1.x - shape2.x);
		float y_dist = Math.abs(shape1.y - shape2.y);
		
		if(x_dist + 0.5 < (shape1.side_a + shape2.side_a)/2 &&
		   y_dist + 0.5 < (shape1.side_b + shape2.side_b)/2 ){
			textSize(30);
			fill(255, 0, 0);
			text("OUT!!", 50, 50);
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
			case 32:
				shape.step(0,1);
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