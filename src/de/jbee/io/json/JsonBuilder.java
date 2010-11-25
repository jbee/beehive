package de.jbee.io.json;

import java.util.LinkedList;
import java.util.List;

import de.jbee.io.IProcessable;
import de.jbee.io.json.JsonObject.JsonMember;

public abstract class JsonBuilder
		implements IJsonProcessor {

	public static JsonBuilder newInstance() {
		return new RootJsonBuilder();
	}

	static final class JsonArrayBuilder
			extends JsonBuilder {

		private final List<IJsonValue> elements = new LinkedList<IJsonValue>();

		@Override
		public void process( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
			JsonBuilder builder = forType( type );
			element.processBy( builder );
			elements.add( builder.build() );
		}

		@Override
		public IJsonValue build() {
			return new JsonArray( elements.toArray( new IJsonValue[elements.size()] ) );
		}
	}

	static final class JsonValueBuilder
			extends JsonBuilder {

		private IJsonValue value;

		@Override
		public void process( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
			throwBuildException();
		}

		@Override
		public final void visit( String value ) {
			this.value = new JsonString( value );
		}

		@Override
		public final void visit( Number value ) {
			this.value = new JsonNumber( value );
		}

		@Override
		public final void visit( boolean value ) {
			this.value = JsonBoolean.valueOf( value );
		}

		@Override
		public final void visitNull() {
			this.value = JsonNull.OBJECT;
		}

		@Override
		public IJsonValue build() {
			return value;
		}

	}

	static final class JsonObjectBuilder
			extends JsonBuilder {

		private final List<JsonMember> members = new LinkedList<JsonMember>();

		@Override
		public void process( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
			JsonBuilder builder = forType( type );
			element.processBy( builder );
			members.add( new JsonMember( name, builder.build() ) );
		}

		@Override
		public IJsonValue build() {
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

	static final class RootJsonBuilder
			extends JsonBuilder {

		private JsonBuilder rootBuilder;

		@Override
		public void process( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
			rootBuilder = forType( type );
			element.processBy( rootBuilder );
		}

		@Override
		public IJsonValue build() {
			return rootBuilder.build();
		}

	}

	public abstract IJsonValue build();

	@Override
	public void visit( boolean value ) {
		throwBuildException();
	}

	protected final void throwBuildException() {
		throw new UnsupportedOperationException( "Not available for builder "
				+ getClass().getSimpleName() );
	}

	@Override
	public void visit( Number value ) {
		throwBuildException();
	}

	@Override
	public void visit( String value ) {
		throwBuildException();
	}

	@Override
	public void visitNull() {
		throwBuildException();
	}

}
