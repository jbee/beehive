package de.jbee.io;

public final class CharScanner {

	public static <T> ICharScanner<T> combine( ICharScanner<? super T> first,
			ICharScanner<? super T> second ) {
		return new ChainCharScanner<T>( first, second );
	}

	public static ICharScanner<Object> of( ICharProcessor out ) {
		return new ProcessorVoidAdapter( out );
	}

	public static ICharScanner<Appendable> of( ICharScanner<ICharWriter> scanner ) {
		return new AppendableAdapter( scanner );
	}

	public static <T> ICharScanner<T> successively( ICharProcessor open, ICharScanner<T> content,
			ICharProcessor close ) {
		return combine( combine( of( open ), content ), of( close ) );
	}

	public static <T> IProcessableUnit<T> processable( ICharReader in,
			ICharScanner<? super T> processing, ICharScanner<? super T> discarding ) {
		return new UnitAdapter<T>( in, processing, discarding );
	}

	private CharScanner() {
		// util
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

	static final class UnitAdapter<T>
			implements IProcessableUnit<T> {

		private final ICharReader in;
		private final ICharScanner<? super T> processing;
		private final ICharScanner<? super T> discarding;

		UnitAdapter( ICharReader in, ICharScanner<? super T> processing,
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
