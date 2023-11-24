package com.galaxy.gunpang.notification.scheduler;

import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.dailyRecord.model.enums.FoodType;
import com.galaxy.gunpang.dailyRecord.repository.DailyRecordRepository;
import com.galaxy.gunpang.notification.repository.NotificationRepository;
import com.galaxy.gunpang.notification.service.NotificationService;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
    private final DailyRecordRepository dailyRecordRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    //초 분 시 일 월 요일
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void notificationBreakfast() throws IOException {
        logger.debug("notificationBreakfast");
        List<User> users = userRepository.findAll();

        for (int i=0;i<users.size();i++){
            logger.debug("user[i] : " + users.get(i).toString());
            Optional<DailyRecord> dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(users.get(i).getId(), LocalDate.now());

            String deviceToken = notificationRepository.findTokenByUserId(users.get(i).getId()).getToken();
            //얘는 안찍힘
            if (deviceToken == null)
                logger.debug(String.valueOf("deviceToken is null" + deviceToken == null));
            logger.debug("deviceToken : " + deviceToken.length());
            if (dailyRecord.isPresent())
                if (dailyRecord.get().getBreakfastFoodType() == FoodType.NOT_RECORD ||dailyRecord.get().getBreakfastFoodType() == null)
                    notificationService.sendNotification(deviceToken,"건팡","아직 오늘의 아침 식사 기록이 없어요! \n 아침 식사 기록을 하고 건팡이에게 밥을 줘보는 건 어떨까요?");
        }
    }
    @Scheduled(cron = "0 0 15 * * *", zone = "Asia/Seoul")
    public void notificationLunch() throws IOException {
        logger.debug("notificationLunch");
        List<User> users = userRepository.findAll();

        for (int i=0;i<users.size();i++){
            logger.debug("user[i] : " + users.get(i).toString());
            Optional<DailyRecord> dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(users.get(i).getId(), LocalDate.now());

            String deviceToken = notificationRepository.findTokenByUserId(users.get(i).getId()).getToken();
            //얘는 안찍힘
            if (deviceToken == null)
                logger.debug(String.valueOf("deviceToken is null" + deviceToken == null));
            logger.debug("deviceToken : " + deviceToken.length());
            if (dailyRecord.isPresent())
                if (dailyRecord.get().getLunchFoodType() == FoodType.NOT_RECORD ||dailyRecord.get().getLunchFoodType() == null)
                    notificationService.sendNotification(deviceToken,"건팡","아직 오늘의 점심 식사 기록이 없어요! \n 점심 식사 기록을 하고 건팡이에게 밥을 줘보는 건 어떨까요?");
        }
    }
    @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Seoul")
    public void notificationDinner() throws IOException {
        logger.debug("notificationDinner");
        List<User> users = userRepository.findAll();

        for (int i=0;i<users.size();i++){
            logger.debug("user[i] : " + users.get(i).toString());
            Optional<DailyRecord> dailyRecord = dailyRecordRepository.getDailyRecordOnTodayByUserId(users.get(i).getId(), LocalDate.now());

            String deviceToken = notificationRepository.findTokenByUserId(users.get(i).getId()).getToken();
            //얘는 안찍힘
            if (deviceToken == null)
                logger.debug(String.valueOf("deviceToken is null" + deviceToken == null));
            logger.debug("deviceToken : " + deviceToken.length());
            if (dailyRecord.isPresent())
                if (dailyRecord.get().getDinnerFoodType() == FoodType.NOT_RECORD ||dailyRecord.get().getDinnerFoodType() == null)
                    notificationService.sendNotification(deviceToken,"건팡","아직 오늘의 저녁 식사 기록이 없어요! \n 저녁 식사 기록을 하고 건팡이에게 밥을 줘보는 건 어떨까요?");
        }
    }

}
