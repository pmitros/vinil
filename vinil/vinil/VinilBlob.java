//
// VinilBlob.java  
// 

package vinil;

// Imports here

/**
 * class VinilBlob
 * Holds a Binary Large OBject
 */
public class VinilBlob extends VinilElement
{
	/** get/set access functions */
	public byte[] getValue()
	{
		return value;
	}

	public void setValue(byte[] newValue)
	{
		value = newValue;
	}

	/** Return the string equivalent of this element */
	public String toString()
	{
		// TODO:
		return null;
	}

	/** Produces a hashcode for the object */
	public int hashCode()
	{
		return 0;
	}

    /** Determines if two objects are equal */
	public boolean equals(Object obj)
	{
		return (obj != null) && (obj.getClass().equals(getClass())) && (((VinilBlob)obj).value.equals(obj));
	}

	protected byte[] value;

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	boolean isTokenValid(VinilToken token)
	{
		// TODO:
		return false;
	}

	/** Parse the given token stream and store the result in the active object */
	void parse(VinilTokenizer tokenizer) throws VinilException
	{
		// TODO:
		throw new VinilException();
	}

	/** Interpreter helper functions */
	/** Evaluate the current element */
	VinilElement evaluate(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			return this;
		else
		// TODO:
			throw new VinilException();
	}
}
