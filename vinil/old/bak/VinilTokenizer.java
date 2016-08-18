//
// VinilTokenizer.java  
// 

package vinil;

// Imports here

/**
 * class VinilTokenizer
 * Breaks the input stream into tokens
 */
final class VinilTokenizer
{
    VinilTokenizer(String str)
	{
		data = str;
	}

	VinilToken nextToken() throws VinilException
	{
		VinilToken token;
		token = parseToken();
		if (token == null)
			return null;
		data = new String(data.toCharArray(), token.token.length(), data.length() - token.token.length());
		return token;
	}

	VinilToken peek() throws VinilException
	{
		return parseToken();
	}

	protected String data;
	
	protected VinilToken parseToken() throws VinilException
	{
		VinilToken token = new VinilToken();

		char ch;
		int i = 0;

		stripSpace();

		if (data.length() == 0)
		{
			return null;
		}

		if ((data.length() > 3) && (data.substring(0, 4).equals("null")))
		{
			token.tokenType = VinilToken.VT_NULL;
			token.token = "null";
			return token;
		}

		if ((data.length() > 3) && (data.substring(0, 4).equals("true")))
		{
			token.tokenType = VinilToken.VT_BOOLEAN;
			token.token = "true";
			return token;
		}

		if ((data.length() > 4) && (data.substring(0, 5).equals("false")))
		{
			token.tokenType = VinilToken.VT_BOOLEAN;
			token.token = "false";
			return token;
		}

		if ((data.length() > 1) && ((data.substring(0, 2).equals("<<")) ||
			(data.substring(0, 2).equals(">>"))))
		{
			token.tokenType = VinilToken.VT_ANGLEBRACKETS;
			token.token = data.substring(0, 2);
			return token;
		}

		ch = data.charAt(i);
		switch (ch)
		{
			case '[':
			case ']':
			{
				token.tokenType = VinilToken.VT_SQUAREBRACKET;
				token.token = String.valueOf(ch);
				return token;
			}

			case '(':
			case ')':
			{
				token.tokenType = VinilToken.VT_PARENTHESIS;
				token.token = String.valueOf(ch);
				return token;
			}

			case '.':
			{
				token.tokenType = VinilToken.VT_DOT;
				token.token = String.valueOf(ch);
				return token;
			}

			case '\"':
			{
				// TODO: handle escape sequences
				do
				{
					i++;
					if (i >= data.length())
						throw new VinilException();

					ch = data.charAt(i);
				}
				while (ch != '\"');
				token.tokenType = VinilToken.VT_STRING;
				token.token = data.substring(0, i + 1);
				return token;
			}

			case '{':
			{
				do
				{
					i++;
					if (i >= data.length())
						throw new VinilException();

					ch = data.charAt(i);
				}
				while (ch != '}');
				token.tokenType = VinilToken.VT_COMMENT;
				token.token = data.substring(0, i + 1);
				return token;
			}

			case '}':
				throw new VinilException("} without preceding }");

			default:
			{
				// TODO: handle blobs, escape sequences, and decimals
				do
				{
					ch = data.charAt(i++);
				}
				while ((ch != ' ') && 
						(ch != '\t') && 
						(ch != '\n') && 
						(ch != '\r') && 
						(ch != '{') && 
						(ch != '}') && 
						(ch != '[') && 
						(ch != ']') && 
						(ch != '(') && 
						(ch != ')') && 
						(ch != '<') && 
						(ch != '>') && 
						(ch != '\"') && 
						(ch != '.') && 
						(i < data.length()));
				if (i == data.length())
					token.token = data.substring(0, data.length() - 1);
				else
					token.token = data.substring(0, i - 1);
				try
				{
					Float.valueOf(token.token);
					token.tokenType = VinilToken.VT_NUMBER;
				}
				catch (Exception e)
				{
					token.tokenType = VinilToken.VT_KEYWORD;
				}
				return token;
			}
		}
	}

	protected void stripSpace()
	{
		int i = 0;
		char ch;

		if (data.length() > 0)
		{
			do
			{
				ch = data.charAt(i++);
			}
			while (((ch == ' ') || (ch == '\t') || (ch == '\n') || (ch == '\r')) &&
				(i < (data.length())));

			if (!((ch == ' ') || (ch == '\t') || (ch == '\n') || (ch == '\r')))
				i--;

			data = new String(data.toCharArray(), i, data.length() - i);
		}
	}
}
