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
	public abstract VinilElement doFunction(String name, VinilEnvironment env, VinilElement parameters);
	public abstract Vector getNames();
}
