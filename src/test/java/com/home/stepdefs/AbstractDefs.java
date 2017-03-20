package com.home.stepdefs;

import com.home.MainApplication;
import com.home.domain.Order;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by nagendra on 19/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainApplication.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
@IntegrationTest
public abstract class AbstractDefs {

    private String host = "localhost";
    private String port = "8080";
    protected String uri = new String("http://" + host + ":" + port + "/v1.0/");

    protected RestTemplate restTemplate = null;

    protected ResponseEntity lastResponseEntity;

    protected HttpClientErrorException lastException;

    protected void executePost(String contextPrefix, Order order) {
        restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Order> request = new HttpEntity<>(order, headers);

        lastResponseEntity = restTemplate.exchange(uri + contextPrefix, HttpMethod.POST, request, Integer.class);
    }

    protected void executeDelete(String contextPrefix) {
        restTemplate = new RestTemplate();

        try {
            lastResponseEntity = restTemplate.exchange(uri + contextPrefix, HttpMethod.DELETE, null, Void.class);
        } catch (HttpClientErrorException e){
            lastException = e;
        }
    }

    protected void executeGet(String contextPrefix, String orderType) {
        restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri + contextPrefix)
                .queryParam("orderType", orderType);

        lastResponseEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, request, String.class);
    }

    protected void checkStatusCode(int statusCode) {
        assertThat(lastResponseEntity.getStatusCode().value()).isEqualTo(statusCode);
    }

    protected void checkResponseBodyHasOrderId() {
        assertThat((Integer) lastResponseEntity.getBody()).isGreaterThan(0);
    }

}
