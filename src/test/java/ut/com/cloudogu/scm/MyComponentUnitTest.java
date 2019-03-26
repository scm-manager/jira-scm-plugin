package ut.com.cloudogu.scm;

import org.junit.Test;
import com.cloudogu.scm.api.MyPluginComponent;
import com.cloudogu.scm.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}