package com.ums.schedule.orchestrator.adaptor.out.persistance.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.QSendRequestUploadEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestUploadEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository.SendRequestUploadQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class SendRequestUploadQueryRepositoryImpl implements SendRequestUploadQueryRepository {
    private final JPAQueryFactory queryFactory;


}
