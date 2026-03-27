package com.ums.schedule.send.domain.reporing;

import lombok.Getter;

@Getter
public class SendReport {
    private Long totalCount;
    private Long successCount;
    private Long failCount;

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
}
