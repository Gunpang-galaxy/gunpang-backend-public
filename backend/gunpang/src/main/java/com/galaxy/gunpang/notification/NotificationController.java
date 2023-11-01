package com.galaxy.gunpang.notification;

import com.galaxy.gunpang.notification.model.dto.RecordNotificationReqDto;
import com.galaxy.gunpang.notification.model.dto.SendReqDto;
import com.galaxy.gunpang.notification.service.NotificationService;
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
    @PostMapping(value = "/fcm/notification", consumes = "application/json;charset=UTF-8")
    public ResponseEntity sendNotification(@RequestBody SendReqDto sendReqDto) throws IOException {

        logger.debug("controller");
        notificationService.sendNotification(sendReqDto.getTargetToken(), sendReqDto.getTitle(),sendReqDto.getBody());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/fcm/notification/record", consumes = "application/json;charset=UTF-8" )
    public ResponseEntity recordNotification(@RequestBody RecordNotificationReqDto recordNotificationReqDto) throws IOException {

        logger.debug("controller");
        notificationService.recordNotification(recordNotificationReqDto.getToken(), recordNotificationReqDto.getUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
