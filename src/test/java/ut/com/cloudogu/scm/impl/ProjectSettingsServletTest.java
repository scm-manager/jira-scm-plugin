package ut.com.cloudogu.scm.impl;

import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.cloudogu.scm.api.ScmSettings;
import com.cloudogu.scm.impl.ProjectSettingsServlet;
import com.google.common.base.Strings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectSettingsServletTest {

    @Mock
    private TemplateRenderer renderer;

    @Mock
    private I18nResolver i18n;

    @Mock
    private ScmSettings scmSettings;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ProjectSettingsServlet servlet;

    @Test
    public void shouldStoreTheRepositoryURL() throws IOException, ServletException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("project")).thenReturn("SCM");
        when(request.getParameter("repository")).thenReturn("https://scm.cloudogu.com/repo/ns/name");

        servlet.service(request, response);

        verify(scmSettings).setRepositoryURL("SCM", "https://scm.cloudogu.com/repo/ns/name");

        assertRendered("SCM", "https://scm.cloudogu.com/repo/ns/name");
    }

    @Test
    public void shouldRenderEmptyRepositoryURL() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("project")).thenReturn("SCM");

        servlet.service(request, response);

        assertRendered("SCM", "");
    }

    @Test
    public void shouldRenderStoredRepositoryURL() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("project")).thenReturn("SCM");
        when(scmSettings.getRepositoryURL("SCM")).thenReturn("https://scm.cloudogu.com/repo/ns/name");

        servlet.service(request, response);

        assertRendered("SCM", "https://scm.cloudogu.com/repo/ns/name");
    }

    @Test
    public void shouldRenderErrorForInvalidRepositoryURL() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("project")).thenReturn("TST");
        when(request.getParameter("repository")).thenReturn("abc");
        when(i18n.getText("project.settings.error.invalid-repository-url")).thenReturn("invalid repository url");

        servlet.service(request, response);

        assertRendered("TST", "abc", "invalid repository url");
    }

    private void assertRendered(String project, String repositoryURL) throws IOException {
        assertRendered(project, repositoryURL, null);
    }

    @SuppressWarnings("unchecked")
    private void assertRendered(String project, String repositoryURL, String error) throws IOException {
        verify(response).setContentType("text/html;charset=utf-8");

        ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map<String,Object>> modelCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Writer> writerCaptor = ArgumentCaptor.forClass(Writer.class);

        verify(renderer).render(templateCaptor.capture(), modelCaptor.capture(), writerCaptor.capture());

        Map<String, Object> model = modelCaptor.getValue();
        assertEquals(project, model.get("project"));
        assertEquals(repositoryURL, model.get("repository"));
        assertEquals(Strings.nullToEmpty(error), model.get("error"));

        assertEquals("templates/settings.vm", templateCaptor.getValue());
    }
}
