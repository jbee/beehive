package de.jbee.io.titan;

import de.jbee.io.ICharReader;

/**
 * A kind of factory for {@link ITitanText}.
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 * 
 */
public interface ITitanScheme {

	// ein schema ist etwa der header eines dokuments - wechselt die logische/technische struktur
	// eines dokuments beginnt gewöhnlich auch eine neues schema -> ein schema muss ihr ende erkennen
	// und mitteilen können ---> es sollte ein markup geben, dass ein neues schema einleitet, damit
	// der autor selbst ein schema erzwingen kann (der doctype könnte bereits dieses markup nutzen)

	ITitanText next( ICharReader in );

	// vielleicht ist ein schema auch ein art scanner, welcher ein prozessor bekommt:
	// sicher sogar: denn so ist nur ein return typ möglich - es wäre besser hier flexieber zu sein

	//
	//
	//OPEN Eine simple Parser-API und als ein Prozessor (Empfänger) dann eine Impl. die daraus den Baum baut der durch die entsprechenden element beschrieben wird.

	// --------> Zwischensprache nötig, die alles bereits beschreiben kann - geht das ? passt das ?

	// die unterscheidung ist vielleicht komplizierter als nötig. wenn ein schema gefunden wird,
	// heißt das eigentlich nichts weiter als dass das schema selbst von nun den parent spielt und 
	// den rest des eingabestroms bis zu einem punkt (der für das schmea das ende bedetet) verarbeitet.
	// die parse-funktion wird als mit dem scheme als argument weitergeführt, bis dies beendet ist
	// oder ein neues sich noch unten einhängt usw.
}
