package org.twuni.evy;

public class IndirectStatementExecutor implements StatementExecutor {

	protected final StatementTreeExecutor root;
	private final Statement delegate;
	private final Statement parent;

	public IndirectStatementExecutor( StatementTreeExecutor evy, Statement delegate, Statement parent ) {
		root = evy;
		this.delegate = delegate;
		this.parent = parent;
	}

	@Override
	public void execute( Statement trigger ) {
		if( parent != null ) {
			for( int i = 2; i < parent.getParameters().size(); i++ ) {
				String [] parameter = parent.getParameters().get( i );
				if( parameter[0] == null ) {
					if( trigger.lookup( parameter[1] ) == null ) {
						throw new RuntimeException( "No match!" );
					}
				} else {
					if( !parameter[1].equals( trigger.lookup( parameter[0] ) ) ) {
						throw new RuntimeException( "No match!" );
					}
				}
			}
		}
		delegate.copySymbolsFrom( trigger );
		root.execute( delegate );
	}

}
