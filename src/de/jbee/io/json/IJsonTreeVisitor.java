package de.jbee.io.json;

public interface IJsonTreeVisitor
		extends IJsonVisitor {

	void visitMember( String name, IJsonValue value );

	void visitElement( int index, IJsonValue value );

	void visit( JsonObject value );

	void visit( JsonArray value );

	void visit( JsonBoolean value );

	void visit( JsonNull value );

	void visit( JsonNumber value );

	void visit( JsonString value );

}
