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
        assertTrue(contracts.size() >= 3, "Harus ada minimal 3 kontrak hasil seeding");
    }

    @Test
    void testAddContractSuccess() {
        boolean result = contractController.addContract("UnitTest User", 5000000, 12, 1); // idUser 1 = admin
        assertTrue(result, "Tambah kontrak baru harus berhasil");
        // Optionally: cek kontrak baru muncul di getAllContracts()
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
        int id = contracts.get(0).getId();
        Contract c = contractController.getContractById(id);
        assertNotNull(c, "Kontrak dengan id valid harus ditemukan");
        assertEquals(id, c.getId(), "ID kontrak harus sama");
    }

    @Test
    void testUpdateContract() {
        List<Contract> contracts = contractController.getAllContracts();
        Contract contract = contracts.get(0);
        int oldTotal = contract.getTotal();
        contract.setTotal(oldTotal + 12345);

        boolean result = contractController.updateContract(contract);
        assertTrue(result, "Update kontrak harus berhasil");

        Contract updated = contractController.getContractById(contract.getId());
        assertEquals(oldTotal + 12345, updated.getTotal(), "Total kontrak harus terupdate");
    }
}