package controllers;

import models.Instalment;
import services.InstalmentService;
import java.util.List;

public class InstalmentController extends BaseController {
    private final InstalmentService instalmentService;

    public InstalmentController() {
        this.instalmentService = new InstalmentService();
    }

    public boolean tambahCicilan(int idKontrak, int jumlahBayar) {
        return instalmentService.createInstalment(idKontrak, jumlahBayar);
    }

    public List<Instalment> getCicilanByKontrak(int idKontrak) {
        return instalmentService.getInstalmentsByContract(idKontrak);
    }

    public Instalment getCicilanById(int idCicilan) {
        return instalmentService.getInstalmentById(idCicilan);
    }
}
