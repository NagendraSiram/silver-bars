package com.home.stepdefs;

import cucumber.api.java.Before;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by nagendra on 20/03/2017.
 */
public class Hooks extends AbstractDefs {
    @Before
    public void reset() {
        restTemplate = new RestTemplate();
        ResponseEntity<?> responseEntity = restTemplate.exchange(uri + "/reset", HttpMethod.GET, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
