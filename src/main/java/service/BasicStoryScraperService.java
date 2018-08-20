package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.HackerNewsStory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * Abstract class with common validation and json generation methods
 */
public abstract class BasicStoryScraperService implements HackerScraperService {
    protected ObjectMapper mapper;
    protected final Validator validator;

    public BasicStoryScraperService() {
        this.mapper = new ObjectMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public void validateStories(List<HackerNewsStory> newsStories) {
        for (HackerNewsStory story : newsStories) {
            Set<ConstraintViolation<HackerNewsStory>> constraintViolations = validator
                    .validate(story);
            constraintViolations
                    .stream()
                    .forEach(violation -> System.out.println(violation.getMessage()));
            if (!constraintViolations.isEmpty()) throw new ConstraintViolationException(constraintViolations);
        }
    }

    public String objectToPrettyJsonString(List<HackerNewsStory> newsStories) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newsStories);
    }
}
