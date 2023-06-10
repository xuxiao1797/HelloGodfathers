package com.example.hellogodfather.tok_parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Single query between usernames
 * @author Peng Zhao
 */
public class UsernameQuery extends Query {
    private String value;

    public UsernameQuery(String username) {
        this.value = username;
    }

    @Override
    public String show() {
        return value;
    }

    @Override
    public List<String> evaluateUsernames() {
        List<String> values = new ArrayList<>();
        values.add(value.substring(1));
        return values;
    }

    @Override
    public List<String> evaluateTags() {
        return null;
    }
}
