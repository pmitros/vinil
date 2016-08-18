//
// VinilComment.java  
// 

package vinil;

// Imports here

/**
 * class VinilComment
 * Stores comments in the code
 */
public class VinilComment extends VinilElement
{
	/** get/set access functions */
	public String getValue()
	{
		return value;
	}

	public void setValue(String newValue)
	{
		value = newValue;
	}

	protected String value;

	/** quickly determines if an object is a comment */
	static public boolean isComment(Object o)
	{
		try
		{
			return Class.forName("vinil.VinilComment").equals(o.getClass());
		}
		catch (Exception e)
		{
			// eat it
			return false;
		}
	}

	/** Return the string equivalent of this element */
	public String toString()
	{
		return "{" + value + "}";
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return 0;
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilComment)obj).value.equals(value));
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return (token.tokenType == VinilToken.VT_COMMENT);
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token = tokenizer.nextToken();
		if (!isTokenValid(token))
			throw new VinilException("comments start with {");

		value = token.token.substring(1, token.token.length() - 1);
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			return this;
		else
			throw new VinilException("comment dereferencement not supported");
	}
}
