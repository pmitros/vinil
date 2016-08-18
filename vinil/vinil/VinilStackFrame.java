//
// VinilStackFrame.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * class VinilStackFrame
 * A set of runtime stacks for exchanging variables
 */
class VinilStackFrame
{
	VinilStackFrame()
	{
		returnStack = new Stack();
		parameterStack = new Stack();
	}
	
	protected Stack returnStack;
	protected Stack parameterStack;

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
