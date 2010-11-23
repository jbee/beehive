package de.jbee.io;

public interface IProcessable<T> {

	void processBy( T processor );

	void discardBy( T processor );
}
