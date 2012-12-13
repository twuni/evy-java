package org.twuni.evy;

public class Subscription implements StatementExecutor {

	protected final StatementTreeExecutor root;

	public Subscription( StatementTreeExecutor root ) {
		this.root = root;
	}

	/**
	 * @param context
	 *            The context in which this executor will run.
	 * @param eventName
	 *            The event to which this executor will subscribe.
	 */
	protected StatementExecutor createExecutor( Statement context, String eventName, Statement parent ) {
		return new IndirectStatementExecutor( root, context, parent );
	}

	@Override
	public void execute( Statement statement ) {
		String eventName = statement.lookup( 0 );
		for( int i = 0; i < statement.getChildren().size(); i++ ) {
			Statement child = statement.getChildren().get( i );
			root.subscribe( eventName, createExecutor( child, eventName, statement ) );
		}
	}

}
