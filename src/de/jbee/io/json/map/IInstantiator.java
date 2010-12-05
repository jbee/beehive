package de.jbee.io.json.map;

public interface IInstantiator<T> {

	// verwendet irgendwie das IJsonVisitor interface, um die Werte mitgeteilt zu bekommen,
	// für welche er den Wert T erstellen soll.

	// Oft gibt es da für die Erzeugung aus boolean oder Number keine sinnvolle Möglichkeit
	// Wirft man dann eine Exception ?

	// Und wie werden komplexe Objekte zusammengesetzt ? 
}
