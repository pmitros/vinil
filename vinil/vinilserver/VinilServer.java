import vinil.*;
import java.io.*;
import java.net.*;

//
//
// VinilServer
//
//
class VinilServer 
{
	public static void main(String args[])
	{
		VinilEnvironment env;
		ServerSocket s;

		System.out.println("VinilServer Version 1.0\nCopyright(C) Dennis Quan and Piotr Mitros, 1997");

		env = new VinilEnvironment();

		try
		{
			s = new ServerSocket(3141);

			while (true)
			{
				Socket slave = s.accept();
				VinilServerThread t = new VinilServerThread();

				t.socket = slave;
				t.env = env;
				t.start();
			}
		}
		catch (IOException e)
		{
			System.out.println("Socket accept failed");
		}
	}
}

