package de.jbee.io;

import java.util.regex.Pattern;

import de.jbee.io.CharProcess.ExpectingCharProcessor;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 * @see CharProcessor
 * @see CharProcess
 */
public final class Gobble {

	private static final CharProcessor NOTHING = CharProcess.NULL_OBJECT;
	private static final CharProcessor NEXT_1 = new GobbleN( 1 );
	private static final CharProcessor WHITESPACE = new GobbleWhitespace();
	private static final CharProcessor LETTERS = new GobbleLetters();
	private static final CharProcessor UNICODE_STRING = new GobbleEscapedUnicodeString();
	private static final CharProcessor CDATA = new GobbleCDATA();
	private static final CharProcessor UNTIL_WHITESPACE = new GobbleUntilWhitespace();

	public static CharProcessor a( char mandatory ) {
		return new GobbleAChar( mandatory );
	}

	public static CharProcessor all( char optional ) {
		return new GobbleAll( optional );
	}

	public static CharProcessor aWhitespaced( char mandatory ) {
		return whitespaced( a( mandatory ) );
	}

	public static CharProcessor block( String openUniverse, String closeUniverse,
			String escapeingUniverse, CharProcessor escapeingBlockProcessor ) {
		return new GobbleBlock( openUniverse, closeUniverse, escapeingUniverse,
				escapeingBlockProcessor );
	}

	public static CharProcessor eitherOr( String either, String or ) {
		return new GobbleEitherOr( either, or );
	}

	public static CharProcessor oneOf( String mandatory ) {
		return new GobbleAMemberChar( mandatory );
	}

	public static CharProcessor letters() {
		return LETTERS;
	}

	public static CharProcessor cDATA() {
		return CDATA;
	}

	public static CharProcessor maybe( char optional ) {
		return new GobbleMaybeChar( optional );
	}

	public static CharProcessor next( int n ) {
		return n <= 0
			? NOTHING
			: n == 1
				? NEXT_1
				: new GobbleN( n );
	}

	public static CharProcessor next1() {
		return NEXT_1;
	}

	public static CharProcessor just( String sequence ) {
		return new GobbleASequence( sequence );
	}

	public static CharProcessor unicode() {
		return UNICODE_STRING;
	}

	public static CharProcessor universe( String universe ) {
		return new GobbleUniverse( universe );
	}

	public static CharProcessor until( char end ) {
		return new GobbleUntilChar( end );
	}

	public static CharProcessor untilWhitespace() {
		return UNTIL_WHITESPACE;
	}

	public static CharProcessor whitespace() {
		return WHITESPACE;
	}

	public static CharProcessor whitespaced( CharProcessor inner ) {
		return inner == NOTHING
			? WHITESPACE
			: CharProcess.combine( CharProcess.combine( WHITESPACE, inner ), WHITESPACE );
	}

	private Gobble() {
		// util
	}

	static final class GobbleAChar
			extends ExpectingCharProcessor {

		final char mandatory;

		GobbleAChar( char mandatory ) {
			super();
			this.mandatory = mandatory;
		}

		@Override
		public void process( CharReader in ) {
			expect( in, mandatory );
		}
	}

	static final class GobbleAMemberChar
			extends ExpectingCharProcessor {

		final String members;

		GobbleAMemberChar( String members ) {
			super();
			this.members = members;
		}

		@Override
		public void process( CharReader in ) {
			expectAny( in, members );
		}
	}

	static final class GobbleAll
			implements CharProcessor {

		final char gobbled;

		GobbleAll( char gobbled ) {
			super();
			this.gobbled = gobbled;
		}

		@Override
		public void process( CharReader in ) {
			while ( in.hasNext() && in.peek() == gobbled ) {
				in.next();
			}
		}
	}

	static final class GobbleASequence
			extends ExpectingCharProcessor {

		final CharSequence sequence;

		GobbleASequence( CharSequence sequence ) {
			super();
			this.sequence = sequence;
		}

		@Override
		public void process( CharReader in ) {
			for ( int i = 0; i < sequence.length(); i++ ) {
				expect( in, sequence.charAt( i ) );
			}
		}

	}

