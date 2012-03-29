package de.jbee.io.json;

import java.util.LinkedList;
import java.util.List;

import de.jbee.io.ProcessableBy;
import de.jbee.io.json.JsonObject.JsonMember;

/**
 * Creates a {@link Json}-tree structure describing the {@link JSON}-object with java types.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public abstract class JsonTreeBuilder
		implements JsonProcessor {

	public static JsonTreeBuilder newInstance() {
		return new RootJsonBuilder();
	}

	static JsonTreeBuilder forType( JsonType type ) {
		return !type.isComposite()
			? new JsonValueBuilder()
			: type == JsonType.ARRAY
				? new JsonArrayBuilder()
				: new JsonObjectBuilder();
	}

	public abstract Json build();

	@Override
	public void process( boolean value ) {
		throwBuildException();
	}

	@Override
	public void process( Number value ) {
		throwBuildException();
	}

	@Override
	public void process( String value ) {
		throwBuildException();
	}

	@Override
	public void processNull() {
		throwBuildException();
	}

	protected final void throwBuildException() {
		throw new UnsupportedOperationException( "Not available for builder "
				+ getClass().getSimpleName() );
	}

	static final class JsonArrayBuilder
			extends JsonTreeBuilder {

		private final List<Json> elements = new LinkedList<Json>();

		@Override
		public Json build() {
			return new JsonArray( elements );
		}

		@Override
		public void process( JsonType type, String name, ProcessableBy<JsonProcessor> element ) {
			JsonTreeBuilder builder = forType( type );
			element.processBy( builder );
			elements.add( builder.build() );
		}
	}

	static final class JsonObjectBuilder
			extends JsonTreeBuilder {

		private final List<JsonMember> members = new LinkedList<JsonMember>();

		@Override
		public Json build() {
			return new JsonObject( members.toArray( new JsonMember[members.size()] ) );
		}

		@Override
		public void process( JsonType type, String name, ProcessableBy<JsonProcessor> element ) {
			JsonTreeBuilder builder = forType( type );
			element.processBy( builder );
			members.add( new JsonMember( name, builder.build() ) );
		}

	}

	static final class JsonValueBuilder
			extends JsonTreeBuilder {

		private Json value;

		@Override
		public Json build() {
			return value;
		}

		@Override
		public final void process( boolean value ) {
			this.value = JsonBoolean.json( value );
		}

		@Override
		public void process( JsonType type, String name, ProcessableBy<JsonProcessor> element ) {
			throwBuildException();
		}

		@Override
		public final void process( Number value ) {
			this.value = JSON.json( value );
		}

		@Override
		public final void process( String value ) {
			this.value = JSON.json( value );
		}

		@Override
		public final void processNull() {
			this.value = JSON.NULL;
		}

	}

	/**
	 * Necessary because we cannot know what type of {@link JSON} will be processed first later when
	 * a fresh builder is used. Therefore this builder delays creation of real root until we get to
	 * know by call to {@link #process(JsonType, String, ProcessableBy)}. This is transparent to the
	 * user.
	 * 
	 * In fact the {@linkplain RootJsonBuilder} can be used multiple times in sequence for different
	 * json values.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	static final class RootJsonBuilder
			extends JsonTreeBuilder {

		private JsonTreeBuilder rootBuilder;

		@Override
		public Json build() {
			return rootBuilder.build();
		}

		@Override
		public void process( JsonType type, String name, ProcessableBy<JsonProcessor> element ) {
			rootBuilder = forType( type );
			element.processBy( rootBuilder );
		}

	}

}
