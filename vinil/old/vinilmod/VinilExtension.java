//
// VinilExtension.java  
// 

package vinil;

// Imports here
import java.util.*;

/**
 * interface VinilExtension
 * An extension which can be called by the Vinil system
 * Each extension provides a series of functions that integrate into Vinil
 */

public interface VinilExtension
{
  /** Perform function name on parameters in the environment env */
  public abstract VinilElement doFunction(String name, VinilEnvironment env, VinilElement parameters) throws VinilException;
  
  /** Return a Vector of Strings containing the names of the functions 
   * implemented in the extension */
  public abstract Vector getNames();
}
