package de.jbee.io;

public interface ICharScanner {

	// maybe the second parameter should be a generic since it is also an
	// idea to do some conversion inside a scanner an feed enhanced input to the output
	// Than a very common use is to use the ICharWriter as output type

	void scan( ICharReader in, ICharWriter out );
}
