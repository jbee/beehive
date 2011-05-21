package de.jbee.io;

public interface IProcessableElement<T> {

	void processBy( T processor );

	void discardBy( T processor );

	public abstract class NoopDiscardingProcessableElement<T>
			implements IProcessableElement<T> {

		public final void discardBy( T processor ) {
			// just do nothing
		}
	}
}
