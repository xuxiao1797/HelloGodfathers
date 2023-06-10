package com.example.hellogodfather.tok_parser;

import java.util.List;

/**
 * @author Peng Zhao
 */
public class AndQuery extends Query {
    private Query a;
    private Query b;

    public AndQuery(Query usernames, Query tags) {
        this.a = usernames;
        this.b = tags;
    }

    @Override
    public String show() {
        return "(" + a.show() + " AND " + b.show() + ")";
    }

    @Override
    public List<String> evaluateUsernames() {
        return a.evaluateUsernames();
    }

    @Override
    public List<String> evaluateTags() {
        return b.evaluateTags();
    }
}
