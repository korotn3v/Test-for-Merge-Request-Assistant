package my.githubmanager.cli;

import my.githubmanager.GitHubManager;
import my.githubmanager.exception.GitHubManagerException;
import my.githubmanager.model.Repository;
import my.githubmanager.model.PullRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class GitHubCLI {
    private final GitHubManager manager;
    private final Scanner scanner;

    public GitHubCLI() throws GitHubManagerException {
        this.manager = new GitHubManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            try {
                switch (choice) {
                    case 1:
                        listRepositories();
                        break;
                    case 2:
                        getRepository();
                        break;
                    case 3:
                        createPullRequest();
                        break;
                    case 0:
                        System.out.println("Exit...");
                        return;
                    default:
                        System.out.println("Wrong choice");
                }
            } catch (GitHubManagerException e) {
                System.err.println("Failed: " + e.getMessage());
            }

//            System.out.println("\nPress Enter to continue...");
//            scanner.nextLine();
        }
    }

    private void printMenu() {
        System.out.println("\n=== GitHub Manager CLI ===");
        System.out.println("1. List of repositories");
        System.out.println("2. info about repositories");
        System.out.println("3. Create Pull Request");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void listRepositories() throws GitHubManagerException {
        List<Repository> repos = manager.listRepositories();
        System.out.println("\nList of repositories:");
        int i = 1;
        for (Repository repo : repos) {
            System.out.printf("-%d- %s (%s)%n", i, repo.getFullName(), repo.getHtmlUrl());
            i++;
        }
    }

    private void getRepository() throws GitHubManagerException {
        System.out.print("Write the owner: ");
        String owner = scanner.nextLine();
        System.out.print("Write the name of repository: ");
        String repoName = scanner.nextLine();

        manager.getRepository(owner, repoName).ifPresent(repo -> {
            System.out.println("\nRepository information:");
            System.out.println("Name: " + repo.getFullName());
            System.out.println("URL: " + repo.getHtmlUrl());
            System.out.println("Description: " + repo.getDescription());
            System.out.println("Main branch: " + repo.getDefaultBranch());
        });
    }

    private void createPullRequest() throws GitHubManagerException {
        String owner = scanner.nextLine();
        String repo = scanner.nextLine();
        String title = scanner.nextLine();
        String head = scanner.nextLine();
        String base = scanner.nextLine();
        String body = scanner.nextLine();

        PullRequest pr = manager.createPullRequest(owner, repo, title, head, base, body);
        System.out.println("\nWas created Pull Request #" + pr.getNumber());
        System.out.println("URL: " + pr.getHtmlUrl());
    }

    public static void main(String[] args) {
        try {
            GitHubCLI cli = new GitHubCLI();
            cli.start();
        } catch (GitHubManagerException e) {
            throw new RuntimeException(e);
        }


    }
}
