package com.camping_rental.server.domain.naver.email.service;

import com.camping_rental.server.config.naver.NaverCloudProperties;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.naver.email.dto.NaverEmailRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class NaverEmailService {
    private final NaverCloudProperties naverCloudProperties;

    public void sendEmail(NaverEmailRequestDto mailRequestDto) {
        try {
            Long time = System.currentTimeMillis();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(mailRequestDto);
            HttpHeaders headers = setApiRequestHeader(time);

            HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.postForObject(new URI(naverCloudProperties.getMailer().getMailRequestUrl() + naverCloudProperties.getMailer().getMailApiUri()), body, HashMap.class);
        } catch (URISyntaxException e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        } catch (JsonProcessingException e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        }
    }

    public void sendEmail(NaverEmailRequestDto.Send mailRequestDto) {
        try {
            Long time = System.currentTimeMillis();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(mailRequestDto);
            HttpHeaders headers = setApiRequestHeader(time);

            HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.postForObject(new URI(naverCloudProperties.getMailer().getMailRequestUrl() + naverCloudProperties.getMailer().getMailApiUri()), body, HashMap.class);
        } catch (URISyntaxException e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        } catch (JsonProcessingException e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        }
    }

    public HttpHeaders setApiRequestHeader(Long time) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", naverCloudProperties.getAccessKey());
        String sig = makeSignature(time);
        headers.set("x-ncp-apigw-signature-v2", sig);

        return headers;
    }

    public String makeSignature(Long time) {
        String newLine = "\n";
        String method = "POST";
        String space = " ";
        String url = naverCloudProperties.getMailer().getMailApiUri();
        String accessKey = naverCloudProperties.getAccessKey();
        String secretKey = naverCloudProperties.getSecretKey();

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(time.toString())
                .append(newLine)
                .append(accessKey)
                .toString();

        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
            return encodeBase64String;
        } catch (InvalidKeyException e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        } catch (NoSuchAlgorithmException e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        } catch (UnsupportedEncodingException e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        } catch (Exception e) {
            throw new NotMatchedFormatException("이메일 전송이 불가능 합니다.");
        }
    }
}
