package models;

import java.time.LocalDate;

public class Cicilan {
    private int idCicilan;
    private int idKontrak;
    private int tenor;
    private int jumlahCicilan;
    private LocalDate tanggalCicilan;
    private int idStaff;

    public Cicilan(int idCicilan, int idKontrak, int tenor, int jumlahCicilan, LocalDate tanggalCicilan, int idStaff) {
        this.idCicilan = idCicilan;
        this.idKontrak = idKontrak;
        this.tenor = tenor;
        this.jumlahCicilan = jumlahCicilan;
        this.tanggalCicilan = tanggalCicilan;
        this.idStaff = idStaff;
    }

    // Getters
    public int getIdCicilan() { return idCicilan; }
    public int getIdKontrak() { return idKontrak; }
    public int getTenor() { return tenor; }
    public int getJumlahCicilan() { return jumlahCicilan; }
    public LocalDate getTanggalCicilan() { return tanggalCicilan; }
    public int getIdStaff() { return idStaff; }
}