package com.galaxy.gunpang.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galaxy.gunpang.notification.model.Notification;
import com.galaxy.gunpang.notification.model.dto.FcmMessage;
import com.galaxy.gunpang.notification.repository.NotificationRepository;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/gunpang-60e56/messages:send";
    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public boolean checkInNotification(String token){
        logger.debug("checkInNotification");
        if(notificationRepository.findByToken(token) == null ){
            return false;
        }
        return true;
    }

    public void  sendNotification(String targetToken, String title, String body) throws IOException{

        logger.debug("sendNotification");

        //targetToken은 front에서 얻을 수 o
        String notification = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(notification, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .build();

        Response response = client.newCall(request)
                .execute();

        logger.debug(response.body().string());

    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {

        logger.debug("makeMessage");

        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                //.image("/resources/static/images/appLogo.png")
                                .build()
                        )
                        .build()
                )
                .validate_only(false)
                .build();

        logger.debug(objectMapper.writeValueAsString(fcmMessage));

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException{
        logger.debug("getAccessToken");

        String firebaseConfigPath = "firebase/gunpangServiceKey.json";
        //FCM 권한 부여된 인스턴스
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        //accessToken 생성
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
