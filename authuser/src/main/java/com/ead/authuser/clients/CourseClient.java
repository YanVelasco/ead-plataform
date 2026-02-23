package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.PageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClient) {
        this.restClient = restClient.build();
    }

    public PageDto<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable) {

        var url = baseUrlCourse + "/courses?userInstructor=" + userId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replace(": ", ",");

        log.info("Requesting courses for userId: {} - URL: {}", userId, url);

        try{
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<PageDto<CourseDto>>() {});
        }catch (Exception e){
            log.error("Error requesting courses for userId: {}", userId, e);
            throw e;
        }
    }

}
