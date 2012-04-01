package de.jbee.io;

import static de.jbee.io.CharIs.ASCII_DIGIT;
import static de.jbee.io.CharIs.ASCII_LETTER;
import static de.jbee.io.CharIs.anyLetter;
import static de.jbee.io.CharIs.anyWhitespace;
import static de.jbee.io.CharIs.exact;
import static de.jbee.io.CharIs.in;
import static de.jbee.io.CharIs.not;
import static de.jbee.io.CharIs.or;
import static de.jbee.io.CharIs.type;

import java.util.InputMismatchException;

public final class Collect {

	private static final CharCollector UNI_STRING = new EscapedUnicodeStringCharCollector();

	private static final CharCollector LINE = until( '\n' );
	private static final CharCollector CDATA = new CDATACharCollector();
	private static final CharCollector UNI_DIGITS = whileItIs( type( Character.DECIMAL_DIGIT_NUMBER ) );
	private static final CharCollector UNI_LETTERS = whileItIs( anyLetter() );
	private static final CharCollector UNTIL_UNI_WHITESPACE = until( anyWhitespace() );

	public static final CharCollector ASCII_DIGITS = whileItIs( ASCII_DIGIT );
	public static final CharCollector ASCII_LETTERS = whileItIs( ASCII_LETTER );

	public static CharCollector next( int n ) {
		return new FixLengthCharCollector( n );
	}

	public static void toBuffer( CharReader in, StringBuilder out, CharScanner<CharWriter> scanner ) {
		ScanTo.appending( scanner ).scan( in, out );
	}

	public static String toString( CharReader in, CharScanner<CharWriter> scanner ) {
		StringBuilder out = new StringBuilder();
		toBuffer( in, out, scanner );
		return out.toString();
	}

	public static CharCollector cDATA() {
		return CDATA;
	}

	public static CharCollector letters() {
		return UNI_LETTERS;
	}

	public static CharCollector until( char endExclusive ) {
		return until( exact( endExclusive ) );
	}

	public static CharCollector until( CharPredicate predicate ) {
		return whileItIs( not( predicate ) );
	}

	public static CharCollector unicode() {
		return UNI_STRING;
	}

	public static CharCollector letterOrIn( String charset ) {
		return whileItIs( or( anyLetter(), in( charset ) ) );
	}

	public static CharCollector charset( String charset ) {
		return whileItIs( in( charset ) );
	}

	private static CharCollector whileItIs( CharPredicate predicate ) {
		return new WhileCharCollector( predicate );
	}

	public static CharCollector eitherOr( CharPredicate next, CharScanner<CharWriter> either,
			CharScanner<CharWriter> or ) {
		return new EitherOrCharCollector( next, either, or );
	}

	private Collect() {
		// util
	}

	private static final class CDATACharCollector
			implements CharCollector {

		CDATACharCollector() {
			//make visible
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			char quote = in.peek();
			if ( quote == '"' || quote == '\'' ) {
				until( quote ).scan( in, out );
				Gobble.a( quote ).process( in );
			} else {
				UNTIL_UNI_WHITESPACE.scan( in, out );
			}

		}
	}

	private static final class EscapedUnicodeStringCharCollector
			implements CharCollector {

		EscapedUnicodeStringCharCollector() {
			// make visible
		}

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

	private static final class FixLengthCharCollector
			implements CharCollector {

		private final int n;

		FixLengthCharCollector( int n ) {
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

	private static final class EitherOrCharCollector
			implements CharCollector {

		private final CharPredicate next;
		private final CharScanner<CharWriter> either;
		private final CharScanner<CharWriter> or;

		EitherOrCharCollector( CharPredicate next, CharScanner<CharWriter> either,
				CharScanner<CharWriter> or ) {
			super();
			this.next = next;
			this.either = either;
			this.or = or;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			if ( in.hasNext() ) {
				if ( next.isSuitable( in.peek() ) ) {
					either.scan( in, out );
				} else {
					or.scan( in, out );
				}
			}
		}

	}

	private static final class WhileCharCollector
			implements CharCollector {

		private final CharPredicate whileIt;

		WhileCharCollector( CharPredicate whileIt ) {
			super();
			this.whileIt = whileIt;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			while ( in.hasNext() && whileIt.isSuitable( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

}
