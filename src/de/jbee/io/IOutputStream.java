package de.jbee.io;

public interface IOutputStream<T, S> {

	IOutputStream<T, S> append( T token );

	IOutputStream<T, S> appendSequence( S sequence );
}
