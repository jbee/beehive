package de.jbee.io;

public interface CharScanner<T> {

	void scan( CharReader in, T out );
}
