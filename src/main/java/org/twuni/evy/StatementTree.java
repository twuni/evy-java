package org.twuni.evy;

import java.util.ArrayList;
import java.util.List;

public class StatementTree extends Statement {

	public StatementTree( String program ) {
		super( null );
		List<Statement> ancestors = new ArrayList<Statement>();
		ancestors.add( this );
		String [] lines = program.split( "\n" );
		for( int i = 0; i < lines.length; i++ ) {
			Statement parent = ancestors.get( 0 );
			Statement node = new Statement( parent, lines[i] );
			if( node.isEmpty() ) {
				continue;
			}
			if( node.getDepth() > parent.getDepth() ) {
				parent.getChildren().add( node );
				ancestors.add( 0, node );
			} else {
				ancestors.remove( 0 );
				i--;
			}
		}
	}

}
