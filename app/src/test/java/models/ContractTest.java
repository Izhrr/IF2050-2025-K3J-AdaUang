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
        // Jangan set jumlah_bayar_bunga & cicilan_per_bulan manual!
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

        // Perhitungan otomatis dari setter:
        assertEquals(1100000, contract.getJumlah_bayar_bunga());
        assertEquals(110000, contract.getCicilan_per_bulan());  

        assertTrue(contract.isStatus());
        assertEquals(now, contract.getTanggal_pinjam());
        assertEquals(2, contract.getId_user());
        assertEquals("budiuser", contract.getUsername());
        assertEquals("Bandung", contract.getBranch());
    }

    @Test
    void testIsNewRecord() {
        Contract c = new Contract();
        assertTrue(c.isNewRecord());
        c.setId_kontrak(17);
        assertFalse(c.isNewRecord());
    }

    @Test
    void testGetNamaPeminjamAndGetIdKontrak() {
        Contract contract = new Contract();
        contract.setNama_user("Siti");
        contract.setId_kontrak(20);
        assertEquals("Siti", contract.getNamaPeminjam());
        assertEquals(20, contract.getIdKontrak());
    }

    @Test
    void testGetFormattedCicilanPerBulan() {
        Contract contract = new Contract();
        contract.setCicilan_per_bulan(150000);
        String formatted = contract.getFormattedCicilanPerBulan();
        assertTrue(formatted.contains("Rp"));
    }

    @Test
    void testGetStatusText() {
        Contract contract = new Contract();
        contract.setStatus(false);
        assertEquals("Aktif", contract.getStatusText());
        contract.setStatus(true);
        assertEquals("Lunas", contract.getStatusText());
    }

    @Test
    void testGetFormattedTotal() {
        Contract contract = new Contract();
        contract.setTotal(1000000);
        String formatted = contract.getFormattedTotal();
        assertTrue(formatted.contains("Rp"));
    }
}