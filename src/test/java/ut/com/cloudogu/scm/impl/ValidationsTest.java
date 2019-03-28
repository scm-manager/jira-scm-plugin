package ut.com.cloudogu.scm.impl;

import com.cloudogu.scm.impl.Validations;
import org.junit.Test;

import static org.junit.Assert.*;


public class ValidationsTest {

    @Test
    public void shouldReturnTrueForValidUrls() {
        assertTrue(Validations.isValidURL("https://scm.cloudogu.com/repo/scm/test"));
    }

    @Test
    public void shouldReturnFalseForInvalidUrls() {
        assertFalse(Validations.isValidURL("abc"));
        assertFalse(Validations.isValidURL(""));
        assertFalse(Validations.isValidURL(null));
    }
}
