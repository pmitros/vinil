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
	String safesubstring(String str, int begin, int end)
	{
		String ret = new String();
		int i;

		for (i = begin; i < end; i++)
		{
			ret += str.charAt(i);
		}

		return ret;
	}

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

		int len = data.length();

		if (len == 0)
		{
			return null;
		}

		if ((len > 4) && (data.substring(0, 5).equals("false")))
		{
			token.tokenType = VinilToken.VT_BOOLEAN;
			token.token = "false";
			return token;
		}

		if (len > 3)
		{
			String sub4 = data.substring(0, 4);
			if (sub4.equals("null"))
			{
				token.tokenType = VinilToken.VT_NULL;
				token.token = "null";
				return token;
			}

			if ((len > 3) && (sub4.equals("true")))
			{
				token.tokenType = VinilToken.VT_BOOLEAN;
				token.token = "true";
				return token;
			}
		}

		if (len > 1)
		{
			String sub2 = data.substring(0, 2);
			if ((sub2.equals("<<")) ||
				(sub2.equals(">>")))
			{
				token.tokenType = VinilToken.VT_ANGLEBRACKETS;
				token.token = sub2;
				return token;
			}
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
				do
				{
					i++;
					if (i >= len)
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
					if (i >= len)
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
				// TODO: handle blobs and decimals
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
						(i < len));
				if (i == len)
					token.token = safesubstring(data, 0, len - 1);
				else
					token.token = safesubstring(data, 0, i - 1);
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
		int len = data.length();

		if (len > 0)
		{
			do
			{
				ch = data.charAt(i++);
			}
			while (((ch == ' ') || (ch == '\t') || (ch == '\n') || (ch == '\r')) &&
				(i < len));

			if (!((ch == ' ') || (ch == '\t') || (ch == '\n') || (ch == '\r')))
				i--;

			data = data.substring(i);
		}
	}
}
