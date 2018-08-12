package models.db.dao;

import com.fasterxml.jackson.databind.JsonNode;
import models.db.PatientAddress;
import org.junit.Test;
import play.libs.Json;
import play.test.WithApplication;

import java.util.HashMap;

public class PatientAddressDaoImplTest extends WithApplication {

    @Test
    public void testAddDeletePatientAddressTrue(){
        final PatientAddressDaoImpl patientAddressDao = app.injector().instanceOf(PatientAddressDaoImpl.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("nickname", "John Doe");
        map.put("province", "Manitoba");
        map.put("postalCode", "V0S0B1");
        map.put("streetAddress", "410, Hasbrouck, Gali number 37, Sector 4");
        map.put("city", "Toronto");

        JsonNode json = Json.toJson(map);

        long patient_id = 100;
        PatientAddress newPatientAddress = Json.fromJson(json, PatientAddress.class);
        patientAddressDao.addPatientAddress(patient_id, newPatientAddress).thenAccept(address_id -> {
            patientAddressDao.deletePatientAddress(address_id).thenAccept(data -> {
                assert(data);
                patientAddressDao.deletePatientAddress(address_id).thenAccept(data2 -> {
                   assert(!data);
                });
            });
        });
    }
}