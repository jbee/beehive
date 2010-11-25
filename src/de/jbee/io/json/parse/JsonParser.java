package de.jbee.io.json.parse;


import de.jbee.io.CharReader;
import de.jbee.io.Gobble;
import de.jbee.io.ICharReader;
import de.jbee.io.IProcessable;
import de.jbee.io.json.IJsonProcessor;
import de.jbee.io.json.JsonType;
import de.jbee.io.json.JsonValue;

public final class JsonParser {

	private static final String NUMBER_UNIVERSE = ".0123456789+-eE";

	private final CharReader in;

	public JsonParser( ICharReader in ) {
		super();
		this.in = new CharReader( in );
	}

	public void parse( IJsonProcessor out ) {
		parseValue( out );
	}

	private void parseValue( IJsonProcessor out ) {
		parseValue( (String) null, out );
	}

	void parseValue( JsonType type, IJsonProcessor out ) {
		switch ( type ) {
			case OBJECT:
				parseObject( out );
				break;
			case ARRAY:
				parseArray( out );
				break;
			case STRING:
				parseString( out );
				break;
			case BOOLEAN:
				parseBoolean( out );
				break;
			case NULL:
				parseNull( out );
				break;
			case NUMBER:
				parseNumber( out );
		}
	}

	private void parseObject( IJsonProcessor out ) {
		in.once( Gobble.aWhitespaced( '{' ) );
		parseMembers( out );
		in.once( Gobble.aWhitespaced( '}' ) );
	}

	private void parseMembers( IJsonProcessor out ) {
		parseMember( out );
		in.once( Gobble.whitespace() );
		if ( in.peek() == ',' ) {
			in.once( Gobble.a( ',' ) );
			parseMembers( out );
		}
	}

	private void parseMember( IJsonProcessor out ) {
		in.once( Gobble.whitespace() );
		String name = readString();
		in.once( Gobble.aWhitespaced( ':' ) );
		parseValue( name, out );
	}

	private void parseValue( String name, IJsonProcessor out ) {
		in.once( Gobble.whitespace() );
		final JsonType type = JsonType.valueOf( in.peek() );
		out.process( type, name, new IProcessable<IJsonProcessor>() {

			@Override
			public void processBy( IJsonProcessor processor ) {
				parseValue( type, processor );
			}

			@Override
			public void discardBy( IJsonProcessor processor ) {
				gobbleValue( type );
			}
		} );
		in.once( Gobble.whitespace() );
	}

	private void parseArray( IJsonProcessor out ) {
		in.once( Gobble.aWhitespaced( '[' ) );
		parseElement( out );
		in.once( Gobble.aWhitespaced( ']' ) );
	}

	private void parseElement( IJsonProcessor out ) {
		parseValue( out );
		if ( in.peek() == ',' ) {
			in.once( Gobble.a( ',' ) );
			parseElement( out );
		}
	}

	private void parseString( IJsonProcessor out ) {
		out.visit( readString() );
	}

	private void parseNumber( IJsonProcessor out ) {
		StringBuilder b = new StringBuilder( 10 );
		b.append( in.next() );
		while ( in.hasNext() && NUMBER_UNIVERSE.indexOf( in.peek() ) >= 0 ) {
			b.append( in.next() );
		}
		out.visit( JsonValue.number( b.toString() ) );
	}

	private void parseBoolean( IJsonProcessor out ) {
		boolean expectTrue = in.peek() == 't';
		in.once( Gobble.one( expectTrue
			? "true"
			: "false" ) );
		out.visit( expectTrue );
	}

	private void parseNull( IJsonProcessor out ) {
		in.once( Gobble.one( "null" ) );
		out.visitNull();
	}

	void gobbleValue( JsonType type ) {
		switch ( type ) {
			case ARRAY:
				in.once( Gobble.block( "[{", "}]", '"', Gobble.unicode() ) );
				break;
			case BOOLEAN:
			case NULL:
				in.once( Gobble.letters() );
				break;
			case NUMBER:
				in.once( Gobble.universe( NUMBER_UNIVERSE ) );
				break;
			case STRING:
				in.once( Gobble.unicode() );
				break;
			case OBJECT:
				in.once( Gobble.block( "{[", "]}", '"', Gobble.unicode() ) );
		}
	}

	private char[] read( int n ) {
		char[] chars = new char[n];
		for ( int i = 0; i < n; i++ ) {
			chars[i] = in.next();
		}
		return chars;
	}

	private String readString() {
		in.once( Gobble.a( '"' ) );
		StringBuilder b = new StringBuilder();
		while ( in.peek() != '"' ) {
			char c = in.next();
			if ( c == '\\' ) {
				c = in.next();
				switch ( c ) {
					case 'u':
						b.append( Character.toChars( Integer.parseInt( String.valueOf( read( 4 ) ),
								16 ) ) );
						break;
					case '"':
						b.append( '"' );
						break;
					case '\\':
						b.append( '\\' );
						break;
					case '/':
						b.append( '/' );
						break;
					case 'b':
						b.append( '\b' );
					case 'f':
						b.append( '\f' );
					case 'n':
						b.append( '\n' );
					case 'r':
						b.append( '\r' );
					case 't':
						b.append( '\t' );
					default:
						break;
				}
			} else {
				b.append( c );
			}
		}
		in.once( Gobble.a( '"' ) );
		return b.toString();
	}

}
