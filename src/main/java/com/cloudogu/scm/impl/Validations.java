package com.cloudogu.scm.impl;

import java.net.MalformedURLException;
import java.net.URL;

public final class Validations {

    private Validations() {
    }

    public static boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }
}
