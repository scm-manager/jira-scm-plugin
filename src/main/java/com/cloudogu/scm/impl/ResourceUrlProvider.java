package com.cloudogu.scm.impl;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.UrlMode;
import com.cloudogu.scm.Constants;
import com.google.common.annotations.VisibleForTesting;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Scanned
public class ResourceUrlProvider {

    @VisibleForTesting
    static final String RESOURCE_KEY = "jira-scm-plugin-resources";

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public ResourceUrlProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getIconUrl() {
        return createResourceUrl("images/icon.png");
    }

    public String getLogoUrl() {
        return createResourceUrl("images/logo.png");
    }

    private String createResourceUrl(String path) {
        return String.format("%s/download/resources/%s:%s/%s",
                applicationProperties.getBaseUrl(UrlMode.ABSOLUTE),
                Constants.ADDON_KEY,
                RESOURCE_KEY,
                path
        );
    }
}
