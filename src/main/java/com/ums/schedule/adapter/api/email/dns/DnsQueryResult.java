package com.ums.schedule.adapter.api.email.dns;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.send.email.code.DnsQueryResultEnum;

import java.util.List;

public record DnsQueryResult(
        String domainName,
        DnsQueryResultEnum result,
        List<DnsQuery> queries
) {

    public static DnsQueryResult of(String domainName, List<EnumMapperValue> results, List<DnsQuery> dnsQueryList) {
        DnsQueryResultEnum resultEnum = results.stream()
                .map(result -> DnsQueryResultEnum.valueOf(result.code()))
                .findFirst().orElseThrow();
        return new DnsQueryResult(domainName, resultEnum, dnsQueryList);
    }
}
