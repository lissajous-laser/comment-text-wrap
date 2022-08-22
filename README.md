## Comment Text Wrap

# Background
Text editors and IDEs generally don't automatically wrap text for programming code, so it is up to the author to put in sensible line breaks.

Comments in code should be kept to a length that is easy to read. In typography, although opinions vary, a line length of between 2 - 3 alphabets (52 to 78 characters) is often considered an suitable line length.

It can be tedious to manually format a comment section, especially when adding to an existing comment at any point except the end. You have to reflow words into the following lines to maintain the line length.

# Implementation
This is a GUI application using JavaFX. The program will attempt to reformat code comments so that the text is wrapped at a suitable line length. It is designed to be language agnostic, to work with single line comments and multi-line comments.

### Limitations
- Do to difficulties in inferring symbols being used to denote comments, it will only work for comments with more than one line break.
- It was only been implemented to work indentation by spaces, and not tabs.
- Multi-line comments only work for languages where the opening and closing delimiters are the reverse of each other. e.g.
  - `/* comment */` for C-syntax languages works because `/*` and `*/` are the reverse of each other. 
  - `(* comment *)` in OCaml or Pascal also works, because the symbol pairs (), {}, [], <> have been made to be matched. 
  - `%{ comment %}` in MATLAB will not work, because `%{` and `%}` are not the reverse of each other.
 The following table illustrates which multi-line comments will work for some programming languages:
| Language (Family)                           | Block Comment         | Works  |
| --------------------------------------------| ----------------------| ------ |
| C syntax (C, C++, C#, Java, Javascript, PHP)| `/* comment */`       | Yes    |
| Python pseudo-comment                       | `""" comment """`     | Yes    |
| Haskell                                     | `{- comment -}`       | Yes    |
| Julia                                       | `#= comment =#`       | Yes    |
| ML syntax (OCaml, F#)                       | `(* comment *)`       | Yes    |
| Ruby                                        | `=begin comment =end` | No     |
| Perl                                        | `=begin comment =cut` | No     |
| Lua                                         | `--[[ comment --]]`   | No     |
| MATLAB                                      | `%{ comment %}`       | No     |

 Single line comments will work for the vast majority of languages.
- Comment blocks with ascii borders will only work if the top and bottom borders are reverse of each other and if no \s or \w characters are used.

### Text Wrapping
The text column is set at a maximum of 65 characters, not including indentation spaces. The total length of the line will not exceed 80 characters unless the text column length has been reduced to a minimum of 20 characters, in which case the limit is the indent level + 20 characters.

### Style
The output is formatted in a pre-defined style, so it does not striclty maintain the style of the original comment. Single line comments will work as expected. Multi line comment symbols will try to fit into a single line if possible:
```
/* It was the best of times, it was the worst of times. */
```
If the comment does not fit a single line the comment symbols will be on separate lines:
```
/*
It was the best of times, it was the worst of times, it was the
age of wisdom, it was the age of foolishness, it was the epoch of
belief, it was the epoch of incredulity
*/
```
C-syntax languages that use asterisks in their multi-line comments like:
```
/*
 * Line 1
 * Line 2
 */
```
are supported. The program will align the asterisks to the same column. Java Javadoc comments are not natively supported, but a workaround could be to use:
```
/**
 * Line 1
 * Line 2
 **/
```