	static final class GobbleBlock
			extends ExpectingCharProcessor {

		final String openUniverse;
		final String closeUniverse;
		final String escapeingUniverse;
		final CharProcessor escapeingBlockProcessor;

		GobbleBlock( String openUniverse, String closeUniverse, String escapeingUniverse,
				CharProcessor escapeingBlockProcessor ) {
			super();
			this.openUniverse = openUniverse;
			this.closeUniverse = closeUniverse;
			this.escapeingUniverse = escapeingUniverse;
			this.escapeingBlockProcessor = escapeingBlockProcessor;
		}

		@Override
		public void process( CharReader in ) {
			int level = 0;
			do {
				char c = in.peek();
				if ( level > 0 && escapeingUniverse.indexOf( c ) >= 0 ) {
					escapeingBlockProcessor.process( in );
				} else if ( openUniverse.indexOf( c ) >= 0 ) {
					level++;
					in.next();
				} else if ( closeUniverse.indexOf( c ) >= 0 ) {
					level--;
					in.next();
				} else {
					if ( level > 0 ) {
						in.next();
					}
				}
			} while ( level > 0 );
		}
	}

	static final class GobbleCDATA
			implements CharProcessor {

		@Override
		public void process( CharReader in ) {
			char quote = in.peek();
			if ( quote == '"' || quote == '\'' ) {
				until( quote ).process( in );
			} else {
				untilWhitespace().process( in );
			}
		}
	}

	static final class GobbleEscapedUnicodeString
			extends ExpectingCharProcessor {

		@Override
		public void process( CharReader in ) {
			char frameOpener = in.peek();
			if ( "\"'".indexOf( frameOpener ) < 0 ) {
				mismatch( "Not a valid unicode string opener: " + frameOpener );
			}
			while ( in.peek() != frameOpener ) {
				char c = in.next();
				if ( c == '\\' ) {
					c = in.next(); // gobble the escaped char
					if ( c == 'u' ) { // if it was u
						next( 4 ).process( in ); // gobble following 4 digit hex number
					}
				}
			}
			a( frameOpener ).process( in );
		}
	}

	static final class GobbleLetters
			implements CharProcessor {

		@Override
		public void process( CharReader in ) {
			while ( in.hasNext() && Character.isLetter( in.peek() ) ) {
				in.next();
			}
		}

	}

	static final class GobbleMaybeChar
			implements CharProcessor {

		private final char optional;

		GobbleMaybeChar( char optional ) {
			super();
			this.optional = optional;
		}

		@Override
		public void process( CharReader in ) {
			if ( in.hasNext() && in.peek() == optional ) {
				in.next();
			}
		}
	}

	static final class GobbleN
			implements CharProcessor {

		final int n;

		GobbleN( int n ) {
			super();
			this.n = n;
		}

		@Override
		public void process( CharReader in ) {
			for ( int i = 0; i < n; i++ ) {
				in.next();
			}
		}
	}

	static final class GobblePattern
			implements CharProcessor {

		final Pattern pattern;

		GobblePattern( Pattern pattern ) {
			super();
			this.pattern = pattern;
		}

		@Override
		public void process( CharReader in ) {
			StringBuilder b = new StringBuilder();
			boolean matches = true;
			while ( in.hasNext() && matches ) {
				b.append( in.peek() );
				matches = pattern.matcher( b ).matches();
				if ( matches ) {
					in.next();
				}
			}
		}
	}

	static final class GobbleUniverse
			implements CharProcessor {

		final String universe;

		GobbleUniverse( String universe ) {
			super();
			this.universe = universe;
		}

		@Override
		public void process( CharReader in ) {
			while ( in.hasNext() && universe.indexOf( in.peek() ) >= 0 ) {
				in.next();
			}
		}
	}

	static final class GobbleEitherOr
			implements CharProcessor {

		private final char eitherStart;
		private final CharProcessor either;
		private final CharProcessor or;

		GobbleEitherOr( String either, String or ) {
			super();
			this.eitherStart = either.charAt( 0 );
			this.either = just( either );
			this.or = just( or );
		}

		@Override
		public void process( CharReader in ) {
			if ( in.peek() == eitherStart ) {
				either.process( in );
			} else {
				or.process( in );
			}
		}
	}

	static final class GobbleUntilWhitespace
			implements CharProcessor {

		@Override
		public void process( CharReader in ) {
			while ( in.hasNext() && !Character.isWhitespace( in.peek() ) ) {
				in.next();
			}
		}
	}

	static final class GobbleUntilChar
			implements CharProcessor {

		final char end;

		GobbleUntilChar( char end ) {
			super();
			this.end = end;
		}

		@Override
		public void process( CharReader in ) {
			while ( in.hasNext() && in.peek() != end ) {
				in.next();
			}
		}
	}

	static final class GobbleWhitespace
			implements CharProcessor {

		@Override
		public void process( CharReader in ) {
			while ( in.hasNext() && Character.isWhitespace( in.peek() ) ) {
				in.next();
			}

		}
	}
}
