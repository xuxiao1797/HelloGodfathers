package com.example.hellogodfather;

import static org.junit.Assert.assertEquals;

import com.example.hellogodfather.tok_parser.Token;
import com.example.hellogodfather.tok_parser.Tokenizer;

import org.junit.Test;

public class TokenizerTest {

    private static final String TEST = "@novak, @paul_; #_Wimbledon, #Melbourne, #Novak20";
    private static Tokenizer tokenizer = new Tokenizer(TEST);;

    @Test(timeout = 1000)
    public void testAuthor() {

        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.USERNAME, tokenizer.current().getType());

        // check the actual token value"
        assertEquals("wrong token value", "@paul_", tokenizer.current().getToken());
    }

    @Test(timeout = 1000)
    public void testOr() {
        tokenizer = new Tokenizer(TEST);

        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.OR, tokenizer.current().getType());

        // check the actual token value"
        assertEquals("wrong token value", ",", tokenizer.current().getToken());
    }

    @Test(timeout = 1000)
    public void testAnd() {
        tokenizer = new Tokenizer(TEST);

        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.AND, tokenizer.current().getType());

        // check the actual token value"
        assertEquals("wrong token value", ";", tokenizer.current().getToken());
    }

    @Test(timeout = 1000)
    public void testTag() {
        tokenizer = new Tokenizer(TEST);

        // check the type of the first token
        assertEquals("wrong token type", Token.Type.TAG, tokenizer.current().getType());

        // check the actual token value"
        assertEquals("wrong token value", "#_Wimbledon", tokenizer.current().getToken());
    }

    @Test
    public void testGetValidText() {
        // Test a non-identifiable token
        Tokenizer tokenizer = new Tokenizer("p/aul, @/peng, @David, @Victor9; #Novak20, #20Novak, #Wimbledon, #.Novak");

        assertEquals("wrong text", "#Novak20", tokenizer.current().getToken());

        assertEquals("wrong text", ",#20Novak,#Wimbledon;@David,@Victor9", tokenizer.getBuffer());

    }

    @Test(expected = Token.IllegalTokenException.class)
    public void testInvalidException() {
        // Test a non-identifiable token
        Tokenizer tokenizer = new Tokenizer("/Novak20");
        tokenizer.next();
    }


}
