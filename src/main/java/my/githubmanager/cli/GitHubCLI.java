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
    private static final String CONFIG_FILE = "src/main/resources/github-config.properties";
    private final GitHubManager manager;
    private final Scanner scanner;

    public GitHubCLI() {
        this.manager = new GitHubManager(this.loadGitHubToken());
        this.scanner = new Scanner(System.in);
    }

    private String loadGitHubToken() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            return properties.getProperty("github.token");
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке конфигурации: " + e.getMessage());
            return null;
        }
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
                        System.out.println("Выход...");
                        return;
                    default:
                        System.out.println("Неверный выбор");
                }
            } catch (GitHubManagerException e) {
                System.err.println("Ошибка: " + e.getMessage());
            }

            System.out.println("\nНажмите Enter для продолжения...");
            scanner.nextLine();
        }
    }

    private void printMenu() {
        System.out.println("\n=== GitHub Manager CLI ===");
        System.out.println("1. Список репозиториев");
        System.out.println("2. Информация о репозитории");
        System.out.println("3. Создать Pull Request");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void listRepositories() throws GitHubManagerException {
        List<Repository> repos = manager.listRepositories();
        System.out.println("\nСписок репозиториев:");
        for (Repository repo : repos) {
            System.out.printf("- %s (%s)%n", repo.getFullName(), repo.getHtmlUrl());
        }
    }

    private void getRepository() throws GitHubManagerException {
        System.out.print("Введите владельца репозитория: ");
        String owner = scanner.nextLine();
        System.out.print("Введите название репозитория: ");
        String repoName = scanner.nextLine();

        manager.getRepository(owner, repoName).ifPresent(repo -> {
            System.out.println("\nИнформация о репозитории:");
            System.out.println("Название: " + repo.getFullName());
            System.out.println("URL: " + repo.getHtmlUrl());
            System.out.println("Описание: " + repo.getDescription());
            System.out.println("Основная ветка: " + repo.getDefaultBranch());
        });
    }

    private void createPullRequest() throws GitHubManagerException {
        System.out.print("Владелец репозитория: ");
        String owner = scanner.nextLine();
        System.out.print("Название репозитория: ");
        String repo = scanner.nextLine();
        System.out.print("Заголовок PR: ");
        String title = scanner.nextLine();
        System.out.print("Ветка с изменениями: ");
        String head = scanner.nextLine();
        System.out.print("Целевая ветка: ");
        String base = scanner.nextLine();
        System.out.print("Описание PR: ");
        String body = scanner.nextLine();

        PullRequest pr = manager.createPullRequest(owner, repo, title, head, base, body);
        System.out.println("\nСоздан Pull Request #" + pr.getNumber());
        System.out.println("URL: " + pr.getHtmlUrl());
    }

    public static void main(String[] args) {
        GitHubCLI cli = new GitHubCLI();
        cli.start();
    }
}
