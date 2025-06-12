package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class InstalmentTest {

    @Test
    void testGetterSetter() {
        Instalment ins = new Instalment();
        ins.setid_cicilan(8);
        ins.setid_kontrak(3);
        ins.setjumlah_cicilan(123000);
        Date now = new Date();
        ins.settanggal_cicilan(now);

        assertEquals(8, ins.getid_cicilan());
        assertEquals(3, ins.getid_kontrak());
        assertEquals(123000, ins.getjumlah_cicilan());
        assertEquals(now, ins.gettanggal_cicilan());
    }

    @Test
    void testIsNewRecord() {
        Instalment ins = new Instalment();
        assertTrue(ins.isNewRecord());
        ins.setid_cicilan(10);
        assertFalse(ins.isNewRecord());
    }
}