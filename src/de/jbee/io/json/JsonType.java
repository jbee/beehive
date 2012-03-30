package de.jbee.io.json;

import java.util.InputMismatchException;

import de.jbee.io.Gobble;
import de.jbee.io.CharProcessor;
import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.Collect;
import de.jbee.io.ScanTo.UtilisedCharScanner;

public enum JsonType
		implements CharScanner<JsonProcessor>, CharProcessor {

	NULL( "n", new JsonNullScanner(), Gobble.letters() ),
	BOOLEAN( "tf", new JsonBooleanScanner(), Gobble.letters() ),
	NUMBER( "-0123456789", new JsonNumberScanner(), Gobble.universe( JSON.NUMBER_UNIVERSE ) ),
	STRING( "\"'", new JsonStringScanner(), Gobble.unicode() ),
	ARRAY( "[", Collect.list( '[', JsonParser.getInstance(), ',', ']' ), Gobble.block( "[{", "}]",
			"'\"", Gobble.unicode() ) ),
	OBJECT( "{", Collect.list( '{', new JsonMemberScanner(), ',', '}' ), Gobble.block( "{[", "]}",
			"'\"", Gobble.unicode() ) );

	private static final int OPENER_OFFSET = '"';
	private static final JsonType[] TYPE_LOOKUP = new JsonType['{' - OPENER_OFFSET + 1];

	static {
		for ( JsonType type : values() ) {
			for ( char opener : type.openingUniverse.toCharArray() ) {
				TYPE_LOOKUP[opener - OPENER_OFFSET] = type;
			}
		}
	}

	private final String openingUniverse;
	private final CharProcessor gobbler;
	private final CharScanner<JsonProcessor> scanner;

	private JsonType( String openingUniverse, CharScanner<JsonProcessor> scanner,
			CharProcessor gobbler ) {
		this.gobbler = gobbler;
		this.openingUniverse = openingUniverse;
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
	public void scan( CharReader in, JsonProcessor out ) {
		scanner.scan( in, out );
	}

	/**
	 * Will {@link Gobble} up the element from <code>in</code>.
	 */
	@Override
	public void process( CharReader in ) {
		gobbler.process( in );
	}

	static final class JsonMemberScanner
			extends UtilisedCharScanner<JsonProcessor> {

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			once( Gobble.whitespace(), in );
			String name = Collect.toString( in, Collect.universeBranch( "\"'", Collect.unicode(),
					Collect.until( ':' ) ) );
			once( Gobble.aWhitespaced( ':' ), in );
			JsonParser.yieldInstance( name ).scan( in, out );
			once( Gobble.whitespace(), in );
		}
	}

	static final class JsonStringScanner
			implements CharScanner<JsonProcessor> {

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			out.process( Collect.toString( in, Collect.unicode() ) );
		}
	}

	static final class JsonNumberScanner
			implements CharScanner<JsonProcessor> {

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			out.process( JSON.parseNumber( Collect.toString( in, Collect.universe( JSON.NUMBER_UNIVERSE ) ) ) );
		}
	}

	static final class JsonNullScanner
			extends UtilisedCharScanner<JsonProcessor> {

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			once( Gobble.just( "null" ), in );
			out.processNull();
		}
	}

	static final class JsonBooleanScanner
			implements CharScanner<JsonProcessor> {

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			boolean expectTrue = in.peek() == 't';
			Gobble.eitherOr( "true", "false" ).process( in );
			out.process( expectTrue );
		}
	}
}