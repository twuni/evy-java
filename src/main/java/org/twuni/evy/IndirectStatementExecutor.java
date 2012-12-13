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
	public void execute( Statement statement ) {
		if( parent != null ) {
			for( int i = 1; i < parent.getParameters().size(); i++ ) {
				String [] parameter = parent.getParameters().get( i );
				if( parameter[0] != null && statement.lookup( parameter[0] ) == null ) {
					return;
				}
			}
		}
		delegate.copySymbolsFrom( statement );
		root.execute( delegate );
	}

}
