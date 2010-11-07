package de.jbee.io.json;

public interface IJsonProcessor
		extends IJsonVisitor {

	IJsonProcessor begin( JsonType member, String name );

	IJsonProcessor end( JsonType element );

	boolean skip( JsonType element );
}
