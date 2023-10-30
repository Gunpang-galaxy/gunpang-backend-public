package com.galaxy.gunpang.bodyComposition.service;

import com.galaxy.gunpang.bodyComposition.BodyCompositionController;
import com.galaxy.gunpang.bodyComposition.model.BodyComposition;
import com.galaxy.gunpang.bodyComposition.repository.BodyCompositionRepository;
import com.galaxy.gunpang.user.exception.UserNotFoundException;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BodyCompositionServiceImpl implements BodyCompositionService{

    private static final Logger logger = LoggerFactory.getLogger(BodyCompositionService.class);
    private final BodyCompositionRepository bodyCompositionRepository;
    private final UserRepository userRepository;
    @Override
    public void setBodyCompositionsWithHealthApi(Long userId, float weight, float muscleMass, float fatMass, float bodyWaterMass) {
        logger.debug("service");
        //체지방률
        float fatMassPct = ( fatMass / weight ) * 100;
        //만약 유저가 없으면 에러
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        BodyComposition bodyComposition = new BodyComposition(user,weight,muscleMass,fatMass,fatMassPct,bodyWaterMass);
        bodyCompositionRepository.save(bodyComposition);

    }
}
