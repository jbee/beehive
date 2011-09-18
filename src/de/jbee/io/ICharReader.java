package de.jbee.io;

public interface ICharReader {

	char next();

	char peek();

	boolean hasNext();

	/**
	 * returns a reader that will read the next count characters twice.
	 * 
	 * <pre>
	 * Example: reading "hello" with reread(2) will read "hehello"
	 * </pre>
	 */
	ICharReader reread( int count );
}
