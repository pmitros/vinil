//
// VinilElement.java  
// 

package vinil;

// Imports here

/**
 * class VinilElement
 * Abstract base for all elements
 */
public abstract class VinilElement
{
	/** Construction */
	public VinilElement()
	{
	}

	/** Parser helper functions */
	/** Determine if the token can denote this object */
	abstract boolean isTokenValid(VinilToken token);

	/** Parse the given token stream and store the result in the active object */
	abstract void parse(VinilTokenizer tokenizer) throws VinilException;

	/** Interpreter helper functions */
	/** Evaluate the current element */
	abstract VinilElement evaluate(VinilEnvironment env, VinilElement parameters) throws VinilException;
}
