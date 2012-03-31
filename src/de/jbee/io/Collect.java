package de.jbee.io;

import java.util.InputMismatchException;

public final class Collect {

	private static final CharPredicate ANY_LETTER = new LetterCharPredicate();
	private static final CharPredicate ANY_WHITESPACE = new WhitespaceCharPredicate();

	private static final CharCollector UNI_STRING = new EscapedUnicodeStringCharCollector();

	public static final CharCollector LINE = until( '\n' );
	public static final CharCollector CDDATA = new CDATACharCollector();
	public static final CharCollector UNI_DIGITS = is( type( Character.DECIMAL_DIGIT_NUMBER ) );
	public static final CharCollector UNI_LETTERS = is( anyLetter() );
	public static final CharCollector UNTIL_UNI_WHITESPACE = until( anyWhitespace() );

	public static final CharCollector ASCII_DIGITS = is( in( '0', '9' ) );
	public static final CharCollector ASCII_LETTERS = is( or( in( 'a', 'z' ), in( 'A', 'Z' ) ) );

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

	public static CharCollector until( char endExclusive ) {
		return is( not( exact( endExclusive ) ) );
	}

	public static CharCollector until( CharPredicate predicate ) {
		return is( not( predicate ) );
	}

	public static CharCollector unicode() {
		return UNI_STRING;
	}

	public static CharCollector letterOrIn( String charset ) {
		return new WhileCharCollector( or( anyLetter(), in( charset ) ) );
	}

	public static CharCollector charset( String charset ) {
		return is( in( charset ) );
	}

	private static CharCollector is( CharPredicate predicate ) {
		return new WhileCharCollector( predicate );
	}

	public static CharCollector eitherOr( CharPredicate whileIt, CharScanner<CharWriter> either,
			CharScanner<CharWriter> or ) {
		return new EitherOrCharCollector( whileIt, either, or );
	}

	public static CharPredicate exact( char exact ) {
		return new ExactCharPredicate( exact );
	}

	public static CharPredicate in( String charset ) {
		return new InCharsetCharPredicate( charset );
	}

	public static CharPredicate in( char lower, char upper ) {
		return lower == upper
			? exact( lower )
			: lower < upper
				? new RangeCharPredictae( lower, upper )
				: new RangeCharPredictae( upper, lower );
	}

	public static CharPredicate type( byte type ) {
		return new CharacterTypeCharPredicate( type );
	}

	public static CharPredicate and( CharPredicate left, CharPredicate right ) {
		return new AndCharPredicate( left, right );
	}

	public static CharPredicate or( CharPredicate left, CharPredicate right ) {
		return new OrCharPredicate( left, right );
	}

	public static CharPredicate not( CharPredicate negated ) {
		if ( negated instanceof NotCharPredicate ) {
			return ( (NotCharPredicate) negated ).negated;
		}
		return new NotCharPredicate( negated );
	}

	public static CharPredicate anyLetter() {
		return ANY_LETTER;
	}

	public static CharPredicate anyWhitespace() {
		return ANY_WHITESPACE;
	}

	private Collect() {
		// util
	}

	private static final class WhitespaceCharPredicate
			implements CharPredicate {

		WhitespaceCharPredicate() {
			//make visible
		}

		@Override
		public boolean is( char c ) {
			return Character.isWhitespace( c );
		}

	}

	private static final class LetterCharPredicate
			implements CharPredicate {

		LetterCharPredicate() {
			//make visible
		}

		@Override
		public boolean is( char c ) {
			return Character.isLetter( c );
		}

	}

	private static final class OrCharPredicate
			implements CharPredicate {

		private final CharPredicate left;
		private final CharPredicate right;

		OrCharPredicate( CharPredicate left, CharPredicate right ) {
			super();
			this.left = left;
			this.right = right;
		}

		@Override
		public boolean is( char c ) {
			return left.is( c ) || right.is( c );
		}

	}

	private static final class AndCharPredicate
			implements CharPredicate {

		private final CharPredicate left;
		private final CharPredicate right;

		AndCharPredicate( CharPredicate left, CharPredicate right ) {
			super();
			this.left = left;
			this.right = right;
		}

		@Override
		public boolean is( char c ) {
			return left.is( c ) && right.is( c );
		}

	}

	private static final class NotCharPredicate
			implements CharPredicate {

		final CharPredicate negated;

		NotCharPredicate( CharPredicate negated ) {
			super();
			this.negated = negated;
		}

		@Override
		public boolean is( char c ) {
			return !negated.is( c );
		}

	}

	private static final class CharacterTypeCharPredicate
			implements CharPredicate {

		private final int type;

		CharacterTypeCharPredicate( int type ) {
			super();
			this.type = type;
		}

		@Override
		public boolean is( char c ) {
			return Character.getType( c ) == type;
		}
	}

	private static final class RangeCharPredictae
			implements CharPredicate {

		private final char lower;
		private final char upper;

		RangeCharPredictae( char lower, char upper ) {
			super();
			this.lower = lower;
			this.upper = upper;
		}

		@Override
		public boolean is( char c ) {
			return c >= lower && c <= upper;
		}

	}

	private static final class InCharsetCharPredicate
			implements CharPredicate {

		private final String charset;

		InCharsetCharPredicate( String charset ) {
			super();
			this.charset = charset;
		}

		@Override
		public boolean is( char c ) {
			return charset.indexOf( c ) >= 0;
		}

	}

	private static final class ExactCharPredicate
			implements CharPredicate {

		private final char exact;

		ExactCharPredicate( char exact ) {
			super();
			this.exact = exact;
		}

		@Override
		public boolean is( char c ) {
			return c == exact;
		}

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

		private final CharPredicate whileIt;
		private final CharScanner<CharWriter> either;
		private final CharScanner<CharWriter> or;

		EitherOrCharCollector( CharPredicate whileIt, CharScanner<CharWriter> either,
				CharScanner<CharWriter> or ) {
			super();
			this.whileIt = whileIt;
			this.either = either;
			this.or = or;
		}

		@Override
		public void scan( CharReader in, CharWriter out ) {
			if ( in.hasNext() ) {
				if ( whileIt.is( in.peek() ) ) {
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
			while ( in.hasNext() && whileIt.is( in.peek() ) ) {
				out.append( in.next() );
			}
		}
	}

}
