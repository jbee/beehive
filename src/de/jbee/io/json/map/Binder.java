package de.jbee.io.json.map;

import de.jbee.io.json.JsonType;

public class Binder {

	static class InstantiatorBinder<T> {

		ScopeBinder to( IInstantiator<T> instantiator ) {

			return null;
		}
	}

	static class ScopeBinder {

		public void in( Class<?> pojoType ) {

		}

		public void in( Class<?> pojoType, String member ) {

		}

		public ScopeBinder when( JsonType type ) {

			return null;
		}

	}

	public <T> InstantiatorBinder<T> bind( Class<T> valueType ) {

		return null;
	}

	static class MyBean {

	}

	private void test() {
		IInstantiator<String> i = null;
		bind( String.class ).to( i ).when( JsonType.STRING ).in( MyBean.class );
	}
}
