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
  /** Call correct function */
  public VinilElement doFunction(String name, VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (name.equals("sto"))
      return VinilRuntime.sto(env, frame, parameters);
    else if (name.equals("set"))
      return VinilRuntime.set(env, frame, parameters);
    else if (name.equals("ret"))
      return VinilRuntime.ret(env, frame, parameters);
    else if (name.equals("args"))
      return VinilRuntime.args(env, frame, parameters);
    else if (name.equals("if"))
      return VinilRuntime.ift(env, frame, parameters);
    else if (name.equals("while"))
      return VinilRuntime.whilet(env, frame, parameters);
    else if (name.equals("equ"))
      return VinilRuntime.equ(env, frame, parameters);
    else if (name.equals("!equ"))
      return VinilRuntime.notequ(env, frame, parameters);
    else if (name.equals("+"))
      return VinilRuntime.plus(env, frame, parameters);
    else if (name.equals("*"))
      return VinilRuntime.times(env, frame, parameters);
    else if (name.equals("!"))
      return VinilRuntime.not(env, frame, parameters);
    else if (name.equals("cat"))
      return VinilRuntime.cat(env, frame, parameters);
    else
      throw new VinilException("internal error: extension function not found");
  }
  

  /** Return the names of the functions implemented by extension */
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
    v.addElement("cat");
    return v;
  }
  

  /** Concatenate a series of strings into one */
  static VinilElement cat(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    try
      {
	if(parameters==null)
	  throw new VinilException("Cat requires a list parameter");

	
      }
    catch(VinilException e)
      {
	throw e;
      }
    catch(Exception e)
      {
	throw new VinilException("Internal Exception in cat: "+e);
      }
  }


  /**  */
  static VinilElement sto(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
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
	
	if (symbol.getClass().equals(Class.forName("vinil.VinilDereference")))
	  {
	    Vector v = ((VinilDereference)symbol).getData();
	    VinilElement el = ((VinilElement)(v.elementAt(v.size() - 2)));
	    
	    if (!el.getClass().equals(Class.forName("vinil.VinilKeyword")))
	      throw new VinilException("invalid dereferencement in assignment");
	    
	    el = env.get((VinilKeyword)el);
	    
	    if (el == null)
	      throw new VinilException("invalid dereferencement in assignment");
	    
	    if (el.getClass().equals(Class.forName("vinil.VinilList")))
	      {
		if (!v.elementAt(v.size() - 1).getClass().equals(Class.forName("vinil.VinilNumber")))
		  throw new VinilException("invalid list dereferencement in assignment");
		
		VinilNumber n = ((VinilNumber)(v.elementAt(v.size() - 1)));
		VinilList l = (VinilList)el;
		l.setElementAt(value, n.getValue().intValue());
		return value;
	      }
	    else if (el.getClass().equals(Class.forName("vinil.VinilDictionary")))
	      {
		VinilElement n = ((VinilElement)(v.elementAt(v.size() - 1)));
		VinilDictionary d = (VinilDictionary)el;
		d.put(n, value);
		return value;
	      }
	    else throw new VinilException("invalid assignment");
	  }
	
	if (symbol.getClass().equals(Class.forName("vinil.VinilKeyword")))
	  {
	    env.put((VinilKeyword)symbol, value);
	    return value;
	  }
	
	throw new VinilException("improper variable name");
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
  

  /** */
  static VinilElement set(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
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
	value = value.evaluate(env, frame, null);
	
	if (symbol.getClass().equals(Class.forName("vinil.VinilDereference")))
	  {
	    Vector v = ((VinilDereference)symbol).getData();
	    
	    if (v.elementAt(v.size() - 2).getClass().equals(Class.forName("vinil.VinilList")))
	      {
		if (!v.elementAt(v.size() - 1).getClass().equals(Class.forName("vinil.VinilNumber")))
		  throw new VinilException("invalid list dereferencement in assignment");
		
		VinilNumber n = ((VinilNumber)(v.elementAt(v.size() - 1)));
		VinilList l = ((VinilList)(v.elementAt(v.size() - 2)));
		l.setElementAt(value, n.getValue().intValue());
		return value;
	      }
	    else if (v.elementAt(v.size() - 2).getClass().equals(Class.forName("vinil.VinilDictionary")))
	      {
		VinilElement n = ((VinilElement)(v.elementAt(v.size() - 1)));
		VinilDictionary d = ((VinilDictionary)(v.elementAt(v.size() - 2)));
		d.put(n, value);
		return value;
	      }
	    else throw new VinilException("invalid assignment");
	  }
	
	if (symbol.getClass().equals(Class.forName("vinil.VinilKeyword")))
	  {
	    env.put((VinilKeyword)symbol, value);
	    return value;
	  }
	
	throw new VinilException("improper variable name");
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
  

  /** Return a value from a function */ 
  static VinilElement ret(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    frame.popReturn();
    if (parameters != null)
      frame.pushReturn(parameters.evaluate(env, frame, null));
    else
      frame.pushReturn(null);
    return parameters;
  }
  

  /** */
  static VinilElement args(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    return frame.peekParameter();
  }
  

  /** While loop - either using list or dictionary*/
  static VinilElement vWhile(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    VinilElement vBool, vCode;
    try
      {
	// Sort out condition, then and else statements
	if(parameters instanceof VinilList) // it is a list
	  {
	    VinilList l=(VinilList)parameters;
	    if(l.size()!=2)
	      throw new VinilException("While takes two parameters");
	    vBool = l.elementAt(0);
	    vCode = l.elementAt(1);
	  }
	else if(parameters instanceof VinilDictionary) // it is a dictionary
	  {
	    VinilDictionary d=(VinilDictionary)parameters;
	    vBool=d.get(new VinilString("bool"));
	    vCode=d.get(new VinilString("code"));
	  }
	else // it is neither a list nor a dictionary
	  throw new VinilException("While requires list or dictionary");

	boolean looper=true;
	VinilElement tempBool;
	VinilBoolean vb;
	VinilList retval=new VinilList();

	while(looper)
	  {
	    // if the boolean is code, run it
	    tempBool=env.interpret(vBool);

	    // If it ain't a boolean, throw an exception
	    if(!(tempBool instanceof VinilBoolean))
	      throw new VinilException("While requires a boolean");

	    // Evaluate value
	    vb=(VinilBoolean)tempBool;
	    if(vb.getValue()==true)
	      retval.addTail(env.interpret(vCode));
	    else
	      looper=false;
	  }
	return retval;
      }
    catch (VinilException e)
      {
	throw e;
      }
    catch (Exception e)
      {
	throw new VinilException("internal error: "+e.toString());
      }
  }

  static VinilElement vIf(VinilEnvironment env, VinilElement parameters) throws VinilException
  {
    VinilElement vBool, vThen, vElse, retval;
    try
      {
	// Sort out condition, then and else statements
	if(parameters instanceof VinilList) // it is a list
	  {
	    VinilList l=(VinilList)parameters;
	    if((l.size()!=2)&&(l.size()!=3))
	      throw new VinilException("If takes a list of 2 or 3 items");
	    vBool = l.elementAt(0);
	    vThen = l.elementAt(1);
	    if(l.size()==3)
	      vElse = l.elementAt(2);
	    else 
	      vElse = new VinilNull();
	  }
	else if(parameters instanceof VinilDictionary) // it is a dictionary
	  {
	    VinilDictionary d=(VinilDictionary)parameters;
	    vBool=d.get(new VinilString("cond"));
	    vThen=d.get(new VinilString("then"));
	    vElse=d.get(new VinilString("else"));
	  }
	else // it is neither a list nor a dictionary
	  throw new VinilException("If requires list or dictionary");

	// if the boolean is code, run it
	vBool=env.interpret(vBool);

	// If it ain't a boolean, throw an exception
	if(!(vBool instanceof VinilBoolean))
	  throw new VinilException("If requires a boolean");

	// Evaluate value
	VinilBoolean vb=(VinilBoolean)vBool;
	if(vb.getValue()==true)
	  retval=vThen;
	else
	  retval=vElse;

	// If the result is code, run it
        retval=env.interpret(retval);

	return retval;
      }
    catch (VinilException e)
      {
	throw e;
      }
    catch (Exception e)
      {
	throw new VinilException("internal error: "+e.toString());
      }
  }



  static VinilElement ift(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (parameters == null)
      throw new VinilException("if requires a list of 2 or 3 parameters");
    
    try
      {
	parameters = parameters.evaluate(env, frame, null);
	if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
	  {
	    VinilList l = (VinilList)parameters;
	    if ((l.size() < 2) || (l.size() > 3))
	      throw new VinilException("if requires a list of 2 or 3 parameters");
	    
	    VinilElement e = l.elementAt(0);
	    VinilElement f = l.elementAt(1);
	    VinilElement g = null;
	    
	    e = e.evaluate(env, frame, null);
	    if (l.size() == 3)
	      {
		g = l.elementAt(2);
	      }
	    
	    if (!Class.forName("vinil.VinilBoolean").equals(e.getClass()))
	      throw new VinilException("first parameter to if must be a boolean expression");
	    
	    if (((VinilBoolean)e).getValue() == true)
	      return f.evaluate(env, frame, null);
	    else if (g != null)
	      return g.evaluate(env, frame, null);
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
  
  static VinilElement whilet(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (parameters == null)
      throw new VinilException("while operator requires a list of 2 parameters");
    
    try
      {
	parameters = parameters.evaluate(env, frame, null);
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
		g = e.evaluate(env, frame, null);
		
		if (!Class.forName("vinil.VinilBoolean").equals(g.getClass()))
		  throw new VinilException("first parameter to while must be a boolean expression");
		
		if (!((VinilBoolean)g).getValue())
		  return null;
		else
		  f.evaluate(env, frame, null);
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
  
  static VinilElement equ(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (parameters == null)
      throw new VinilException("equ operator requires a list of 2 parameters");
    
    try
      {
	parameters = parameters.evaluate(env, frame, null);
	if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
	  {
	    VinilList l = (VinilList)parameters;
	    if (l.size() != 2)
	      throw new VinilException("equ operator requires a list of 2 parameters");
	    
	    VinilElement e = l.elementAt(0);
	    VinilElement f = l.elementAt(1);
	    e = e.evaluate(env, frame, null);
	    f = f.evaluate(env, frame, null);
	    
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
  
  static VinilElement notequ(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (parameters == null)
      throw new VinilException("notequ operator requires a list of 2 parameters");
    
    try
      {
	parameters = parameters.evaluate(env, frame, null);
	if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
	  {
	    VinilList l = (VinilList)parameters;
	    if (l.size() != 2)
	      throw new VinilException("notequ operator requires a list of 2 parameters");
	    
	    VinilElement e = l.elementAt(0);
	    VinilElement f = l.elementAt(1);
	    e = e.evaluate(env, frame, null);
	    f = f.evaluate(env, frame, null);
	    
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
  
  static VinilElement plus(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (parameters == null)
      throw new VinilException("+ operator requires parameters");
    
    float f = 0;
    
    try
      {
	parameters = parameters.evaluate(env, frame, null);
	if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
	  {
	    VinilList l = (VinilList)parameters;
	    for (int i = 0; i < l.size(); i++)
	      {
		VinilElement e = l.elementAt(i);
		e = e.evaluate(env, frame, null);
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
  
  static VinilElement times(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (parameters == null)
      throw new VinilException("+ operator requires parameters");
    
    float f = 1;
    
    try
      {
	parameters = parameters.evaluate(env, frame, null);
	if (Class.forName("vinil.VinilList").equals(parameters.getClass()))
	  {
	    VinilList l = (VinilList)parameters;
	    for (int i = 0; i < l.size(); i++)
	      {
		VinilElement e = l.elementAt(i);
		e = e.evaluate(env, frame, null);
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
  
  static VinilElement not(VinilEnvironment env, VinilStackFrame frame, VinilElement parameters) throws VinilException
  {
    if (parameters == null)
      throw new VinilException("! operator requires parameters");
    
    try
      {
	parameters = parameters.evaluate(env, frame, null);
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
