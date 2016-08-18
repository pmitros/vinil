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

	static public VinilElement parse(String sourceData) throws VinilException
	{
		String translated = new String();

		// Translate out the escape sequences
		boolean quotelevel = false;

		for (int i = 0; i < sourceData.length(); i++)
		{
			if (sourceData.charAt(i) == '\"')
				quotelevel = !quotelevel;

			if ((sourceData.charAt(i) == '\\') && quotelevel)
			{
				i++;

				if (i == sourceData.length())
					throw new VinilException("invalid escape sequence");

				switch (sourceData.charAt(i))
				{
				case 'n':
					translated += '\n';
					break;

				case 'r':
					translated += '\r';
					break;

				case 'a':
					translated += (char)7;
					break;

				case 't':
					translated += '\t';
					break;

				case '\"':
					translated += '\"';
					break;

				case '\\':
					translated += '\\';
					break;

				default:
					throw new VinilException("unrecognized escape sequence");
				}
			}
			else
				translated += sourceData.charAt(i);
		}

		translated += ' ';

		return getNextElement(new VinilTokenizer(translated));
	}
	
	public VinilElement interpret(VinilElement input) throws VinilException
	{
		return input.evaluate(this, new VinilStackFrame(), null);
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
		System.out.println(data.toString());

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
}
