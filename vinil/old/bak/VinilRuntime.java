//
// VinilRuntime.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * class VinilRuntime
 * A VinilExtension which provides basic run-time services
 */
public class VinilRuntime implements VinilExtension
{
	public VinilElement doFunction(String name, VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (name.equals("sto"))
			return VinilRuntime.sto(env, parameters);
		else if (name.equals("set"))
			return VinilRuntime.set(env, parameters);
		else if (name.equals("ret"))
			return VinilRuntime.ret(env, parameters);
		else if (name.equals("equ"))
			return VinilRuntime.equ(env, parameters);
		else if (name.equals("+"))
			return VinilRuntime.plus(env, parameters);
		else if (name.equals("*"))
			return VinilRuntime.times(env, parameters);
		else
			throw new VinilException("internal error: extension function not found");
	}

	public Vector getNames()
	{
		Vector v = new Vector();
		v.addElement("sto");
		v.addElement("ret");
		v.addElement("set");
		v.addElement("equ");
		v.addElement("+");
		v.addElement("*");
		return v;
	}

	static VinilElement sto(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		try
		{
			if ((parameters == null) || 
				(!parameters.getClass().equals(Class.forName("vinil.VinilList"))) ||
				(((VinilList)parameters).size() != 2))
				throw new VinilException("sto takes two parameters");

			VinilElement symbol, value;
			symbol = ((VinilList)parameters).elementAt(0);
			value = ((VinilList)parameters).elementAt(1);

			if (!symbol.getClass().equals(Class.forName("vinil.VinilKeyword")))
				throw new VinilException("improper variable name");

			env.put((VinilKeyword)symbol, value);
			return value;
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: VinilList or VinilKeyword not found");
		}
	}

	static VinilElement set(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		try
		{
			if ((parameters == null) || 
				(!parameters.getClass().equals(Class.forName("vinil.VinilList"))) ||
				(((VinilList)parameters).size() != 2))
				throw new VinilException("sto takes two parameters");

			VinilElement symbol, value;
			symbol = ((VinilList)parameters).elementAt(0);
			value = ((VinilList)parameters).elementAt(1);
			value = value.evaluate(env, null);

			if (!symbol.getClass().equals(Class.forName("vinil.VinilKeyword")))
				throw new VinilException("improper variable name");

			env.put((VinilKeyword)symbol, value);
			return value;
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: VinilList or VinilKeyword not found");
		}
	}

	static VinilElement ret(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		env.popReturn();
		if (parameters != null)
			env.pushReturn(parameters.evaluate(env, null));
		else
			env.pushReturn(null);
		return parameters;
	}

	static VinilElement equ(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			throw new VinilException("equ operator requires a list of 2 parameters");

		try
		{
			parameters = parameters.evaluate(env, null);
			if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
			{
				VinilList l = (VinilList)parameters;
				if (l.size() != 2)
					throw new VinilException("equ operator requires a list of 2 parameters");

				VinilElement e = l.elementAt(0);
				VinilElement f = l.elementAt(1);
				e = e.evaluate(env, null);
				f = f.evaluate(env, null);

				boolean b;
				if ((e == null) && (f == null))
					b = true;
				else if ((f == null) && (e != null))
					b = e.equals(f);
				else
					b = f.equals(e);

				VinilBoolean ret = new VinilBoolean();
				ret.setValue(b);

				return ret;
			}
			else
				throw new VinilException("equ operator requires a list of 2 parameters");
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: class VinilList not found");
		}
	}

	static VinilElement plus(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			throw new VinilException("+ operator requires parameters");

		float f = 0;

		try
		{
			parameters = parameters.evaluate(env, null);
			if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
			{
				VinilList l = (VinilList)parameters;
				for (int i = 0; i < l.size(); i++)
				{
					VinilElement e = l.elementAt(i);
					e = e.evaluate(env, null);
					if (!Class.forName("vinil.VinilNumber").equals(e.getClass()))
						throw new VinilException("+ operator operates only on numbers");
					f += ((VinilNumber)e).getValue().floatValue();
				}
				VinilNumber n = new VinilNumber();
				n.setValue(new Float(f));
				return n;
			}
			else if (Class.forName("vinil.VinilNumber").equals(parameters.getClass()))
			{
				return parameters;
			}
			else
				throw new VinilException("+ operator operates only on numbers");
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: VinilList or VinilNumber not found");
		}
	}

	static VinilElement times(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			throw new VinilException("+ operator requires parameters");

		float f = 1;

		try
		{
			parameters = parameters.evaluate(env, null);
			if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
			{
				VinilList l = (VinilList)parameters;
				for (int i = 0; i < l.size(); i++)
				{
					VinilElement e = l.elementAt(i);
					e = e.evaluate(env, null);
					if (!Class.forName("vinil.VinilNumber").equals(e.getClass()))
						throw new VinilException("+ operator operates only on numbers");
					f *= ((VinilNumber)e).getValue().floatValue();
				}
				VinilNumber n = new VinilNumber();
				n.setValue(new Float(f));
				return n;
			}
			else if (Class.forName("vinil.VinilNumber").equals(parameters.getClass()))
			{
				return parameters;
			}
			else
				throw new VinilException("+ operator operates only on numbers");
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: VinilList or VinilNumber not found");
		}
	}
}
