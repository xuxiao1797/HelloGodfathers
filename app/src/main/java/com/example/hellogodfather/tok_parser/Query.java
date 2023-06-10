package com.example.hellogodfather.tok_parser;

import java.util.List;

/**
 * Abstract class Query to represent query
 * @author Peng Zhao
 */
public abstract class Query {
    public abstract String show();
    public abstract List<String> evaluateUsernames();
    public abstract List<String> evaluateTags();
}
