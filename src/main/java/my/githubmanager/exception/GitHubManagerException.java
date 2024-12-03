package my.githubmanager.exception;

public class GitHubManagerException extends Exception {
    public GitHubManagerException(String message) {
        super(message);
    }

    public GitHubManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}