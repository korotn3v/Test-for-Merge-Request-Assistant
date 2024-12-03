package my.githubmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.githubmanager.exception.GitHubManagerException;
//import my.githubmanager.exception.Repository;
import my.githubmanager.http.GitHubHttpClient;
import my.githubmanager.model.Repository;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RepositoryService {
    private final GitHubHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RepositoryService(GitHubHttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    public List<Repository> listRepositories() throws GitHubManagerException {
        try {
            HttpResponse<String> response = httpClient.get("/user/repos?type=all");

            if (response.statusCode() != 200) {
                throw new GitHubManagerException("Failed to fetch repositories. Status: " + response.statusCode());
            }

            return Arrays.asList(objectMapper.readValue(response.body(), Repository[].class));
        } catch (Exception e) {
            throw new GitHubManagerException("Failed to fetch repositories", e);
        }
    }

    public Optional<Repository> getRepository(String owner, String repoName) throws GitHubManagerException {
        try {
            HttpResponse<String> response = httpClient.get("/repos/" + owner + "/" + repoName);

            if (response.statusCode() == 404) {
                return Optional.empty();
            }

            if (response.statusCode() != 200) {
                throw new GitHubManagerException("Failed to fetch repository. Status: " + response.statusCode());
            }

            Repository repo = objectMapper.readValue(response.body(), Repository.class);
            return Optional.of(repo);
        } catch (Exception e) {
            throw new GitHubManagerException("Failed to fetch repository", e);
        }
    }
}