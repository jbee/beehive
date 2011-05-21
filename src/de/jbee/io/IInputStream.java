package de.jbee.io;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @deprecated faktisch geht es doch eher um chars - generische lösung nicht sinnvoll ?
 */
@Deprecated
public interface IInputStream<T> {

	T next();

	T peek();

	boolean hasNext();
}
