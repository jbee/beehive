package de.jbee.io;

public final class CharScanner {

	public static <T> ICharScanner<T> combine( ICharScanner<? super T> first,
			ICharScanner<? super T> second ) {
		return new ChainCharScanner<T>( first, second );
	}

	public static ICharScanner<Object> of( ICharProcessor out ) {
		return new ProcessorVoidAdapter( out );
	}

	public static ICharScanner<Appendable> appending( ICharScanner<ICharWriter> scanner ) {
		return new AppendableAdapter( scanner );
	}

	public static <T> ICharScanner<T> successively( ICharProcessor open, ICharScanner<T> content,
			ICharProcessor close ) {
		return combine( combine( of( open ), content ), of( close ) );
	}

	public static <T> ProcessableBy<T> processing( ICharReader in,
			ICharScanner<? super T> processing, ICharScanner<? super T> discarding ) {
		return new ElementAdapter<T>( in, processing, discarding );
	}

	public static <T> ICharScanner<T> trimming( ICharScanner<T> scanner ) {
		return new TrimmingScanner<T>( scanner );
	}

	private CharScanner() {
		// util
	}

	public static abstract class UtilisedCharScanner<T>
			implements ICharScanner<T> {

		protected final void once( ICharProcessor processor, ICharReader in ) {
			processor.process( in );
		}

	}

	public static abstract class AssembledCharScanner<T>
			implements ICharScanner<T> {

		private ICharScanner<T> assembled;

		@Override
		public void scan( ICharReader in, T out ) {
			assembled.scan( in, out );
		}

		abstract void assemble(); //OPEN somehow offer a way to assemble the scanner in concrete classes - maybe this is also more a factory ? than we impl. that factory instead of a scanner class because that could be achieved by assembling others.
	}

	static final class AppendableAdapter
			implements ICharScanner<Appendable> {

		private final ICharScanner<ICharWriter> scanner;

		AppendableAdapter( ICharScanner<ICharWriter> scanner ) {
			super();
			this.scanner = scanner;
		}

		@Override
		public void scan( ICharReader in, Appendable out ) {
			scanner.scan( in, CharWriter.of( out ) );
		}
	}

	static final class TrimmingScanner<T>
			implements ICharScanner<T> {

		private final ICharScanner<T> scanner;

		TrimmingScanner( ICharScanner<T> scanner ) {
			super();
			this.scanner = scanner;
		}

		@Override
		public void scan( ICharReader in, T out ) {
			Gobble.whitespace().process( in );
			scanner.scan( in, out );
			Gobble.whitespace().process( in );
		}

	}

	static final class ChainCharScanner<T>
			implements ICharScanner<T> {

		private final ICharScanner<? super T> first;
		private final ICharScanner<? super T> second;

		ChainCharScanner( ICharScanner<? super T> first, ICharScanner<? super T> second ) {
			super();
			this.first = first;
			this.second = second;
		}

		public void scan( ICharReader in, T out ) {
			first.scan( in, out );
			second.scan( in, out );
		}
	}

	static final class ProcessorVoidAdapter
			implements ICharScanner<Object> {

		private final ICharProcessor out;

		ProcessorVoidAdapter( ICharProcessor out ) {
			super();
			this.out = out;
		}

		@Override
		public void scan( ICharReader in, Object nothing ) {
			out.process( in );
		}
	}

	static final class ElementAdapter<T>
			implements ProcessableBy<T> {

		private final ICharReader in;
		private final ICharScanner<? super T> processing;
		private final ICharScanner<? super T> discarding;

		ElementAdapter( ICharReader in, ICharScanner<? super T> processing,
				ICharScanner<? super T> discarding ) {
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
