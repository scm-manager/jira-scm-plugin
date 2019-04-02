package ut.com.cloudogu.scm.impl;

import com.atlassian.jira.project.Project;
import com.cloudogu.scm.api.ScmSettings;
import com.cloudogu.scm.impl.IsConfiguredCondition;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IsConfiguredConditionTest {

    @Mock
    private Project project;

    @Mock
    private ScmSettings settings;

    @InjectMocks
    private IsConfiguredCondition condition;

    @Test
    public void shouldDisplayIfConfigured() {
        when(project.getKey()).thenReturn("SCM");
        when(settings.getRepositoryURL("SCM")).thenReturn("https://scm.cloudogu.com/scm/repo/ns/name");

        Map<String, Object> parameters = ImmutableMap.of("project", project);
        assertTrue(condition.shouldDisplay(parameters));
    }

    @Test
    public void shouldNotDisplayIfNotConfigured() {
        when(project.getKey()).thenReturn("SCM");

        Map<String, Object> parameters = ImmutableMap.of("project", project);
        assertFalse(condition.shouldDisplay(parameters));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfProjectIsMissing() {
        Map<String, Object> parameters = ImmutableMap.of();
        condition.shouldDisplay(parameters);
    }

}