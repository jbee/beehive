package de.jbee.io;

public interface IStreamTranslator<I, T, S> {

	void translate( IInputStream<I> in, IOutputStream<T, S> out );
}
