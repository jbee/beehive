package de.jbee.io.json.parse;

import java.io.InputStreamReader;
import java.io.StringBufferInputStream;

import org.junit.Test;

import de.jbee.io.CharacterInputStream;
import de.jbee.io.json.IJsonValue;
import de.jbee.io.json.JsonTreeBuilder;

public class JsonParserTest {

	@Test
	public void testTreeBuilder() {
		JsonParser parser = new JsonParser( new CharacterInputStream( new InputStreamReader(
				new StringBufferInputStream(
						"{\"alf\" : 3, \"peter\": \"hallo\", \"klaus\": { \"hans\" : 4.67 }}" ) ) ) );
		JsonTreeBuilder treeBuilder = JsonTreeBuilder.newInstance();
		parser.parse( treeBuilder );
		IJsonValue value = treeBuilder.build();
		System.out.println( value );
	}
}
