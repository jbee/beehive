package de.jbee.io;

import java.util.InputMismatchException;

public final class Collect {

	private static final CharCollector UNICODE_STRING = new CollectEscapedUnicodeString();

	public static final CharCollector LINE = until( '\n' );
	public static final CharCollector CDDATA = new CollectCDATA();
	public static final CharCollector DIGITS = new CollectDigits();
	public static final CharCollector LETTERS = new CollectLetters();
	public static final CharCollector UNTIL_WHITESPACE = new CollectUntilWhitespace();

	public static CharCollector next( int n ) {
		return new ReadN( n );
	}

	public static void toBuffer( CharReader in, StringBuilder out,
			CharScanner<CharWriter> scanner ) {
		ScanTo.appending( scanner ).scan( in, out );
	}

	public static String toString( CharReader in, CharScanner<CharWriter> scanner ) {
		StringBuilder out = new StringBuilder();
		toBuffer( in, out, scanner );
		return out.toString();
	}

	public static CharCollector until( char endExclusive ) {
		return new CollectUntilCharacter( endExclusive );
	}

	public static CharCollector unicode() {
		return UNICODE_STRING;
	}

	public static CharCollector letterOrUniverse( String universe ) {
		return new CollectLetterOrUniverse( universe );
	}

	public static CharCollector universe( String universe ) {
		return new CollectUniverse( universe );
	}

	//TODO split decision based on a char from collect/scan so that both can be combined independently --> see CharPredicate
	public static CharCollector universeBranch( String universe,
			CharScanner<CharWriter> contained, CharScanner<CharWriter> notContained ) {
		return new CollectUniverseBranch( universe, contained, notContained );
	}

	public static <T> CharScanner<T> list( char open, CharScanner<T> element, char separator,
			char close ) {
		return new ListCharScanner<T>( open, element, separator, close );
	}

	private Collect() {
		// util
	}

	static final class ListCharScanner<T>
			implements CharScanner<T> {

		private final char open;
		private final CharScanner<T> element;
		private final char separator;
		private final char close;

		ListCharScanner( char open, CharScanner<T> element, char separator, char close ) {
			super();
			this.open = open;
			this.element = element;
			this.separator = separator;
			this.close = close;
		}

		public void scan( CharReader in, T out ) {
			Gobble.a( open ).process( in );
			if ( in.peek() != close ) {
				scanElement( in, out );
			}
			Gobble.a( close ).process( in );
		}

		private void scanElement( CharReader in, T out ) {
			element.scan( in, out );
			if ( in.peek() == separator ) {
				Gobble.a( separator ).process( in );
				scanElement( in, out );
			}
		}
	}

	static final class CollectCDATA
			implements CharCollector {

		@Override
		public void scan( CharReader in, CharWriter out ) {
			char quote = in.peek();
			if ( quote == '"' || quote == '\'' ) {
				until( quote ).scan( in, out );
				Gobble.a( quote ).process( in );
			} else {
				UNTIL_WHITESPACE.scan( in, out );
			}

		}
	}

	static final class CollectUntilCharacter
			implements CharCollector {

		final char endExclusive;

		CollectUntilCharacter( char endExclusive ) {
			super();
			this.endExclusive = endExclusive;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			while ( in.hasNext() && in.peek() != endExclusive ) {
				out.append( in.next() );
			}
		}
	}

	static final class CollectUntilWhitespace
			implements CharCollector {

		@Override
		public void scan( CharReader in, CharWriter out ) {
			while ( in.hasNext() && !Character.isWhitespace( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class CollectDigits
			implements CharCollector {

		@Override
		public void scan( CharReader in, CharWriter out ) {
			while ( in.hasNext() && Character.isDigit( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class CollectEscapedUnicodeString
			implements CharCollector {

		@Override
		public void scan( CharReader in, CharWriter out ) {
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
							out.append( Character.toChars( Integer.parseInt( Collect.toString( in,
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

	static final class CollectLetters
			implements CharCollector {

		@Override
		public void scan( CharReader in, CharWriter out ) {
			while ( in.hasNext() && Character.isLetter( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class ReadN
			implements CharCollector {

		private final int n;

		ReadN( int n ) {
			super();
			this.n = n;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			for ( int i = 0; i < n; i++ ) {
				out.append( in.next() );
			}
		}
	}

	static final class CollectLetterOrUniverse
			implements CharCollector {

		private final String universe;

		CollectLetterOrUniverse( String universe ) {
			super();
			this.universe = universe;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			while ( in.hasNext()
					&& ( Character.isLetter( in.peek() ) || universe.indexOf( in.peek() ) >= 0 ) ) {
				out.append( in.next() );
			}
		}
	}

	static final class CollectUniverseBranch
			implements CharCollector {

		private final String universe;
		private final CharScanner<CharWriter> contained;
		private final CharScanner<CharWriter> notContained;

		CollectUniverseBranch( String universe, CharScanner<CharWriter> contained,
				CharScanner<CharWriter> notContained ) {
			super();
			this.universe = universe;
			this.contained = contained;
			this.notContained = notContained;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			if ( in.hasNext() ) {
				if ( universe.indexOf( in.peek() ) >= 0 ) {
					contained.scan( in, out );
				} else {
					notContained.scan( in, out );
				}
			}
		}

	}

	static final class CollectUniverse
			implements CharCollector {

		private final String universe;

		CollectUniverse( String universe ) {
			super();
			this.universe = universe;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			while ( in.hasNext() && universe.indexOf( in.peek() ) >= 0 ) {
				out.append( in.next() );
			}
		}
	}

}
