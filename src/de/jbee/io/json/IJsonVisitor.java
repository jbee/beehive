package de.jbee.io.json;

public interface IJsonVisitor {

	// path ?

	// array ?

	// rename to process ??

	void visit( String value );

	void visit( Number value );

	void visit( boolean value );

	void visitNull();
}
