package com.ragabala.networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler implements Runnable {

	static ServerSocket sSocket;
	Socket socket;
	DataOutputStream outputStream;
	DataInputStream inputStream;

	public ClientHandler(Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				outputStream.writeUTF("Enter : Any String / Enter : exit to quit\n");
				String readString = inputStream.readUTF();
				if(!readString.equals("quit"))
					System.out.println("From client "+readString);
				else
				{
					System.out.println("Closing Connection with client "+socket.getPort());
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket sSocket = new ServerSocket(15000);
		System.out.println("server listening at " + sSocket.getLocalPort());
		while (true) {
			Socket socket = sSocket.accept();
			System.out.println("Client Connected to : "+socket.getPort());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			ClientHandler newClient = new ClientHandler(socket, dos, dis);
			new Thread(newClient).start();
		}

	}

}

