package com.ragabala.networks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.ragabala.sketcher.Rectangle;
import com.ragabala.sketcher.Shape;

class ShapeSender implements Runnable {

	Socket socket;

	public ShapeSender(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public void collectInputs(Scanner sn, int[] values) {
		System.out.println("Enter the shape specs for rectangle\n");
		System.out.println("Rectangle length<Int> :");
		values[0] = sn.nextInt();
		System.out.println("Rectangle bredth<Int> :");
		values[1] = sn.nextInt();
		System.out.println("Rectangle x pos<Int> :");
		values[2] = sn.nextInt();
		System.out.println("Rectangle y pos<Int> :");
		values[3] = sn.nextInt();
	}

	@Override
	public void run() {
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				Scanner sn = new Scanner(System.in);
				int values[] = new int[4];
				collectInputs(sn, values);
				// the Papplet component of the shape will be set in the server side
				Shape shape = new Rectangle(null, values[0], values[1], values[2], values[3]);
				outputStream.writeObject(shape);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

class ClientReceiver implements Runnable {
	Socket socket;
	public ClientReceiver(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			while (true) {
				System.out.println("From Server : "+inputStream.readInt());
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}

public class AsynchronousClient {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Socket socket = new Socket("127.0.0.1",15001);
		System.out.println("Client to server socker established");
		Thread sender = new Thread(new ShapeSender(socket));
		Thread receiver = new Thread(new ClientReceiver(socket));
		sender.start();
		receiver.start();
		sender.join();
		receiver.join();

	}
}
