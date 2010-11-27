package de.jbee.io;

public final class Read {

	private static final ReadEscapedUnicodeString UNICODE_STRING = new ReadEscapedUnicodeString();

	private Read() {
		// util
	}

	static final ICharScanner DIGITS = new ReadDigits();

	static final class ReadUniverse
			implements ICharScanner {

		private final String universe;

		ReadUniverse( String universe ) {
			super();
			this.universe = universe;
		}

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext() && universe.indexOf( in.peek() ) >= 0 ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadDigits
			implements ICharScanner {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext() && Character.isDigit( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadLetters
			implements ICharScanner {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext() && Character.isLetter( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadN
			implements ICharScanner {

		private final int n;

		ReadN( int n ) {
			super();
			this.n = n;
		}

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			for ( int i = 0; i < n; i++ ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadEscapedUnicodeString
			implements ICharScanner {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			Gobble.a( '"' ).process( in );
			while ( in.peek() != '"' ) {
				char c = in.next();
				if ( c == '\\' ) {
					c = in.next();
					switch ( c ) {
						case 'u':
							out.append( Character.toChars( Integer.parseInt( Read.toString( in,
									next( 4 ) ), 16 ) ) );
							break;
						case '"':
							out.append( '"' );
							break;
						case '\\':
							out.append( '\\' );
							break;
						case '/':
							out.append( '/' );
							break;
						case 'b':
							out.append( '\b' );
						case 'f':
							out.append( '\f' );
						case 'n':
							out.append( '\n' );
						case 'r':
							out.append( '\r' );
						case 't':
							out.append( '\t' );
						default:
							break;
					}
				} else {
					out.append( c );
				}
			}
			Gobble.a( '"' ).process( in );
		}
	}

	public static ICharScanner unicode() {
		return UNICODE_STRING;
	}

	public static ICharScanner universe( String universe ) {
		return new ReadUniverse( universe );
	}

	public static ICharScanner next( int n ) {
		return new ReadN( n );
	}

	public static String toString( ICharReader in, ICharScanner scanner ) {
		StringBuilder out = new StringBuilder();
		scanner.scan( in, CharWriter.of( out ) );
		return out.toString();
	}
}
