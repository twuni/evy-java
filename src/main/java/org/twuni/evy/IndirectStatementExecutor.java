package org.twuni.evy;

public class IndirectStatementExecutor implements StatementExecutor {

	protected final StatementTreeExecutor root;
	private final Statement delegate;

	public IndirectStatementExecutor( StatementTreeExecutor evy, Statement delegate ) {
		this.root = evy;
		this.delegate = delegate;
	}

	@Override
	public void execute( Statement statement ) {
		delegate.copySymbolsFrom( statement );
		root.execute( delegate );
	}

}