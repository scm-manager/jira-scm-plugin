package ut.com.cloudogu.scm.impl;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.cloudogu.scm.api.ScmSettings;
import com.cloudogu.scm.impl.ScmSettingsImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ScmSettingsImplTest {

    private ScmSettings settings;

    @Before
    public void setUpScmSettings() {
        settings = new ScmSettingsImpl(new MockPluginSettingsFactory());
    }

    @Test
    public void shouldSetAndGetRepository() {
        settings.setRepositoryURL("SCM", "https://scm.cloudogu.com/repo/ns/name");
        String repositoryURL = settings.getRepositoryURL("SCM");
        assertEquals("https://scm.cloudogu.com/repo/ns/name", repositoryURL);
    }

    @Test
    public void shouldReturnNullForNonExistingProjects() {
        String repositoryURL = settings.getRepositoryURL("TST");
        assertNull(repositoryURL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptInvalidRepositoryURL() {
        settings.setRepositoryURL("SCM", "abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptInvalidProjectKey() {
        settings.setRepositoryURL("", "https://scm.cloudogu.com/repo/ns/name");
    }

    private static class MockPluginSettingsFactory implements PluginSettingsFactory {

        private Map<String,PluginSettings> pluginSettingsMap = new HashMap<>();

        @Override
        public PluginSettings createSettingsForKey(String key) {
            PluginSettings pluginSettings = this.pluginSettingsMap.get(key);
            if (pluginSettings == null) {
                pluginSettings = new MockPluginSettings();
                pluginSettingsMap.put(key, pluginSettings);
            }
            return pluginSettings;
        }

        @Override
        public PluginSettings createGlobalSettings() {
            return createSettingsForKey("_global");
        }
    }

    private static class MockPluginSettings implements PluginSettings {

        private Map<String,Object> settings = new HashMap<>();

        @Override
        public Object get(String key) {
            return settings.get(key);
        }

        @Override
        public Object put(String key, Object value) {
            return settings.put(key, value);
        }

        @Override
        public Object remove(String key) {
            return settings.remove(key);
        }
    }
}
