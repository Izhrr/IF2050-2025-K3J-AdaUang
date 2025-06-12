package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class InstalmentTest {

    @Test
    void testGetterSetter() {
        Instalment ins = new Instalment();
        ins.setIdCicilan(8);
        ins.setIdKontrak(3);
        ins.setJumlahCicilan(123000);
        LocalDate now = LocalDate.now();
        ins.setTanggalCicilan(now);
        ins.setTenor(2);
        ins.setIdStaff(7);
        ins.setNamaPeminjam("Budi");
        ins.setStaffName("Admin");

        assertEquals(8, ins.getIdCicilan());
        assertEquals(3, ins.getIdKontrak());
        assertEquals(123000, ins.getJumlahCicilan());
        assertEquals(now, ins.getTanggalCicilan());
        assertEquals(2, ins.getTenor());
        assertEquals(7, ins.getIdStaff());
        assertEquals("Budi", ins.getNamaPeminjam());
        assertEquals("Admin", ins.getStaffName());
    }

    @Test
    void testIsNewRecord() {
        Instalment ins = new Instalment();
        assertTrue(ins.isNewRecord());
        ins.setIdCicilan(10);
        assertFalse(ins.isNewRecord());
    }

    @Test
    void testGetFormattedAmountAndTenorDisplay() {
        Instalment ins = new Instalment();
        ins.setJumlahCicilan(120000);
        ins.setTenor(5);
        assertTrue(ins.getFormattedAmount().contains("Rp"));
        assertEquals("Tenor ke-5", ins.getTenorDisplay());
    }

    @Test
    void testValidatePaymentDateNoPrevious() {
        Instalment ins = new Instalment();
        // No previous payment should return true
        assertTrue(ins.validatePaymentDate(99999)); // idKontrak dummy, must not throw
    }
}