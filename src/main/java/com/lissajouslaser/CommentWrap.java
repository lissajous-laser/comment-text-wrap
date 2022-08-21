package com.lissajouslaser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Privdes the wrap() method for neatly wrapping text.
 */
public class CommentWrap {
    private final Map<Character, Character> openingClosingPairs;
    private final int maxLineLength = 80;
    private final int maxTextBlockLength = 65;
    private final int minTextBlockLength = 20;
    private int indentLevel;

    /**
     * Constructer.
     */
    public CommentWrap() {
        indentLevel = 0;

        openingClosingPairs = new HashMap<>();
        openingClosingPairs.put('(', ')');
        openingClosingPairs.put('[', ']');
        openingClosingPairs.put('<', '>');
        openingClosingPairs.put('{', '}');
    }

    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }

    /**
     * Wraps comment code.
     */
    public String wrap(String comment) {

        // Group of symbols indicating start of comment block.
        String blockStartSymbol = bookmatchedStart(comment);
        String commentWithoutEnds = removeEnds(comment, blockStartSymbol);
        String[] sanitisedLinesWithoutEnds =
                sanitiseBlock(commentWithoutEnds.split("\n"));
        String[] sanitisedLines = sanitiseBlock(comment.split("\n"));

        indentLevel = modeIndentLevel(sanitisedLines);

        // Group of symbols indicating start of line comment.
        String lineSymbol;

        if (blockStartSymbol == null) {
            lineSymbol = singleLineSymbol(sanitisedLines);
        } else {
            // This code block looks wierd because we use the length
            // of sanitisedLinesWithoutEnds to calculate range
            // because we can be sure that sanitiseBlock() only
            // removes the last line with it was empty.
            lineSymbol = singleLineSymbol(
                    Arrays.copyOfRange(
                            sanitisedLines,
                            1, // Remove first line with block start symbol.
                            sanitisedLinesWithoutEnds.length - 1
                    )
            );
        }

        String[] linesWithoutEndsAndLineSymbol = removeLineSymbol(
                sanitisedLinesWithoutEnds,
                lineSymbol
        );

        ArrayList<String> words = toWords(linesWithoutEndsAndLineSymbol);

        String rebuiltBlock = rebuildBlock(words, lineSymbol);

        return addEnds(rebuiltBlock, blockStartSymbol, lineSymbol);
    }

    /*
     * Assumptions:
     * Comment symbols are not letters or numbers, and to not
     * exceed 3 characters in length.
     * Single-line comment symbols will be at the start of
     * each line.
     * Multi-line comment symbols may be at the start, and
     * end, are mirror images, and may have a different
     * symbol at each line (like C-style).
     * User indents by spaces. Program outputs indentation in
     * spaces.
     * C-style (java, C, C++, Javascript):
     * // single-line
     * multi-line (2 ways)
     * Lisp
     * ; single-line
     * Scripting langs (Perl, Python, Ruby, PHP)
     * # single-line
     * (Haskell, Lua, SQL)
     * -- single-line
     * Edge cases
     * - no space between comment symbol and start of text
     * - widow or orphan lines selected
     * - comment block start and/or end symbol group in same
     * line as comment text.
     * - single word greater than 80 words
     * - indent level greater than maximum line length -> all
     * comments will be guaranteed 20 character length.
     * - mirror image characters {} () <> {}
     * - single line comment using multi line comment symbol.
     * - comment uses in-line comment symbol but there is only
     *   one line, hard to work out line symbol.
     * Known incompatibilies for block comment symbols
     * - markup languages html, xml <!--  -->
     * - Lua --[  ]
     * - Ruby =begin  =end
     * - Perl =begin  =cut
     * - Matlab %{  %}
     * - Clojure (comment  )
     */

    /**
     * Adds multi-line comment symbols if required.
     **/
    private String addEnds(
            String comment,
            String blockStartSymbol,
            String lineSymbol) {
        if (blockStartSymbol == null) {
            return comment;
        } else {
            // mirror image of blockStartSymbol
            String blockFinishSymbol = "";
            for (int i = blockStartSymbol.length() - 1; i >= 0; i--) {
                char nthChar = blockStartSymbol.charAt(i);

                if (openingClosingPairs.get(nthChar) != null) {
                    blockFinishSymbol += openingClosingPairs.get(nthChar);
                } else {
                    blockFinishSymbol += nthChar;
                }
            }
            StringBuilder strBuilder = new StringBuilder();

            // Case where comment fits in one line, put block
            // symbols on same line.
            if (comment.length() + 2 * (blockStartSymbol.length() + 1)
                    < lineLimit()) {
                strBuilder.append(comment);
                strBuilder.insert(indentLevel, blockStartSymbol + " ");
                // Remove newline at end.
                strBuilder.deleteCharAt(strBuilder.length() - 1);
                strBuilder.append(" " + blockFinishSymbol + "\n");

                return strBuilder.substring(0);
            }

            int blockStartOffset = indentOffset(blockStartSymbol, lineSymbol);
            int blockFinishOffset = indentOffset(blockFinishSymbol, lineSymbol);
            // Only use offsets if both do not cause a negative
            // indent level.
            if (
                    blockStartOffset + indentLevel < 0
                    || blockFinishOffset + indentLevel < 0) {
                blockStartOffset = 0;
                blockFinishOffset = 0;
            }


            for (int i = 0; i < indentLevel - blockStartOffset; i++) {
                strBuilder.append(' ');
            }
            strBuilder.append(blockStartSymbol + "\n");
            strBuilder.append(comment);
            for (int i = 0; i < indentLevel - blockFinishOffset; i++) {
                strBuilder.append(' ');
            }
            strBuilder.append(blockFinishSymbol + "\n");

            return strBuilder.substring(0);
        }
    }

    /*
     * Checks if lineSymbol is contained within blockSymbol, and if true
     * returns an offset that allows that ensures that the lineSymbol in
     * the blockSymbol is aligned with the other lineSymbols.
     */
    private int indentOffset(String blockSymbol, String lineSymbol) {
        if (lineSymbol == null || blockSymbol == null) {
            return 0;
        }
        // lineSymbol is used as a regex pattern, and needs escape
        // characters applied to any regex metacharacters it has.
        var strBuilder = new StringBuilder();
        for (int i = 0; i < lineSymbol.length(); i++) {
            char ch = lineSymbol.charAt(i);

            if ("()[]{}\\^$|?*+.".contains(String.valueOf(ch))) {
                strBuilder.append('\\');
            }
            strBuilder.append(ch);
        }
        String lineSymbolAsRegex = strBuilder.substring(0);

        Pattern pattern = Pattern.compile(lineSymbolAsRegex);
        Matcher matcher = pattern.matcher(blockSymbol);

        if (matcher.find()) {
            return matcher.start();
        } else {
            return 0;
        }
    }

    /**
     * Calculates maximum length of line for the given
     * indent level.
     */
    private int lineLimit() {
        int lineLimit;
        if (indentLevel + maxTextBlockLength > maxLineLength) {
            if (indentLevel + minTextBlockLength > maxLineLength) {
                lineLimit = indentLevel + minTextBlockLength;
            } else {
                lineLimit = maxLineLength;
            }
        } else {
            lineLimit = indentLevel + maxTextBlockLength;
        }
        return lineLimit;
    }

    /*
     * Creates new comment block with in-line comment symbols
     * if required.
     */
    private String rebuildBlock(
            ArrayList<String> words,
            String lineSymbol) {
        StringBuilder strBuilder = new StringBuilder();

        int lineLength = 0;
        for (int i = 0; i < indentLevel; i++) {
            strBuilder.append(' ');
        }
        lineLength += indentLevel;

        if (lineSymbol != null) {
            strBuilder.append(lineSymbol);
            lineLength += lineSymbol.length();
        } else {
            if (indentLevel > 0) {
                /*
                * If there is no in-line comment symbol, remove
                * one space because a space is added before each
                * word.
                */
                strBuilder.deleteCharAt(strBuilder.length() - 1);
            }
        }

        int lineLimit = lineLimit();

        for (String word : words) {
            if (lineLength + 1 + word.length() > lineLimit) {
                strBuilder.append('\n');

                lineLength = 0;
                for (int i = 0; i < indentLevel; i++) {
                    strBuilder.append(' ');
                }
                lineLength += indentLevel;

                if (lineSymbol != null) {
                    strBuilder.append(lineSymbol);
                    lineLength += lineSymbol.length();
                } else {
                    if (indentLevel > 0) {
                        strBuilder.deleteCharAt(strBuilder.length() - 1);
                    }

                }
            }
            strBuilder.append(" " + word);
            lineLength = lineLength + 1 + word.length();
        }
        strBuilder.append('\n');

        return strBuilder.substring(0);
    }

    private ArrayList<String> toWords(String[] lines) {
        var words = new ArrayList<String>();

        for (String line : lines) {
            String[] wordsInLine = line.split(" ");

            for (String word : wordsInLine) {
                // To deal with double spaces.
                if (word.length() > 0) {
                    words.add(word);
                }
            }
        }
        return words;
    }

    /*
     * Removes comment symbol before the comment.
     */
    private String[] removeLineSymbol(String[] lines, String lineSymbol) {
        if (lineSymbol == null) {
            return lines;
        }
        String[] linesWithoutLineSymbol = new String[lines.length];

        for (int i = 0; i < lines.length; i++) {
            String trimmedLine = lines[i].trim();

            if (trimmedLine.substring(0, lineSymbol.length()).equals(lineSymbol)) {
                linesWithoutLineSymbol[i] = trimmedLine.substring(lineSymbol.length());
            } else {
                linesWithoutLineSymbol[i] = trimmedLine;
            }
        }
        return linesWithoutLineSymbol;
    }

    /*
     * Split.
     * Look for multi-line.
     * Look for single-line.
     * Look for Indent.
     * If single line comment symbol is contained within
     * multi-line comment, use C-style
     */

    /*
     * Gets the mode of the line indent levels in a text
     * block.
     */
    private int modeIndentLevel(String[] lines) {

        int[] indentLevels = Arrays
                .stream(lines)
                .mapToInt(x -> indentLevelSingleLine(x))
                .toArray();
        // Stores occorrences of each indent level.
        Map<Integer, Integer> frequencies = new HashMap<>();

        for (int x : indentLevels) {
            Object mapValue = frequencies.putIfAbsent(x, 1);

            if (mapValue != null) {
                frequencies.put(x, (int) mapValue + 1);
            }
        }
        // Find most common indent level.
        ArrayList<Integer> mostCommonIndentLevel = new ArrayList<>();
        int largestFrequency = 0;

        for (int x : frequencies.keySet()) {
            int frequency = frequencies.get(x);

            if (frequency > largestFrequency) {
                mostCommonIndentLevel = new ArrayList<>();
                mostCommonIndentLevel.add(x);

                largestFrequency = frequency;
            } else if (frequency == largestFrequency) {
                mostCommonIndentLevel.add(x);
            }
        }
        // If there are two equally common indent levels,
        // choose smaller.
        Collections.sort(mostCommonIndentLevel);

        return mostCommonIndentLevel.get(0);
    }

    /*
     * Gets the indent level of a line. Treats spaces or tabs
     * the same, so should be used on text that uses only one
     * or the other.
     */

    private int indentLevelSingleLine(String str) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            char charToCheck = str.charAt(i);

            if (charToCheck == ' ' || charToCheck == '\t') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    /*
     * Removes specified number of characters from the start and
     * end of a string.
     */
    private String removeEnds(String comment, String blockStart) {
        String trimmedComment = comment.trim();

        if (blockStart == null) {
            return trimmedComment;
        }
        return trimmedComment.substring(
                blockStart.length(),
                trimmedComment.length() - blockStart.length());
    }

    /**
     * Checks if the first group and last group of symbols
     * are mirror images of each other. """ """ for Python,
     * {- -} for Haskell, #| |# for lisps, { } for
     * Pascal, (* *) for ML languages, C-style blocks.
     * Assumes symbol groups are not greater than 3
     * characters.
     * @return null if no matches.
     */
    public String bookmatchedStart(String comment) {
        final int maxBlockEndLength = 3;
        String trimmedComment = comment.trim();
        var strBuilder = new StringBuilder();

        for (int i = 0; i < maxBlockEndLength; i++) {

            char nthChar = trimmedComment.charAt(i);
            char nthFromLastChar =
                    trimmedComment.charAt(trimmedComment.length() - (1 + i));

            if (String.valueOf(nthChar).matches("[\\s\\w]")) {
                break;
            }
            // First check if characters are complementary
            // opening and closing paris. eg parens, brackets.
            if (openingClosingPairs.get(nthChar) != null
                    && openingClosingPairs.get(nthChar) == nthFromLastChar) {
                strBuilder.append(nthChar);
            } else if (nthChar == nthFromLastChar) {
                strBuilder.append(trimmedComment.charAt(i));
            } else {
                break;
            }
        }
        if (strBuilder.length() == 0) {
            return null;
        } else {
            return strBuilder.substring(0);
        }
    }

    /**
     * Remove lines with only whitespace.
     */
    public String[] sanitiseBlock(String[] lines) {

        String[] sanitisedLines = Arrays
                .stream(lines)
                .filter(x -> !x.trim().isEmpty())
                .toArray(String[]::new);

        return sanitisedLines;
    }

    /**
     * Finds most common starting symbol group of the lines.
     * Assumes symbol groups are not greater than 2
     * characters and are not \w regex characters.
     * If two character groups have a the same frequency
     * the longer character group is chosen.
     * @return Null is returned if a character group does not
     *         occur in more than 2/3 of the lines.
     */
    private String singleLineSymbol(String[] lines) {
        // Stores occurrances of each symbol group.
        var frequencies = new HashMap<String, Integer>();

        for (String line : lines) {
            String trimmedLine = line.trim();
            var strBuilder = new StringBuilder();

            for (int i = 0; i < 2; i++) {
                char nextChar = trimmedLine.charAt(i);

                if (Character.toString(nextChar).matches("[\\s\\w]")) {
                    break;
                }
                strBuilder.append(nextChar);

                Object mapValue = frequencies.putIfAbsent(strBuilder.substring(0), 1);

                if (mapValue != null) {
                    frequencies.put(
                            strBuilder.substring(0),
                            (int) mapValue + 1);
                }
            }
        }
        // Find most common symbol groups.
        ArrayList<String> mostCommonGroups = new ArrayList<>();
        double largestFrequency = 0;

        for (String symbolGroup : frequencies.keySet()) {
            int frequency = frequencies.get(symbolGroup);

            if (frequency > largestFrequency) {
                mostCommonGroups = new ArrayList<>();
                mostCommonGroups.add(symbolGroup);

                largestFrequency = frequency;

            } else if (frequency == largestFrequency) {
                mostCommonGroups.add(symbolGroup);
            }
        }
        // If there are no symbols equal occurrance to line
        // length.
        if (largestFrequency < lines.length) {
            return null;
        }
        if (mostCommonGroups.size() == 0) {
            return null;
        }
        // Return longest symbol group.
        Collections.sort(
                mostCommonGroups,
                new OrderStringByLength());
        return mostCommonGroups.get(mostCommonGroups.size() - 1);
    }
}
