package com.cloudogu.scm.impl;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.link.RemoteIssueLinkService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.RemoteIssueLink;
import com.atlassian.jira.issue.link.RemoteIssueLinkBuilder;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.UrlMode;
import com.cloudogu.scm.api.ScmSettings;
import com.google.common.net.UrlEscapers;

import javax.inject.Inject;

@Scanned
public class ConnectBranchAction extends JiraWebActionSupport {

    @ComponentImport
    private final IssueService issueService;

    @ComponentImport
    private final RemoteIssueLinkService remoteIssueLinkService;

    @ComponentImport
    private final JiraAuthenticationContext authenticationContext;

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    private final ScmSettings scmSettings;

    private Long id;
    private String branch;

    @Inject
    public ConnectBranchAction(IssueService issueService, RemoteIssueLinkService remoteIssueLinkService, JiraAuthenticationContext authenticationContext, ApplicationProperties applicationProperties, ScmSettings scmSettings) {
        this.issueService = issueService;
        this.remoteIssueLinkService = remoteIssueLinkService;
        this.authenticationContext = authenticationContext;
        this.applicationProperties = applicationProperties;
        this.scmSettings = scmSettings;
    }

    @Override
    public String doDefault() {
        Issue issue = getIssueObject();
        if (issue != null) {
            branch = Branches.createName(issue);
        }

        return INPUT;
    }

    @Override
    protected String doExecute() {
        RemoteIssueLink link = createLink(getIssueObject(), branch);

        RemoteIssueLinkService.CreateValidationResult result = remoteIssueLinkService.validateCreate(authenticationContext.getLoggedInUser(), link);
        if (!result.isValid()) {
            this.addErrorCollection(result.getErrorCollection());
            return ERROR;
        }

        remoteIssueLinkService.create(authenticationContext.getLoggedInUser(), result);
        return returnCompleteWithInlineRedirect("/browse/" + getIssueObject().getKey());
    }


    private RemoteIssueLink createLink(Issue issue, String branch) {
        RemoteIssueLinkBuilder builder = new RemoteIssueLinkBuilder();
        return builder.issueId(issue.getId())
                .iconTitle("SCM-Manager")
                .iconUrl(createIconUrl())
                .title(branch)
                .relationship("related branches")
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

    private static final String GROUP_ID = "com.cloudogu.scm";
    private static final String ARTIFACT_ID = "jira-scm-plugin";
    private static final String ADDON_KEY = GROUP_ID + "." + ARTIFACT_ID;
    private static final String RESOURCE_KEY = "jira-scm-plugin-resources";

    private String createIconUrl() {
        return String.format("%s/download/resources/%s:%s/images/icon.png",
                applicationProperties.getBaseUrl(UrlMode.ABSOLUTE),
                ADDON_KEY,
                RESOURCE_KEY
        );
    }


    public Issue getIssueObject()
    {
        final IssueService.IssueResult issueResult = issueService.getIssue(authenticationContext.getLoggedInUser(), id);
        if (!issueResult.isValid())
        {
            this.addErrorCollection(issueResult.getErrorCollection());
            return null;
        }

        return issueResult.getIssue();
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
