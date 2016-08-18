package vinil.viniltest2;

import java.io.*;
import vinil.*;

public class VinilTest {

    public static void main(String args[]) {
	VinilEnvironment env;
		env = new VinilEnvironment();
        String text = "<< sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) sto.(j << sto.(a 1) if.(equ.(a 0) ret.false) while.(equ.(a 1) set.(a +.(a 1))) ret.a >>) j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j j >>";
		try
		{
			VinilElement e = VinilEnvironment.parse(text);
			System.out.print(e.toString() + "\n");
			VinilElement f = env.interpret(e);
			if (f != null)
				System.out.print(f.toString());
			else
				System.out.print("(null)");
		}
		catch (VinilException ex)
		{
			System.out.print(ex.reason);
		}
	System.out.print("\n");
    }
}