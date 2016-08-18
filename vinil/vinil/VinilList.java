//
// VinilList.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * class VinilList
 * Implements a Lisp-like list
 */
public class VinilList extends VinilElement
{
	public VinilList()
	{
		data = new Vector();
	}

	/** Reference the first element */
	public VinilElement head() throws VinilException
	{
		try
		{
			return (VinilElement)data.elementAt(0);
		}
		catch (Exception e)
		{
			throw new VinilException("cannot get the head of an empty list");
		}
	}

	/** Reference the last element */
	public VinilElement tail() throws VinilException
	{
		try
		{
			return (VinilElement)data.elementAt(data.size() - 1);
		}
		catch (Exception e)
		{
			throw new VinilException("cannot get the tail of an empty list");
		}
	}

	/** Add an element to the top of the list */
	public void addHead(VinilElement newHead)
	{
		try
		{
			data.insertElementAt(newHead, 0);
		}
		catch (Exception e)
		{
			//throw new VinilException("unknown error");
		}
	}

	/** Add an element to the bottom of the list */
	public void addTail(VinilElement newTail)
	{
		data.addElement(newTail);
	}

	/** Remove the first element */
	public void removeHead() throws VinilException
	{
		try
		{
			data.removeElementAt(0);
		}
		catch (Exception e)
		{
			throw new VinilException("cannot delete the head of an empty list");
		}
	}

	/** Remove the last element */
	public void removeTail() throws VinilException
	{
		try
		{
			data.removeElementAt(data.size() - 1);
		}
		catch (Exception e)
		{
			throw new VinilException("cannot delete the tail of an empty list");
		}
	}

	/** Get number of elements */
	public int size()
	{
		return data.size();
	}

	/** Get an element */
	public VinilElement elementAt(int i) throws VinilException
	{
		return (VinilElement)data.elementAt(i);
	}

	/** Set an element */
	public void setElementAt(VinilElement e, int i)
	{
		data.setElementAt(e, i);
	}

	protected Vector data;

	/** Return the string equivalent of this element */
	public String toString()
	{
		String str = "(";

		for (int i = 0; i < data.size(); i++)
			str += " " + data.elementAt(i).toString();

		str += " )";
		return str;
	}

	/** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilList)obj).data.equals(data));
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return data.hashCode();
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return (token.tokenType == VinilToken.VT_PARENTHESIS) &&
			(token.token.equals("("));
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token;

		token = tokenizer.nextToken();
		if (!isTokenValid(token))
			throw new VinilException("lists start with (");

		while ((tokenizer.peek() != null) && (!tokenizer.peek().token.equals(")")))
		{
			VinilElement e = VinilEnvironment.getNextElement(tokenizer);

			if (VinilComment.isComment(e))
				throw new VinilException("comments cannot be members of a list");
			data.addElement(e);
		}

		if (tokenizer.peek() == null)
			throw new VinilException("list needs closing )");

		token = tokenizer.nextToken();
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			return this;
		else
		{
			try
			{
				parameters = parameters.evaluate(env, frame, null);

				if (parameters.getClass() != Class.forName("vinil.VinilNumber"))
					throw new VinilException("list dereferencements must be numbers");

				VinilNumber num = (VinilNumber)parameters;
				int i = num.getValue().intValue();

				try
				{
					return (VinilElement)data.elementAt(i);
				}
				catch (Exception e)
				{
					throw new VinilException("list dereferencement out of range");
				}
			}
			catch (VinilException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new VinilException("syntax error parsing list");
			}
		}
	}
}
