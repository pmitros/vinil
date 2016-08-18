//
// VinilNumber.java  
// 

package vinil;

// Imports here

/**
 * class VinilNumber
 * Any numeric, integer or real
 */
public class VinilNumber extends VinilElement
{
	/** get/set access functions */
	public Number getValue()
	{
		return value;
	}

	public void setValue(Number newValue)
	{
		value = newValue;
	}

	/** Determines if this number is an integer */
	public boolean isInteger()
	{
		// TODO:
		return false;
	}

	Number value;

	/** Return the string equivalent of this element */
	public String toString()
	{
		return value.toString();
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return value.hashCode();
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilNumber)obj).value.equals(value));
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return token.tokenType == VinilToken.VT_NUMBER;
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token;

		token = tokenizer.nextToken();

//		if (!isTokenValid(token))
//			throw new VinilException("internal error: number");

		value = Float.valueOf(token.token);
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			return this;
		else
			throw new VinilException("number dereferencement is not supported");
	}
}
