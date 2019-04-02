package com.cloudogu.scm.impl;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * This class exists only to import the required dependencies for the conditions used in the atlassian-plugin.xml.
 *
 * @see <a href="https://community.developer.atlassian.com/t/solved-action-condition-unsatisfied-dependency/20924/10">Atlassion community</a>
 */
@Scanned
@Component
public class ConditionComponentImporter {

    @ComponentImport
    private final IssueManager issueManager;

    @ComponentImport
    private final PermissionManager permissionManager;

    @Inject
    public ConditionComponentImporter(IssueManager issueManager, PermissionManager permissionManager) {
        this.issueManager = issueManager;
        this.permissionManager = permissionManager;
    }
}
