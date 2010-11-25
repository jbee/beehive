package de.jbee.io.json.parse;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

import de.jbee.io.CharReader;
import de.jbee.io.json.IJsonValue;
import de.jbee.io.json.JsonBuilder;
import de.jbee.io.json.filter.JsonFilter;
import de.jbee.io.property.IPropertySelector;
import de.jbee.io.property.PropertyPath;

public class JsonParserTest {

	@Test
	public void testTreeBuilder()
			throws FileNotFoundException {
		JsonParser parser = new JsonParser( CharReader.of( new FileReader(
				"test/de/jbee/io/json/parse/Test.json" ) ) );
		JsonBuilder treeBuilder = JsonBuilder.newInstance();
		parser.parse( new JsonFilter( treeBuilder, new IPropertySelector() {

			@Override
			public boolean selects( PropertyPath path ) {
				return !path.isSubElementOf( PropertyPath.of( "glossary.GlossDiv.GlossList.GlossEntry.GlossDef" ) );
			}
		} ) );
		IJsonValue value = treeBuilder.build();
		System.out.println( value );
	}
}
