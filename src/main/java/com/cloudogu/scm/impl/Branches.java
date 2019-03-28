package com.cloudogu.scm.impl;

import com.atlassian.jira.issue.Issue;
import com.google.common.base.CharMatcher;

import java.util.Locale;

public final class Branches {

    private static final CharMatcher ALLOWED_CHARS_MATCHER =  CharMatcher.inRange('a', 'z')
            .or(CharMatcher.DIGIT)
            .or(CharMatcher.is('_'));

    private Branches() {
    }

    public static String createName(Issue issue) {
        return createPrefix(issue) + issue.getKey() + "_" + normalizeSummary(issue.getSummary());
    }

    private static String createPrefix(Issue issue) {
        String name = issue.getIssueType().getName();
        if (name.equalsIgnoreCase("bug")) {
            return "bugfix/";
        }
        return "feature/";
    }

    private static String normalizeSummary(String summary) {
        String lowerCase = summary.toLowerCase(Locale.ENGLISH);
        lowerCase = CharMatcher.WHITESPACE.replaceFrom(lowerCase, "_");
        String normalized = ALLOWED_CHARS_MATCHER.retainFrom(lowerCase);
        if (normalized.length() > 24) {
            return normalized.substring(0, 24);
        }
        return normalized;
    }
}
