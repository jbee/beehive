package de.jbee.io;

public interface IInputStream<T> {

	T next();

	T peek();

	boolean hasNext();
}
