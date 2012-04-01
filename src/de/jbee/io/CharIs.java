package de.jbee.io;

import java.util.regex.Pattern;

public class CharIs {

	public static final CharPredicate ASCII_LETTER = or( in( 'a', 'z' ), in( 'A', 'Z' ) );
	public static final CharPredicate ASCII_DIGIT = in( '0', '9' );

	private static final CharPredicate ANY_UNI_LETTER = new LetterCharPredicate();

	private static final CharPredicate ANY_UNI_WHITESPACE = new WhitespaceCharPredicate();

	public static CharPredicate type( byte type ) {
		return new CharacterTypeCharPredicate( type );
	}

	public static CharPredicate and( CharPredicate left, CharPredicate right ) {
		return new AndCharPredicate( left, right );
	}

	public static CharPredicate anyLetter() {
		return ANY_UNI_LETTER;
	}

	public static CharPredicate anyWhitespace() {
		return ANY_UNI_WHITESPACE;
	}

	public static CharPredicate exact( char exact ) {
		return new ExactCharPredicate( exact );
	}

	public static CharPredicate in( char lower, char upper ) {
		return lower == upper
			? exact( lower )
			: lower < upper
				? new RangeCharPredictae( lower, upper )
				: new RangeCharPredictae( upper, lower );
	}

	public static CharPredicate in( String charset ) {
		return new InCharsetCharPredicate( charset );
	}

	public static CharPredicate matches( Pattern pattern ) {
		return new PatternCharPredicate( pattern );
	}

	public static CharPredicate matches( String regex ) {
		return matches( Pattern.compile( regex ) );
	}

	public static CharPredicate not( CharPredicate negated ) {
		if ( negated instanceof NotCharPredicate ) {
			return ( (NotCharPredicate) negated ).negated;
		}
		return new NotCharPredicate( negated );
	}

	public static CharPredicate or( CharPredicate left, CharPredicate right ) {
		return new OrCharPredicate( left, right );
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
		public boolean isSuitable( char c ) {
			return left.isSuitable( c ) && right.isSuitable( c );
		}

		@Override
		public String toString() {
			return "(" + left + " && " + right + ")";
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
		public boolean isSuitable( char c ) {
			return c == exact;
		}

		@Override
		public String toString() {
			return String.valueOf( exact );
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
		public boolean isSuitable( char c ) {
			return charset.indexOf( c ) >= 0;
		}

		@Override
		public String toString() {
			return "[" + charset + "]";
		}
	}

	private static final class LetterCharPredicate
			implements CharPredicate {

		LetterCharPredicate() {
			//make visible
		}

		@Override
		public boolean isSuitable( char c ) {
			return Character.isLetter( c );
		}

		@Override
		public String toString() {
			return "<Letter>";
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
		public boolean isSuitable( char c ) {
			return !negated.isSuitable( c );
		}

		@Override
		public String toString() {
			return "!" + negated;
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
		public boolean isSuitable( char c ) {
			return left.isSuitable( c ) || right.isSuitable( c );
		}

		@Override
		public String toString() {
			return "(" + left + " || " + right + ")";
		}
	}

	private static final class PatternCharPredicate
			implements CharPredicate {

		private final Pattern pattern;

		PatternCharPredicate( Pattern pattern ) {
			super();
			this.pattern = pattern;
		}

		@Override
		public boolean isSuitable( char c ) {
			return pattern.matcher( String.valueOf( c ) ).matches();
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
		public boolean isSuitable( char c ) {
			return c >= lower && c <= upper;
		}

		@Override
		public String toString() {
			return lower + "-" + upper;
		}
	}

	private static final class WhitespaceCharPredicate
			implements CharPredicate {

		WhitespaceCharPredicate() {
			//make visible
		}

		@Override
		public boolean isSuitable( char c ) {
			return Character.isWhitespace( c );
		}

		@Override
		public String toString() {
			return "<Whitespace>";
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
		public boolean isSuitable( char c ) {
			return Character.getType( c ) == type;
		}

		@Override
		public String toString() {
			return "<type:" + type + ">"; //TODO resolve readable name as given by http://unicode.org/Public/UNIDATA/NamesList.txt
		}
	}

}
