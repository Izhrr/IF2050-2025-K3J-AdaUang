package controllers;

import java.time.LocalDate;
import services.InstalmentService;

public class InstalmentController {
    private final InstalmentService instalmentService;

    public InstalmentController() {
        this.instalmentService = new InstalmentService();
    }

    public boolean tambahCicilan(int idKontrak, int jumlah, LocalDate tanggal, int idStaff) {
        return instalmentService.addCicilan(idKontrak, jumlah, tanggal, idStaff);
    }
}
