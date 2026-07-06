package com.ums.schedule.application.sendrequest.template.email;

import com.ums.schedule.adapter.api.request.request.EmailSendCreateRequest;
import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.message.email.result.EmailContentResult;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTemplateDetailResult;
import com.ums.schedule.application.sendrequest.message.email.result.EmailTemplateResult;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailTemplateS3Service implements EmailTemplateService {
    private final AwsS3Repository fileRepository;

    @Override
    public EmailTemplateResult findTemplate(String templateKey, EmailSendCreateRequest request) {
        EmailTemplateDetailResult detail = createTemplateResponse(templateKey, request);
        return EmailTemplateResult.of(detail);
    }
    private EmailTemplateDetailResult createTemplateResponse(String templateKey, EmailSendCreateRequest request) {
        String header = "/template/{customerId}/"+templateKey+"/"+ EmailTemplateSectionEnum.HEADER.value();
        String body = "/template/{customerId}/"+templateKey+"/"+EmailTemplateSectionEnum.BODY.value();
        String footer = "/template/{customerId}/"+templateKey+"/"+EmailTemplateSectionEnum.FOOTER.value();
        String cover = "/template/{customerId}/"+templateKey+"/"+EmailTemplateSectionEnum.COVER.value();

        List<EmailContentResult> contents = Map.of(
                        EmailTemplateSectionEnum.HEADER, header,
                        EmailTemplateSectionEnum.BODY, body,
                        EmailTemplateSectionEnum.FOOTER, footer,
                        EmailTemplateSectionEnum.COVER, cover
                ).entrySet().stream()
                .map(entry -> {
                    AwsS3FileMetadataResponse metadata = fileRepository.getFileMetadata(entry.getValue());
                    return Optional.ofNullable(metadata)
                            .map(m -> EmailContentResult.of(entry.getKey(), m))
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        return EmailTemplateDetailResult.of(templateKey, request, contents);
    }
}
