# word-conversion

A Clojure library to convert numerical representation of whole numbers to British English representation, using correct basic grammar.


## Usage

As this is a library and does not include a user interface.  Open a REPL and load the namespace then evaluate the examples and experiment with the design journal section.

### Command Line / Terminal
In a terminal window, change to the root of the `word-conversion` project and run the command `lein repl`.  Once the repl starts either include the project namespace in the current `user` namespace, using `(require 'word-conversion.core)`

### Clojure aware editors
Or open this project in your favourite editor, run a repl and switch to the `word-conversion.core` namespace.

### main function

Use the `speak-number-as-words` to convert a number to a sentence that describes that word.  For example:

```clojure
(speak-number-as-words british-english-dictionary 1105)

```

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


## Analysis of the problem statement

### Notes
* the British English words are all lower case, no capitalisation is required for the start of each sentence.


### Initial thoughts for a solution

Taking a simplest set first approach:

#### Convert each word to a string using a simple lookup

Define a dictionary that maps numbers 0 to 9 to their word equivalents.


#### Insert the number scales in the correct places

Insert `hundred`, `thousand`, and `million` number scale names into the words.

> Wondering if this needs to be done first or at least at the same time as converting from numbers to strings.


#### Insert the correct grammar into the words (and)

Parse a collection of words inserting `and` at the appropriate point in the sentence.  The rules seem to be positional, so we can just process the word strings for the correct places to insert `and`.

The rules seem to be:
- numbers between 101 and 999 have `and` after the first number (or before the last two numbers).
- the above rule should be applied all at each number scale for numbers larger than 1001


### Additional thoughts ###

#### Splitting up a number into digits ####

How can we split up a number into its digits?  Unlike string, a number is not seen as a collection.  We can convert each number to a string, this would allow us to split the number into individual digits, although if using clojure.core functions then the string will be treated as a collection of characters.  Can we convert characters back to numbers (should we need to)?  Strings can be converted back to numbers easily with the `java.lang.Integer` class.

If we use strings or characters in our dictionary lookup, then we don't need to convert back.

Some experiments in partitioning are in the design journal.


#### replace each number with its whole value ####

If we took each digit in the original number and converted it to its representative number in terms of position, then the dictionary lookup becomes much simpler.

For example, if the original number is 12345, then we would first generate a sequence of `[10000 2000 300 40 5]`.

This approach seems to be pretty obvious, although we still need to group along number levels, for numbers such as 124,110 (One hundred and twenty four thousand, one hundred and ten)


#### Break down numbers into their smallest parts

Replacing the whole number with a value to represent each digit is close to how I want to solve this problem.  However, for larger numbers they still need to be broken down further and have some notation to represent their number level.  Then it is just a simple matter of using a dictionary to map over the individual numbers and levels to convert it to words.

Some post processing on the converted sequence adds grammar correction to the words and gives a sentence by injecting `and` at the relevant place.  This seems to be after any instance of `hundred` which is followed by another number.


## Interesting functions to consider

`partition` will group consistently and provide an simple way to insert new strings into the right parts of the words.
