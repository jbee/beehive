package de.jbee.io.json;

import java.io.CharArrayReader;

import de.jbee.io.CharReader;
import de.jbee.io.IProcessableElement;

public class JsonComposer {

	interface IJsonComposer<P> {

		JsonObjectComposer<P> object();

		JsonArrayComposer<P> array();

		P value( String value );

		P value( Number value );

		P value( Boolean value );

	}

	public static class JsonObjectComposer<P>
			implements IProcessableElement<IJsonProcessor> {

		private final P parent;

		private IJsonProcessor processor;

		JsonObjectComposer( P parent ) {
			super();
			this.parent = parent;
		}

		public JsonMemberComposer<JsonObjectComposer<P>> member( String name ) {
			return new JsonMemberComposer<JsonObjectComposer<P>>( this, name, processor );
		}

		public P endObject() {
			return parent;
		}

		@Override
		public void discardBy( IJsonProcessor processor ) {
			this.processor = processor;
		}

		@Override
		public void processBy( IJsonProcessor processor ) {
			this.processor = processor;
		}

	}

	/**
	 * Means parent is an object and this is a JSON value belongs to a member field
	 */
	public static class JsonMemberComposer<P>
			implements IJsonComposer<P> {

		private final P parent;
		private final String name;
		private final IJsonProcessor processor;

		static JsonMemberComposer<Void> of( IJsonProcessor processor ) {
			return new JsonMemberComposer<Void>( null, null, processor );
		}

		static <P> JsonMemberComposer<P> of( P parent, IJsonProcessor processor ) {
			return new JsonMemberComposer<P>( parent, null, processor );
		}

		JsonMemberComposer( P parent, String name, IJsonProcessor processor ) {
			super();
			this.parent = parent;
			this.name = name;
			this.processor = processor;
		}

		public P parse( String json ) {
			JsonParser.yieldInstance( name ).scan(
					CharReader.of( new CharArrayReader( json.toCharArray() ) ), processor );
			return parent;
		}

		@Override
		public JsonObjectComposer<P> object() {
			final JsonObjectComposer<P> res = new JsonObjectComposer<P>( parent );
			processor.process( JsonType.OBJECT, name, res );
			return res;
		}

		public JsonArrayComposer<P> array() {
			JsonArrayComposer<P> res = new JsonArrayComposer<P>( parent );
			processor.process( JsonType.ARRAY, name, res );
			return res;
		}

		@Override
		public P value( final String value ) {
			JsonProcessor.processAsElement( processor, name, value );
			return parent;
		}

		@Override
		public P value( final Number value ) {
			JsonProcessor.processAsElement( processor, name, value );
			return parent;
		}

		@Override
		public P value( final Boolean value ) {
			JsonProcessor.processAsElement( processor, name, value );
			return parent;
		}

	}

	public static class JsonArrayComposer<P>
			implements IJsonComposer<JsonArrayComposer<P>>, IProcessableElement<IJsonProcessor> {

		private final P parent;

		private IJsonProcessor processor;

		JsonArrayComposer( P parent ) {
			super();
			this.parent = parent;
		}

		@Override
		public JsonObjectComposer<JsonArrayComposer<P>> object() {
			return JsonMemberComposer.of( this, processor ).object();
		}

		@Override
		public JsonArrayComposer<JsonArrayComposer<P>> array() {
			return JsonMemberComposer.of( this, processor ).array();
		}

		@Override
		public JsonArrayComposer<P> value( String value ) {
			return JsonMemberComposer.of( this, processor ).value( value );
		}

		@Override
		public JsonArrayComposer<P> value( Number value ) {
			return JsonMemberComposer.of( this, processor ).value( value );
		}

		@Override
		public JsonArrayComposer<P> value( Boolean value ) {
			return JsonMemberComposer.of( this, processor ).value( value );
		}

		public JsonArrayComposer<P> values( String... values ) {
			for ( String v : values ) {
				value( v );
			}
			return this;
		}

		public JsonArrayComposer<P> values( Number... values ) {
			for ( Number v : values ) {
				value( v );
			}
			return this;
		}

		public JsonArrayComposer<P> values( Boolean... values ) {
			for ( Boolean v : values ) {
				value( v );
			}
			return this;
		}

		public P endArray() {
			return parent;
		}

		@Override
		public void discardBy( IJsonProcessor processor ) {
			this.processor = processor;
		}

		@Override
		public void processBy( IJsonProcessor processor ) {
			this.processor = processor;
		}

	}

	public static void main( String[] args ) {
		JsonBuilder builder = JsonBuilder.newInstance();
		JsonMemberComposer<Void> root = JsonMemberComposer.of( builder );
		root.array().value( false ).array().values( 1, 2, 3 ).endArray().endArray();
		System.out.println( builder.build() );

	}

	private static void testRest( JsonBuilder builder, JsonMemberComposer<Void> root ) {
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
