package com.cloudogu.scm.impl;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.link.RemoteIssueLinkService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.RemoteIssueLink;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import javax.inject.Inject;

@Scanned
@SuppressWarnings("squid:S1948") // serializable
public class ConnectBranchAction extends JiraWebActionSupport {

    @ComponentImport
    private final IssueService issueService;

    @ComponentImport
    private final RemoteIssueLinkService remoteIssueLinkService;

    @ComponentImport
    private final JiraAuthenticationContext authenticationContext;

    private final BranchLinkCreator branchLinkCreator;

    private Long id;
    private String branch;

    @Inject
    public ConnectBranchAction(IssueService issueService, RemoteIssueLinkService remoteIssueLinkService, JiraAuthenticationContext authenticationContext, BranchLinkCreator branchLinkCreator) {
        this.issueService = issueService;
        this.remoteIssueLinkService = remoteIssueLinkService;
        this.authenticationContext = authenticationContext;
        this.branchLinkCreator = branchLinkCreator;
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
        RemoteIssueLink link = branchLinkCreator.createLink(getIssueObject(), branch);

        RemoteIssueLinkService.CreateValidationResult result = remoteIssueLinkService.validateCreate(authenticationContext.getLoggedInUser(), link);
        if (!result.isValid()) {
            this.addErrorCollection(result.getErrorCollection());
            return ERROR;
        }

        remoteIssueLinkService.create(authenticationContext.getLoggedInUser(), result);
        return returnCompleteWithInlineRedirect("/browse/" + getIssueObject().getKey());
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
