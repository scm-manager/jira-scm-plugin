package com.cloudogu.scm.impl;

import com.atlassian.jira.project.Project;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.web.Condition;
import com.cloudogu.scm.api.ScmSettings;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.inject.Inject;
import java.util.Map;

@Scanned
public class IsConfiguredCondition implements Condition {

    private final ScmSettings scmSettings;

    @Inject
    public IsConfiguredCondition(ScmSettings scmSettings) {
        this.scmSettings = scmSettings;
    }

    @Override
    public void init(Map<String, String> map) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> map) {
        Project project = (Project) map.get("project");
        Preconditions.checkState(project != null, "project parameter is required");

        String url = scmSettings.getRepositoryURL(project.getKey());
        return !Strings.isNullOrEmpty(url);
    }
}
