//
// VinilNull.java  
// 

package vinil;

// Imports here

/**
 * class VinilNull
 * The null type
 */
public class VinilNull extends VinilElement
{
	/** Return the string equivalent of this element */
	public String toString()
	{
		return "null";
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && obj.getClass().equals(getClass());
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return 0;
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return (token.tokenType == VinilToken.VT_NULL);
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token = tokenizer.nextToken();

//		if (!isTokenValid(token))
//			throw new VinilException("internal error: null");
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			return this;
		else
			throw new VinilException();
	}
}
