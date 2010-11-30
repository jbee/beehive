package de.jbee.io.json;

import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;

public final class JsonParser
		implements ICharScanner<IJsonProcessor> {

	private static final ICharScanner<IJsonProcessor> INSTANCE = new JsonParser( null );

	private final String member;

	private JsonParser( String member ) {
		super();
		this.member = member;
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
		out.process( type, member, CharScanner.procesable( in, type, CharScanner.of( type ) ) );
		Gobble.whitespace().process( in );
	}
}