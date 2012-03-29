package de.jbee.io;

public interface ProcessableBy<T> {

	void processBy( T processor );

	void discardBy( T processor );

	public abstract class NoopDiscardingProcessableBy<T>
			implements ProcessableBy<T> {

		public final void discardBy( T processor ) {
			// just do nothing
		}
	}
}
