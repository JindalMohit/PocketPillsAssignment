package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class AddressControllerTest extends WithApplication {
    @Test
    public void testGetPatientAddress(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/address?patient_id=120");

        Result result = route(app, request);
        assert(result.status() == OK);
    }

    @Test
    public void testAddPatientAddress(){
        HashMap<String, String> map = new HashMap<>();
        map.put("nickname", "John Doe");
        map.put("province", "Manitoba");
        map.put("postalCode", "V0S0B1");
        map.put("streetAddress", "410, Hasbrouck, Gali number 37, Sector 4");
        map.put("city", "Toronto");

        JsonNode json = Json.toJson(map);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(json)
                .uri("/address?patient_id=120");

        Result result = route(app, request);
        Logger.debug(result.toString());
        assert(result.status() == OK);
        assert(contentAsString(result).contains("Address saved at id: "));
    }

    @Test
    public void testDeletePatientAddress(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/address/7");

        Result result = route(app, request);
        assert(result.status() == OK);
        assert(contentAsString(result).contains("Entry deleted at id: "));
    }
}
