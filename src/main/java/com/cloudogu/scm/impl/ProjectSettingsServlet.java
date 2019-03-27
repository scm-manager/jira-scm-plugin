package com.cloudogu.scm.impl;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
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
    private final PluginSettingsFactory settingsFactory;

    @Inject
    public ProjectSettingsServlet(TemplateRenderer renderer, PluginSettingsFactory settingsFactory) {
        this.renderer = renderer;
        this.settingsFactory = settingsFactory;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        renderSettings(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String project = request.getParameter("project");
        String repository = request.getParameter("repository");


        PluginSettings settings = settingsFactory.createSettingsForKey(project);
        settings.put("repository", repository);

        renderSettings(request, response);
    }

    private Map<String,Object> createModel(String project, String repository) {
        return ImmutableMap.of("project", project, "repository", Strings.nullToEmpty(repository));
    }

    private void renderSettings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        String project = request.getParameter("project");

        PluginSettings settings = settingsFactory.createSettingsForKey(project);

        String repository = (String) settings.get("repository");

        Map<String, Object> model = createModel(project, repository);

        renderer.render("settings.vm", model, response.getWriter());
    }
}
