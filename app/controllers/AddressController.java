package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.db.PatientAddress;
import models.db.dao.PatientAddressDaoImpl;
import models.db.dao.interfaces.PatientAddressDao;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import software.amazon.awssdk.util.json.Jackson;
import utils.AppUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AddressController extends Controller {

    private final PatientAddressDaoImpl patientAddressDao;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public AddressController(PatientAddressDaoImpl patientAddressDao,
                          HttpExecutionContext httpExecutionContext) {
        this.patientAddressDao = patientAddressDao;
        this.httpExecutionContext = httpExecutionContext;
    }

    /**
     * Retrieves the List of addresses which are enabled corresponding to the patientId
     * (presented in the header as patient_id)
     * present in the header
     *
     * We need to return only those addresses which have enabled flag set
     * as True {@see models.db.BaseModel#enabled}
     *
     *
     * The resultant JSON would look like:
     * {
     *     "success": true,
     *     "addresses": [
     *          {
     *              "nickname": "John Doe",
     *              "province": "Manitoba",
     *              "postal_code": "V0S0B1",
     *              "street_address": "410, Hasbrouck, Gali number 37, Sector 4",
     *              "city": "Toronto",
     *          },
     *          {
     *              "nickname": "Batman",
     *              ...
     *          },
     *          ...
     *     ]
     * }
     * The addresses are given in the table
     * @see models.db.PatientAddress
     *
     * @return List<Address> encapsulated in
     * @see utils.AppUtil#getSuccessObject(String) if successful or
     * @see utils.AppUtil#getBadRequestObject(String) if unsuccessful
     */
    public CompletionStage<Result> getPatientAddress() {
        long patient_id = Long.parseLong(request().getQueryString("patient_id"));
        return patientAddressDao.getPatientAddress(patient_id).thenApplyAsync(addressList -> {
            return ok(AppUtil.getSuccessObject(addressList.toString()));
        }, httpExecutionContext.current());

//        return supplyAsync(() -> {
//            List<PatientAddress> patientAddresses = PatientAddress.find.all();
//            return ok(AppUtil.getSuccessObject(patientAddresses.toString()));
//        }, httpExecutionContext.current());
    }

    /**
     * @see models.db.PatientAddress and the body are bound together.
     * The json body will look like as follows:
     * {
     *     "nickname": "John Doe",
     *     "province": "Manitoba",
     *     "postalCode": "V0S0B1",
     *     "streetAddress": "410, Hasbrouck, Gali number 37, Sector 4",
     *     "city": "Toronto",
     * }
     * The patientId is present in the header (presented in the header as patient_id)
     *
     * This object is saved in the table with enabled as True
     *
     * @return json with the id of address encapsulated in
     * @see utils.AppUtil#getSuccessObject(String) if successfull
     * @see utils.AppUtil#getBadRequestObject(String) if unsuccessfull
     */
    public CompletionStage<Result> addPatientAddress() {
        long patient_id = Long.parseLong(request().getQueryString("patient_id"));
        JsonNode jsonAddress = request().body().asJson();
        PatientAddress newPatientAddress = Json.fromJson(jsonAddress, PatientAddress.class);
        patientAddressDao.addPatientAddress(patient_id, newPatientAddress);

        // Run insert db operation, then redirect
        return patientAddressDao.addPatientAddress(patient_id, newPatientAddress).thenApplyAsync(data -> {
            return ok(AppUtil.getSuccessObject("Address saved at id: " + data.toString()));
        }, httpExecutionContext.current());
    }


    /**
     * @see models.db.PatientAddress entry is deleted corresponding to the {@code addressId}
     * For deleting a particular address, we just set the
     * enabled flag as false {@see models.db.BaseModel#enabled}
     *
     * @param addressId which we need to delete
     * @return json denoting success or failure encapsulated in
     * @see utils.AppUtil#getSuccessObject(String)  if successfully deleted
     * @see utils.AppUtil#getBadRequestObject(String) if unsuccessfull
     */
    public CompletionStage<Result> deletePatientAddress(long addressId) {
        return patientAddressDao.deletePatientAddress(addressId).thenApplyAsync(data -> {
            if(data) return ok(AppUtil.getSuccessObject("entry deleted at id: " + addressId));
            return badRequest(AppUtil.getBadRequestObject("unable to delete at id: " + addressId));
        }, httpExecutionContext.current());
    }


}
