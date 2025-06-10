package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUsernameGetterSetter() {
        User user = new User();
        user.setUsername("budi");
        assertEquals("budi", user.getUsername());
    }

    @Test
    void testRoleConstants() {
        assertEquals("admin", User.ROLE_ADMIN);
        assertEquals("staff", User.ROLE_STAFF);
        assertEquals("manager", User.ROLE_MANAGER);
    }

    @Test
    void testIsValid() {
        User user = new User("admin", "Admin", "admin123", "Bandung", User.ROLE_ADMIN);
        assertTrue(user.isValid());
        user.setUsername("a"); // too short
        assertFalse(user.isValid());
    }

    @Test
    void testRoleChecker() {
        User admin = new User("admin", "Admin", "pw", "Bandung", User.ROLE_ADMIN);
        User staff = new User("staff", "Staff", "pw", "Bandung", User.ROLE_STAFF);
        User manager = new User("manager", "Manager", "pw", "Bandung", User.ROLE_MANAGER);
        assertTrue(admin.isAdmin());
        assertFalse(staff.isAdmin());
        assertTrue(manager.isManager());
        assertFalse(staff.isManager());
        assertTrue(staff.isStaff());
    }

    @Test
    void testToStringDisplayName() {
        User user = new User("alice", "Alice Wonderland", "pw", "Jakarta", User.ROLE_STAFF);
        assertTrue(user.toString().contains("alice"));
        assertEquals("Alice Wonderland", user.getDisplayName());
        user.setFullname("");
        assertEquals("alice", user.getDisplayName());
    }
}