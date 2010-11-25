package de.jbee.io.json.parse;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

import de.jbee.io.CharReader;
import de.jbee.io.json.IJsonValue;
import de.jbee.io.json.JsonBuilder;

public class JsonParserTest {

	@Test
	public void testTreeBuilder()
			throws FileNotFoundException {
		JsonParser parser = new JsonParser( CharReader.of( new FileReader(
				"test/de/jbee/io/json/parse/Test.json" ) ) );
		JsonBuilder treeBuilder = JsonBuilder.newInstance();
		parser.parse( treeBuilder );
		IJsonValue value = treeBuilder.build();
		System.out.println( value );
	}
}
