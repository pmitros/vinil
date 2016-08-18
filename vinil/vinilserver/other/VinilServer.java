/** The main server program */
import java.util.*;
import java.io.*;
import java.net.*;
import vinil.*;

class VinilServer extends Thread
{
  public final static int DEFAULT_PORT=9623;
  protected int port;
  protected ServerSocket listenSocket;
  protected ThreadGroup tg;
  protected Vector connections=new Vector();
  protected Vulture vulture;

  /** In case of an error, print the error and die */
  public static void error(String s)
  {
    System.err.println(s);
    System.exit(1);
  }

  /** Create the server listening on a port */
  public Server(int port)
  {
    // Initialize thread
    super("Vinil Server");

    // Create server socket
    if(port==0) port=DEFAULT_PORT;
    this.port=port;
    try { listenSocket=new ServerSocket(port); }
    catch (IOException e) error("Error creating server socket: "+e);

    // Create a threadgroup for the connections
    tg=new ThreadGroup("Vinil Connections");

    // Create a vulture to catch dead connections
    vulture=new Vulture(this);

    // And run server
    this.start();
  }

  public void run()
  {
    try
      {
	while(true)
	  {
	    Socket clientSocket=listenSocket.accept();
	    Connection c=new Connection(clientSocket, tg, 3, vulture);
	    synchronized(connections)
	      connections.addElement(c);
	  }
      }
    catch (IOException e) 
      error("Exception while listening for connections: "+e);
  }

  public static void main(String[] args)
  {
    int port=DEFAULT_PORT;
    if(args.length>1)
      error("Error: More than one parameter\n"+
	    "Syntax: VinilServer [port number]");
    if(args.length==1)
      {
	try port=Integer.parseInt(args[0]);
	catch (NumberFormatException e) port=DEFAULT_PORT;
      }
    new Server(port);
  }
}


