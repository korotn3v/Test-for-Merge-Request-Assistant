package my.githubmanager;

import my.githubmanager.config.Configuration;
import my.githubmanager.exception.GitHubManagerException;
import my.githubmanager.http.GitHubHttpClient;
import my.githubmanager.model.PullRequest;
import my.githubmanager.model.Repository;
import my.githubmanager.service.PullRequestService;
import my.githubmanager.service.RepositoryService;

import java.util.List;
import java.util.Optional;

public class GitHubManager {
    private final RepositoryService repositoryService;
    private final PullRequestService pullRequestService;

    public GitHubManager(String token) {
        GitHubHttpClient httpClient = new GitHubHttpClient(token);
        this.repositoryService = new RepositoryService(httpClient);
        this.pullRequestService = new PullRequestService(httpClient);
    }

    public GitHubManager() throws GitHubManagerException {
        this(new Configuration().getToken());
    }

    public List<Repository> listRepositories() throws GitHubManagerException {
        return repositoryService.listRepositories();
    }

    public Optional<Repository> getRepository(String owner, String repoName) throws GitHubManagerException {
        return repositoryService.getRepository(owner, repoName);
    }

    public PullRequest createPullRequest(String owner, String repo, String title,
                                         String head, String base, String body)
            throws GitHubManagerException {
        return pullRequestService.createPullRequest(owner, repo, title, head, base, body);
    }
}