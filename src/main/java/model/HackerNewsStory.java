package model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URI;

/**
 * This class models a front page Hacker News Story Summary.
 * The class can be serialised and de-serialised into json
 * It also includes validation using the Hibernate implementation of JSR-303
 */
@JsonIgnoreProperties(value = {"rank"}, allowGetters = true, ignoreUnknown = true)
public class HackerNewsStory {
    @NotNull
    @Size(max = 256)
    public String title;
    @URL
    public String uri;
    @NotNull
    @Size(max = 256)
    public String author;
    @Min(0)
    public int points;
    @Min(0)
    public int comments;
    @Min(0)
    public int rank;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    @JsonAlias("url")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAuthor() {
        return author;
    }

    @JsonAlias("by")
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPoints() {
        return points;
    }

    @JsonAlias("score")
    public void setPoints(int points) {
        this.points = points;
    }

    public int getComments() {
        return comments;
    }

    @JsonAlias("descendants")
    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
