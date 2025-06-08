package controllers;

import models.Instalment;
import services.InstalmentService;
import java.util.List;

public class InstalmentController extends BaseController {

    private final InstalmentService instalmentService;

    public InstalmentController() {
        this.instalmentService = new InstalmentService();
    }
    
    public List<Instalment> getAllInstalments() {
        return instalmentService.getAllInstalments();
    }

    public boolean addInstalment(int id_cicilan, int id_kontrak, int jumlah_cicilan) {
        if (id_cicilan <= 0 || id_kontrak <= 0 || jumlah_cicilan <= 0) {
            return false;
        }
        return instalmentService.createInstalment(id_cicilan, id_kontrak, jumlah_cicilan);
    }

    public Instalment getInstalmentById(int id) {
        return instalmentService.getInstalmentById(id);
    }
}