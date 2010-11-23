package de.jbee.io.json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.jbee.io.IProcessable;

public abstract class JsonTreeBuilder
		implements IJsonProcessor {

	public static JsonTreeBuilder newInstance() {
		return new RootJsonTreeBuilder();
	}

	static class ArrayJsonTreeBuilder
			extends JsonTreeBuilder {

		private final List<IJsonValue> elements = new LinkedList<IJsonValue>();

		@Override
		public void decideOn( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
			JsonTreeBuilder builder = forType( type );
			element.processBy( builder );
			elements.add( builder.build() );
		}

		@Override
		public IJsonValue build() {
			return new JsonArray( elements.toArray( new IJsonValue[elements.size()] ) );
		}
	}

	static final class ValueJsonTreeBuilder
			extends JsonTreeBuilder {

		private IJsonValue value;

		@Override
		public void decideOn( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
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

	static class ObjectJsonTreeBuilder
			extends JsonTreeBuilder {

		private final Map<String, IJsonValue> members = new HashMap<String, IJsonValue>();

		@Override
		public void decideOn( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
			JsonTreeBuilder builder = forType( type );
			element.processBy( builder );
			members.put( name, builder.build() );
		}

		@Override
		public IJsonValue build() {
			return new JsonObject( members );
		}

	}

	static JsonTreeBuilder forType( JsonType type ) {
		if ( !type.isComposite() ) {
			return new ValueJsonTreeBuilder();
		}
		return type == JsonType.ARRAY
			? new ArrayJsonTreeBuilder()
			: new ObjectJsonTreeBuilder();
	}

	static class RootJsonTreeBuilder
			extends JsonTreeBuilder {

		private JsonTreeBuilder rootBuilder;

		@Override
		public void decideOn( JsonType type, String name, IProcessable<IJsonProcessor> element ) {
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
