package com.ums.schedule.email.adaptor.out.dns.model;

import com.ums.schedule.common.code.email.DnsQueryResultCode;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import java.util.List;

public record DnsQueryResult(
        String domainName,
        DnsQueryResultCode result,
        List<DnsQuery> queries
) {

    public static DnsQueryResult of(String domainName, List<EnumMapperValue> results, List<DnsQuery> dnsQueryList) {
//        DnsQueryResultCode resultEnum = results.stream()
//                .map(result -> DnsQueryResultCode.valueOf(result.code()))
//                .findFirst().orElseThrow();
        return new DnsQueryResult(domainName, DnsQueryResultCode.SUCCESS, dnsQueryList);
    }
}
