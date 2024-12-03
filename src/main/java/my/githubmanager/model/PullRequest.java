package my.githubmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PullRequest {
    @JsonProperty("id")
    private long id;

    @JsonProperty("number")
    private int number;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("state")
    private String state;

    // Геттеры и сеттеры
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}