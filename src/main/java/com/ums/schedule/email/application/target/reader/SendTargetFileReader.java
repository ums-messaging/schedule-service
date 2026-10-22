package com.ums.schedule.email.application.target.reader;

import com.alibaba.excel.EasyExcel;
import com.ums.schedule.email.application.requests.EmailSendRequestReportService;
import com.ums.schedule.orchestrator.common.code.UploadType;
import com.ums.schedule.email.application.target.reader.listener.TargetFileReadListener;
import com.ums.schedule.email.application.target.reader.model.SendTargetReadContext;
import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
@Component
@RequiredArgsConstructor
public class SendTargetFileReader implements SendTargetReader {

    @Override
    public boolean supports(EnumMapperValue type) {
        return UploadType.FILE == UploadType.valueOf(type.code());
    }

    public SendTargetsReadResult read(String downloadUrl, SendTargetReadContext context) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(downloadUrl))
                .GET()
                .build();

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("request-"+context.getRequestId() + "-", ".xlsx");
            long startMs = System.currentTimeMillis();

            HttpResponse<Path> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofFile(tempFile)
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException(
                        "엑셀 다운로드 실패: HTTP " + response.statusCode()
                );
            }

            long endMs = System.currentTimeMillis();

            log.info("파일 읽는 시간 : {}", endMs - startMs);
            startMs = System.currentTimeMillis();

            TargetFileReadListener listener = new TargetFileReadListener(context);

            EasyExcel.read(tempFile.toFile(), listener)
                    .sheet()
                    .doRead();

            endMs = System.currentTimeMillis();
            log.info("엑셀 파일 처리 시간 : {} ", endMs - startMs);
        } catch (IOException | InterruptedException e) {
            log.error("error", e);
        } finally {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SendTargetsReadResult.of(context);
    }
}
