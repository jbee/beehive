package de.jbee.io.titan;

public interface ITitanElementParser {

	// irgendwo muss auch noch die position im dokument mit rein, damit etwa für den doc type am
	// anfang passende regeln geschrieben werden können -> es gibt "technische parts" - der 1. ist
	// immer der doc type - dieser legt dann die weiteren parts fest -> kein enum sondern die "fabrik"
	// welche anhand von codes den passenden parser ermittelt und befüttert ---> notation ? 

	// hier sollte in irgend einer form auch die hierachie mit rein, damit relative blöcke (ein 
	// Block richtet sein gewicht nach seinem elterncontainer aus) möglich werden

	void process( String arguments );
}
