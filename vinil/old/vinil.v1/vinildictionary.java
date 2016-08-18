//
// VinilDictionary.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * class VinilDictionary
 * A class which maps unique keys to definitions
 */
public class VinilDictionary extends VinilElement
{
	public VinilDictionary()
	{
		data = new Hashtable();
	}

	/** Retrieves a list of the keys */
    public VinilList keys()
	{
		VinilList list = new VinilList();
		Enumeration enum = data.keys();

		while (enum.hasMoreElements())
		{
			list.addTail((VinilElement)enum.nextElement());
		}
		
		return list;
	}
	
	/** Puts an element into the dictionary */
    public VinilElement put(VinilElement key, VinilElement value)
	{
		return (VinilElement)data.put(key, value);
	}
	
	/** Removes a key from the dictionary */
    public VinilElement remove(VinilElement key)
	{
		return (VinilElement)data.remove(key);
	}

	protected java.util.Hashtable data;

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return data.hashCode();
	}

	/** Return the string equivalent of this element */
	public String toString()
	{
		Enumeration enum = data.keys();
		String str = "[ ";

		while (enum.hasMoreElements())
		{
			str += "[ ";
			VinilElement elem = (VinilElement)enum.nextElement();
			str += elem.toString() + " ";
			str += data.get(elem).toString() + " ] ";
		}

		str += "]";
		return str;
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilDictionary)obj).data.equals(data));
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return (token.tokenType == VinilToken.VT_SQUAREBRACKET) &&
			(token.token.equals("["));
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token;

		token = tokenizer.nextToken();
		if (!isTokenValid(token))
			throw new VinilException("dictionaries start with [");

		while ((tokenizer.peek() != null) && (!tokenizer.peek().token.equals("]")))
		{
			token = tokenizer.nextToken();
			if (!isTokenValid(token))
				throw new VinilException("syntax error parsing dictionary");

			VinilElement e1, e2;
			e1 = VinilEnvironment.getNextElement(tokenizer);
			e2 = VinilEnvironment.getNextElement(tokenizer);

			if (VinilComment.isComment(e1))
				throw new VinilException("comments not supported in a dictionary");
			if (VinilComment.isComment(e2))
				throw new VinilException("comments not supported in a dictionary");

			if (data.get(e1) != null)
				throw new VinilException("dictionaries cannot have multiple entries with the same key");

			data.put(e1, e2);

			token = tokenizer.nextToken();
			if (!token.token.equals("]"))
				throw new VinilException("syntax error parsing dictionary");
		}


		if (tokenizer.peek() == null)
			throw new VinilException("list needs closing )");

		token = tokenizer.nextToken();
	}
	
	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			return this;
		else
		{
			try
			{
				VinilElement e = (VinilElement)data.get(parameters);
				if (e == null)
					throw new VinilException();
				return e;	
			}
			catch (Exception e)
			{
				throw new VinilException("key does not exist");
			}
		}
	}
}
