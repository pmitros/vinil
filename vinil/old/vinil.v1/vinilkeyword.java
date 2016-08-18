//
// VinilKeyword.java  
// 

package vinil;

// Imports here

/**
 * class VinilKeyword
 * Holds variable names, operators, built-in forms, and other identifiers
 */
public class VinilKeyword extends VinilElement
{
	/** get/set access methods */
	public String getValue()
	{
		return value;
	}

	public void setValue(String newValue)
	{
		value = newValue;
	}

	protected String value;

	/** Return the string equivalent of this element */
	public String toString()
	{
		return value;
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj.getClass().equals(getClass())) && (((VinilKeyword)obj).value.equals(value));
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return value.hashCode();
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return token.tokenType == VinilToken.VT_KEYWORD;
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token = tokenizer.nextToken();
//		if (!isTokenValid(token))
//			throw new VinilException("internal error: keyword");

		value = token.token;
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (env.getExtension(value) != null)
		{
			return env.getExtension(value).doFunction(value, env, parameters);
		}
		else
		{
			VinilElement e = env.get(this);
			if (e == null)
				throw new VinilException("symbol not found");
			return e.evaluate(env, parameters);
		}
	}
}
