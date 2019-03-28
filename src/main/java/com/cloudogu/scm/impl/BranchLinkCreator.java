package com.cloudogu.scm.impl;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.RemoteIssueLink;
import com.atlassian.jira.issue.link.RemoteIssueLinkBuilder;
import com.cloudogu.scm.api.ScmSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.net.UrlEscapers;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class BranchLinkCreator {

    @VisibleForTesting
    public static final String ICON_TITLE = "SCM-Manager";

    @VisibleForTesting
    public static final String RELATIONSHIP = "related branches";

    private final ResourceUrlProvider resourceUrlProvider;
    private final ScmSettings scmSettings;

    @Inject
    public BranchLinkCreator(ResourceUrlProvider resourceUrlProvider, ScmSettings scmSettings) {
        this.resourceUrlProvider = resourceUrlProvider;
        this.scmSettings = scmSettings;
    }

    public RemoteIssueLink createLink(Issue issue, String branch) {
        RemoteIssueLinkBuilder builder = new RemoteIssueLinkBuilder();
        return builder.issueId(issue.getId())
                .iconUrl(resourceUrlProvider.getIconUrl())
                .iconTitle(ICON_TITLE)
                .relationship(RELATIONSHIP)
                .title(branch)
                .url(createBranchUrl(issue, branch))
                .build();
    }

    private String createBranchUrl(Issue issue, String branch) {
        String branchUrl = scmSettings.getRepositoryURL(issue.getProjectObject().getKey());
        if (!branchUrl.endsWith("/")) {
            branchUrl += "/";
        }
        return branchUrl + "branches/" + UrlEscapers.urlPathSegmentEscaper().escape(branch);
    }

}
