package com.example.hellogodfather.tok_parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Single tag query
 * @author Peng Zhao
 */
public class TagQuery extends Query {
    private String value;

    public TagQuery(String tag) {
        this.value = tag;
    }

    @Override
    public String show() {
        return value;
    }

    @Override
    public List<String> evaluateUsernames() {
        return null;
    }

    @Override
    public List<String> evaluateTags() {
        List<String> values = new ArrayList<>();
        values.add(value.substring(1).toLowerCase());
        return values;
    }
}
