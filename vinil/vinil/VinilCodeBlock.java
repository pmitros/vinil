//
// VinilCodeBlock.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * class VinilCodeBlock
 * A list containing code
 */
public class VinilCodeBlock extends VinilElement
{
	public VinilCodeBlock()
	{
		data = new Vector();
	}

	/** Index-based instruction retrieval */
	public VinilElement getAt(int index)
	{
		return (VinilElement)data.elementAt(index);
	}

	/** Add an instruction to the list */
	public void addTail(VinilElement newTail)
	{
		data.addElement(newTail);
	}

	/** Returns the number of instructions in this code block */
	public int size()
	{
		return data.size();
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilCodeBlock)obj).data.equals(data));
	}

	protected java.util.Vector data;

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return 0;
	}

	/** Return the string equivalent of this element */
	public String toString()
	{
		Enumeration enum = data.elements();
		String output = "<<";

		while (enum.hasMoreElements())
		{
			output += " " +	enum.nextElement().toString();
		}

		output += " >>";
		return output;
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return (token.tokenType == VinilToken.VT_ANGLEBRACKETS) &&
			(token.token.equals("<<"));
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token;

		token = tokenizer.nextToken();
		if (!isTokenValid(token))
			throw new VinilException("code blocks start with <<");

		while ((tokenizer.peek() != null) && (!tokenizer.peek().token.equals(">>")))
		{
			VinilElement e = VinilEnvironment.getNextElement(tokenizer);
			data.addElement(e);
		}

		if (tokenizer.peek() == null)
			throw new VinilException("code block needs closing >>");

		token = tokenizer.nextToken();
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
	{
		frame.pushParameter(parameters);
		frame.pushReturn(null);

		for (int i = 0; i < data.size(); i++)
		{
			((VinilElement)data.elementAt(i)).evaluate(env, frame, null);
		}

		frame.popParameter();
		return frame.popReturn();
	}
}
