package de.jbee.io.json;

import java.util.InputMismatchException;

public enum JsonType {

	NULL,
	BOOLEAN,
	NUMBER,
	STRING,
	ARRAY,
	OBJECT;

	public static JsonType valueOf( char firstOfValue ) {
		switch ( firstOfValue ) {
			case '{':
				return OBJECT;
			case '[':
				return ARRAY;
			case '"':
				return STRING;
			case 't':
			case 'f':
				return BOOLEAN;
			case 'n':
				return NULL;
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return NUMBER;
			default:
				throw new InputMismatchException( "Unexspected token: " + firstOfValue );
		}
	}

	public boolean isComposite() {
		return this == OBJECT || this == ARRAY;
	}
}
