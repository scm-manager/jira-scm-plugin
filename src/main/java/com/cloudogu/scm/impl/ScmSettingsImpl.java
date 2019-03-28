package com.cloudogu.scm.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.cloudogu.scm.api.ScmSettings;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({ScmSettings.class})
@Named("scmSettings")
public class ScmSettingsImpl implements ScmSettings {

    private static final String KEY_REPOSITORY_URL = "repository-url";

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public ScmSettingsImpl(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public String getRepositoryURL(String project) {
        PluginSettings settings = pluginSettingsFactory.createSettingsForKey(project);
        return (String) settings.get(KEY_REPOSITORY_URL);
    }

    @Override
    public void setRepositoryURL(String project, String repositoryURL) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(project), "invalid project key");
        Preconditions.checkArgument(Validations.isValidURL(repositoryURL), "invalid project key");

        PluginSettings settings = pluginSettingsFactory.createSettingsForKey(project);
        settings.put(KEY_REPOSITORY_URL, repositoryURL);
    }
}
