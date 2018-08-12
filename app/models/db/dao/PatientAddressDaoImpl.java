package models.db.dao;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.Transaction;
import io.ebean.config.ServerConfig;
import models.db.BaseModel;
import models.db.ModelServerConfigStartup;
import models.db.PatientAddress;
import models.db.dao.interfaces.PatientAddressDao;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PatientAddressDaoImpl implements PatientAddressDao {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public PatientAddressDaoImpl(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext){
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<PatientAddress>> getPatientAddress(long patientId) {
        return supplyAsync(() ->
                ebeanServer.find(PatientAddress.class)
                        .select("nickname, province, postalCode, streetAddress, city")
                        .where()
                        .eq("patient_id", patientId)
                        .findList(), executionContext);
    }

    @Override
    public CompletionStage<Boolean> deletePatientAddress(long addressId) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            try {
                PatientAddress savedAddress = ebeanServer.find(PatientAddress.class).setId(addressId).findOne();
                if (savedAddress != null) {
                    savedAddress.setEnabled(true);

                    savedAddress.update();
                    txn.commit();
                    return true;
                }
            } finally {
                txn.end();
            }
            return false;
        }, executionContext);
    }


    @Override
    public CompletionStage<Long> addPatientAddress(long patientId, PatientAddress address) {
//        ServerConfig config = new ServerConfig();
//        ModelServerConfigStartup configStartup = new ModelServerConfigStartup();
//        configStartup.onStart(config);
//        EbeanServer ebeanServer = EbeanServerFactory.create(config);
        address.setPatientId(patientId);
        address.setEnabled(false);
        return supplyAsync(() -> {
            ebeanServer.insert(address);
            return address.getId();
        }, executionContext);
    }
}
