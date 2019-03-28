package ut.com.cloudogu.scm.impl;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.cloudogu.scm.impl.Branches;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BranchesTest {

    @Mock
    private Issue issue;

    @Test
    public void shouldCreateBranchName() {
        prepareMock("Task", "SCM-1", "This is your first task");

        assertEquals("feature/SCM-1_this_is_your_first_task", Branches.createName(issue));
    }

    @Test
    public void shouldDropAllSpecialCharacters() {
        prepareMock("Task", "SCM-1", "\"This- .is .your! ?first/ \\\\task\"");

        assertEquals("feature/SCM-1_this_is_your_first_task", Branches.createName(issue));
    }

    @Test
    public void shouldCutLongSummaries() {
        prepareMock("Task", "SCM-1", "This is your first task and this is totally awesome");

        assertEquals("feature/SCM-1_this_is_your_first_task_", Branches.createName(issue));
    }

    @Test
    public void shouldReturnBugfixPrefixForIssuesOfTypeBug() {
        prepareMock("Bug", "SCM-42", "Awesome Bug");

        assertEquals("bugfix/SCM-42_awesome_bug", Branches.createName(issue));
    }

    private void prepareMock(String type, String key, String summary) {
        IssueType issueType = mock(IssueType.class);
        when(issueType.getName()).thenReturn(type);
        when(issue.getIssueType()).thenReturn(issueType);

        when(issue.getKey()).thenReturn(key);
        when(issue.getSummary()).thenReturn(summary);
    }
}
