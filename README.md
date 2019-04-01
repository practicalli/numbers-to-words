# word-conversion

A Clojure library to convert numerical representation of whole numbers to British English representation, using correct basic grammar.


## Usage

As this is a library and does not include a user interface.  Open a REPL and load the namespace then evaluate the examples in the design journal section.

### Command Line / Terminal
In a terminal window, change to the root of the `word-conversion` project and run the command `lein repl`.  Once the repl starts either include the project namespace in the current `user` namespace, using `(require 'word-conversion.core)`

### Clojure aware editors
Or open this project in your favourite editor, run a repl and switch to the `word-conversion.core` namespace.

## License

Creative Commons Attribution Share-Alike 4.0 International

Copyright © 2019 John Stevenson


## Number to Words Conversion

### Problem Description
Create a Clojure library, suitable for use in a server-side application,that can take a Java int in the range 0 to 999,999,999 inclusive andreturns the equivalent number, as a String, in British English words.

### Sample Data

|     Input | Output                                                                                                   |
|-----------|----------------------------------------------------------------------------------------------------------|
|         0 | zero                                                                                                     |
|         1 | one                                                                                                      |
|        21 | twenty  one                                                                                              |
|       105 | one hundred  and five                                                                                    |
|       123 | one hundred and twenty three                                                                             |
|      1005 | one thousand and five                                                                                    |
|      1042 | one thousand and forty two                                                                               |
|      1105 | one thousand one hundred and five                                                                        |
|  56945781 | fifty six million nine hundred and forty five thousand seven hundred and eighty one                      |
| 999999999 | nine hundred and ninety  nine million nine hundred and ninety nine thousand nine hundred and ninety nine |


### Guidelines

●The solution must be correct.
Please pay attention to the specific conventions of British English, particularly concerning the use of ‘and’between certain phrases.

●The solution is not expected to involve a command line or GUI application
– we’re looking for a Clojure library that could be packaged as a jar andused in a larger application.
