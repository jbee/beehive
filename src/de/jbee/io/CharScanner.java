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

	public static <T> IProcessable<T> procesable( ICharReader in,
			ICharScanner<? super T> processBy, ICharScanner<? super T> discardBy ) {
		return new ProcessableAdapter<T>( in, processBy, discardBy );
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

	static final class ProcessableAdapter<T>
			implements IProcessable<T> {

		private final ICharReader in;
		private final ICharScanner<? super T> processBy;
		private final ICharScanner<? super T> discardBy;

		ProcessableAdapter( ICharReader in, ICharScanner<? super T> processBy,
				ICharScanner<? super T> discardBy ) {
			super();
			this.in = in;
			this.processBy = processBy;
			this.discardBy = discardBy;
		}

		public void discardBy( T processor ) {
			discardBy.scan( in, processor );
		}

		public void processBy( T processor ) {
			processBy.scan( in, processor );
		}
	}
}
