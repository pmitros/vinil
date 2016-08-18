//
// VinilToken.java  
// 

package vinil;

// Imports here

/**
 * class VinilToken
 * Used internally by the parser
 */
class VinilToken
{
	String token;
	int tokenType;

	static final int VT_STRING = 1;
	static final int VT_NULL = 2;
	static final int VT_PARENTHESIS = 3;
	static final int VT_SQUAREBRACKET = 4;
	static final int VT_COMMENT = 5;
	static final int VT_ANGLEBRACKETS = 6;
	static final int VT_KEYWORD = 7;
	static final int VT_DOT = 8;
	static final int VT_NUMBER = 9;
	static final int VT_BOOLEAN = 10;
	static final int VT_BLOB = 11;
}
