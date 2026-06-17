package com.ums.schedule.adapter.api.email.smtp;


import com.ums.schedule.adapter.api.email.dns.DnsClient;
import com.ums.schedule.adapter.api.email.dns.DnsQuery;
import com.ums.schedule.adapter.api.email.dns.DnsQueryResult;
import com.ums.schedule.adapter.api.email.smtp.response.SmtpSessionInfo;
import com.ums.schedule.domain.send.email.code.DnsQueryResultEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SmtpHelper {
    private final DnsClient dnsClient;

    public SmtpSessionInfo createSession(String domain) throws IOException {
        DnsQueryResult domainInfo = dnsClient.getDomainInfo(domain);
        List<DnsQuery> queryList = getQueryList(domainInfo.queries());

        SmtpSessionInfo session = SmtpSessionInfo.of(queryList, "localhost");

        return session;
    }

    private static List<DnsQuery> getQueryList(List<DnsQuery> queries) {
        return queries.stream()
                .filter(query -> query.result() == DnsQueryResultEnum.SUCCESS)
                .collect(Collectors.toList());
    }
}
