package de.jbee.io;

public interface ICharScanner<T> {

	void scan( ICharReader in, T out );
}
