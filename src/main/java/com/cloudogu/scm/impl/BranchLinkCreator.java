package com.cloudogu.scm.impl;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.RemoteIssueLink;
import com.atlassian.jira.issue.link.RemoteIssueLinkBuilder;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.cloudogu.scm.api.ScmSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.net.UrlEscapers;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Scanned
public class BranchLinkCreator {

    @VisibleForTesting
    public static final String ICON_TITLE = "issue.branch-link.icon-title";

    @VisibleForTesting
    public static final String RELATIONSHIP = "issue.branch-link.relationship";

    private final ResourceUrlProvider resourceUrlProvider;

    @ComponentImport
    private final I18nResolver i18n;

    private final ScmSettings scmSettings;

    @Inject
    public BranchLinkCreator(ResourceUrlProvider resourceUrlProvider, I18nResolver i18n, ScmSettings scmSettings) {
        this.resourceUrlProvider = resourceUrlProvider;
        this.i18n = i18n;
        this.scmSettings = scmSettings;
    }

    public RemoteIssueLink createLink(Issue issue, String branch) {
        RemoteIssueLinkBuilder builder = new RemoteIssueLinkBuilder();
        return builder.issueId(issue.getId())
                .iconUrl(resourceUrlProvider.getIconUrl())
                .iconTitle(i18n.getText(ICON_TITLE))
                .relationship(i18n.getText(RELATIONSHIP))
                .title(branch)
                .url(createBranchUrl(issue, branch))
                .build();
    }

    private String createBranchUrl(Issue issue, String branch) {
        String branchUrl = scmSettings.getRepositoryURL(issue.getProjectObject().getKey());
        if (!branchUrl.endsWith("/")) {
            branchUrl += "/";
        }
        return branchUrl + "branch/" + UrlEscapers.urlPathSegmentEscaper().escape(branch) + "?create=true";
    }

}
