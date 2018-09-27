import processing.core.PApplet;

class Color {
	public int r, g, b;

	public Color(int r, int g, int b) {
		// TODO Auto-generated constructor stub
		this.r = r;
		this.g = g;
		this.b = b;
	}
}

abstract class Shape {
	PApplet sketcher;
	Color color;
	int x_speed, y_speed;
	int x, y;
	int side_a, side_b;

	public void dir(int x_dir, int y_dir) {

	}

	public abstract void step();

	public abstract void render();
	
	public abstract void init_color(Color c);

}

class Square extends Shape {
	public Square(PApplet sketcher, int side) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.side_a = side;
		this.side_b = side;
		x = (int) sketcher.width /2;
		y = (int) sketcher.height /2;
		
	}

	public void init_color(Color c) {
		this.color = c;
	}

	public void step() {

	}

	public void render() {
		sketcher.fill(color.r, color.g, color.b);
		sketcher.rect(x, y, side_a, side_b);
	}

}

class Rectangle extends Shape {
	public Rectangle(PApplet sketcher, int side_a, int side_b) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.side_a = side_a;
		this.side_b = side_b;
		x_speed = 10;
		y_speed = 10;
		x = (int) sketcher.random(0, sketcher.width);
		y = (int) sketcher.random(0, sketcher.height);
	}

	public void init_color(Color c) {
		this.color = c;
	}

	public void step() {

	}
	
	public void dir(int x_dir, int y_dir) {
		switch (x_dir) {
		case 1:
			x += x_speed;
			break;
		case -1:
			x -= x_speed;
			break;
		default:

		}
		switch (y_dir) {
		case 1:
			y += y_speed;
			break;
		case -1:
			y -= y_speed;
			break;
		default:
		}
		
		if (x > sketcher.width)
			x = 0;
		if (x < 0)
			x = sketcher.width;
		if (y > sketcher.height)
			y = 0;
		if (y < 0)
			y = sketcher.height;
	}

	public void render() {
		sketcher.fill(color.r, color.g, color.b);
		sketcher.rect(x, y, side_a, side_b);
	}

}

public class Renderer extends PApplet {

	Shape[] shapes;

	@Override
	public void settings() {
		size(1024, 1024);
	}

	@Override
	public void setup() {
		background(100);
		shapes = new Shape[2];
		shapes[0] = new Square(this, 10);
		shapes[0].init_color(new Color(0, 255, 255));
		shapes[0].render();
		
		shapes[1] = new Rectangle(this, 30, 30);
		shapes[1].init_color(new Color(0, 255, 0));
		shapes[1].render();
	}

	@Override
	public void draw(){
		background(100);
		for (Shape shape : shapes) {
			shape.step();
			shape.render();
		}
		detechCollision(shapes[0], shapes[1]);
		
	}

	public void detechCollision(Shape shape1, Shape shape2) {
		
		float shape1_x_center = shape1.x + (shape1.side_a /2);
		float shape1_y_center = shape1.y + (shape1.side_b /2);
		
		float shape2_x_center = shape2.x + (shape2.side_a /2);
		float shape2_y_center = shape2.y + (shape2.side_b /2);
		
		
		float x_dist = Math.abs(shape1_x_center - shape2_x_center);
		float y_dist = Math.abs(shape1_y_center-shape2_y_center);
		
		if(x_dist < (shape1.side_a + shape2.side_a)/2 + 0.5 &&
		   y_dist < (shape1.side_b + shape2.side_b)/2 + 0.5 )
		{
			textSize(30);
			fill(255, 0, 0);
			text("Danger!!", 50, 50);
		}
	}
	
	@Override
	public void keyPressed() {
		for (Shape shape : shapes) {
			switch (keyCode) {
			case RIGHT:
				shape.dir(1, 0);
				break;
			case LEFT:
				shape.dir(-1, 0);
				break;
			case UP:
				shape.dir(0, -1);
				break;
			case DOWN:
				shape.dir(0, 1);
				break;
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