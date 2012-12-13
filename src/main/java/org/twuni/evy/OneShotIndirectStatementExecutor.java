package org.twuni.evy;

public class OneShotIndirectStatementExecutor extends IndirectStatementExecutor {

	private final String eventName;

	public OneShotIndirectStatementExecutor( StatementTreeExecutor root, Statement delegate, String eventName, Statement parent ) {
		super( root, delegate, parent );
		this.eventName = eventName;
	}

	@Override
	public void execute( Statement statement ) {
		try {
			super.execute( statement );
			root.unsubscribe( eventName, this );
		} catch( RuntimeException exception ) {
			// This wasn't a match.
		}
	}

}
