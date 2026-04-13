package com.ums.schedule.domain.target;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
public class SendReport {
    private Long totalCount;
    private Long successCount;
    private Long failCount;
    @OneToMany
    private List<SendTarget> failTargetList = new ArrayList<>();

    public static SendReport of(int totalCount) {
        Long parseTotalCount = Long.parseLong(String.valueOf(totalCount));
        return new SendReport(parseTotalCount);
    }

    // totalCount는 외부에서 select 카운트 해서 해야하는데, 대상자 다 돌고 해야함.대량은

    private SendReport(Long totalCount) {
        this.totalCount = totalCount;
        this.successCount = 0L;
        this.failCount = 0L;
    }

    public Long increaseSuccessCount(int targetSize) {
        AtomicLong getSuccessCount = new AtomicLong(this.successCount);
        getSuccessCount.addAndGet(targetSize);
        this.successCount = getSuccessCount.get();
        return this.successCount;
    }

    public SendTarget reportUploadFailTarget(SendTarget target, String errMessage) {
        AtomicLong failCount = new AtomicLong(this.failCount);
//        target.toError(TargetError.ofErrorMessage(errMessage));
        this.failTargetList.add(target);
        this.failCount = failCount.incrementAndGet();
        return target;
    }

}
