package com.camping_rental.server.domain.naver.sens.service;

import com.camping_rental.server.config.naver.NaverCloudProperties;
import com.camping_rental.server.domain.exception.dto.NotMatchedFormatException;
import com.camping_rental.server.domain.naver.sens.dto.NaverCloudSensSmsSendDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverCloudSensSmsService {
    private final NaverCloudProperties naverCloudProperties;

    public void sendSms(NaverCloudSensSmsSendDto receiverForm){
        try {
            String timestamp = Long.toString(new Date().getTime());
            String url = "https://sens.apigw.ntruss.com/sms/v2/services/" + naverCloudProperties.getSens().getSms().getServiceId() + "/messages";

            // 메시지 데이터 구성
            Map<String, Object> messages = new HashMap<>();
            messages.put("to", receiverForm.getPhoneNumber());

            Map<String, Object> body = new HashMap<>();
            body.put("type", "LMS");
            body.put("contentType", "COMM");
            body.put("countryCode", receiverForm.getCountryCode());
            body.put("from", naverCloudProperties.getSens().getSms().getSenderPhoneNumber());
            body.put("subject", receiverForm.getSubject());
            body.put("content", receiverForm.getMessage());
            body.put("messages", new Map[] { messages });

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(body);

            // 요청 생성
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setHeader("x-ncp-apigw-timestamp", timestamp);
            httpPost.setHeader("x-ncp-iam-access-key", naverCloudProperties.getAccessKey());
            String signature = naverCloudProperties.makeSignature("POST", "/sms/v2/services/" + naverCloudProperties.getSens().getSms().getServiceId() + "/messages", timestamp, naverCloudProperties.getAccessKey(), naverCloudProperties.getSecretKey());
            httpPost.setHeader("x-ncp-apigw-signature-v2", signature);
            httpPost.setEntity(new StringEntity(jsonBody, "UTF-8"));

            // 요청 보내기
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMultiple(List<NaverCloudSensSmsSendDto> formList) {
        if(formList.size() <= 0){
            return;
        }

        formList.forEach(this::sendSms);
    }
}
