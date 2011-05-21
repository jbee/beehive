package de.jbee.io.json;

public interface IJsonTreeVisitor
		extends IJsonValueProcessor {

	void visitMember( String name, IJson value );

	void visitElement( int index, IJson value );

	void visit( JsonObject value );

	void visit( JsonArray value );

	void visit( JsonBoolean value );

	void visit( JsonNull value );

	void visit( JsonNumber value );

	void visit( JsonString value );

}
