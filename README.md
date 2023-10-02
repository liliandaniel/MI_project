# MI_project

To run the game with these players, you can follow the instructions:

Compile the Agent.java and SamplePlayer.java classes using the game engine JAR file:

```ruby
javac -cp game_engine.jar Agent.java
javac -cp game_engine.jar SamplePlayer.java
```
Run the game using the java command and specify the required parameters:
```ruby
java -jar game_engine.jar 3 game.quoridor.QuoridorGame 1234567890 1000 Agent game.quoridor.players.BlockRandomPlayer
```
This command will run the Quoridor game with the specified parameters the Agent and BlockRandomPlayer as players.

Make sure you have the game_engine.jar file in the same directory or provide the correct path to it in the -cp (classpath) option when compiling and running the Java classes.

