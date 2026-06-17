package com.ums.schedule.adapter.api.email.dns;

public interface DnsClient {
    DnsQueryResult getDomainInfo(String domainName);
}
