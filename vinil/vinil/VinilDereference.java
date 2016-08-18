//
// VinilDereference.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * class VinilDereference
 * Stores a list of dereferencements
 */
public class VinilDereference extends VinilElement
{
	protected Vector data;

	public VinilDereference()
	{
		data = new Vector();
	}

	/** Get the underlying vector */
	public Vector getData()
	{
		return data;
	}

	/** Add a parameter to the parameter list */
	public void addParameter(VinilElement e)
	{
		data.addElement(e);
	}

	/** Return the string equivalent of this element */
	public String toString()
	{
		String str = new String();

		if (data.size() == 0)
			return "";

		str += data.elementAt(0).toString();
		
		for (int i = 1; i < data.size(); i++)
		{
			str += "." + data.elementAt(i).toString();
		}

		return str;
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		// Does not apply
		return false;
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		// Does not apply
		return 0;
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		// Does not apply
		return false;
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		// Does not apply
		throw new VinilException("internal error: VinilDereferencement.parse called");
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
		{
			if (data.size() == 0)
				return null;

			VinilElement current, result;
			current = (VinilElement)data.elementAt(0);

			for (int i = 1; i < data.size(); i++)
			{
				current = current.evaluate(env, frame, (VinilElement)data.elementAt(i));
			}

			return current;
		}
		else
			throw new VinilException("internal error: VinilDereference.evaluate received parameters");
	}
}
