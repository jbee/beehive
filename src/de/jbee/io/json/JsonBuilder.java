package de.jbee.io.json;

import java.util.LinkedList;
import java.util.List;

import de.jbee.io.IProcessableUnit;
import de.jbee.io.json.JsonObject.JsonMember;

public abstract class JsonBuilder
		implements IJsonProcessor {

	public static JsonBuilder newInstance() {
		return new RootJsonBuilder();
	}

	static final class JsonArrayBuilder
			extends JsonBuilder {

		private final List<IJson> elements = new LinkedList<IJson>();

		@Override
		public void process( JsonType type, String name, IProcessableUnit<IJsonProcessor> unit ) {
			JsonBuilder builder = forType( type );
			unit.processBy( builder );
			elements.add( builder.build() );
		}

		@Override
		public IJson build() {
			return new JsonArray( elements );
		}
	}

	static final class JsonValueBuilder
			extends JsonBuilder {

		private IJson value;

		@Override
		public void process( JsonType type, String name, IProcessableUnit<IJsonProcessor> unit ) {
			throwBuildException();
		}

		@Override
		public final void process( String value ) {
			this.value = Json.valueOf( value );
		}

		@Override
		public final void process( Number value ) {
			this.value = Json.valueOf( value );
		}

		@Override
		public final void process( boolean value ) {
			this.value = JsonBoolean.valueOf( value );
		}

		@Override
		public final void processNull() {
			this.value = Json.NULL;
		}

		@Override
		public IJson build() {
			return value;
		}

	}

	static final class JsonObjectBuilder
			extends JsonBuilder {

		private final List<JsonMember> members = new LinkedList<JsonMember>();

		@Override
		public void process( JsonType type, String name, IProcessableUnit<IJsonProcessor> unit ) {
			JsonBuilder builder = forType( type );
			unit.processBy( builder );
			members.add( new JsonMember( name, builder.build() ) );
		}

		@Override
		public IJson build() {
			return new JsonObject( members.toArray( new JsonMember[members.size()] ) );
		}

	}

	static JsonBuilder forType( JsonType type ) {
		return !type.isComposite()
			? new JsonValueBuilder()
			: type == JsonType.ARRAY
				? new JsonArrayBuilder()
				: new JsonObjectBuilder();
	}

	/**
	 * Necessary because we cannot know what type of json will be processed first later when a fresh
	 * builder is used. Therefore this builder delays creation of real root until we get to know by
	 * call to {@link #process(JsonType, String, IProcessableUnit)}. This is transparent to the
	 * user.
	 * 
	 * In fact the {@linkplain RootJsonBuilder} can be used multiple times in sequence for different
	 * json values.
	 * 
	 * @author Jan Bernitt (jan.bernitt@gmx.de)
	 */
	static final class RootJsonBuilder
			extends JsonBuilder {

		private JsonBuilder rootBuilder;

		@Override
		public void process( JsonType type, String name, IProcessableUnit<IJsonProcessor> unit ) {
			rootBuilder = forType( type );
			unit.processBy( rootBuilder );
		}

		@Override
		public IJson build() {
			return rootBuilder.build();
		}

	}

	public abstract IJson build();

	@Override
	public void process( boolean value ) {
		throwBuildException();
	}

	protected final void throwBuildException() {
		throw new UnsupportedOperationException( "Not available for builder "
				+ getClass().getSimpleName() );
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

}
