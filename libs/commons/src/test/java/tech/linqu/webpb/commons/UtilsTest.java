package tech.linqu.webpb.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.AccessDeniedException;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void shouldCallUncheckedSuccess() {
        assertTrue(Utils.uncheckedCall(() -> true));
    }

    @Test
    void shouldUncheckedCallThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> Utils.uncheckedCall(() -> {
            throw new RuntimeException();
        }));
    }

    @Test
    void shouldUncheckedCallAlwaysThrowRuntimeException() {
        assertThrows(RuntimeException.class, () -> Utils.uncheckedCall(() -> {
            throw new AccessDeniedException("Denied");
        }));
    }

    @Test
    void shouldEmptyOrDefaultReturnValueGivenValueIsNotEmpty() {
        assertEquals("hello", Utils.emptyOrDefault("hello", ""));
    }

    @Test
    void shouldEmptyOrDefaultReturnDefaultGivenValueIsNull() {
        assertEquals("abc", Utils.emptyOrDefault(null, "abc"));
    }

    @Test
    void shouldEmptyOrDefaultReturnDefaultGivenValueIsEmpty() {
        assertEquals("abc", Utils.emptyOrDefault("", "abc"));
    }
}
