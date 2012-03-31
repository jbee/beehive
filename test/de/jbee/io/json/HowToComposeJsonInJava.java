package de.jbee.io.json;

import org.junit.Test;

import de.jbee.io.json.JsonInstructor.JsonArrayInstructor;
import de.jbee.io.json.JsonInstructor.JsonMemberInstructor;

/**
 * A tests that demonstrates how {@link Json} can be composed ad hoc in java.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public class HowToComposeJsonInJava {

	@Test
	public void composeToJson() {
		JsonTreeBuilder builder = JsonTreeBuilder.newInstance();
		JsonMemberInstructor<Void> root = JsonMemberInstructor.instruct( builder );
		JsonArrayInstructor<JsonArrayInstructor<Void>> array = root.array().value( false ).array();
		array.value( 1 ).values( 1, 2, 3 ).endArray().endArray();
		System.out.println( builder.build() );
		root.array().value( true );
		System.out.println( builder.build() );
	}

	@Test
	public void tesMisc() {
		JsonTreeBuilder builder = JsonTreeBuilder.newInstance();
		JsonMemberInstructor<Void> root = JsonMemberInstructor.instruct( builder );

		root.object().member( "parsed" ).parse( "[3,4,{'foo':'bar'}, false, null, [[1,3],[3,6]]]" ).endObject();
		System.out.println( builder.build() );
		root.object().member( "bla" ).object().member( "bla" ).value( 4 ).endObject().endObject();
		System.out.println( builder.build() );
		root.array().value( 1 ).value( 2 ).value( 3 ).values( 4, 5, 6 ).endArray();
		System.out.println( builder.build() );
		root.object().member( "bla" ).array().value( 4 ).values( 5, 6, 7 ).endArray().endObject();
		System.out.println( builder.build() );
		root.object().member( "peter" ).value( "arm" ).member( "doubleMember" ).value( 6.8d ).endObject();
		System.out.println( builder.build() );
		root.object().member( "aObj" ).object().member( "aArray" ).array().value( true ).value(
				false ).endArray().member( "another" ).array().values( 34, 5, 6, 7, 8 ).endArray().endObject().endObject();
		System.out.println( builder.build() );
		root.array().object().member( "foo" ).value( false ).endObject().object().member( "bar" ).value(
				true ).endObject().endArray();
		System.out.println( builder.build() );
		root.object().member( "multiarray" ).array().array().values( 5, 6, 7 ).endArray().array().values(
				6, 7, 8, 9 ).endArray().endArray().endObject();
		System.out.println( builder.build() );
	}
}
