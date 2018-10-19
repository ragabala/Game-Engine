package com.hw1.networks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import com.hw1.sketcher.Color;
import com.hw1.sketcher.Rectangle;
import com.hw1.sketcher.Shape;


/**
 * @author ragbalak
 * The Cloent class that sends the Game objects to the server
 * and also reads the counter from the server in a synchronized 
 * manner 
 */
public class Client {
	private static DataInputStream input_stream;
	private static ObjectOutputStream outputStream;
	static Random randGen;
	
	
	/**
	 * @param values
	 * This function takes care of randomizing the rectangle values.
	 * The values generated are 
	 *  1. Length
	 *  2. Breadth
	 *  3. Pos_x
	 *  4. Pox_y
	 */
	public static void collectRectData(int[] values) {
		// side lengths : 50 - 100
		values[0] = randGen.nextInt(50) + 50;
		values[1] = values[0] + randGen.nextInt(30);

		// pos x,y = 100 - 1000
		values[2] = randGen.nextInt(901) + 100;
		values[3] = randGen.nextInt(901) + 100;

	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * The main function of the client reads and writes objects synchronized.
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Socket s = new Socket("127.0.0.1", 15000);
		input_stream = new DataInputStream(s.getInputStream());
		outputStream = new ObjectOutputStream(s.getOutputStream());
		randGen = new Random();
		Color color = new Color(randGen.nextInt(256), randGen.nextInt(256), randGen.nextInt(256));		
		while (true) {
			System.out.println("From server : "+input_stream.readInt());	
			int rectData[] = new int[4];
			collectRectData(rectData);
			// the Papplet component of the shape will be set in the server side
			Shape shape = new Rectangle(null, rectData[0], rectData[1], rectData[2], rectData[3],color);
			outputStream.writeObject(shape);
			// generate a shape every 2 secs
			Thread.sleep(2000);
		}
		
	}
}
