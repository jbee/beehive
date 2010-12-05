package de.jbee.io.json.parse;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

import de.jbee.io.CharReader;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;
import de.jbee.io.json.IJsonProcessor;
import de.jbee.io.json.IJson;
import de.jbee.io.json.JsonBuilder;
import de.jbee.io.json.JsonParser;
import de.jbee.io.json.filter.JsonFilter;
import de.jbee.io.property.IPropertySelector;
import de.jbee.io.property.PropertyPath;

public class JsonParserTest {

	@Test
	public void testTreeBuilder()
			throws FileNotFoundException {
		ICharReader in = CharReader.of( new FileReader( "test/de/jbee/io/json/parse/Test.json" ) );
		ICharScanner<IJsonProcessor> parser = JsonParser.getInstance();
		JsonBuilder treeBuilder = JsonBuilder.newInstance();
		parser.scan( in, new JsonFilter( treeBuilder, new IPropertySelector() {

			@Override
			public boolean selects( PropertyPath path ) {
				return !path.isSubElementOf( PropertyPath.of( "web-app.servlet.init-param" ) );
			}
		} ) );
		IJson value = treeBuilder.build();
		System.out.println( value );
	}

	@Test
	public void testParseUtilityMethod()
			throws FileNotFoundException {
		ICharReader in = CharReader.of( new FileReader( "test/de/jbee/io/json/parse/Test.json" ) );
		System.out.println( JsonParser.parse( in ) );
	}

}
