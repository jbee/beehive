package de.jbee.io;

import java.util.InputMismatchException;
import java.util.regex.Pattern;

public final class Gobble {

	private static final ICharProcessor NOTHING = new NullCharProcessor();
	private static final ICharProcessor NEXT_1 = new GobbleN( 1 );
	private static final ICharProcessor WHITESPACE = new GobbleWhitespace();
	private static final ICharProcessor LETTERS = new GobbleLetters();
	private static final ICharProcessor UNICODE_STRING = new GobbleEscapedUnicodeString();

	private Gobble() {
		// util
	}

	static final class GobbleAll
			implements ICharProcessor {

		final char gobbled;

		GobbleAll( char gobbled ) {
			super();
			this.gobbled = gobbled;
		}

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && in.peek() == gobbled ) {
				in.next();
			}
		}
	}

	static final class GobbleUniverse
			implements ICharProcessor {

		final String universe;

		GobbleUniverse( String universe ) {
			super();
			this.universe = universe;
		}

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && universe.indexOf( in.peek() ) >= 0 ) {
				in.next();
			}
		}
	}

	static final class GobbleLetters
			implements ICharProcessor {

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && Character.isLetter( in.peek() ) ) {
				in.next();
			}
		}

	}

	static final class GobblePattern
			implements ICharProcessor {

		final Pattern pattern;

		GobblePattern( Pattern pattern ) {
			super();
			this.pattern = pattern;
		}

		@Override
		public void process( ICharReader in ) {
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

	static final class GobbleUntilChar
			implements ICharProcessor {

		final char end;

		GobbleUntilChar( char end ) {
			super();
			this.end = end;
		}

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && in.peek() != end ) {
				in.next();
			}
		}
	}

	static final class CombinedCharProcesspr
			implements ICharProcessor {

		final ICharProcessor first;
		final ICharProcessor second;

		CombinedCharProcesspr( ICharProcessor first, ICharProcessor second ) {
			super();
			this.first = first;
			this.second = second;
		}

		@Override
		public void process( ICharReader in ) {
			first.process( in );
			second.process( in );
		}
	}

	static final class GobbleN
			implements ICharProcessor {

		final int n;

		GobbleN( int n ) {
			super();
			this.n = n;
		}

		@Override
		public void process( ICharReader in ) {
			for ( int i = 0; i < n; i++ ) {
				in.next();
			}
		}
	}

	static final class NullCharProcessor
			implements ICharProcessor {

		@Override
		public void process( ICharReader in ) {
			// do nothing
		}
	}

	static final class GobbleWhitespace
			implements ICharProcessor {

		@Override
		public void process( ICharReader in ) {
			while ( in.hasNext() && Character.isWhitespace( in.peek() ) ) {
				in.next();
			}

		}
	}

	static final class GobbleAChar
			extends ExpectingCharProcessor {

		final char mandatory;

		GobbleAChar( char mandatory ) {
			super();
			this.mandatory = mandatory;
		}

		@Override
		public void process( ICharReader in ) {
			expect( in, mandatory );
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
		public void process( ICharReader in ) {
			for ( int i = 0; i < sequence.length(); i++ ) {
				expect( in, sequence.charAt( i ) );
			}
		}

	}

	static abstract class ExpectingCharProcessor
			implements ICharProcessor {

		protected final void mismatch( String message )
				throws InputMismatchException {
			throw new InputMismatchException( message );
		}

		protected final void expect( ICharReader in, char expected ) {
			if ( !in.hasNext() ) {
				mismatch( "Needed '" + expected + "' but no further token available!" );
			}
			char next = in.next();
			if ( next != expected ) {
				mismatch( "Expeceted '" + expected + "' but found: '" + next + "'" );
			}
		}
	}

	static final class GobbleEscapedUnicodeString
			extends ExpectingCharProcessor {

		@Override
		public void process( ICharReader in ) {
			a( '"' ).process( in );
			while ( in.peek() != '"' ) {
				char c = in.next();
				if ( c == '\\' ) {
					c = in.next(); // gobble the escaped char
					if ( c == 'u' ) { // if it was u
						next( 4 ).process( in ); // gobble following 4 digit hex number
					}
				}
			}
			a( '"' ).process( in );
		}
	}

	static final class GobbleBlock
			extends ExpectingCharProcessor {

		final String openUniverse;
		final String closeUniverse;
		final char escapeingBlockOpener;
		final ICharProcessor escapeingBlockProcessor;

		GobbleBlock( String openUniverse, String closeUniverse, char escapeingBlockOpener,
				ICharProcessor escapeingBlockProcessor ) {
			super();
			this.openUniverse = openUniverse;
			this.closeUniverse = closeUniverse;
			this.escapeingBlockOpener = escapeingBlockOpener;
			this.escapeingBlockProcessor = escapeingBlockProcessor;
		}

		@Override
		public void process( ICharReader in ) {
			int level = 0;
			char c = in.peek();
			do {
				if ( escapeingBlockOpener == c ) {
					escapeingBlockProcessor.process( in );
				} else if ( openUniverse.indexOf( c ) >= 0 ) {
					level++;
				} else if ( closeUniverse.indexOf( c ) >= 0 ) {
					level--;
				}
				if ( level > 0 ) {
					c = in.next();
				}
			} while ( level > 0 );
		}
	}

	public static ICharProcessor combine( ICharProcessor first, ICharProcessor second ) {
		return first == NOTHING
			? second
			: second == NOTHING
				? first
				: new CombinedCharProcesspr( first, second );
	}

	public static ICharProcessor next( int n ) {
		return n <= 0
			? NOTHING
			: n == 1
				? NEXT_1
				: new GobbleN( n );
	}

	public static ICharProcessor until( char end ) {
		return new GobbleUntilChar( end );
	}

	public static ICharProcessor next1() {
		return NEXT_1;
	}

	public static ICharProcessor whitespace() {
		return WHITESPACE;
	}

	public static ICharProcessor whitespaced( ICharProcessor inner ) {
		return inner == NOTHING
			? WHITESPACE
			: combine( combine( WHITESPACE, inner ), WHITESPACE );
	}

	public static ICharProcessor aWhitespaced( char mandatory ) {
		return whitespaced( a( mandatory ) );
	}

	public static ICharProcessor a( char mandatory ) {
		return new GobbleAChar( mandatory );
	}

	public static ICharProcessor one( String sequence ) {
		return new GobbleASequence( sequence );
	}

	public static ICharProcessor all( char optional ) {
		return new GobbleAll( optional );
	}

	public static ICharProcessor universe( String universe ) {
		return new GobbleUniverse( universe );
	}

	public static ICharProcessor letters() {
		return LETTERS;
	}

	public static ICharProcessor unicode() {
		return UNICODE_STRING;
	}

	public static ICharProcessor block( String openUniverse, String closeUniverse,
			char escapeingBlockOpener, ICharProcessor escapeingBlockProcessor ) {
		return new GobbleBlock( openUniverse, closeUniverse, escapeingBlockOpener,
				escapeingBlockProcessor );
	}
}
