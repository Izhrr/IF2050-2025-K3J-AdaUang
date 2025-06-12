package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class CicilanTest {

    @Test
    void testGetters() {
        LocalDate now = LocalDate.now();
        Cicilan c = new Cicilan(9, 5, 2, 150000, now, 4);

        assertEquals(9, c.getIdCicilan());
        assertEquals(5, c.getIdKontrak());
        assertEquals(2, c.getTenor());
        assertEquals(150000, c.getJumlahCicilan());
        assertEquals(now, c.getTanggalCicilan());
        assertEquals(4, c.getIdStaff());
    }
}