package de.jbee.io;

public final class ScanTo {

	public static <T> CharScanner<T> combine( CharScanner<? super T> first,
			CharScanner<? super T> second ) {
		return new ChainCharScanner<T>( first, second );
	}

	public static CharScanner<Object> scansTo( CharProcessor out ) {
		return new ProcessorVoidAdapter( out );
	}

	public static CharScanner<Appendable> appending( CharScanner<CharWriter> scanner ) {
		return new AppendableAdapter( scanner );
	}

	public static <T> CharScanner<T> successively( CharProcessor open, CharScanner<T> content,
			CharProcessor close ) {
		return combine( combine( scansTo( open ), content ), scansTo( close ) );
	}

	public static <T> ProcessableBy<T> processing( CharReader in,
			CharScanner<? super T> processing, CharScanner<? super T> discarding ) {
		return new ElementAdapter<T>( in, processing, discarding );
	}

	public static <T> CharScanner<T> trimming( CharScanner<T> scanner ) {
		return new TrimmingScanner<T>( scanner );
	}

	private ScanTo() {
		// util
	}

	public static abstract class UtilisedCharScanner<T>
			implements CharScanner<T> {

		protected final void once( CharProcessor processor, CharReader in ) {
			processor.process( in );
		}

	}

	public static abstract class AssembledCharScanner<T>
			implements CharScanner<T> {

		private CharScanner<T> assembled;

		@Override
		public void scan( CharReader in, T out ) {
			assembled.scan( in, out );
		}

		abstract void assemble(); //OPEN somehow offer a way to assemble the scanner in concrete classes - maybe this is also more a factory ? than we impl. that factory instead of a scanner class because that could be achieved by assembling others.
	}

	static final class AppendableAdapter
			implements CharScanner<Appendable> {

		private final CharScanner<CharWriter> scanner;

		AppendableAdapter( CharScanner<CharWriter> scanner ) {
			super();
			this.scanner = scanner;
		}

		@Override
		public void scan( CharReader in, Appendable out ) {
			scanner.scan( in, CharIO.appender( out ) );
		}
	}

	static final class TrimmingScanner<T>
			implements CharScanner<T> {

		private final CharScanner<T> scanner;

		TrimmingScanner( CharScanner<T> scanner ) {
			super();
			this.scanner = scanner;
		}

		@Override
		public void scan( CharReader in, T out ) {
			Gobble.whitespace().process( in );
			scanner.scan( in, out );
			Gobble.whitespace().process( in );
		}

	}

	static final class ChainCharScanner<T>
			implements CharScanner<T> {

		private final CharScanner<? super T> first;
		private final CharScanner<? super T> second;

		ChainCharScanner( CharScanner<? super T> first, CharScanner<? super T> second ) {
			super();
			this.first = first;
			this.second = second;
		}

		public void scan( CharReader in, T out ) {
			first.scan( in, out );
			second.scan( in, out );
		}
	}

	static final class ProcessorVoidAdapter
			implements CharScanner<Object> {

		private final CharProcessor out;

		ProcessorVoidAdapter( CharProcessor out ) {
			super();
			this.out = out;
		}

		@Override
		public void scan( CharReader in, Object nothing ) {
			out.process( in );
		}
	}

	static final class ElementAdapter<T>
			implements ProcessableBy<T> {

		private final CharReader in;
		private final CharScanner<? super T> processing;
		private final CharScanner<? super T> discarding;

		ElementAdapter( CharReader in, CharScanner<? super T> processing,
				CharScanner<? super T> discarding ) {
			super();
			this.in = in;
			this.processing = processing;
			this.discarding = discarding;
		}

		public void discardBy( T processor ) {
			discarding.scan( in, processor );
		}

		public void processBy( T processor ) {
			processing.scan( in, processor );
		}
	}
}
