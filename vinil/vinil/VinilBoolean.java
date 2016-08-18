//
// VinilBoolean.java  
// 

package vinil;

// Imports here

/**
 * class VinilBoolean
 * True or False
 */
public class VinilBoolean extends VinilElement
{
	/** get/set access functions */
	public boolean getValue()
	{
		return value;
	}

	public void setValue(boolean newValue)
	{
		value = newValue;
	}

	protected boolean value;

	/** Return the string equivalent of this element */
	public String toString()
	{
		if (value)
			return "true";
		else
			return "false";
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		if (value)
			return 1;
		else
			return 0;
	}

	/** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilBoolean)obj).value == value);
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return (token.tokenType == VinilToken.VT_BOOLEAN);
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token = tokenizer.nextToken();

//		if (!isTokenValid(token))
//			throw new VinilException("internal error: boolean");

		if (token.token.equals("false"))
			value = false;
		else if (token.token.equals("true"))
			value = true;
		else throw new VinilException("boolean must be true or false");
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			return this;
		else
			throw new VinilException("boolean dereferencement not supported");
	}
}
