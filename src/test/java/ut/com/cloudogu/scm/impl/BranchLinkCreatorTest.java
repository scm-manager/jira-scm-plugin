package ut.com.cloudogu.scm.impl;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.RemoteIssueLink;
import com.atlassian.jira.project.Project;
import com.cloudogu.scm.api.ScmSettings;
import com.cloudogu.scm.impl.BranchLinkCreator;
import com.cloudogu.scm.impl.ResourceUrlProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BranchLinkCreatorTest {

    @Mock
    private ResourceUrlProvider resourceUrlProvider;

    @Mock
    private ScmSettings scmSettings;

    @InjectMocks
    private BranchLinkCreator branchLinkCreator;

    @Mock
    private Issue issue;

    @Test
    public void shouldCreateRemoteLink() {
        prepareMocks("SCM", "https://scm.cloudogu.com/repo/ns/name", "/scmicon.png");

        RemoteIssueLink link = branchLinkCreator.createLink(issue, "feature/SCM-1_awesome_stuff");
        assertEquals("https://scm.cloudogu.com/repo/ns/name/branches/feature%2FSCM-1_awesome_stuff", link.getUrl());
        assertEquals("/scmicon.png", link.getIconUrl());
        assertEquals(BranchLinkCreator.ICON_TITLE, link.getIconTitle());
        assertEquals(BranchLinkCreator.RELATIONSHIP, link.getRelationship());
    }

    private Issue prepareMocks(String projectKey, String repositoryUrl, String iconUrl) {
        Project project = mock(Project.class);
        when(project.getKey()).thenReturn(projectKey);
        when(issue.getProjectObject()).thenReturn(project);
        when(scmSettings.getRepositoryURL(projectKey)).thenReturn(repositoryUrl);
        when(resourceUrlProvider.getIconUrl()).thenReturn(iconUrl);
        return issue;
    }

}
