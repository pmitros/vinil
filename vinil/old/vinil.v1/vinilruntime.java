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
		else if (name.equals("args"))
			return VinilRuntime.args(env, parameters);
		else if (name.equals("if"))
			return VinilRuntime.ift(env, parameters);
		else if (name.equals("while"))
			return VinilRuntime.whilet(env, parameters);
		else if (name.equals("equ"))
			return VinilRuntime.equ(env, parameters);
		else if (name.equals("!equ"))
			return VinilRuntime.notequ(env, parameters);
		else if (name.equals("+"))
			return VinilRuntime.plus(env, parameters);
		else if (name.equals("*"))
			return VinilRuntime.times(env, parameters);
		else if (name.equals("!"))
			return VinilRuntime.not(env, parameters);
		else
			throw new VinilException("internal error: extension function not found");
	}

	public Vector getNames()
	{
		Vector v = new Vector();
		v.addElement("sto");
		v.addElement("ret");
		v.addElement("args");
		v.addElement("set");
		v.addElement("if");
		v.addElement("while");
		v.addElement("equ");
		v.addElement("!equ");
		v.addElement("+");
		v.addElement("*");
		v.addElement("!");
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

	static VinilElement args(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		return env.peekParameter();
	}

	static VinilElement ift(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			throw new VinilException("if requires a list of 2 or 3 parameters");

		try
		{
			parameters = parameters.evaluate(env, null);
			if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
			{
				VinilList l = (VinilList)parameters;
				if ((l.size() < 2) || (l.size() > 3))
					throw new VinilException("if requires a list of 2 or 3 parameters");

				VinilElement e = l.elementAt(0);
				VinilElement f = l.elementAt(1);
				VinilElement g = null;
				
				e = e.evaluate(env, null);
				if (l.size() == 3)
				{
					g = l.elementAt(2);
				}

				if (!Class.forName("vinil.VinilBoolean").equals(e.getClass()))
					throw new VinilException("first parameter to if must be a boolean expression");

				if (((VinilBoolean)e).getValue() == true)
					return f.evaluate(env, null);
				else if (g != null)
					return g.evaluate(env, null);
			}
			else
				throw new VinilException("if requires a list of 2 or 3 parameters");
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: VinilList and VinilBoolean not found");
		}

		return null; // used to get over ms-java bug
	}

	static VinilElement whilet(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			throw new VinilException("while operator requires a list of 2 parameters");

		try
		{
			parameters = parameters.evaluate(env, null);
			if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
			{
				VinilList l = (VinilList)parameters;
				if ((l.size() < 2) || (l.size() > 3))
					throw new VinilException("while requires a list of 2 parameters");

				VinilElement e = l.elementAt(0);
				VinilElement f = l.elementAt(1);
				VinilElement g;
				
				while (true)
				{
					g = e.evaluate(env, null);

					if (!Class.forName("vinil.VinilBoolean").equals(g.getClass()))
						throw new VinilException("first parameter to while must be a boolean expression");

					if (!((VinilBoolean)g).getValue())
						return null;
					else
						f.evaluate(env, null);
				}
			}
			else
				throw new VinilException("while a list of 2 parameters");
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: VinilList and VinilBoolean not found");
		}
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

	static VinilElement notequ(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			throw new VinilException("notequ operator requires a list of 2 parameters");

		try
		{
			parameters = parameters.evaluate(env, null);
			if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
			{
				VinilList l = (VinilList)parameters;
				if (l.size() != 2)
					throw new VinilException("notequ operator requires a list of 2 parameters");

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
				ret.setValue(!b);

				return ret;
			}
			else
				throw new VinilException("notequ operator requires a list of 2 parameters");
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

	static VinilElement not(VinilEnvironment env, VinilElement parameters) throws VinilException
	{
		if (parameters == null)
			throw new VinilException("! operator requires parameters");

		try
		{
			parameters = parameters.evaluate(env, null);
			if (Class.forName("vinil.VinilBoolean").equals(parameters.getClass()))
			{
				VinilBoolean b = (VinilBoolean)parameters;
				b.setValue(!b.getValue());
				return b;
			}
			else
				throw new VinilException("! operator operates only on booleans");
		}
		catch (VinilException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new VinilException("internal error: VinilBoolean not found");
		}
	}
}
