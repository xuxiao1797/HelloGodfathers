package com.example.hellogodfather.tok_parser;

import java.util.Scanner;

/**
 * Parsing is the process of taking a bunch of tokens and evaluating them.
 * The following is the grammar rule:
 * <query>     -> <usernames> <tags> | <usernames> | <tags>
 * <usernames>   -> <username> | <username> <usernames>
 * <tags>      -> <tag> | <tag> <tags>
 * <username>    -> <at> <[a-zA-z0-9_>
 * <tag>       -> <pound> <[a-zA-z0-9_]>
 * @author Peng Zhao
 */
public class Parser {

    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);

        /*
         Continue to get the user's input until they exit.
         */
        System.out.println("Provide a mathematical string to be parsed:");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();

            // Check if 'quit' is provided.
            if (input.equals("q"))
                break;

            // Create an instance of the tokenizer.
            Tokenizer tokenizer = new Tokenizer(input);

            // Print out the query from the parser.
            Parser parser = new Parser(tokenizer);
            Query expression = parser.parseQuery();
            System.out.println("Parsing: " + expression.show());
            System.out.println("Evaluation: " + expression.evaluateUsernames());
            System.out.println("Evaluation: " + expression.evaluateTags());
        }
    }

    public static class IllegalQueryException extends IllegalArgumentException {
        public IllegalQueryException(String errorMessage) {
            super(errorMessage);
        }
    }

    // The tokenizer (class field) this parser will use.
    Tokenizer tokenizer;

    /**
     * Parser class constructor
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * Adheres to the grammar rule:
     * <query>     -> <usernames> <tags> | <usernames> | <tags>
     * @return type: Query.
     */
    public Query parseQuery() {
        if (tokenizer.current().getType() != Token.Type.USERNAME
                && tokenizer.current().getType() != Token.Type.TAG) {
            throw new IllegalQueryException("");
        }
        Token.Type t = tokenizer.current().getType();
        Query q = parseUsernamesOrTags();
        if (tokenizer.hasNext()) {
            if (t == Token.Type.USERNAME) {
                if (tokenizer.current().getType() == Token.Type.AND) {
                    tokenizer.next();
                    if (tokenizer.current().getType() == Token.Type.TAG) {
                        return new AndQuery(q, parseQuery());
                    } else {
                        throw new IllegalQueryException("");
                    }
                }
            }
            if (t == Token.Type.TAG) {
                if (tokenizer.current().getType() == Token.Type.AND) {
                    tokenizer.next();
                    if (tokenizer.current().getType() == Token.Type.USERNAME) {
                        return new AndQuery(parseQuery(), q);
                    } else {
                        throw new IllegalQueryException("");
                    }
                }
            }
        }
        return q;
    }

    /**
     * Adheres to the grammar rule:
     * <usernames>   -> <username> | <username> <usernames>
     * <tags>      -> <tag> | <tag> <tags>
     * @return type: Query.
     */
    public Query parseUsernamesOrTags() {
        if (tokenizer.current().getType() != Token.Type.USERNAME
                && tokenizer.current().getType() != Token.Type.TAG) {
            throw new IllegalQueryException("");
        }
        Query q = parseUsernameOrTag();
        Token.Type t = tokenizer.current().getType();
        tokenizer.next();
        if (tokenizer.hasNext()) {
            if (tokenizer.current().getType() == Token.Type.OR) {
                tokenizer.next();
                if (tokenizer.current().getType() == t) {
                    if (tokenizer.current().getType() == Token.Type.USERNAME) {
                        return new UsernamesQuery(q, parseUsernamesOrTags());
                    }
                    if (tokenizer.current().getType() == Token.Type.TAG) {
                        return new TagsQuery(q, parseUsernamesOrTags());
                    }
                } else {
                    throw new IllegalQueryException("");
                }
            }
        }
        return q;
    }

    /**
     * Adheres to the grammar rule:
     * <username>   -> <username>
     * <tag>      -> <tag>
     * @return type: Query.
     */
    public Query parseUsernameOrTag() {
        if (tokenizer.current().getType() != Token.Type.USERNAME
                && tokenizer.current().getType() != Token.Type.TAG) {
            throw new IllegalQueryException("");
        }
        if (tokenizer.current().getType() == Token.Type.USERNAME) {
            return new UsernameQuery(tokenizer.current().getToken());
        }
        if (tokenizer.current().getType() == Token.Type.TAG) {
            return new TagQuery(tokenizer.current().getToken());
        }
        throw new IllegalQueryException("");
    }
}
