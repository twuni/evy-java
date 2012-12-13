package org.twuni.evy;

public class Evy extends Publisher {

	public Evy() {
		super();
	}

	public Evy( String program ) {
		super( program );
	}

	@Override
	protected void registerDefaultSubscriptions() {

		super.registerDefaultSubscriptions();

		Subscription oneShot = new OneShotSubscription( this );
		Subscription forever = new Subscription( this );

		subscribe( "@", oneShot );
		subscribe( "@@", forever );

	}

}
