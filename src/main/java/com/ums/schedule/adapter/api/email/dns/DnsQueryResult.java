package com.ums.schedule.adapter.api.email.dns;

import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.List;

public record DnsQueryResult(
        String domainName,
        com.ums.schedule.common.code.email.DnsQueryResult result,
        List<DnsQuery> queries
) {

    public static DnsQueryResult of(String domainName, List<EnumMapperValue> results, List<DnsQuery> dnsQueryList) {
        com.ums.schedule.common.code.email.DnsQueryResult resultEnum = results.stream()
                .map(result -> com.ums.schedule.common.code.email.DnsQueryResult.valueOf(result.code()))
                .findFirst().orElseThrow();
        return new DnsQueryResult(domainName, resultEnum, dnsQueryList);
    }
}
