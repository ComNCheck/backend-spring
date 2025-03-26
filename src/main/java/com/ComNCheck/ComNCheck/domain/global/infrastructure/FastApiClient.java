package com.ComNCheck.ComNCheck.domain.global.infrastructure;

import com.ComNCheck.ComNCheck.domain.employmentNotice.model.dto.response.FastAPIEmploymentNoticeResponseListDTO;
import com.ComNCheck.ComNCheck.domain.global.exception.FastApiException;
import com.ComNCheck.ComNCheck.domain.majorNotice.model.dto.response.FastAPIMajorNoticesResponseListDTO;
import com.ComNCheck.ComNCheck.domain.member.model.dto.response.FastApiStudentCardDTO;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

@Component
@RequiredArgsConstructor
public class FastApiClient {
    private final RestTemplate restTemplate;

    private static final String LOCAL = "http://localhost:8000";
    private static final String TEST_DISTRIBUTION = "http://comncheck.iptime.org:8000";
    @Value("${target.server.ip}")
    private String PROD_FASTAPI_IP;

    private String PROD_FASTAPI;
    private String FAST_API_URL_OCR;
    private String FAST_API_URL_SCRAPE_NOTICE;
    private String Fast_API_URL_EMPLOYMENT;

    @PostConstruct
    public void init() {
        PROD_FASTAPI = "http://" + PROD_FASTAPI_IP;
        FAST_API_URL_OCR= PROD_FASTAPI + "/api/v1/compare-and-ocr";
        FAST_API_URL_SCRAPE_NOTICE = PROD_FASTAPI + "/api/v1/scrape/notice";
        Fast_API_URL_EMPLOYMENT = PROD_FASTAPI + "/api/v1/scrape/employment";
    }

    public FastApiStudentCardDTO sendImage(MultipartFile imageFile) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            ByteArrayResource resource = new ByteArrayResource(imageFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            };
            body.add("file", resource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiStudentCardDTO> response = restTemplate.postForEntity(
                    FAST_API_URL_OCR,
                    requestEntity,
                    FastApiStudentCardDTO.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new FastApiException("FastAPI 호출 실패: " + response.getStatusCode());
            }

            return response.getBody();

        } catch (IOException e) {
            throw new FastApiException("이미지 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public FastAPIMajorNoticesResponseListDTO fetchMajorNotices() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<FastAPIMajorNoticesResponseListDTO> response = restTemplate.exchange(
                    FAST_API_URL_SCRAPE_NOTICE,
                    HttpMethod.GET,
                    requestEntity,
                    FastAPIMajorNoticesResponseListDTO.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new FastApiException("FastAPI 호출 실패: " + response.getStatusCode());
            }

            return response.getBody();

        } catch (Exception e) {
            throw new FastApiException("공지사항 가져오기 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public FastAPIEmploymentNoticeResponseListDTO fetchEmploymentNotices() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<FastAPIEmploymentNoticeResponseListDTO> response = restTemplate.exchange(
                    Fast_API_URL_EMPLOYMENT,
                    HttpMethod.GET,
                    requestEntity,
                    FastAPIEmploymentNoticeResponseListDTO.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new FastApiException("FastAPI 호출 실패: " + response.getStatusCode());
            }

            return response.getBody();

        } catch (Exception e) {
            throw new FastApiException("공지사항 가져오기 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
