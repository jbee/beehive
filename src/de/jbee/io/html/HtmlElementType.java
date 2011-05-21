package de.jbee.io.html;

import de.jbee.io.CharScanner;
import de.jbee.io.Gobble;
import de.jbee.io.ICharReader;
import de.jbee.io.ICharScanner;
import de.jbee.io.Read;

public enum HtmlElementType
		implements ICharScanner<IHtmlProcessor> {

	EMPTY( new HtmlEmptyElementScanner( null ) ),
	INLINE( new HtmlInlineElementScanner( null ) ),
	BLOCK( new HtmlBlockElementScanner( null ) ),
	/**
	 * Is a {@link #INLINE} or {@link #BLOCK} depending on its parent.
	 */
	AUTO( new HtmlInlineOrBlockElementScanner( null ) ),
	DOCTYPE( CharScanner.of( Gobble.just( "!DOCTYPE" ) ) ),
	COMMENT( CharScanner.of( Gobble.until( '>' ) ) ), ;

	private final ICharScanner<? super IHtmlProcessor> scanner;

	private HtmlElementType( ICharScanner<? super IHtmlProcessor> scanner ) {
		this.scanner = scanner;
	}

	public void scan( ICharReader in, IHtmlProcessor out ) {
		scanner.scan( in, out );
	}

	static abstract class HtmlElementScanner
			implements ICharScanner<IHtmlProcessor> {

		private final IHtmlTag tag;

		HtmlElementScanner( IHtmlTag tag ) {
			super();
			this.tag = tag;
		}

		@Override
		public final void scan( ICharReader in, IHtmlProcessor out ) {
			IHtmlTag tag = this.tag;
			if ( in.peek() == '<' ) {
				Gobble.a( '<' ).process( in );
				if ( tag == null ) {
					tag = out.dialect().tag( Read.toString( in, Read.LETTERS ) );
				} else {
					Gobble.just( tag.name() ).process( in );
				}
			}
			HtmlAttrType.CDATA.scan( in, out );
			scan( tag, in, out );
		}

		protected abstract void scan( IHtmlTag tag, ICharReader in, IHtmlProcessor out );
	}

	static final class HtmlEmptyElementScanner
			extends HtmlElementScanner {

		HtmlEmptyElementScanner( IHtmlTag tag ) {
			super( tag );
		}

		@Override
		public void scan( IHtmlTag tag, ICharReader in, IHtmlProcessor out ) {
			Gobble.maybe( '/' ).process( in );
			Gobble.a( '>' ).process( in );
		}
	}

	static final class HtmlBlockElementScanner
			extends HtmlElementScanner {

		HtmlBlockElementScanner( IHtmlTag tag ) {
			super( tag );
		}

		@Override
		public void scan( IHtmlTag tag, ICharReader in, IHtmlProcessor out ) {
			// TODO Auto-generated method stub

		}
	}

	static final class HtmlInlineElementScanner
			extends HtmlElementScanner {

		HtmlInlineElementScanner( IHtmlTag tag ) {
			super( tag );
		}

		@Override
		public void scan( IHtmlTag tag, ICharReader in, IHtmlProcessor out ) {
			boolean empty = in.peek() == '/';
			Gobble.maybe( '/' ).process( in );
			Gobble.a( '>' ).process( in );
			if ( empty ) {
				return;
			}
			Gobble.a( '<' ).process( in );
			Gobble.a( '/' ).process( in );
			Gobble.just( tag.name() );
			Gobble.a( '>' ).process( in );
		}

	}

	static final class HtmlInlineOrBlockElementScanner
			extends HtmlElementScanner {

		HtmlInlineOrBlockElementScanner( IHtmlTag tag ) {
			super( tag );
		}

		@Override
		public void scan( IHtmlTag tag, ICharReader in, IHtmlProcessor out ) {
			// TODO Auto-generated method stub

		}
	}
}
