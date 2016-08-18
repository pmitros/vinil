//
// VinilExtension.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * interface VinilExtension
 * An extension which can be called by the Vinil system
 */
public interface VinilExtension
{
  /** Perform function name on parameters in the environment env */
  public abstract VinilElement doFunction(String name, VinilEnvironment env, 
    VinilStackFrame frame, VinilElement parameters) throws VinilException;

  /** Return a Vector of Strings containing the names of the functions 
   * implemented in the extension */
  public abstract Vector getNames();
}
