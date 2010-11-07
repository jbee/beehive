package de.jbee.io.json.map;

public interface IJsonMapper {

	// prio ? sodass man default-binds überschreiben kann ? 
	// oder besser so, dass man auch einen "leeren" mapper leicht zu
	// einem leicht angepassten zusammensetzen kann

	<T> void bind( ISelector paths, Class<T> type, IInstantiator<T> i );

}
