package org.twuni.evy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {

	public static void main( String [] args ) {

		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );

		Evy evy = new Evy() {

			@Override
			protected void registerDefaultSubscriptions() {

				super.registerDefaultSubscriptions();

				subscribe( "print", new Subscriber() {

					@Override
					public void onPublish( Event event ) {
						for( int i = 0; i < event.length(); i++ ) {
							System.out.print( event.get( i ) + " " );
						}
						System.out.println();
					}

				} );

				subscribe( "exit", new Subscriber() {

					@Override
					public void onPublish( Event event ) {
						int status = 0;
						if( event.get( 0 ) != null ) {
							status = Integer.parseInt( event.get( 0 ) );
						}
						System.exit( status );
					}

				} );

				subscribe( "help", new Subscriber() {

					@Override
					public void onPublish( Event event ) {
						String [] help = {
							"SYNTAX",
							"",
							"  PUBLICATION  := EVENT [PARAMETERS]",
							"  PARAMETERS   := ( KEY=VALUE | VALUE ) [PARAMETERS]",
							"",
							"NATIVE EVENTS",
							"",
							"print            Prints its parameter values.",
							"exit             Exits the Evy CLI.",
							"help             Displays this help menu.",
							"",
							"@ PUBLICATION    Registers a one-time subscription.",
							"@@ PUBLICATION   Registers a persistent subscription.",
							"",
							"SUBSCRIPTIONS",
							"",
							"Lines indented below a subscription are not published until",
							"an event matching the subscription is published.",
							"For example:",
							"",
							"  @ eat food=pizza",
							"    print Mmm... tasty!",
							"",
							"Now, the next time an 'eat' event is published with a 'food'",
							"parameter whose value is 'pizza', the phrase 'Mmm... tasty!'",
							"is printed to the screen.",
							""
						};
						for( String line : help ) {
							System.out.println( line );
						}
					}

				} );

			}

		};

		System.out.print( "Evy$ " );

		try {
			StringBuilder program = new StringBuilder();
			for( String line = reader.readLine(); line != null; line = reader.readLine() ) {
				if( !"".equals( line ) ) {
					program.append( line ).append( "\n" );
				}
				if( "".equals( line ) || line.charAt( 0 ) != '@' && line.charAt( 0 ) != ' ' && line.charAt( 0 ) != '\t' ) {
					if( program.length() > 0 ) {
						evy.publish( new EventTree( program.toString() ) );
						program.setLength( 0 );
						System.out.print( "Evy$ " );
						continue;
					}
				}
				System.out.print( "---> " );
			}
		} catch( IOException exception ) {
			// Don't bother.
		}

	}

}
