package com.example.hellogodfather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.hellogodfather.tok_parser.Parser;
import com.example.hellogodfather.tok_parser.Query;
import com.example.hellogodfather.tok_parser.Tokenizer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    private static Tokenizer tokenizer;
    private static final String[] SIMPLE_CASE = new String[]{"@Novak", "#Wimbledon"};
    private static final String[] MID_CASE = new String[] {"@novak, @paul", "#Wimbledon, #Melbourne", "@novak; #Wimbledon"};
    private static final String[] COMPLEX_CASE = new String[] {"@novak, @paul; #Wimbledon, #Melbourne, #Novak20", "#Wimbledon, #Melbourne, #Novak20; @novak, @paul"};

    @Test(timeout = 1000)
    public void testSingleAuthor() {
        tokenizer = new Tokenizer(SIMPLE_CASE[0]);
        Query q = new Parser(tokenizer).parseQuery();

        List<String> a = new ArrayList<>();
        a.add("Novak");
        assertEquals(a, q.evaluateUsernames());

        List<String> b = new ArrayList<>();
        b.add("novak");
        assertNotEquals(b, q.evaluateUsernames());
    }

    @Test(timeout = 1000)
    public void testSingleTag() {
        tokenizer = new Tokenizer(SIMPLE_CASE[1]);
        Query q = new Parser(tokenizer).parseQuery();
        List<String> a = new ArrayList<>();
        a.add("wimbledon");
        assertEquals(a, q.evaluateTags());

        List<String> b = new ArrayList<>();
        b.add("Wimbledon");
        assertNotEquals(b, q.evaluateUsernames());
    }

    @Test(timeout = 1000)
    public void testMultipleAuthors() {
        tokenizer = new Tokenizer(MID_CASE[0]);
        Query q = new Parser(tokenizer).parseQuery();
        List<String> a = new ArrayList<>();
        a.add("novak");
        a.add("paul");
        assertEquals(a, q.evaluateUsernames());
    }

    @Test(timeout = 1000)
    public void testMultipleTags() {
        tokenizer = new Tokenizer(MID_CASE[1]);
        Query q = new Parser(tokenizer).parseQuery();
        List<String> a = new ArrayList<>();
        a.add("wimbledon");
        a.add("melbourne");
        assertEquals(a, q.evaluateTags());
    }

    @Test(timeout = 1000)
    public void testAuthorAndTag() {
        tokenizer = new Tokenizer(MID_CASE[2]);
        Query q = new Parser(tokenizer).parseQuery();
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        a.add("novak");
        b.add("wimbledon");
        assertEquals(a, q.evaluateUsernames());
        assertEquals(b, q.evaluateTags());
    }

    @Test(timeout = 1000)
    public void testMultipleAuthorFirst() {
        tokenizer = new Tokenizer(COMPLEX_CASE[0]);
        Query q = new Parser(tokenizer).parseQuery();
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        a.add("novak");
        a.add("paul");
        b.add("wimbledon");
        b.add("melbourne");
        b.add("novak20");
        assertEquals(a, q.evaluateUsernames());
        assertEquals(b, q.evaluateTags());
    }

    @Test(timeout = 1000)
    public void testMultipleTagsFirst() {
        tokenizer = new Tokenizer(COMPLEX_CASE[1]);
        Query q = new Parser(tokenizer).parseQuery();
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        a.add("novak");
        a.add("paul");
        b.add("wimbledon");
        b.add("melbourne");
        b.add("novak20");
        assertEquals(a, q.evaluateUsernames());
        assertEquals(b, q.evaluateTags());
    }

//    @Test(expected = Parser.IllegalQueryException.class)
//    public void testOrException() {
//        // Test a non-identifiable token
//        tokenizer = new Tokenizer("@novak, #Wimbledon");
//        Query q = new Parser(tokenizer).parseQuery();
//    }
//
//    @Test(expected = Parser.IllegalQueryException.class)
//    public void testAndException() {
//        // Test a non-identifiable token
//        tokenizer = new Tokenizer("@novak; @paul");
//        Query q = new Parser(tokenizer).parseQuery();
//    }
}
