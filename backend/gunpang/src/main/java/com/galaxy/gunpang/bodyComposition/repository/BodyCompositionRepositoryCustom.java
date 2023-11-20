package com.galaxy.gunpang.bodyComposition.repository;

import com.galaxy.gunpang.bodyComposition.model.BodyComposition;
import com.galaxy.gunpang.user.model.User;
import java.util.List;
import java.util.Optional;

public interface BodyCompositionRepositoryCustom {
    Optional<List<BodyComposition>> getRecentTwoBodyCompositionByUser(User user);
}
