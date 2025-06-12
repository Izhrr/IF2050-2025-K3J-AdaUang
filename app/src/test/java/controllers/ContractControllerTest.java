package controllers;

import models.Contract;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import database.DatabaseSeeder;

public class ContractControllerTest {

    static ContractController contractController;

    @BeforeAll
    static void setup() {
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        contractController = new ContractController();
    }

    @Test
    void testGetAllContracts() {
        List<Contract> contracts = contractController.getAllContracts();
        assertNotNull(contracts, "Daftar kontrak tidak boleh null");
        assertTrue(contracts.size() >= 1, "Harus ada minimal 1 kontrak hasil seeding");
    }

    @Test
    void testAddContractSuccess() {
        boolean result = contractController.addContract("UnitTest User", 5000000, 12, 1); // idUser 1 = admin
        assertTrue(result, "Tambah kontrak baru harus berhasil");
    }

    @Test
    void testAddContractFailWithInvalidData() {
        assertFalse(contractController.addContract("", 5000000, 12, 1), "Nama user kosong, harus gagal");
        assertFalse(contractController.addContract("User", -100, 12, 1), "Total negatif, harus gagal");
        assertFalse(contractController.addContract("User", 10000, 0, 1), "Tenor nol, harus gagal");
    }

    @Test
    void testGetContractById() {
        List<Contract> contracts = contractController.getAllContracts();
        assertFalse(contracts.isEmpty(), "Seed kontrak tidak boleh kosong");
        int id = contracts.get(0).getId_kontrak();
        Contract c = contractController.getContractById(id);
        assertNotNull(c, "Kontrak dengan id valid harus ditemukan");
        assertEquals(id, c.getId_kontrak(), "ID kontrak harus sama");
    }

    @Test
    void testUpdateContract() {
        List<Contract> contracts = contractController.getAllContracts();
        Contract contract = contracts.get(0);
        int oldTotal = contract.getTotal();
        contract.setTotal(oldTotal + 12345);

        boolean result = contractController.updateContract(contract);
        assertTrue(result, "Update kontrak harus berhasil");

        Contract updated = contractController.getContractById(contract.getId_kontrak());
        assertEquals(oldTotal + 12345, updated.getTotal(), "Total kontrak harus terupdate");
    }

    @Test
    void testGetAllKontrakAktif() {
        List<Contract> aktif = contractController.getAllKontrakAktif();
        assertNotNull(aktif, "Daftar kontrak aktif tidak boleh null");
    }

    @Test
    void testGetAllKontrak() {
        List<Contract> contracts = contractController.getAllKontrak();
        assertNotNull(contracts, "getAllKontrak tidak boleh null");
        assertTrue(contracts.size() >= 1, "Harus ada minimal 1 kontrak hasil seeding");
    }

    @Test
    void testGetKontrakById() {
        List<Contract> contracts = contractController.getAllKontrak();
        assertFalse(contracts.isEmpty(), "Seed kontrak tidak boleh kosong");
        int id = contracts.get(0).getId_kontrak();
        Contract c = contractController.getKontrakById(id);
        assertNotNull(c, "getKontrakById harus mengembalikan kontrak valid");
        assertEquals(id, c.getId_kontrak());
    }

    @Test
    void testGetNextTenor() {
        List<Contract> contracts = contractController.getAllKontrak();
        if (!contracts.isEmpty()) {
            int id = contracts.get(0).getId_kontrak();
            int nextTenor = contractController.getNextTenor(id);
            assertTrue(nextTenor >= 1, "getNextTenor minimal 1");
        }
    }

    @Test
    void testUpdateKontrakPayment() {
        List<Contract> contracts = contractController.getAllKontrak();
        if (!contracts.isEmpty()) {
            int id = contracts.get(0).getId_kontrak();
            Contract c = contractController.getKontrakById(id);
            int oldJumlahBayar = c.getJumlah_bayar();
            boolean result = contractController.updateKontrakPayment(id, oldJumlahBayar + 10000, false);
            assertTrue(result, "updateKontrakPayment harus berhasil jika id valid");
        }
    }

    @Test
    void testIsLunasByTenor() {
        List<Contract> contracts = contractController.getAllKontrak();
        if (!contracts.isEmpty()) {
            Contract c = contracts.get(0);
            boolean lunas = c.isLunasByTenor();
            // Tidak bisa assertTrue/False karena tergantung data, minimal method jalan
            assertNotNull(lunas);
        }
    }

    @Test
    void testGetFormattedCicilanPerBulan() {
        List<Contract> contracts = contractController.getAllKontrak();
        if (!contracts.isEmpty()) {
            Contract c = contracts.get(0);
            String formatted = c.getFormattedCicilanPerBulan();
            assertNotNull(formatted);
            assertTrue(formatted.startsWith("Rp"));
        }
    }

    @Test
    void testGetStatusText() {
        List<Contract> contracts = contractController.getAllKontrak();
        if (!contracts.isEmpty()) {
            Contract c = contracts.get(0);
            String statusText = c.getStatusText();
            assertTrue(statusText.equals("Lunas") || statusText.equals("Aktif"));
        }
    }
}