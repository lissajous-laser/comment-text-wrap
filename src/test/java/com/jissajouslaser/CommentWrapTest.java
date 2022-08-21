package com.jissajouslaser;

import com.lissajouslaser.CommentWrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for CommentWrapClass.
 */
public class CommentWrapTest {
    private CommentWrap cw;

    @BeforeEach
    public void init() {
        cw = new CommentWrap();
    }

    @Test
    public void lineComment1() {
        String formattedComment = cw.wrap(
            "            // It was the best of times,\n"
            + "            // it was the worst of times,\n"
            + "            // it was the age of wisdom,\n"
            + "            // it was the age of foolishness,\n"
            + "            // it was the epoch of belief,\n"
            + "            // it was the epoch of incredulity\n"
        );
        assertEquals(
            "            // It was the best of times, it was the worst of times, it was\n"
            + "            // the age of wisdom, it was the age of foolishness, it was the\n"
            + "            // epoch of belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void mostCommonIndentLevelUsed() {
        String formattedComment = cw.wrap(
            "// It was the best of times,\n"
            + "            // it was the worst of times,\n"
            + "            // it was the age of wisdom,\n"
            + "        // it was the age of foolishness,\n"
            + "            // it was the epoch of belief,\n"
            + "            // it was the epoch of incredulity\n"
        );
        assertEquals(
            "            // It was the best of times, it was the worst of times, it was\n"
            + "            // the age of wisdom, it was the age of foolishness, it was the\n"
            + "            // epoch of belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void lineCommentScripting() {
        String formattedComment = cw.wrap(
            "            # It was the best of times,\n"
            + "            # it was the worst of times,\n"
            + "            # it was the age of wisdom,\n"
            + "            # it was the age of foolishness,\n"
            + "            # it was the epoch of belief,\n"
            + "            # it was the epoch of incredulity\n"
        );
        assertEquals(
            "            # It was the best of times, it was the worst of times, it was the\n"
            + "            # age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "            # of belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void blockComment1() {
        String formattedComment = cw.wrap(
            "            /*\n"
            + "            It was the best of times,\n"
            + "            it was the worst of times,\n"
            + "            it was the age of wisdom,\n"
            + "            it was the age of foolishness,\n"
            + "            it was the epoch of belief,\n"
            + "            it was the epoch of incredulity\n"
            + "            */"
        );
        assertEquals(
            "            /*\n"
            + "            It was the best of times, it was the worst of times, it was the\n"
            + "            age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "            of belief, it was the epoch of incredulity\n"
            + "            */\n",
            formattedComment
        );
    }

    @Test
    public void blockCommentPython() {
        String formattedComment = cw.wrap(
            "            \"\"\"\n"
            + "            It was the best of times,\n"
            + "            it was the worst of times,\n"
            + "            it was the age of wisdom,\n"
            + "            it was the age of foolishness,\n"
            + "            it was the epoch of belief,\n"
            + "            it was the epoch of incredulity\n"
            + "            \"\"\""
        );
        assertEquals(
            "            \"\"\"\n"
            + "            It was the best of times, it was the worst of times, it was the\n"
            + "            age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "            of belief, it was the epoch of incredulity\n"
            + "            \"\"\"\n",
            formattedComment
        );
    }

    // Test for regex charactes and also opening closing pairs
    // like (), {}, [], <>
    @Test
    public void blockCommentUsingRegexMetacharacters() {
        String formattedComment = cw.wrap(
            "            (*\n"
            + "            It was the best of times,\n"
            + "            it was the worst of times,\n"
            + "            it was the age of wisdom,\n"
            + "            it was the age of foolishness,\n"
            + "            it was the epoch of belief,\n"
            + "            it was the epoch of incredulity\n"
            + "            *)"
        );
        assertEquals(
            "            (*\n"
            + "            It was the best of times, it was the worst of times, it was the\n"
            + "            age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "            of belief, it was the epoch of incredulity\n"
            + "            *)\n",
            formattedComment
        );
    }

    @Test
    public void blockAndLineComment1() {
        String formattedComment = cw.wrap(
            "           /*\n"
            + "            * It was the best of times,\n"
            + "            * it was the worst of times,\n"
            + "            * it was the age of wisdom,\n"
            + "            * it was the age of foolishness,\n"
            + "            * it was the epoch of belief,\n"
            + "            * it was the epoch of incredulity\n"
            + "            */"
        );
        assertEquals(
            "           /*\n"
            + "            * It was the best of times, it was the worst of times, it was the\n"
            + "            * age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "            * of belief, it was the epoch of incredulity\n"
            + "            */\n",
            formattedComment
        );
    }

    @Test
    public void modifiedJavadoc() {
        String formattedComment = cw.wrap(
            "           /**\n"
            + "            * It was the best of times,\n"
            + "            * it was the worst of times,\n"
            + "            * it was the age of wisdom,\n"
            + "            * it was the age of foolishness,\n"
            + "            * it was the epoch of belief,\n"
            + "            * it was the epoch of incredulity\n"
            + "            **/"
        );
        assertEquals(
            "           /**\n"
            + "            * It was the best of times, it was the worst of times, it was the\n"
            + "            * age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "            * of belief, it was the epoch of incredulity\n"
            + "            **/\n",
            formattedComment
        );
    }

    @Test
    public void maxLineLimitObservedWhileBlockLengthGreaterThanMin() {
        String formattedComment = cw.wrap(
            "                        /*\n"
            + "                        * It was the best of times,\n"
            + "                        * it was the worst of times,\n"
            + "                        * it was the age of wisdom,\n"
            + "                        * it was the age of foolishness,\n"
            + "                        * it was the epoch of belief,\n"
            + "                        * it was the epoch of incredulity\n"
            + "                        */"
        );
        assertEquals(
            "                       /*\n"
            + "                        * It was the best of times, it was the worst of times,\n"
            + "                        * it was the age of wisdom, it was the age of\n"
            + "                        * foolishness, it was the epoch of belief, it was the\n"
            + "                        * epoch of incredulity\n"
            + "                        */\n",
            formattedComment
        );
    }

    @Test
    public void blockEndOffSetsZeroedIfItWasGoingToBeNegative() {
        String formattedComment = cw.wrap(
            "/*\n"
            + "* It was the best of times,\n"
            + "* it was the worst of times,\n"
            + "* it was the age of wisdom,\n"
            + "* it was the age of foolishness,\n"
            + "* it was the epoch of belief,\n"
            + "* it was the epoch of incredulity\n"
            + "*/"
        );
        assertEquals(
            "/*\n"
            + "* It was the best of times, it was the worst of times, it was the\n"
            + "* age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "* of belief, it was the epoch of incredulity\n"
            + "*/\n",
            formattedComment
        );
    }

    @Test
    public void unmatchedBlockEndSymbolsIgnored() {
        String formattedComment = cw.wrap(
            "            @#\n"
            + "            It was the best of times,\n"
            + "            it was the worst of times,\n"
            + "            it was the age of wisdom,\n"
            + "            it was the age of foolishness,\n"
            + "            it was the epoch of belief,\n"
            + "            it was the epoch of incredulity\n"
            + "            $%"
        );
        assertEquals(
            "            @# It was the best of times, it was the worst of times, it was\n"
            + "            the age of wisdom, it was the age of foolishness, it was the\n"
            + "            epoch of belief, it was the epoch of incredulity $%\n",
            formattedComment
        );
    }

    @Test
    public void notAllLinesHaveLineSymbol() {
        String formattedComment = cw.wrap(
            "            // It was the best of times,\n"
            + "             it was the worst of times,\n"
            + "            // it was the age of wisdom,\n"
            + "            // it was the age of foolishness,\n"
            + "            // it was the epoch of belief,\n"
            + "            // it was the epoch of incredulity\n"
        );
        assertEquals(
            "            // It was the best of times, it was the worst of times, // it\n"
            + "            was the age of wisdom, // it was the age of foolishness, // it\n"
            + "            was the epoch of belief, // it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void includingFragmentsFromPreviousAndFollowingLinesIsHandled() {
        String formattedComment = cw.wrap(
            "\n"
            + "            // It was the best of times,\n"
            + "            // it was the worst of times,\n"
            + "            // it was the age of wisdom,\n"
            + "            // it was the age of foolishness,\n"
            + "            // it was the epoch of belief,\n"
            + "            // it was the epoch of incredulity\n"
            + "   "
        );
        assertEquals(
            "            // It was the best of times, it was the worst of times, it was\n"
            + "            // the age of wisdom, it was the age of foolishness, it was the\n"
            + "            // epoch of belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void ifItFitsPutSingleLineWithBlockCommentSymbol() {
        String formattedComment = cw.wrap(
            "            /*\n"
            + "            It was the best of times,\n"
            + "             it was the worst of times.\n"
            + "            */"
        );
        assertEquals(
            "            /* It was the best of times, it was the worst of times. */\n",
            formattedComment
        );
    }

}
