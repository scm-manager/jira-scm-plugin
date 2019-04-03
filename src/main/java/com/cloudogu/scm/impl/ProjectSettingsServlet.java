package com.cloudogu.scm.impl;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.cloudogu.scm.api.ScmSettings;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Scanned
public class ProjectSettingsServlet extends HttpServlet {

    @ComponentImport
    private final TemplateRenderer renderer;

    @ComponentImport
    private final I18nResolver i18n;

    private final ScmSettings scmSettings;

    @Inject
    public ProjectSettingsServlet(TemplateRenderer renderer, I18nResolver i18n, ScmSettings scmSettings) {
        this.renderer = renderer;
        this.i18n = i18n;
        this.scmSettings = scmSettings;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String project = request.getParameter("project");
        String repository = scmSettings.getRepositoryURL(project);

        renderSettings(response, project, repository, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String project = request.getParameter("project");
        String repository = request.getParameter("repository");
        String error = null;

        if (Validations.isValidURL(repository)) {
            scmSettings.setRepositoryURL(project, repository);
        } else {
            error = i18n.getText("project.settings.error.invalid-repository-url");
        }


        renderSettings(response, project, repository, error);
    }

    private void renderSettings(HttpServletResponse response, String project, String repository, String error) throws IOException {
        Map<String, Object> model = createModel(project, repository, error);

        response.setContentType("text/html;charset=utf-8");
        renderer.render("templates/settings.vm", model, response.getWriter());
    }

    private Map<String,Object> createModel(String project, String repository, String error) {
        return ImmutableMap.of(
                "project", project,
                "repository", Strings.nullToEmpty(repository),
                "error", Strings.nullToEmpty(error)
        );
    }

}
