package com.limac.diffservice.rest

import com.limac.diffservice.type.ResultType
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.nio.charset.StandardCharsets

import static io.restassured.RestAssured.given

@Stepwise
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DiffControllerIntegrationSpec extends Specification {

    @LocalServerPort
    int port

    @Shared
    String diffId = UUID.randomUUID()

    def 'call POST to save the left input without basic authorization'() {
        given: 'a diffId and the payload'
        RequestSpecification diffLeftRequest = given()
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/left")

        String base64Value = new String(Base64.encoder.encode('{"property":"value"}'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffLeftRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status UNAUTHORIZED'
        response.statusCode == 401
    }

    def 'call POST to save null left input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffLeftRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/left")

        String payload = '{"base64Value":null}'

        when: 'POST is called'
        Response response = diffLeftRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status BAD_REQUEST'
        response.statusCode == 400

        and: 'should return the error message'
        response.path('errorMessage') == 'base64Value must not be blank'
    }

    def 'call POST to save empty left input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffLeftRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/left")

        String payload = '{"base64Value":""}'

        when: 'POST is called'
        Response response = diffLeftRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status BAD_REQUEST'
        response.statusCode == 400

        and: 'should return the error message'
        String errorResponse = response.path('errorMessage')
        errorResponse == 'base64Value must be base64 encoded, base64Value must not be blank' || errorResponse == 'base64Value must not be blank, base64Value must be base64 encoded'
    }

    def 'call POST to save invalid left input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffLeftRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/left")

        String payload = '{"base64Value":"QQ="}'

        when: 'POST is called'
        Response response = diffLeftRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status BAD_REQUEST'
        response.statusCode == 400

        and: 'should return the error message'
        response.path('errorMessage') == 'base64Value must be base64 encoded'
    }

    def 'call POST to save the right input without basic authorization'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String base64Value = new String(Base64.encoder.encode('{"sameSize":"false"}'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status UNAUTHORIZED'
        response.statusCode == 401
    }

    def 'call POST to save null right input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String payload = '{"base64Value":null}'

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status BAD_REQUEST'
        response.statusCode == 400

        and: 'should return the error message'
        response.path('errorMessage') == 'base64Value must not be blank'
    }

    def 'call POST to save empty right input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String payload = '{"base64Value":""}'

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status BAD_REQUEST'
        response.statusCode == 400

        and: 'should return the error message'
        String errorResponse = response.path('errorMessage')
        errorResponse == 'base64Value must be base64 encoded, base64Value must not be blank' || errorResponse == 'base64Value must not be blank, base64Value must be base64 encoded'
    }

    def 'call POST to save invalid right input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String payload = '{"base64Value":"QQ="}'

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status BAD_REQUEST'
        response.statusCode == 400

        and: 'should return the error message'
        response.path('errorMessage') == 'base64Value must be base64 encoded'
    }

    def 'call GET to diff the left and right inputs without basic authorization'() {
        given: 'a diffId'
        RequestSpecification diffRequest = given()
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}")

        when: 'GET is called'
        Response response = diffRequest.get()
        response.then().log().all()

        then: 'should return status UNAUTHORIZED'
        response.statusCode == 401
    }

    def 'call POST to save the left input with unsupported media type'() {
        given: 'a diffId and the payload'
        RequestSpecification diffLeftRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/left")

        String base64Value = new String(Base64.encoder.encode('<root><property>value</property></root>'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffLeftRequest.body(payload).contentType(ContentType.XML).post()
        response.then().log().all()

        then: 'should return status UNSUPPORTED_MEDIA_TYPE'
        response.statusCode == 415

        and: 'should return the error message'
        response.path('errorMessage') == 'Content type \'application/xml;charset=ISO-8859-1\' not supported'
    }

    def 'call POST to save the left input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffLeftRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/left")

        String base64Value = new String(Base64.encoder.encode('{"property":"value"}'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffLeftRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the left input'
        response.path('diffId') == diffId
        response.path('left') == base64Value
    }

    def 'call GET to diff the left and right inputs before both inputs are stored'() {
        given: 'a diffId'
        RequestSpecification diffRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}")

        when: 'GET is called'
        Response response = diffRequest.get()
        response.then().log().all()

        then: 'should return status INTERNAL_SERVER_ERROR'
        response.statusCode == 500

        and: 'should return the error message'
        response.path('errorMessage') == "left and/or right input is missing for diffId: ${diffId}"
    }

    def 'call POST to save the right input with unsupported media type'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String base64Value = new String(Base64.encoder.encode('<root><sameSize>false</sameSize></root>'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.XML).post()
        response.then().log().all()

        then: 'should return status UNSUPPORTED_MEDIA_TYPE'
        response.statusCode == 415

        and: 'should return the error message'
        response.path('errorMessage') == 'Content type \'application/xml;charset=ISO-8859-1\' not supported'
    }

    def 'call POST to save the right input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String base64Value = new String(Base64.encoder.encode('{"sameSize":"false"}'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the right input'
        response.path('diffId') == diffId
        response.path('right') == base64Value
    }

    def 'call GET to diff the left and right inputs with a non existing diffId'() {
        given: 'a non existing diffId'
        RequestSpecification diffRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath('/api/diff-service/v1/diff/nonExistingDiffId')

        when: 'GET is called'
        Response response = diffRequest.get()
        response.then().log().all()

        then: 'should return status NOT_FOUND'
        response.statusCode == 404

        and: 'should return the error message'
        response.path('errorMessage') == 'could not find diffId: nonExistingDiffId'
    }

    def 'call GET to diff the left and right inputs with same size but different'() {
        given: 'a diffId'
        RequestSpecification diffRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}")

        when: 'GET is called'
        Response response = diffRequest.get()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the diffId'
        response.path('diffId') == diffId

        and: 'should return the result type SAME_SIZE_BUT_DIFFERENT'
        response.path('resultType') == ResultType.SAME_SIZE_BUT_DIFFERENT.toString()

        and: 'should return the diffResults'
        response.path('diffResults') == [['offset': 3, 'length': 10], ['offset': 17, 'length': 1], ['offset': 22, 'length': 1]]
    }

    def 'call POST to update the left input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffLeftRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/left")

        String base64Value = new String(Base64.encoder.encode('{"property":"anotherValue"}'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffLeftRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the left input'
        response.path('diffId') == diffId
        response.path('left') == base64Value
    }

    def 'call POST to update the right input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String base64Value = new String(Base64.encoder.encode('{"sameSize":"true"}'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the right input'
        response.path('diffId') == diffId
        response.path('right') == base64Value
    }

    def 'call GET to diff the left and right inputs with different size'() {
        given: 'a diffId'
        RequestSpecification diffRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}")

        when: 'GET is called'
        Response response = diffRequest.get()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the diffId'
        response.path('diffId') == diffId

        and: 'should return the result type DIFFERENT_SIZE'
        response.path('resultType') == ResultType.DIFFERENT_SIZE.toString()

        and: 'should return empty diffResults'
        response.path('diffResults') == null
    }

    def 'call POST to update the right input with a value equal to the left input'() {
        given: 'a diffId and the payload'
        RequestSpecification diffRightRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}/right")

        String base64Value = new String(Base64.encoder.encode('{"property":"anotherValue"}'.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
        String payload = "{\"base64Value\":\"${base64Value}\"}"

        when: 'POST is called'
        Response response = diffRightRequest.body(payload).contentType(ContentType.JSON).post()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the right input'
        response.path('diffId') == diffId
        response.path('right') == base64Value
    }

    def 'call GET to diff the left and right inputs that are equal'() {
        given: 'a diffId'
        RequestSpecification diffRequest = given()
            .auth().basic('user', 'pass')
            .baseUri('http://localhost').port(port).basePath("/api/diff-service/v1/diff/${diffId}")

        when: 'GET is called'
        Response response = diffRequest.get()
        response.then().log().all()

        then: 'should return status OK'
        response.statusCode == 200

        and: 'should return the diffId'
        response.path('diffId') == diffId

        and: 'should return the result type EQUAL'
        response.path('resultType') == ResultType.EQUAL.toString()

        and: 'should return empty diffResults'
        response.path('diffResults') == null
    }
}
