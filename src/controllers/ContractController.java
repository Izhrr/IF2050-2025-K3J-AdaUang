package controllers;

import models.Contract;
import services.ContractService;
import java.util.List;

public class ContractController extends BaseController {
    
    private final ContractService contractService;

    public ContractController() {
        this.contractService = new ContractService();
    }
    
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    public boolean addContract(String namaUser, int total, int tenor, int idUser) {
        if (namaUser == null || namaUser.trim().isEmpty() || total <= 0 || tenor <= 0) {
            return false;
        }
        return contractService.createContract(namaUser, total, tenor, idUser);
    }

    public Contract getContractById(int id) {
        return contractService.getContractById(id);
    }
}