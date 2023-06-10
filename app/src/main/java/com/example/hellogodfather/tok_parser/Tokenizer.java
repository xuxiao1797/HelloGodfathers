package com.example.hellogodfather.tok_parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenization, split a string into small units called, Tokens, to be passed to parser
 */
public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    /**
     * Tokenizer class constructor
     * The constructor extracts the first token and save it to currentToken
     * @param text
     */
    public Tokenizer(String text) {
        buffer = getValidText(text);         // save input text (string)

        if (!buffer.equals("")) {
            next();                         // extracts the first token.
        } else {
            throw new Token.IllegalTokenException("");
        }
    }

    private String getValidText(String text) {
        ArrayList<String> validQueryWithPound = new ArrayList<>();
        ArrayList<String> validQueryWithAt = new ArrayList<>();
        String t = text.replace(" ", "");
        String[] h = t.split(";");
        Pattern pattern_pound = Pattern.compile("(#[a-zA-Z0-9_]+)");
        Pattern pattern_at = Pattern.compile("(@[a-zA-Z0-9_]+)");
        for (String i : h) {
            String[] j = i.split(",");
            for (String k : j){
                if (k.contains("#")) {
                    Matcher matcher_pound = pattern_pound.matcher(k);
                    if (matcher_pound.matches()) {
                        validQueryWithPound.add(k);
                    }
                }
                if (k.contains("@")) {
                    Matcher matcher_at = pattern_at.matcher(k);
                    if (matcher_at.matches()) {
                        validQueryWithAt.add(k);
                    }
                }
            }
        }
        StringBuilder inputTemp = new StringBuilder();
        for (int i = 0; i < validQueryWithPound.size() - 1; i++) {
            inputTemp.append(validQueryWithPound.get(i)).append(",");
        }
        if (validQueryWithPound.size() >= 1) {
            inputTemp.append(validQueryWithPound.get(validQueryWithPound.size() - 1));
        }
        if (!validQueryWithAt.isEmpty()) {
            if (validQueryWithPound.size() >= 1) {
                inputTemp.append(";");
            }
            for (int i = 0; i < validQueryWithAt.size() - 1; i++) {
                inputTemp.append(validQueryWithAt.get(i)).append(",");
            }
            inputTemp.append(validQueryWithAt.get(validQueryWithAt.size() - 1));
        }
        return inputTemp.toString();
    }

    /**
     * This function will find and extract a next token from {@code _buffer} and
     * save the token to {@code currentToken}.
     */
    public void next() {
        buffer = buffer.replace(" ", ""); // remove whitespace

        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }

        char firstChar = buffer.charAt(0);
        if (firstChar == '#') {
            StringBuilder s = new StringBuilder(Character.toString(firstChar));
            for (int i = 1; i < buffer.length(); i++) {
                char c = buffer.charAt(i);
                if (Character.isDigit(c) || Character.isAlphabetic(c) || c == '_') {
                    s.append(c);
                } else {
                    break;
                }
            }
            currentToken = new Token(s.toString(), Token.Type.TAG);
        }
        if (firstChar == '@') {
            StringBuilder s = new StringBuilder(Character.toString(firstChar));
            for (int i = 1; i < buffer.length(); i++) {
                char c = buffer.charAt(i);
                if (Character.isDigit(c) || Character.isAlphabetic(c) || c == '_') {
                    s.append(c);
                } else {
                    break;
                }
            }
            currentToken = new Token(s.toString(), Token.Type.USERNAME);
        }
        if (firstChar == ',')
            currentToken = new Token(",", Token.Type.OR);
        if (firstChar == ';')
            currentToken = new Token(";", Token.Type.AND);

        if (firstChar != '#'
                && firstChar != '@'
                && firstChar != ','
                && firstChar != ';')
            throw new Token.IllegalTokenException("");

        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);
    }

    /**
     * Return current token extracted by {@code next()}
     * @return type: Token
     */
    public Token current() {
        return currentToken;
    }

    /**
     * Check whether there are still tokens
     * @return type: boolean
     */
    public boolean hasNext() {
        return currentToken != null;
    }

    /**
     * Check whether there are still tokens
     * @return type: boolean
     */
    public String getBuffer() {
        return buffer;
    }
}
