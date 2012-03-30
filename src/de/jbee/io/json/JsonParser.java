package de.jbee.io.json;

import de.jbee.io.ScanTo;
import de.jbee.io.CharReader;
import de.jbee.io.CharScanner;

/**
 * Provides the missing link between the different {@link CharScanner}s provided by each
 * {@link JsonType} to combine them to a full featured JSON parser.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see JsonType
 */
public final class JsonParser
		implements CharScanner<JsonProcessor> {

	private static final CharScanner<JsonProcessor> INSTANCE = newInstance( null );

	private final String member;

	private JsonParser( String member ) {
		super();
		this.member = member;
	}

	private static CharScanner<JsonProcessor> newInstance( String member ) {
		return ScanTo.trimming( new JsonParser( member ) );
	}

	public static CharScanner<JsonProcessor> getInstance() {
		return INSTANCE;
	}

	//OPEN instead of a String we could ask for a CharCollector for that name
	public static CharScanner<JsonProcessor> yieldInstance( String name ) {
		return name == null
			? INSTANCE
			: newInstance( name );
	}

	@Override
	public void scan( final CharReader in, JsonProcessor out ) {
		final JsonType type = JsonType.starts( in.peek() );
		out.process( type, member, ScanTo.processing( in, type, ScanTo.scansTo( type ) ) );
	}
}