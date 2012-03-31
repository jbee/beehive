package de.jbee.io.json;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import de.jbee.io.CharIO;
import de.jbee.io.CharReader;
import de.jbee.io.ProcessableBy;
import de.jbee.io.ProcessableBy.NoopDiscardingProcessableBy;

/**
 * Util to work with JSON data.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public final class JSON {

	public static final String NUMBER_CHARSET = ".0123456789+-eE";

	public static Json NULL = JsonNull.OBJECT;

	private JSON() {
		// util
	}

	public static Json parse( CharReader in ) {
		JsonTreeBuilder builder = JsonTreeBuilder.newInstance();
		JsonParser.getInstance().scan( in, builder );
		return builder.build();
	}

	public static Json parse( String in ) {
		return parse( CharIO.reader( new StringReader( in ) ) );
	}

	public static Json json( String value ) {
		return JsonString.json( value );
	}

	public static Json json( Boolean value ) {
		return JsonBoolean.json( value );
	}

	public static Json json( Number value ) {
		return JsonNumber.json( value );
	}

	public static Number parseNumber( String value ) {
		if ( value.indexOf( '.' ) >= 0 ) {
			if ( value.length() > 14 || value.indexOf( 'e' ) >= 0 || value.indexOf( 'E' ) >= 0 ) {
				return new BigDecimal( value );
			}
			return value.length() < 7
				? Float.valueOf( value )
				: Double.valueOf( value );
		}
		if ( value.length() < 10 ) {
			return Integer.valueOf( value );
		}
		if ( value.length() < 19 ) {
			return Long.valueOf( value );
		}
		return new BigInteger( value );
	}

	static String escape( String s ) {
		StringBuilder b = new StringBuilder( s.length() );
		escape( s, b );
		return b.toString();
	}

	static void escape( String s, StringBuilder sb ) {
		for ( int i = 0; i < s.length(); i++ ) {
			sb.append( escape( s.charAt( i ) ) );
		}
	}

	static String escape( char c ) {
		switch ( c ) {
			case '"':
				return "\\\"";
			case '\\':
				return "\\\\";
			case '\b':
				return "\\b";
			case '\f':
				return "\\f";
			case '\n':
				return "\\n";
			case '\r':
				return "\\r";
			case '\t':
				return "\\t";
			case '/':
				return "\\/";
			default:
				//Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ( ( c >= '\u0000' && c <= '\u001F' ) || ( c >= '\u007F' && c <= '\u009F' )
						|| ( c >= '\u2000' && c <= '\u20FF' ) ) {
					String ss = Integer.toHexString( c ).toUpperCase();
					while ( ss.length() < 4 ) {
						ss = '0' + ss;
					}
					return "\\u" + ss;
				}
				return String.valueOf( c );
		}
	}

	private static final ProcessableBy<JsonProcessor> PROCESS_NULL = new ProcessJsonNull();

	public static ProcessableBy<JsonProcessor> element( String value ) {
		return value == null
			? PROCESS_NULL
			: new ProcessJsonString( value );
	}

	public static ProcessableBy<JsonProcessor> element( Number value ) {
		return value == null
			? PROCESS_NULL
			: new ProcessJsonNumber( value );
	}

	public static ProcessableBy<JsonProcessor> element( Boolean value ) {
		return value == null
			? PROCESS_NULL
			: new ProcessJsonBoolean( value );
	}

	public static void processAsElement( JsonProcessor processor, String name, String value ) {
		processor.process( type( JsonType.STRING, value ), name, element( value ) );
	}

	public static void processAsElement( JsonProcessor processor, String name, Number value ) {
		processor.process( type( JsonType.NUMBER, value ), name, element( value ) );
	}

	public static void processAsElement( JsonProcessor processor, String name, Boolean value ) {
		processor.process( type( JsonType.BOOLEAN, value ), name, element( value ) );
	}

	private static JsonType type( JsonType type, Object value ) {
		return value == null
			? JsonType.NULL
			: type;
	}

	private static final class ProcessJsonString
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		private final String value;

		ProcessJsonString( String value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.process( value );
		}
	}

	private static final class ProcessJsonNumber
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		private final Number value;

		ProcessJsonNumber( Number value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.process( value );
		}
	}

	private static final class ProcessJsonBoolean
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		private final boolean value;

		ProcessJsonBoolean( boolean value ) {
			super();
			this.value = value;
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.process( value );
		}
	}

	private static final class ProcessJsonNull
			extends NoopDiscardingProcessableBy<JsonProcessor> {

		ProcessJsonNull() {
			// make visible
		}

		@Override
		public void processBy( JsonProcessor processor ) {
			processor.processNull();
		}
	}
}
