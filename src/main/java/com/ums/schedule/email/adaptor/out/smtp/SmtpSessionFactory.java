package com.ums.schedule.email.adaptor.out.smtp;


import com.ums.schedule.email.adaptor.out.dns.DnsClient;
import com.ums.schedule.email.adaptor.out.dns.model.DnsQuery;
import com.ums.schedule.email.adaptor.out.dns.model.DnsQueryResult;
import com.ums.schedule.email.adaptor.out.smtp.model.SmtpSessionInfo;
import com.ums.schedule.common.code.email.DnsQueryResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SmtpSessionFactory implements SendSessionFactory {
    private final DnsClient dnsClient;

    public SmtpSessionInfo createSession(String domain) {
//        DnsQueryResult domainInfo = dnsClient.getDomainInfo(domain);
//        List<DnsQuery> queryList = getQueryList(domainInfo.queries());
        SmtpSessionInfo session = null;
//        for (DnsQuery query : queryList) {
//            String[] ips = query.bindIp();
//            for (String ip : ips) {
//                Socket socket = null;
//                try {
//                    socket = new Socket(ip, 8986);
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    OutputStream os = socket.getOutputStream();
                    session = new SmtpSessionInfo(new SendReader(), new SendWriter());
                    session.queryHelo("localhost");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            if(session != null) break;
//        }
        return Optional.ofNullable(session)
                .orElseThrow(() -> new RuntimeException("[600] DNS Query Error."));
    }

    private static List<DnsQuery> getQueryList(List<DnsQuery> queries) {
        return queries.stream()
                .filter(query -> query.result() == DnsQueryResultCode.SUCCESS)
                .collect(Collectors.toList());
    }
}
