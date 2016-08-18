//
// VinilEnvironment.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * class VinilEnvironment
 * The runtime environment for program execution
 */
public class VinilEnvironment
{
	public VinilEnvironment()
	{
		keywords = new Hashtable();
		extensions = new Hashtable();
		runtime = new VinilRuntime();
		returnStack = new Stack();
		parameterStack = new Stack();
		loadExtension(runtime);
	}
	
	/** Look up a symbol */
	public VinilElement get(VinilKeyword identifier)
	{
		return (VinilElement)keywords.get(identifier);
	}

	/** Load an extension */
	public void loadExtension(VinilExtension extension)
	{
		Vector v = extension.getNames();
		for (int i = 0; i < v.size(); i++)
			extensions.put(v.elementAt(i), extension);
	}

	/** Retrieve an extension */
	public VinilExtension getExtension(String name)
	{
		return (VinilExtension)extensions.get(name);
	}

	protected Hashtable keywords;
	protected Hashtable extensions;
	protected VinilRuntime runtime;
	protected Stack returnStack;
	protected Stack parameterStack;

	static public VinilElement parse(String sourceData) throws VinilException
	{
		sourceData += " ";

		return getNextElement(new VinilTokenizer(sourceData));
	}
	
	public VinilElement interpret(VinilElement input) throws VinilException
	{
		return input.evaluate(this, null);
	}

	/** Parser helper functions */

	static VinilElement getNextElementHelper(VinilTokenizer tokenizer) throws VinilException
	{
		// TODO: support blobs
		VinilToken token = tokenizer.peek();
		
		if (token == null)
			return null;

		VinilElement data;

		switch (token.tokenType)
		{
			case VinilToken.VT_STRING:
				data = new VinilString();
				break;

			case VinilToken.VT_NULL:
				data = new VinilNull();
				break;

			case VinilToken.VT_PARENTHESIS:
				data = new VinilList();
				break;

			case VinilToken.VT_SQUAREBRACKET:
				data = new VinilDictionary();
				break;

			case VinilToken.VT_COMMENT:
				data = new VinilComment();
				break;

			case VinilToken.VT_ANGLEBRACKETS:
				data = new VinilCodeBlock();
				break;

			case VinilToken.VT_NUMBER:
				data = new VinilNumber();
				break;

			case VinilToken.VT_BOOLEAN:
				data = new VinilBoolean();
				break;

			case VinilToken.VT_KEYWORD:
				data = new VinilKeyword();
				break;

			case VinilToken.VT_DOT:
			default:
				throw new VinilException("dereferencement operator cannot stand alone");
		}

		data.parse(tokenizer);

		return data;
	}

	static VinilElement getNextElement(VinilTokenizer tokenizer) throws VinilException
	{
		VinilElement e;
		e = getNextElementHelper(tokenizer);

		VinilToken token = tokenizer.peek();

		if ((token != null) && (token.tokenType == VinilToken.VT_DOT))
		{
			// Create a dereference
			VinilDereference d = new VinilDereference();
			d.addParameter(e);

			token = tokenizer.peek();

			while ((token != null) && (token.tokenType == VinilToken.VT_DOT))
			{
				token = tokenizer.nextToken();
				e = getNextElementHelper(tokenizer);

				if (e == null)
					throw new VinilException("syntax error parsing parameters to dereferencement");

				d.addParameter(e);

				token = tokenizer.peek();
			}

			return d;
		}
		else
			return e;
	}

	/** Interpreter helper functions */
	void put(VinilKeyword identifier, VinilElement element)
	{
		keywords.put(identifier, element);
	}

	void pushReturn(VinilElement e)
	{
		returnStack.push(e);
	}

	VinilElement popReturn() throws VinilException
	{
		try
		{
			return (VinilElement)returnStack.pop();
		}
		catch (Exception e)
		{
			throw new VinilException("invalid ret");
		}
	}

	void pushParameter(VinilElement e)
	{
		parameterStack.push(e);
	}

	VinilElement peekParameter() throws VinilException
	{
		try
		{
			return (VinilElement)parameterStack.peek();
		}
		catch (Exception e)
		{
			throw new VinilException("invalid args");
		}
	}

	VinilElement popParameter() throws VinilException
	{
		try
		{
			return (VinilElement)parameterStack.pop();
		}
		catch (Exception e)
		{
			throw new VinilException("invalid args");
		}
	}
}
