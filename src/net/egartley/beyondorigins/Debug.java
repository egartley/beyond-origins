package net.egartley.beyondorigins;

public class Debug {

	/**
	 * Prints the given {@link java.lang.Object Object} using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(Object)}
	 * 
	 * @param o
	 *            {@link java.lang.Object Object}
	 */
	public static void out(Object o)
	{
		System.out.println(o);
	}

	/**
	 * Prints the given {@link java.lang.Object Object} using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(Object)}
	 * with the tag "INFO"
	 * 
	 * @param o
	 *            {@link java.lang.Object Object}
	 */
	public static void info(Object o)
	{
		out("INFO: " + o);
	}

	/**
	 * Prints the given {@link java.lang.Object Object} using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(Object)}
	 * with the tag "WARNING"
	 * 
	 * @param o
	 *            {@link java.lang.Object Object}
	 */
	public static void warning(Object o)
	{
		out("WARNING: " + o);
	}

	/**
	 * Prints the given {@link java.lang.Object Object} using
	 * {@linkplain java.io.PrintStream#println(Object) System.out.println(Object)}
	 * with the tag "ERROR"
	 * 
	 * @param o
	 *            {@link java.lang.Object Object}
	 */
	public static void error(Object o)
	{
		out("ERROR: " + o);
	}

}
