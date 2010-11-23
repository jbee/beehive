package de.jbee.io.json;

public final class JsonString
		implements IJsonValue {

	private final String value;

	public JsonString( String value ) {
		super();
		this.value = value;
	}

	@Override
	public void pass( IJsonTreeVisitor visitor ) {
		visitor.visit( this );
	}

	@Override
	public void passChildren( IJsonTreeVisitor visitor ) {
		visitor.visit( value );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( value.length() );
		escape( value, sb );
		return sb.toString();
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

}
