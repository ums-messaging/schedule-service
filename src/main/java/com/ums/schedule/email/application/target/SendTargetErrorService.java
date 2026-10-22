package com.ums.schedule.email.application.target;

import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendTargetErrorService {
    private final SendTargetErrorRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(List<SendTargetError> errorList) {
        repository.saveAll(errorList);
    }
}
