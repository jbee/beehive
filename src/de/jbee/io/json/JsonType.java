package de.jbee.io.json;

import java.util.InputMismatchException;

import de.jbee.io.CharProcessor;
import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;
import de.jbee.io.Collect;
import de.jbee.io.Gobble;
import de.jbee.io.ScanTo;

public enum JsonType
		implements CharScanner<JsonProcessor>, CharProcessor {

	NULL( "n", new JsonNullScanner(), Gobble.letters() ),
	BOOLEAN( "tf", new JsonBooleanScanner(), Gobble.letters() ),
	NUMBER( "-0123456789", new JsonNumberScanner(), Gobble.charset( JSON.NUMBER_CHARSET ) ),
	STRING( "\"'", new JsonStringScanner(), Gobble.unicode() ),
	ARRAY( "[", ScanTo.list( '[', JsonParser.getInstance(), ',', ']' ), Gobble.block( "[{", "}]",
			"'\"", Gobble.unicode() ) ),
	OBJECT( "{", ScanTo.list( '{', new JsonMemberScanner(), ',', '}' ), Gobble.block( "{[", "]}",
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

	private static final class JsonMemberScanner
			implements CharScanner<JsonProcessor> {

		JsonMemberScanner() {
			// make visible
		}

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			Gobble.whitespace().process( in );
			String name = Collect.toString( in, Collect.eitherOr( Collect.in( "\"'" ),
					Collect.unicode(), Collect.until( ':' ) ) );
			Gobble.aWhitespaced( ':' ).process( in );
			JsonParser.yieldInstance( name ).scan( in, out );
			Gobble.whitespace().process( in );
		}
	}

	private static final class JsonStringScanner
			implements CharScanner<JsonProcessor> {

		JsonStringScanner() {
			// make visible
		}

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			out.process( Collect.toString( in, Collect.unicode() ) );
		}
	}

	private static final class JsonNumberScanner
			implements CharScanner<JsonProcessor> {

		JsonNumberScanner() {
			// make visible
		}

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			out.process( JSON.parseNumber( Collect.toString( in,
					Collect.charset( JSON.NUMBER_CHARSET ) ) ) );
		}
	}

	private static final class JsonNullScanner
			implements CharScanner<JsonProcessor> {

		JsonNullScanner() {
			// make visible
		}

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			Gobble.just( "null" ).process( in );
			out.processNull();
		}
	}

	private static final class JsonBooleanScanner
			implements CharScanner<JsonProcessor> {

		JsonBooleanScanner() {
			// make visible
		}

		@Override
		public void scan( CharReader in, JsonProcessor out ) {
			boolean expectTrue = in.peek() == 't';
			Gobble.eitherOr( "true", "false" ).process( in );
			out.process( expectTrue );
		}
	}
}