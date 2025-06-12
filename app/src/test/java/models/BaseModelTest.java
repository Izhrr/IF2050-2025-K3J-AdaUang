package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

// Dummy subclass for testing BaseModel
class DummyModel extends BaseModel {
    @Override
    public boolean save() { return false; }
    @Override
    public boolean delete() { return false; }
}

public class BaseModelTest {

    @Test
    void testIdGetterSetter() {
        DummyModel model = new DummyModel();
        model.setId(42);
        assertEquals(42, model.getId());
    }

    @Test
    void testCreatedAtGetterSetter() {
        DummyModel model = new DummyModel();
        Timestamp now = Timestamp.valueOf("2025-06-10 18:15:00");
        model.setCreatedAt(now);
        assertEquals(now, model.getCreatedAt());
    }

    @Test
    void testUpdatedAtGetterSetter() {
        DummyModel model = new DummyModel();
        Timestamp now = Timestamp.valueOf("2025-06-10 18:16:00");
        model.setUpdatedAt(now);
        assertEquals(now, model.getUpdatedAt());
    }

    @Test
    void testIsNewRecord() {
        DummyModel model = new DummyModel();
        assertTrue(model.isNewRecord(), "Baru dibuat, id = 0, harus newRecord");
        model.setId(5);
        assertFalse(model.isNewRecord(), "Setelah id diisi, harus bukan newRecord");
    }
}