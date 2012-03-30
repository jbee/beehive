package de.jbee.io.json.draft;

public final class TypeSelector {

	public static ITypeSelector is( Class<?> type ) {
		return new IdentitySelector( type );
	}

	public static ITypeSelector instanceOf( Class<?> type ) {
		return new InstanceOfSelector( type );
	}

	private TypeSelector() {
		// util
	}

	static final class IdentitySelector
			implements ITypeSelector {

		private final Class<?> type;

		IdentitySelector( Class<?> type ) {
			super();
			this.type = type;
		}

		@Override
		public boolean matches( Class<?> type ) {
			return this.type == type;
		}
	}

	static final class InstanceOfSelector
			implements ITypeSelector {

		private final Class<?> moreWideType;

		InstanceOfSelector( Class<?> moreWideType ) {
			super();
			this.moreWideType = moreWideType;
		}

		@Override
		public boolean matches( Class<?> type ) {
			return moreWideType.isAssignableFrom( type );
		}
	}
}
