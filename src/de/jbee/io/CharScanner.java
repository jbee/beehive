package de.jbee.io;

public final class CharScanner {

	private CharScanner() {
		// util
	}

	static final class ProcessorAdapter
			implements ICharScanner<ICharWriter> {

		private final ICharProcessor processor;

		ProcessorAdapter( ICharProcessor processor ) {
			super();
			this.processor = processor;
		}

		@Override
		public void scan( ICharReader in, ICharWriter out ) {
			processor.process( in );
		}
	}

}
