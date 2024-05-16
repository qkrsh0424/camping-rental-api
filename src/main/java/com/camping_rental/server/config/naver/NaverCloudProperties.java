package com.camping_rental.server.config.naver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@ConfigurationProperties(prefix = "app.cloud.naver")
@Getter
@Setter
public class NaverCloudProperties {
    private String accessKey;
    private String secretKey;
    private Mailer mailer = new Mailer();
    private Sens sens = new Sens();

    @Getter
    @Setter
    public static class Mailer{
        private String mailRequestUrl;
        private String mailApiUri;
    }

    @Getter
    @Setter
    public static class Sens{
        private Sms sms = new Sms();
        private BizMessage bizMessage = new BizMessage();

        @Getter
        @Setter
        public static class Sms{
            private String serviceId;
            private String senderPhoneNumber;
        }

        @Getter
        @Setter
        public static class BizMessage{
            private String serviceId;
        }
    }

    public static String makeSignature(String method, String url, String timestamp, String accessKey, String secretKey) {
        String space = " ";                    // one space
        String newLine = "\n";                    // new line

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = null;
        try {
            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

            return encodeBase64String;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
