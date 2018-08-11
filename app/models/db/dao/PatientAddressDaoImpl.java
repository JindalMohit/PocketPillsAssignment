package models.db.dao;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import models.db.BaseModel;
import models.db.ModelServerConfigStartup;
import models.db.PatientAddress;
import models.db.dao.interfaces.PatientAddressDao;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
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
        return null;
    }

    @Override
    public CompletionStage<Boolean> deletePatientAddress(long patientId, long addressId) {
        return null;
    }


    @Override
    public CompletionStage<Long> addPatientAddress(long patientId, PatientAddress address) {
//        ServerConfig config = new ServerConfig();
//        ModelServerConfigStartup configStartup = new ModelServerConfigStartup();
//        configStartup.onStart(config);
//        EbeanServer ebeanServer = EbeanServerFactory.create(config);
        address.setPatientId(patientId);
        address.setEnabled(true);
        return supplyAsync(() -> {
            ebeanServer.insert(address);
            return address.getId();
        }, executionContext);
    }
}
