//
// VinilString.java  
// 

package vinil;

// Imports here

/**
 * class VinilString
 * Stores a String type for Vinil
 */
public class VinilString extends VinilElement
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

	/** Return the string equivalent of this element */
	public String toString()
	{
		return "\"" + value + "\"";
	}
	
	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return value.hashCode();
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilString)obj).value.equals(value));
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		return token.tokenType == VinilToken.VT_STRING;
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		VinilToken token = tokenizer.nextToken();
//		if (!isTokenValid(token))
//			throw new VinilException("internal error: string");

		value = token.token.substring(1, token.token.length() - 1);
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
				if (!parameters.getClass().equals(Class.forName("vinil.VinilNumber")))
					throw new VinilException("string dereferencements must be numbers");
			}
			catch (VinilException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new VinilException("internal error: VinilNumber class missing");
			}

			VinilString str = new VinilString();
			VinilNumber num = (VinilNumber)parameters;
			int i = num.getValue().intValue();

			try
			{
				str.setValue(String.valueOf(value.charAt(i)));
			}
			catch (Exception e)
			{
				throw new VinilException("string dereferencement out of range");
			}

			return str;
		}
	}
}
