package de.jbee.io;

import java.util.InputMismatchException;

public final class Read {

	private static final ICharScanner<ICharWriter> UNICODE_STRING = new ReadEscapedUnicodeString();

	public static final ICharScanner<ICharWriter> LINE = until( '\n' );
	public static final ICharScanner<ICharWriter> CDDATA = new ReadCDATA();
	public static final ICharScanner<ICharWriter> DIGITS = new ReadDigits();
	public static final ICharScanner<ICharWriter> LETTERS = new ReadLetters();
	public static final ICharScanner<ICharWriter> UNTIL_WHITESPACE = new ReadUntilWhitespace();

	public static ICharScanner<ICharWriter> next( int n ) {
		return new ReadN( n );
	}

	public static void toBuffer( ICharReader in, StringBuilder out,
			ICharScanner<ICharWriter> scanner ) {
		CharScanner.appending( scanner ).scan( in, out );
	}

	public static String toString( ICharReader in, ICharScanner<ICharWriter> scanner ) {
		StringBuilder out = new StringBuilder();
		toBuffer( in, out, scanner );
		return out.toString();
	}

	public static ICharScanner<ICharWriter> until( char endExclusive ) {
		return new ReadUntilCharacter( endExclusive );
	}

	public static ICharScanner<ICharWriter> unicode() {
		return UNICODE_STRING;
	}

	public static ICharScanner<ICharWriter> letterOrUniverse( String universe ) {
		return new ReadLetterOrUniverse( universe );
	}

	public static ICharScanner<ICharWriter> universe( String universe ) {
		return new ReadUniverse( universe );
	}

	public static <T> ICharScanner<T> list( char open, ICharScanner<T> element, char separator,
			char close ) {
		return new ListCharScanner<T>( open, element, separator, close );
	}

	private Read() {
		// util
	}

	static final class ListCharScanner<T>
			implements ICharScanner<T> {

		private final char open;
		private final ICharScanner<T> element;
		private final char separator;
		private final char close;

		ListCharScanner( char open, ICharScanner<T> element, char separator, char close ) {
			super();
			this.open = open;
			this.element = element;
			this.separator = separator;
			this.close = close;
		}

		public void scan( ICharReader in, T out ) {
			Gobble.a( open ).process( in );
			if ( in.peek() != close ) {
				scanElement( in, out );
			}
			Gobble.a( close ).process( in );
		}

		private void scanElement( ICharReader in, T out ) {
			element.scan( in, out );
			if ( in.peek() == separator ) {
				Gobble.a( separator ).process( in );
				scanElement( in, out );
			}
		}
	}

	static final class ReadCDATA
			implements ICharScanner<ICharWriter> {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			char quote = in.peek();
			if ( quote == '"' || quote == '\'' ) {
				until( quote ).scan( in, out );
				Gobble.a( quote ).process( in );
			} else {
				UNTIL_WHITESPACE.scan( in, out );
			}

		}
	}

	static final class ReadUntilCharacter
			implements ICharScanner<ICharWriter> {

		final char endExclusive;

		ReadUntilCharacter( char endExclusive ) {
			super();
			this.endExclusive = endExclusive;
		}

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext() && in.peek() != endExclusive ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadUntilWhitespace
			implements ICharScanner<ICharWriter> {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext() && !Character.isWhitespace( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadDigits
			implements ICharScanner<ICharWriter> {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext() && Character.isDigit( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadEscapedUnicodeString
			implements ICharScanner<ICharWriter> {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			char frameOpener = in.peek();
			if ( "\"'".indexOf( frameOpener ) < 0 ) {
				throw new InputMismatchException( "Not a valid unicode string opener: "
						+ frameOpener );
			}
			Gobble.a( frameOpener ).process( in );
			while ( in.peek() != frameOpener ) {
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
							break;
						case 'f':
							out.append( '\f' );
							break;
						case 'n':
							out.append( '\n' );
							break;
						case 'r':
							out.append( '\r' );
							break;
						case 't':
							out.append( '\t' );
							break;
						case '\'':
							if ( frameOpener == '\'' ) {
								out.append( '\'' );
							}
							break;
						default:
							break;
					}
				} else {
					out.append( c );
				}
			}
			Gobble.a( frameOpener ).process( in );
		}
	}

	static final class ReadLetters
			implements ICharScanner<ICharWriter> {

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext() && Character.isLetter( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadN
			implements ICharScanner<ICharWriter> {

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

	static final class ReadLetterOrUniverse
			implements ICharScanner<ICharWriter> {

		private final String universe;

		ReadLetterOrUniverse( String universe ) {
			super();
			this.universe = universe;
		}

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			while ( in.hasNext()
					&& ( Character.isLetter( in.peek() ) || universe.indexOf( in.peek() ) >= 0 ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadUniverse
			implements ICharScanner<ICharWriter> {

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

}
