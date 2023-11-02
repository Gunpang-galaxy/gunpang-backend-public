package com.galaxy.gunpang.notification;

import com.galaxy.gunpang.notification.model.dto.RecordNotificationReqDto;
import com.galaxy.gunpang.notification.model.dto.SendReqDto;
import com.galaxy.gunpang.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Operation(summary = "알림 보내기", description = "사용자에게 백그라운드 알림을 보냅니다.")
    @PostMapping(value = "/fcm/notification", consumes = "application/json;charset=UTF-8")
    public ResponseEntity sendNotification(@RequestBody SendReqDto sendReqDto) throws IOException {

        logger.debug("controller");
        notificationService.sendNotification(sendReqDto.getTargetToken(), sendReqDto.getTitle(), sendReqDto.getBody());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "디바이스 토큰 저장", description = "사용자의 디바이스 토큰을 저장합니다.")
    @PostMapping(value = "/fcm/notification/record", consumes = "application/json;charset=UTF-8")
    public ResponseEntity recordNotification(@RequestBody RecordNotificationReqDto recordNotificationReqDto) throws IOException {

        logger.debug("controller");
        notificationService.recordNotification(recordNotificationReqDto.getToken(), recordNotificationReqDto.getUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
