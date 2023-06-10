package com.example.hellogodfather.tok_parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Or query between tags
 * @author Peng Zhao
 */
public class TagsQuery extends Query {
    private Query a;
    private Query b;

    public TagsQuery(Query a, Query b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String show() {
        return "(" + a.show() + " OR " + b.show() + ")";
    }

    @Override
    public List<String> evaluateUsernames() {
        return null;
    }

    @Override
    public List<String> evaluateTags() {
        List<String> values = new ArrayList<>();
        values.addAll(a.evaluateTags());
        values.addAll(b.evaluateTags());
        return values;
    }
}
