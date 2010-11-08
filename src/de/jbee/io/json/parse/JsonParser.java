package de.jbee.io.json.parse;

import de.jbee.io.Gobble;
import de.jbee.io.ICharProcessor;
import de.jbee.io.ICharReader;
import de.jbee.io.json.IJsonProcessor;
import de.jbee.io.json.JsonType;

public final class JsonParser {

	private static final String NUMBER_TOKEN = "0123456789+-eE";

	private final ICharReader in;

	private JsonParser( ICharReader in ) {
		super();
		this.in = in;
	}

	public void parse( IJsonProcessor out ) {
		in( Gobble.whitespace() );
		parseValue( out );
	}

	private void in( ICharProcessor... processors ) {
		for ( ICharProcessor p : processors ) {
			p.process( in );
		}
	}

	private void parseValue( IJsonProcessor out ) {
		parseValue( (String) null, out );
	}

	private void parseValue( JsonType type, IJsonProcessor out ) {
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
		in( Gobble.aWhitespaced( '{' ) );
		parseMembers( out );
	}

	private void parseMembers( IJsonProcessor out ) {
		gobbleWhitespace();
		parseMember( out );
		gobbleWhitespace();
		if ( in.next() == ',' ) {
			parseMembers( out );
		}
	}

	private void parseMember( IJsonProcessor out ) {
		String name = readString();
		gobbleWhitespace();
		gobble1(); // ':'
		parseValue( name, out );
	}

	private void parseValue( String name, IJsonProcessor out ) {
		gobbleWhitespace();
		JsonType type = JsonType.valueOf( in.peek() );
		out = out.begin( type, name );
		if ( out.skip( type ) ) {
			gobbleValue( type );
		} else {
			parseValue( type, out );
			out = out.end( type );
		}
	}

	private void parseArray( IJsonProcessor out ) {
		gobble1(); // '['
		parseElement( out );
		gobble1(); // ']'
	}

	private void parseElement( IJsonProcessor out ) {
		parseValue( out );
		if ( in.next() == ',' ) {
			parseElement( out );
		}
	}

	private void parseString( IJsonProcessor out ) {
		out.visit( readString() );
	}

	private void parseNumber( IJsonProcessor out ) {
		StringBuilder b = new StringBuilder( 10 );
		b.append( in.next() );
		while ( in.hasNext() && NUMBER_TOKEN.indexOf( in.peek() ) >= 0 ) {
			b.append( in.next() );
		}
		String value = b.toString();
		out.visit( value.indexOf( '.' ) >= 0
			? Double.parseDouble( value )
			: Long.parseLong( value ) );
	}

	private void parseBoolean( IJsonProcessor out ) {
		boolean expectTrue = in.peek() == 't';
		gobble( expectTrue
			? "true"
			: "false" );
		out.visit( expectTrue );
	}

	private void parseNull( IJsonProcessor out ) {
		gobble( "null" );
		out.visitNull();
	}

	private void gobble( String expected ) {
		for ( char c : expected.toCharArray() ) {
			ensure( c );
			gobble1();
		}
	}

	private void gobbleNumber() {
		while ( in.hasNext() && NUMBER_TOKEN.indexOf( in.peek() ) >= 0 ) {
			in.next();
		}
	}

	private void gobbleWhitespace() {
		while ( in.hasNext() && Character.isWhitespace( in.peek() ) ) {
			in.next();
		}
	}

	private void gobbleValue( JsonType type ) {
		switch ( type ) {
			case ARRAY:
				gobble1();
				gobbleSameLevelUntil( ']' );
				break;
			case BOOLEAN:
			case NULL:
				gobbleLetters();
				break;
			case NUMBER:
				gobbleNumber();
				break;
			case STRING:
				gobbleString();
				break;
			case OBJECT:
				gobble1();
				gobbleSameLevelUntil( '}' );
		}
	}

	private void gobbleSameLevelUntil( char end ) {
		int objectLevel = 0;
		int arrayLevel = 0;
		char c = in.peek();
		while ( objectLevel > 0 || arrayLevel > 0 || c != end ) {
			if ( c == '{' ) {
				++objectLevel;
			}
			if ( c == '}' ) {
				--objectLevel;
			}
			if ( c == '[' ) {
				++arrayLevel;
			}
			if ( c == ']' ) {
				--arrayLevel;
			}
			if ( c == '"' ) {
				gobbleString();
			}
			c = in.next();
		}
	}

	private void gobble1() {
		in.next();
	}

	private void gobble( int n ) {
		for ( int i = 0; i < n; i++ ) {
			gobble1();
		}
	}

	private void gobbleLetters() {
		while ( in.hasNext() && Character.isLetter( in.peek() ) ) {
			in.next();
		}
	}

	private void gobbleString() {
		gobble1(); // opening "
		while ( in.peek() != '"' ) {
			char c = in.next();
			if ( c == '\\' ) {
				c = in.next(); // gobble the escaped char
				if ( c == 'u' ) { // if it was u
					gobble( 4 ); // gobble following 4 digit hex number
				}
			}
		}
		gobble1(); // closing "
	}

	private char[] read( int n ) {
		char[] chars = new char[n];
		for ( int i = 0; i < n; i++ ) {
			chars[i] = in.next();
		}
		return chars;
	}

	private String readString() {
		ensure( '"' );
		gobble1();
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
		ensure( '"' );
		gobble1();
		return b.toString();
	}

	private void ensure( char c ) {

	}
}
