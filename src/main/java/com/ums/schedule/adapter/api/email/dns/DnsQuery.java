package com.ums.schedule.adapter.api.email.dns;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.DnsQueryResult;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;

import java.util.Arrays;
import java.util.List;

public record DnsQuery(String record, DnsQueryResult result, String[] bindIp) {

    public static DnsQuery of(Record record, List<EnumMapperValue> results, Lookup lookupA) {
        Record[] records = lookupA.getAnswers();
        DnsQueryResult resultEnum = results.stream()
                .filter(result -> result.value().equals(String.valueOf(lookupA.getResult())))
                .map(result -> DnsQueryResult.valueOf(result.code()))
                .findFirst()
                .orElseThrow();
        return new DnsQuery(record.getAdditionalName().toString(), resultEnum, Arrays.stream(records)
                .map(r -> r.rdataToString()).toArray(String[]::new));
    }
}
