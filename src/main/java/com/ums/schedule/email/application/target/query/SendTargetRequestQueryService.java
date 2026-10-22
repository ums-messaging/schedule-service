package com.ums.schedule.email.application.target.query;

import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.SendTargetRequestQueryRepository;
import com.ums.schedule.email.application.target.query.model.SendTargetGroupQuery;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SendTargetRequestQueryService {
    private final SendTargetRequestQueryRepository repository;



}
