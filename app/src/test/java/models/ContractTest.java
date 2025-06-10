package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class ContractTest {

    @Test
    void testGettersSetters() {
        Contract contract = new Contract();
        contract.setId_kontrak(7);
        contract.setNama_user("Budi");
        contract.setTotal(1000000);
        contract.setTenor(10);
        contract.setJumlah_bayar(10000);
        contract.setJumlah_bayar_bunga(1100000);
        contract.setCicilan_per_bulan(110000);
        contract.setStatus(true);
        Date now = new Date();
        contract.setTanggal_pinjam(now);
        contract.setId_user(2);
        contract.setUsername("budiuser");
        contract.setBranch("Bandung");

        assertEquals(7, contract.getId_kontrak());
        assertEquals("Budi", contract.getNama_user());
        assertEquals(1000000, contract.getTotal());
        assertEquals(10, contract.getTenor());
        assertEquals(10000, contract.getJumlah_bayar());
        assertEquals(1100000, contract.getJumlah_bayar_bunga());
        assertEquals(110000, contract.getCicilan_per_bulan());
        assertTrue(contract.isStatus());
        assertEquals(now, contract.getTanggal_pinjam());
        assertEquals(2, contract.getId_user());
        assertEquals("budiuser", contract.getUsername());
        assertEquals("Bandung", contract.getBranch());
        assertEquals(7, contract.getId());
    }

    @Test
    void testIsNewRecord() {
        Contract c = new Contract();
        assertTrue(c.isNewRecord());
        c.setId_kontrak(17);
        assertFalse(c.isNewRecord());
    }
}