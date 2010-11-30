package de.jbee.io.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Json {

	public static final String NUMBER_UNIVERSE = ".0123456789+-eE";

	private Json() {
		// util
	}

	public static Number number( String value ) {
		if ( value.indexOf( '.' ) >= 0 ) {
			if ( value.length() > 14 || value.indexOf( 'e' ) >= 0 || value.indexOf( 'E' ) >= 0 ) {
				return new BigDecimal( value );
			}
			return value.length() < 7
				? Float.valueOf( value )
				: Double.valueOf( value );
		}
		if ( value.length() < 10 ) {
			return Integer.valueOf( value );
		}
		if ( value.length() < 19 ) {
			return Long.valueOf( value );
		}
		return new BigInteger( value );
	}
}
