package com.limac.diffservice

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiffActuatorIntegrationSpec extends Specification {

    @LocalServerPort
    int port

    def 'call GET to actuator health check without basic authorization'() {
        given: 'a health check endpoint'
        RequestSpecification diffLeftRequest = RestAssured.given()
            .baseUri('http://localhost').port(port).basePath('/api/diff-service/actuator/health')

        when: 'GET is called'
        Response response = diffLeftRequest.contentType(ContentType.JSON).get()
        response.then().log().all()

        then: 'should return status UNAUTHORIZED'
        response.statusCode == 401
    }

    def 'call GET to actuator health check'() {
        given: 'a health check endpoint'
        RequestSpecification diffLeftRequest = RestAssured.given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath('/api/diff-service/actuator/health')

        when: 'GET is called'
        Response response = diffLeftRequest.contentType(ContentType.JSON).get()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'status is up'
        response.path('status') == 'UP'
    }
}
