package com.example.hellogodfather.tok_parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Or query between usernames
 * @author Peng Zhao
 */
public class UsernamesQuery extends Query {
    private Query a;
    private Query b;

    public UsernamesQuery(Query a, Query b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String show() {
        return "(" + a.show() + " OR " + b.show() + ")";
    }

    @Override
    public List<String> evaluateUsernames() {
        List<String> values = new ArrayList<>();
        values.addAll(a.evaluateUsernames());
        values.addAll(b.evaluateUsernames());
        return values;
    }

    @Override
    public List<String> evaluateTags() {
        return null;
    }
}
