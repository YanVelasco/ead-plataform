package com.ead.course.clients;

import com.ead.course.dtos.PageDto;
import com.ead.course.dtos.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

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
            log.error("Error requesting users for courseId: {}", courseId, e);
            throw e;
        }

    }

}
