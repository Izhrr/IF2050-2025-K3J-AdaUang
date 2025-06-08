package services;

import models.Instalment;
import java.util.Date;
import java.util.List;

public class InstalmentService {
    public boolean createInstalment(int idKontrak, int jumlahBayar) {
        Instalment cicilan = new Instalment();
        cicilan.setId_kontrak(idKontrak);
        cicilan.setJumlah_membayar(jumlahBayar);
        cicilan.setTanggal_membayar(new Date());
        return cicilan.save();
    }

    public List<Instalment> getInstalmentsByContract(int idKontrak) {
        return Instalment.findByContractId(idKontrak);
    }

    public Instalment getInstalmentById(int idCicilan) {
        return Instalment.findById(idCicilan);
    }
}