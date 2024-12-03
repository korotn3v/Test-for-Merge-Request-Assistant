package my.githubmanager.http;

import my.githubmanager.exception.GitHubManagerException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitHubHttpClient {
    private static final String API_BASE_URL = "https://api.github.com";
    private final java.net.http.HttpClient httpClient;
    private final String authToken;

    public GitHubHttpClient(String authToken) {
        this.httpClient = java.net.http.HttpClient.newHttpClient();
        this.authToken = authToken;
    }

    public HttpResponse<String> get(String endpoint) throws GitHubManagerException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Accept", "application/vnd.github.v3+json")
                    .GET()
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new GitHubManagerException("Failed to execute GET request", e);
        }
    }

    public HttpResponse<String> post(String endpoint, String jsonBody) throws GitHubManagerException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Authorization", "Bearer " + authToken)
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new GitHubManagerException("Failed to execute POST request", e);
        }
    }
}
