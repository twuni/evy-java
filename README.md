# The Evy Programming Language

Evy is an interpreted, event-oriented, dynamic, general purpose functional language.
Evy's simple, expressive syntax makes it especially suitable for creating domain-specific
languages, such as conversation trees for video games and UI workflows for applications.

Evy closely models the publisher-subscriber pattern. Each statement either publishes or
subscribes to an event. Events can have any number of parameters, which can be key-value
pairs, or just values. Value-only parameters are dynamic: they act as values unless or 
until a matching key comes along, after which the parameter is transformed into a key-value
pair.

## Learning by Example

As with any new language, it's generally easier to learn by example than by studying
descriptions of the language or by parsing syntax. Here are some practical examples of
Evy in action.

### Hello, world!

    print "Hello, world!"

This does exactly what you would expect: it prints the phrase "Hello, world!" to the screen.
What you're doing in this line of code is publishing the `print` event with a single parameter:
the string `"Hello, world!"`.

The following program does exactly the same thing, but in a slightly different way:

    print Hello, world!

Notice that I've left out the quotes. It still does exactly the same thing as the first example,
but why? What you're doing in this line of code is publishing the `print` event with two parameters
this time: the value `Hello,` and the value `world!`. The `print` event is handled natively, by
joining the values of each of its parameters with a space.

### Subscriptions

Let's say you want to cause the program to exit whenever the phrase "exit" is printed...

    @ print exit
      exit

Now, if the `print` event is published with a first parameter of `exit`, you will exit the program.

### Conversation tree

Now, let's say we want to implement a conversation tree for a game we're developing. The Evy script
might look something like this:

    @@ Greeting
    
      NPC says="Greetings, stranger!"
    
      Consider saying="Do I know you?"
      Consider saying="Hello, sir."
    
      @ Player says="Do I know you?"
    
        NPC says="We met in a dream a long, long time ago."
    
        ...
    
      @ Player says="Hello, sir."
    
        NPC says="You don't remember me, do you?"
    
        ...
    
Your game would interact with this script by implementing subscribers for the `NPC` and `Consider`
events and publishers for the `Greeting` and `Player` events, as described below:

 * The subscriber for `NPC` would cause the NPC to say the value of the `says` parameter.
 * The subscriber for `Consider` would present an option for the player to say the value of the `saying` parameter.
 * You would publish a `Greeting` event when the player begins to interact with the NPC.
 * You would publish a `Player` event whose `says` parameter matches one of the options presented to the player via the `Consider` event.

### More Examples

...TODO: provide some more practical examples...
