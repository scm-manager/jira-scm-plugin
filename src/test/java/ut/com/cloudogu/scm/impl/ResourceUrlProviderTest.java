package ut.com.cloudogu.scm.impl;

import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.UrlMode;
import com.cloudogu.scm.impl.ResourceUrlProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceUrlProviderTest {

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private ResourceUrlProvider resourceUrlProvider;

    @Test
    public void shouldReturnAbsoluteUrlForIcon() {
        when(applicationProperties.getBaseUrl(UrlMode.ABSOLUTE)).thenReturn("https://jira.cloudogu.com");

        String iconUrl = resourceUrlProvider.getIconUrl();
        assertEquals(
            "https://jira.cloudogu.com/download/resources/com.cloudogu.scm.jira-scm-plugin:jira-scm-plugin-resources/images/icon.png",
            iconUrl
        );
    }

}
