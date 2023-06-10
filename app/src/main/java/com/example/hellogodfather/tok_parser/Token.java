package com.example.hellogodfather.tok_parser;

import java.util.Objects;

/**
 * Token class to save extracted token from tokenizer.
 * The following are different types of tokens:
 * TAG: #Novak20_
 * USERNAME: @paul_;
 * OR: ,
 * AND: ;
 */
public class Token {
    // The following enum defines different types of tokens. Example of accessing this: Token.Type.
    public enum Type {TAG, USERNAME, OR, AND}

    /**
     * The following exception should be thrown if a tokenizer attempts to tokenize something that is not of one
     * of the types of tokens.
     */
    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Fields of the class Token.
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken(){
        return token;
    }

    public Type getType() {
        return type;
    }

    // TODO: Complete the following methods.
    @Override
    public String toString() {
        if (type == Type.OR)
            return type + "";
        if (type == Type.AND)
            return type + "";
        return token;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true; // Same hashcode.
        if (!(other instanceof Token)) return false; // Null or not the same type.
        return this.type == ((Token) other).getType() && this.token.equals(((Token) other).getToken()); // Values are the same.
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, type);
    }
}
