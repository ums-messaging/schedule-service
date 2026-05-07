package com.ums.schedule.fixture;

import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.application.target.dto.TargetDataTransfer;
import com.ums.schedule.code.send.TargetColumnEnum;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;

import java.util.Map;
import java.util.UUID;

public class SendTargetDomainFixture {

    public static SendTarget createSendTarget(TargetUpload targetUpload) {
        SendTargetDto targetDto = SendTargetDto.of(SendTargetDomainFixture.createTargetDto());
        SendTarget sendTarget = SendTarget.of(targetUpload, targetDto, new FakeTemplate());
        return sendTarget;
    }

    public static SendTarget createSendTarget() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        return SendTargetDomainFixture.createSendTarget(targetUpload);
    }


    public static FakeSendTarget createTargetDto() {
        return new FakeSendTarget();
    }


    static class FakeSendTarget implements TargetDataTransfer {
        private final Map<TargetColumnEnum, String> paramMap;

        public FakeSendTarget() {
            this.paramMap = Map.of(
                    TargetColumnEnum.TARGET_KEY, UUID.randomUUID().toString(),
                    TargetColumnEnum.TARGET_EMAIL, "jang314@naver.com",
                    TargetColumnEnum.TARGET_NAME, "jang"
            );
        }

        public FakeSendTarget(Map<TargetColumnEnum, String> paramMap) {
            this.paramMap = paramMap;
        }

        @Override
        public Map<String, Object> extractMessageVariable() {
            return Map.of("serial_no", UUID.randomUUID().toString());
        }

        @Override
        public Map<TargetColumnEnum, String> resolveTargetData() {
            return this.paramMap;
        }
    }
}
