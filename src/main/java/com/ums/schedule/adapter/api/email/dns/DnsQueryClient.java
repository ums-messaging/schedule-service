package com.ums.schedule.adapter.api.email.dns;

import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.EmailCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DnsQueryClient implements DnsClient {
    private final EnumMapperFactory factory;
    private final String DNS_SERVER_IP = "168.126.63.1";

    @Override
    public DnsQueryResult getDomainInfo(String domainName) {
        List<EnumMapperValue> enumMapperList = factory.findEnumMapperList(EmailCode.DNS_QUERY_RESULT);
        List<DnsQuery> queries = new ArrayList<>();
        try {
            Lookup lookup = queryLookup(domainName, Type.MX);
            Record[] recordMX = lookup.getAnswers();
            queries = Arrays.stream(recordMX)
                    .map(record -> queryARecord(record, enumMapperList, Type.A))
                    .toList();
            return DnsQueryResult.of(domainName, enumMapperList, queries);
        } catch (TextParseException | UnknownHostException e) {
            log.error(e.getLocalizedMessage());
        }
        return DnsQueryResult.of(domainName, enumMapperList, queries);
    }

    private DnsQuery queryARecord(Record record, List<EnumMapperValue> enumMapperList, int type) {
        try {
            Lookup lookupA = queryLookup(record.getAdditionalName().toString(), Type.A);
            return DnsQuery.of(record, enumMapperList, lookupA);
        } catch (TextParseException | UnknownHostException e) {
            return DnsQuery.of(record, enumMapperList, null);
        }
    }

    private Lookup queryLookup(String domainName, int type) throws TextParseException, UnknownHostException {
        Lookup lookup = new Lookup(domainName, type);
        lookup.setResolver(new SimpleResolver(DNS_SERVER_IP));
        lookup.run();
        return lookup;
    }
}
