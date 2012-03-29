package de.jbee.io.json;

public interface JsonTreeVisitor
		extends JsonValueProcessor {

	void visitMember( String name, Json value );

	void visitElement( int index, Json value );

	void visit( JsonObject value );

	void visit( JsonArray value );

	void visit( JsonBoolean value );

	void visit( JsonNull value );

	void visit( JsonNumber value );

	void visit( JsonString value );

}
