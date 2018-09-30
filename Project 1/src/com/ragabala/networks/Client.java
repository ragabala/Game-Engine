package com.ragabala.networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static DataInputStream input_stream;
	private static DataOutputStream outputStream;

	public static void main(String[] args) throws IOException, InterruptedException {
		Socket s = new Socket("127.0.0.1", 15000);
		input_stream = new DataInputStream(s.getInputStream());
		outputStream = new DataOutputStream(s.getOutputStream());
		Scanner sn = new Scanner(System.in);
		while (true) {
			String readLine = input_stream.readUTF();
			System.out.println("From server : "+readLine);
			System.out.print("ENTER: ");
			String toServer = sn.nextLine();
			outputStream.writeUTF(toServer);
			if(toServer.equals("quit"))
				break;
		}
	}
}
