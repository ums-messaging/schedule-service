package com.ums.schedule.email.adaptor.out.dns;

import com.ums.schedule.email.adaptor.out.dns.model.DnsQueryResult;

public interface DnsClient {
    DnsQueryResult getDomainInfo(String domainName);
}
