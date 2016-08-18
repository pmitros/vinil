//
// VinilException.java  
// 

package vinil;

/**
 * class VinilException
 * Represents all exceptions specific to Vinil
 */
public class VinilException extends Exception
{
	VinilException(String s)
	{
		super(s);
		reason = s;
	}

	VinilException()
	{
		super();
		reason = "unknown error";
	}

	public void print()
	{
		System.out.print("Vinil error: " + reason + "\n");
	}

	public String reason;
}
