package de.jbee.io.json;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

import de.jbee.io.CharIO;
import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.json.filter.JsonFilter;
import de.jbee.io.property.IPropertySelector;
import de.jbee.io.property.PropertyPath;

public class HowToParseJson {

	@Test
	public void parseFromString() {
		assertThat( JSON.parse( "42" ), instanceOf( JsonNumber.class ) );
		assertThat( JSON.parse( "3.1516" ), instanceOf( JsonNumber.class ) );
		assertThat( JSON.parse( "true" ), instanceOf( JsonBoolean.class ) );
		assertThat( JSON.parse( "false" ), instanceOf( JsonBoolean.class ) );
		assertThat( JSON.parse( "null" ), instanceOf( JsonNull.class ) );
		assertThat( JSON.parse( "[]" ), instanceOf( JsonArray.class ) );
		assertThat( JSON.parse( "[42,null]" ), instanceOf( JsonArray.class ) );
		assertThat( JSON.parse( "{}" ), instanceOf( JsonObject.class ) );
		assertThat( JSON.parse( "{'member':'value'}" ), instanceOf( JsonObject.class ) );
		assertThat( JSON.parse( "{member:'value'}" ), instanceOf( JsonObject.class ) );
	}

	@Test
	public void parseFromFile()
			throws FileNotFoundException {
		CharReader in = CharIO.reader( new FileReader( "test/de/jbee/io/json/Test.json" ) );
		Json obj = JSON.parse( in );
		assertThat( obj, instanceOf( JsonObject.class ) );
	}

	@Test
	public void testTreeBuilder()
			throws FileNotFoundException {
		CharReader in = CharIO.reader( new FileReader( "test/de/jbee/io/json/Test.json" ) );
		CharScanner<JsonProcessor> parser = JsonParser.getInstance();
		JsonTreeBuilder treeBuilder = JsonTreeBuilder.newInstance();
		parser.scan( in, new JsonFilter( treeBuilder, new IPropertySelector() {

			@Override
			public boolean selects( PropertyPath path ) {
				return !path.isSubElementOf( PropertyPath.of( "web-app.servlet.init-param" ) );
			}
		} ) );
		Json value = treeBuilder.build();
		System.out.println( value );
	}

}
