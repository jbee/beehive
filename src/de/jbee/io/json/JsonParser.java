package de.jbee.io.json;

import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;
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
		implements ICharScanner<IJsonProcessor> {

	private static final ICharScanner<IJsonProcessor> INSTANCE = new JsonParser( null );

	private final String member;

	private JsonParser( String member ) {
		super();
		this.member = member;
	}

	public static IJson parse( ICharReader in ) {
		JsonBuilder builder = JsonBuilder.newInstance();
		getInstance().scan( in, builder );
		return builder.build();
	}

	public static ICharScanner<IJsonProcessor> getInstance() {
		return INSTANCE;
	}

	public static ICharScanner<IJsonProcessor> getInstance( String name ) {
		return name == null
			? INSTANCE
			: new JsonParser( name );
	}

	@Override
	public void scan( final ICharReader in, IJsonProcessor out ) {
		Gobble.whitespace().process( in );
		final JsonType type = JsonType.starts( in.peek() );
		out.process( type, member, CharScanner.processable( in, type, CharScanner.of( type ) ) );
		Gobble.whitespace().process( in );
	}
}