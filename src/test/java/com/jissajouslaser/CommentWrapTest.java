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
            + "            age of wisdom, it was the age of foolishness, it was the epoch of\n"
            + "            belief, it was the epoch of incredulity\n"
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
            + "            age of wisdom, it was the age of foolishness, it was the epoch of\n"
            + "            belief, it was the epoch of incredulity\n"
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
            + "            age of wisdom, it was the age of foolishness, it was the epoch of\n"
            + "            belief, it was the epoch of incredulity\n"
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
    public void noIndentSingleLineSymbolWorks() {
        String formattedComment = cw.wrap(
            "// It was the best of times,\n"
            + "// it was the worst of times,\n"
            + "// it was the age of wisdom,\n"
            + "// it was the age of foolishness,\n"
            + "// it was the epoch of belief,\n"
            + "// it was the epoch of incredulity\n"
        );
        assertEquals(
            "// It was the best of times, it was the worst of times, it was\n"
            + "// the age of wisdom, it was the age of foolishness, it was the\n"
            + "// epoch of belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void noIndentBlockEndSymbolWorks() {
        String formattedComment = cw.wrap(
            "/*\n"
            + "It was the best of times,\n"
            + "it was the worst of times,\n"
            + "it was the age of wisdom,\n"
            + "it was the age of foolishness,\n"
            + "it was the epoch of belief,\n"
            + "it was the epoch of incredulity\n"
            + "*/"
        );
        assertEquals(
            "/*\n"
            + "It was the best of times, it was the worst of times, it was the\n"
            + "age of wisdom, it was the age of foolishness, it was the epoch of\n"
            + "belief, it was the epoch of incredulity\n"
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
            "            // It was the best of times, it was the worst of times, // it was\n"
            + "            the age of wisdom, // it was the age of foolishness, // it was\n"
            + "            the epoch of belief, // it was the epoch of incredulity\n",
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

    @Test
    public void someLinesHaveOneCharacter() {
        String formattedComment = cw.wrap(
            "           /*\n"
            + "            *\n"
            + "            * it was the worst of times,\n"
            + "            *\n"
            + "            * it was the age of foolishness,\n"
            + "            *\n"
            + "            * it was the epoch of incredulity\n"
            + "            */"
        );
        assertEquals(
            "           /*\n"
            + "            * it was the worst of times, it was the age of foolishness, it\n"
            + "            * was the epoch of incredulity\n"
            + "            */\n",
            formattedComment
        );
    }

    @Test
    public void someLinesHaveGaps() {
        String formattedComment = cw.wrap(
            "            // it was the worst of times,\n"
            + "            \n"
            + "            // it was the age of foolishness,\n"
            + "            \n"
            + "            // it was the epoch of incredulity\n"
        );
        assertEquals(
            "            // it was the worst of times, it was the age of foolishness, it\n"
            + "            // was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void singleLineCommentIsReturnedAsIs() {
        String formattedComment = cw.wrap(
            "            // it was the worst of times,"
            + " it was the age of foolishness,\n"
        );
        assertEquals(
            "            // it was the worst of times,"
            + " it was the age of foolishness,\n",
            formattedComment
        );
    }

    @Test
    public void emptyStringIsReturnedAsIs() {
        String formattedComment = cw.wrap(
            ""
        );
        assertEquals(
            "",
            formattedComment
        );
    }

    @Test
    public void uncommentedText() {
        String formattedComment = cw.wrap(
            "It was the best of times,\n"
            + "it was the worst of times,\n"
            + "it was the age of wisdom,\n"
            + "it was the age of foolishness,\n"
            + "it was the epoch of belief,\n"
            + "it was the epoch of incredulity\n"
        );
        assertEquals(
            "It was the best of times, it was the worst of times, it was the\n"
            + "age of wisdom, it was the age of foolishness, it was the epoch of\n"
            + "belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void commentAsNumbers() {
        String formattedComment = cw.wrap(
            "# 0.8822620394120473 0.07484343029412677 0.29067634467332193 0.6209728411369321\n"
            + "# 0.6545777840851758 0.5732667167518397 0.3422455327447361 0.6560189180675021\n"
            + "# 0.15303730980833974 0.7711973251711577 0.18154755583324367 0.1655220511354173\n"
            + "# 0.5633374087994983 0.45525850983494376 0.24948127876311355 0.41582896689800986\n"
            + "# 0.7414954780694972 0.6583620247539784 0.7342856990003805 0.14536576838562232"
        );
        assertEquals(
            "# 0.8822620394120473 0.07484343029412677 0.29067634467332193\n"
            + "# 0.6209728411369321 0.6545777840851758 0.5732667167518397\n"
            + "# 0.3422455327447361 0.6560189180675021 0.15303730980833974\n"
            + "# 0.7711973251711577 0.18154755583324367 0.1655220511354173\n"
            + "# 0.5633374087994983 0.45525850983494376 0.24948127876311355\n"
            + "# 0.41582896689800986 0.7414954780694972 0.6583620247539784\n"
            + "# 0.7342856990003805 0.14536576838562232\n",
            formattedComment
        );
    }

    @Test
    public void noSpaceBetweenLineSymbolAndText() {
        String formattedComment = cw.wrap(
            "            --It was the best of times,\n"
            + "            --it was the worst of times,\n"
            + "            --it was the age of wisdom,\n"
            + "            --it was the age of foolishness,\n"
            + "            --it was the epoch of belief,\n"
            + "            --it was the epoch of incredulity\n"
        );
        assertEquals(
            "            -- It was the best of times, it was the worst of times, it was\n"
            + "            -- the age of wisdom, it was the age of foolishness, it was the\n"
            + "            -- epoch of belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void commentContainsConsecutiveSpaces() {
        String formattedComment = cw.wrap(
            "            ; It was the best of times,\n"
            + "            ;  it was the worst of times,\n"
            + "            ;    it was the age of wisdom,\n"
            + "            ; it was the age of foolishness,\n"
            + "            ; it was the  epoch of belief,\n"
            + "            ; it was the    epoch of incredulity\n"
        );
        assertEquals(
            "            ; It was the best of times, it was the worst of times, it was the\n"
            + "            ; age of wisdom, it was the age of foolishness, it was the epoch\n"
            + "            ; of belief, it was the epoch of incredulity\n",
            formattedComment
        );
    }

    @Test
    public void lineSymbolUsedAsBorder() {
        String formattedComment = cw.wrap(
            "            ////////////////////////////////////////////////////////////\n"
            + "            // It was the best of times,\n"
            + "            // it was the worst of times,\n"
            + "            // it was the age of wisdom,\n"
            + "            // it was the age of foolishness,\n"
            + "            // it was the epoch of belief,\n"
            + "            // it was the epoch of incredulity\n"
            + "            ////////////////////////////////////////////////////////////"
        );
        assertEquals(
            "            ////////////////////////////////////////////////////////////\n"
            + "            // It was the best of times, it was the worst of times, it was\n"
            + "            // the age of wisdom, it was the age of foolishness, it was the\n"
            + "            // epoch of belief, it was the epoch of incredulity\n"
            + "            ////////////////////////////////////////////////////////////\n",
            formattedComment
        );
    }
}
