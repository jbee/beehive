package de.jbee.io.json;

import java.util.InputMismatchException;

import de.jbee.io.Gobble;
import de.jbee.io.ICharProcessor;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;
import de.jbee.io.Read;

public enum JsonType
		implements ICharScanner<IJsonProcessor>, ICharProcessor {

	NULL( "n", new JsonNullScanner(), Gobble.letters() ),
	BOOLEAN( "tf", new JsonBooleanScanner(), Gobble.letters() ),
	NUMBER( "-0123456789", new JsonNumberScanner(), Gobble.universe( Json.NUMBER_UNIVERSE ) ),
	STRING( "\"'", new JsonStringScanner(), Gobble.unicode() ),
	ARRAY( "[", Read.list( '[', JsonParser.getInstance(), ',', ']' ), Gobble.block( "[{", "}]",
			"'\"", Gobble.unicode() ) ),
	OBJECT( "{", Read.list( '{', new JsonMemberScanner(), ',', '}' ), Gobble.block( "{[", "]}",
			"'\"", Gobble.unicode() ) );

	private static final int OPENER_OFFSET = '"';
	private static final JsonType[] TYPE_LOOKUP = new JsonType['{' - OPENER_OFFSET + 1];

	static {
		for ( JsonType type : values() ) {
			for ( char opener : type.openUniverse.toCharArray() ) {
				TYPE_LOOKUP[opener - OPENER_OFFSET] = type;
			}
		}
	}

	private final String openUniverse;
	private final ICharProcessor gobbler;
	private final ICharScanner<IJsonProcessor> scanner;

	private JsonType( String openUniverse, ICharScanner<IJsonProcessor> scanner,
			ICharProcessor gobbler ) {
		this.gobbler = gobbler;
		this.openUniverse = openUniverse;
		this.scanner = scanner;
	}

	public static JsonType starts( char firstOfValue ) {
		JsonType res = at( firstOfValue );
		if ( res == null ) {
			throw new InputMismatchException( "Unexspected token: " + firstOfValue );
		}
		return res;
	}

	private static JsonType at( char firstOfValue ) {
		final int index = firstOfValue - OPENER_OFFSET;
		return ( index < 0 || index > TYPE_LOOKUP.length )
			? null
			: TYPE_LOOKUP[index];
	}

	public boolean isComposite() {
		return this == OBJECT || this == ARRAY;
	}

	@Override
	public void scan( ICharReader in, IJsonProcessor out ) {
		scanner.scan( in, out );
	}

	/**
	 * Will {@link Gobble} up the element from <code>in</code>.
	 */
	@Override
	public void process( ICharReader in ) {
		gobbler.process( in );
	}

	static final class JsonMemberScanner
			implements ICharScanner<IJsonProcessor> {

		@Override
		public void scan( ICharReader in, IJsonProcessor out ) {
			Gobble.whitespace().process( in );
			String name = Read.toString( in, Read.unicode() );
			Gobble.aWhitespaced( ':' ).process( in );
			JsonParser.getInstance( name ).scan( in, out );
			Gobble.whitespace().process( in );
		}
	}

	static final class JsonStringScanner
			implements ICharScanner<IJsonProcessor> {

		@Override
		public void scan( ICharReader in, IJsonProcessor out ) {
			out.process( Read.toString( in, Read.unicode() ) );
		}
	}

	static final class JsonNumberScanner
			implements ICharScanner<IJsonProcessor> {

		@Override
		public void scan( ICharReader in, IJsonProcessor out ) {
			out.process( Json.number( Read.toString( in, Read.universe( Json.NUMBER_UNIVERSE ) ) ) );
		}
	}

	static final class JsonNullScanner
			implements ICharScanner<IJsonProcessor> {

		@Override
		public void scan( ICharReader in, IJsonProcessor out ) {
			Gobble.just( "null" ).process( in );
			out.processNull();
		}
	}

	static final class JsonBooleanScanner
			implements ICharScanner<IJsonProcessor> {

		@Override
		public void scan( ICharReader in, IJsonProcessor out ) {
			boolean expectTrue = in.peek() == 't';
			Gobble.eitherOr( "true", "false" ).process( in );
			out.process( expectTrue );
		}
	}
}