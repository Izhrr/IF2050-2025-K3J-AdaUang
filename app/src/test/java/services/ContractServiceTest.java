package services;

import models.Contract;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import database.DatabaseSeeder;

import java.util.List;

public class ContractServiceTest {

    static ContractService contractService;

    @BeforeAll
    static void setup() {
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        contractService = new ContractService();
    }

    @Test
    void testGetAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        assertNotNull(contracts, "Contracts tidak boleh null");
        assertTrue(contracts.size() > 0, "Harus ada minimal satu contract di DB");
    }

    @Test
    void testCreateContractSuccess() {
        boolean result = contractService.createContract("Test User", 5000000, 5, 1);
        assertTrue(result, "Create contract harus berhasil");
        // Cek bahwa kontrak terbaru bisa ditemukan
        List<Contract> contracts = contractService.getAllContracts();
        assertTrue(
            contracts.stream().anyMatch(c -> "Test User".equals(c.getNama_user())),
            "Harus ditemukan contract dengan nama user 'Test User'"
        );
    }

    @Test
    void testCreateContractFailInvalidInput() {
        // total <= 0
        assertFalse(contractService.createContract("Test User", 0, 5, 1));
        // tenor <= 0
        assertFalse(contractService.createContract("Test User", 5000000, 0, 1));
        // namaUser kosong
        assertFalse(contractService.createContract("", 5000000, 5, 1));
    }

    @Test
    void testGetContractById() {
        // Asumsi ada contract id=1 dari seeder
        Contract c = contractService.getContractById(1);
        assertNotNull(c, "Contract id=1 harus ditemukan");
        assertEquals(1, c.getId_kontrak());
    }

    @Test
    void testUpdateContract() {
        // Ambil salah satu kontrak dulu
        List<Contract> contracts = contractService.getAllContracts();
        assertFalse(contracts.isEmpty());
        Contract c = contracts.get(0);
        int oldTotal = c.getTotal();
        c.setTotal(oldTotal + 12345);
        boolean updated = contractService.updateContract(c);
        assertTrue(updated, "Update contract harus berhasil");

        // Ambil ulang dan cek perubahan
        Contract c2 = contractService.getContractById(c.getId_kontrak());
        assertNotNull(c2);
        assertEquals(oldTotal + 12345, c2.getTotal());
    }
}