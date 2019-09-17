Blather is a solution to a coding exercise used by Codurance for evaluating job applicants for craftsperson or apprentice positions.

The program runs on the command line and implements a simplified chat room program.

This fork adds a simple TCP server which can be accessed with `telnet` etc, to practice concurrency and socket programming in Java.

To run the end-to-end test, be in the project directory and execute:

```bash
gradle build
src/test/resources/test-end-to-end.sh
```

To run Blather, build the 'fat jar' which includes all necessary dependencies:

```bash
gradle fatJar
```

then execute:

```bash
java -jar build/libs/blather-all.jar
```

To post a message to a user, execute:

```
rich -> Hello world!
sarah -> Omg it's snowing!
```

To read messages posted to a user, enter just their name:

```
rich
Hello world! (1 minute ago)
sarah
Omg it's snowing! (3 seconds ago)
```

To make a user follow another user, execute:

```
jolene follows rich
jolene follows sarah
```

To read all messages posted to users followed by a particular user, execute:

```
jolene wall
rich - Hello world! (3 minutes ago)
sarah - Omg it's snowing! (1 minute ago)
```

To exit Blather, execute:

```
quit
```
