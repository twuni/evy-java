package org.twuni.evy;

public class OneShotSubscription extends Subscription {

	public OneShotSubscription( StatementTreeExecutor root ) {
		super( root );
	}

	@Override
	protected StatementExecutor createExecutor( Statement context, String eventName, Statement parent ) {
		return new OneShotIndirectStatementExecutor( root, context, eventName, parent );
	}

}
