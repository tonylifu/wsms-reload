package com.lifu.wsms.reload.http;

import com.lifu.wsms.reload.api.http.HttpApiService;
import com.lifu.wsms.reload.dto.response.HttpApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HttpApiRequestService implements HttpApiService {

    @Override
    public HttpApiResponse postApiRequest(String apiRequest, String endPoint, String token) {
        log.info("\n:::Request:::\n{}\n", apiRequest);
        HttpApiResponse apiResponse;
        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("content-type", "application/json")
                    .headers("Authorization", token)
                    .uri(URI.create(endPoint))
                    .POST(HttpRequest.BodyPublishers.ofString(apiRequest))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiResponse = HttpApiResponse.builder()
                    .responseBody(response.body())
                    .statusCode(response.statusCode())
                    .headers(response.headers())
                    .build();
        } catch (Exception e) {
            var errorMsg = "failed http client request <=> "+ e.getMessage();
            log.error(errorMsg);
            Map<String, List<String>> headersMap = Map.of(
                    "status", List.of("400"),
                    "Error-Message", List.of("Failed Request")
            );
            HttpHeaders headers = HttpHeaders.of(headersMap, (name, value) -> true);
            apiResponse = HttpApiResponse.builder()
                    .responseBody(getErrorMsg(errorMsg))
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .headers(headers)
                    .build();
        }
        log.info("\n:::Response:::\nStatus Code => {}\n Headers Info => {}\n Response => {}\n",
                apiResponse.statusCode(), apiResponse.headers(), apiResponse.responseBody());
        return apiResponse;
    }

    @Override
    public HttpApiResponse getApiRequest(String endPoint, String token) {
        log.info("\n:::Request=>GET:::\n");
        log.info("\n\n:::Request TOKEN=>:::\n{}\n\n", token);
        HttpApiResponse apiResponse;
        try(HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("content-type", "application/json")
                    .headers("Authorization", token)
                    .uri(URI.create(endPoint))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 404) {
                apiResponse = HttpApiResponse.builder()
                        .responseBody(
                                """
                                {
                                    "statusMessage": "Resource was not found"
                                }
                                """
                        )
                        .statusCode(response.statusCode())
                        .headers(response.headers())
                        .build();
            } else {
                apiResponse = HttpApiResponse.builder()
                        .responseBody(response.body())
                        .statusCode(response.statusCode())
                        .headers(response.headers())
                        .build();
            }
        } catch (Exception e) {
            var errorMsg = "failed http client request <=> "+ e.getMessage();
            log.error(errorMsg);
            Map<String, List<String>> headersMap = Map.of(
                    "status", List.of("400"),
                    "Error-Message", List.of("Failed Request")
            );
            HttpHeaders headers = HttpHeaders.of(headersMap, (name, value) -> true);
            apiResponse = HttpApiResponse.builder()
                    .responseBody(getErrorMsg(errorMsg))
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .headers(headers)
                    .build();
        }
        log.info("\n:::Response:::\nStatus Code => {}\n Headers Info => {}\n Response => {}\n",
                apiResponse.statusCode(), apiResponse.headers(), apiResponse.responseBody());
        return apiResponse;
    }

    private String getErrorMsg(String errorMsg) {
        return String.format("{'status': %s}", errorMsg);
    }
}
