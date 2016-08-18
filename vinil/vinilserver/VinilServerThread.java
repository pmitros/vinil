import java.lang.Thread;
import java.net.*;
import java.io.*;
import vinil.*;

//
//
// VinilServerThread
//
//
class VinilServerThread extends Thread
{
	VinilEnvironment env;
	Socket socket;
	
	public void run()
	{
		StringBuffer buf = new StringBuffer();
		InputStream is;
		OutputStream os;

		try		
		{
			is = socket.getInputStream();
			os = socket.getOutputStream();
		}
		catch(Exception e)
		{
			try
			{
				socket.close();
			}
			catch(Exception f)
			{
			}
			return;
		}
		
		while (true)
		{
			int c = 0;
			buf = new StringBuffer();

			try
			{
				while ((c = is.read()) != '\r')
				{
					if (c != '\n')
						buf.append((char)c);
				}
			}
			catch(Exception e)
			{
				try
				{
					socket.close();
				}
				catch(Exception f)
				{
				}
				return;
			}

			String resp;
			
			try
			{
				VinilElement e = VinilEnvironment.parse(buf.toString());
				VinilElement out = null;
				if (e != null)
					out = env.interpret(e);
				if (out != null)
					resp = out.toString();
				else
					resp = "(null)";
			}
			catch (VinilException e)
			{
				resp = e.reason;
			}

			try
			{
				System.out.println(buf.toString());
				for (int i = 0; i < resp.length(); i++)
				{
					os.write(resp.charAt(i));
					System.out.write(resp.charAt(i));
				}
				os.write('\n');
				os.write('\r');
				System.out.write('\n');
				System.out.write('\r');
			}
			catch (Exception e)
			{
				try
				{
					socket.close();
				}
				catch(Exception f)
				{
				}
				return;
			}
		}
	}
}

