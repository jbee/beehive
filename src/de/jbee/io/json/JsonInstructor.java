package de.jbee.io.json;

import static de.jbee.io.json.JsonInstructor.JsonMemberInstructor.instruct;

import java.io.CharArrayReader;

import de.jbee.io.CharIO;
import de.jbee.io.ProcessableBy;

public interface JsonInstructor<P> {

	JsonObjectInstructor<P> object();

	JsonArrayInstructor<P> array();

	P value( String value );

	P value( Number value );

	P value( Boolean value );

	class JsonObjectInstructor<P>
			implements ProcessableBy<JsonProcessor> {

		private final P parent;

		private JsonProcessor processor;

		JsonObjectInstructor( P parent ) {
			super();
			this.parent = parent;
		}

		public JsonMemberInstructor<JsonObjectInstructor<P>> member( String name ) {
			return new JsonMemberInstructor<JsonObjectInstructor<P>>( this, name, processor );
		}

		public P endObject() {
			return parent;
		}

		@Override
		public void discardBy( JsonProcessor processor ) {
			this.processor = processor;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			this.processor = processor;
		}

	}

	/**
	 * Means parent is an object and this is a JSON value belongs to a member field
	 */
	class JsonMemberInstructor<P>
			implements JsonInstructor<P> {

		private final P parent;
		private final String name;
		private final JsonProcessor processor;

		public static JsonMemberInstructor<Void> instruct( JsonProcessor processor ) {
			return new JsonMemberInstructor<Void>( null, null, processor );
		}

		static <P> JsonMemberInstructor<P> instruct( P parent, JsonProcessor processor ) {
			return new JsonMemberInstructor<P>( parent, null, processor );
		}

		JsonMemberInstructor( P parent, String name, JsonProcessor processor ) {
			super();
			this.parent = parent;
			this.name = name;
			this.processor = processor;
		}

		public P parse( String json ) {
			JsonParser.yieldInstance( name ).scan(
					CharIO.reader( new CharArrayReader( json.toCharArray() ) ), processor );
			return parent;
		}

		@Override
		public JsonObjectInstructor<P> object() {
			final JsonObjectInstructor<P> res = new JsonObjectInstructor<P>( parent ); //TODO parent seamed to be wrong ?!
			processor.process( JsonType.OBJECT, name, res );
			return res;
		}

		public JsonArrayInstructor<P> array() {
			JsonArrayInstructor<P> res = new JsonArrayInstructor<P>( parent );
			processor.process( JsonType.ARRAY, name, res );
			return res;
		}

		@Override
		public P value( final String value ) {
			JSON.processAsElement( processor, name, value );
			return parent;
		}

		@Override
		public P value( final Number value ) {
			JSON.processAsElement( processor, name, value );
			return parent;
		}

		@Override
		public P value( final Boolean value ) {
			JSON.processAsElement( processor, name, value );
			return parent;
		}

	}

	class JsonArrayInstructor<P>
			implements JsonInstructor<JsonArrayInstructor<P>>, ProcessableBy<JsonProcessor> {

		private final P parent;

		private JsonProcessor processor;

		JsonArrayInstructor( P parent ) {
			super();
			this.parent = parent;
		}

		@Override
		public JsonObjectInstructor<JsonArrayInstructor<P>> object() {
			return instructor().object();
		}

		@Override
		public JsonArrayInstructor<JsonArrayInstructor<P>> array() {
			return instructor().array();
		}

		@Override
		public JsonArrayInstructor<P> value( String value ) {
			return instructor().value( value );
		}

		@Override
		public JsonArrayInstructor<P> value( Number value ) {
			return instructor().value( value );
		}

		@Override
		public JsonArrayInstructor<P> value( Boolean value ) {
			return instructor().value( value );
		}

		private JsonMemberInstructor<JsonArrayInstructor<P>> instructor() {
			return instruct( this, processor );
		}

		public JsonArrayInstructor<P> values( String... values ) {
			for ( String v : values ) {
				value( v );
			}
			return this;
		}

		public JsonArrayInstructor<P> values( Number... values ) {
			for ( Number v : values ) {
				value( v );
			}
			return this;
		}

		public JsonArrayInstructor<P> values( Boolean... values ) {
			for ( Boolean v : values ) {
				value( v );
			}
			return this;
		}

		public P endArray() {
			return parent;
		}

		@Override
		public void discardBy( JsonProcessor processor ) {
			this.processor = processor;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			this.processor = processor;
		}

	}

}
