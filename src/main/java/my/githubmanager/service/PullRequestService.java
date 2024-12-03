package my.githubmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.githubmanager.exception.GitHubManagerException;
import my.githubmanager.http.GitHubHttpClient;
import my.githubmanager.model.PullRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class PullRequestService {
    private final GitHubHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PullRequestService(GitHubHttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    public PullRequest createPullRequest(String owner, String repo, String title,
                                         String head, String base, String body)
            throws GitHubManagerException {
        try {
            Map<String, String> prData = new HashMap<>();
            prData.put("title", title);
            prData.put("head", head);
            prData.put("base", base);
            prData.put("body", body);

            String jsonBody = objectMapper.writeValueAsString(prData);

            HttpResponse<String> response = httpClient.post(
                    "/repos/" + owner + "/" + repo + "/pulls",
                    jsonBody
            );

            if (response.statusCode() != 201) {
                throw new GitHubManagerException("Failed to create pull request. Status: " + response.statusCode());
            }

            return objectMapper.readValue(response.body(), PullRequest.class);
        } catch (Exception e) {
            throw new GitHubManagerException("Failed to create pull request", e);
        }
    }
}