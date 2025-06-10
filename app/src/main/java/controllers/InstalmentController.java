package controllers;

import java.time.LocalDate;
import java.util.List;

import services.InstalmentService;
import models.Cicilan; 
public class InstalmentController {
    private final InstalmentService instalmentService;

    public InstalmentController() {
        this.instalmentService = new InstalmentService();
    }

    public boolean tambahCicilan(int idKontrak, int jumlah, int tenor, LocalDate tanggal, int idStaff) {
        return instalmentService.addCicilan(idKontrak, jumlah, tenor, tanggal, idStaff);
    }

    public List<Cicilan> getAllCicilan() {
        return instalmentService.getAllCicilan();
    }
}
