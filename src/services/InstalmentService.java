package services;

import models.Instalment;
import java.util.List;

public class InstalmentService {
    
    public List<Instalment> getAllInstalments() {
        return Instalment.findAllWithContractDetails();
    }

    public boolean createInstalment(int id_cicilan, int id_kontrak, int jumlah_cicilan) {
        Instalment instalment = new Instalment();
        instalment.setid_cicilan(id_cicilan);
        instalment.setid_kontrak(id_kontrak);
        instalment.settanggal_cicilan(new java.util.Date());
        instalment.setjumlah_cicilan(jumlah_cicilan);

        return instalment.save();
    }
    
    public Instalment getInstalmentById(int id) {
        return Instalment.findById(id);
    }
}