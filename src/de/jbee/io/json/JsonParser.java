package de.jbee.io.json;

import de.jbee.io.CharScanner;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;

/**
 * Provides the missing link between the different {@link ICharScanner}s provided by each
 * {@link JsonType} to combine them to a full featured JSON parser.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see JsonType
 */
public final class JsonParser
		implements ICharScanner<JsonProcessor> {

	private static final ICharScanner<JsonProcessor> INSTANCE = newInstance( null );

	private final String member;

	private JsonParser( String member ) {
		super();
		this.member = member;
	}

	private static ICharScanner<JsonProcessor> newInstance( String member ) {
		return CharScanner.trimming( new JsonParser( member ) );
	}

	public static ICharScanner<JsonProcessor> getInstance() {
		return INSTANCE;
	}

	public static ICharScanner<JsonProcessor> yieldInstance( String name ) {
		return name == null
			? INSTANCE
			: newInstance( name );
	}

	@Override
	public void scan( final ICharReader in, JsonProcessor out ) {
		final JsonType type = JsonType.starts( in.peek() );
		out.process( type, member, CharScanner.processing( in, type, CharScanner.of( type ) ) );
	}
}