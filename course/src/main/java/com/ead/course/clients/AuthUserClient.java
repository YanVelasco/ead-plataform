package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.PageDto;
import com.ead.course.dtos.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Component
public class AuthUserClient {

    @Value("${ead.api.url.authuser}")
    String baseUrlAuthUser;

    final RestClient restClient;

    public AuthUserClient(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }

    public PageDto<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable) {

        var url = baseUrlAuthUser + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replace(": ", ",");

        log.info("Requesting users for courseId: {} - URL: {}", courseId, url);

        try{
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        }catch (Exception e){
            log.debug("Error requesting users for courseId: {} - Details: {}", courseId, e.getMessage());
            throw e;
        }

    }

    public UserDto getUserById(UUID userId) {

        var url = baseUrlAuthUser + "/users/" + userId;

        log.info("Requesting user by userId: {} - URL: {}", userId, url);

        try{
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(UserDto.class);
        }catch (Exception e){
            log.debug("Error requesting user by userId: {} - Details: {}", userId, e.getMessage());
            throw e;
        }

    }

    public void saveUserSubscriptionInAuthUser(UUID userId, UUID courseId) {

        var url = baseUrlAuthUser + "/users/" + userId + "/courses/subscription";

        log.info("Saving user subscription in AuthUser for userId: {} and courseId: {} - URL: {}", userId, courseId, url);

        var courseUser = new CourseUserDto(courseId, userId);

        try{
            restClient.post()
                    .uri(url)
                    .contentType(APPLICATION_JSON)
                    .body(courseUser)
                    .retrieve()
                    .toBodilessEntity();
        }catch (Exception e){
            log.debug("Error saving user subscription in AuthUser for userId: {} and courseId: {} - Details: {}", userId, courseId, e.getMessage());
            throw e;
        }

    }

}
