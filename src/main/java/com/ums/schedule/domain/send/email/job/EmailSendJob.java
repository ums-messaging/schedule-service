package com.ums.schedule.domain.send.email.job;

import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record EmailSendJob(
        SendJob job,
        String mailFrom,
        String mailFromName,
        String convertType,
        String imageDir,
        String elho,
        String topic
) {
    public static EmailSendJob of(EmailAttachment attachment, String elho) {
        return null;
    }

    public String getDomain(TargetMessage target) {
        return null;
    }

    public List<DomainGroupEntry> createDomainGroup(Map<String, DomainGroup> groupMap, List<List<TargetMessage>> targetList) {
        List<DomainGroupEntry> entryAllList = new ArrayList<>();
        List<Map<String, List<TargetMessage>>> targetMapList = groupedTargets(targetList);
        for (Map<String, List<TargetMessage>> stringListMap : targetMapList) {
            List<DomainGroupEntry> entryList = getGroupEntryList(groupMap, stringListMap);
            entryAllList.addAll(entryList);
        }
        return entryAllList;
    }

    private List<DomainGroupEntry> getGroupEntryList(Map<String, DomainGroup> groupMap, Map<String, List<TargetMessage>> targetListMap) {
        return targetListMap.entrySet()
                .stream()
                .map(entry -> getDomainGroupEntry(groupMap, entry.getKey(), entry.getValue()))
                .toList();
    }

    private DomainGroupEntry getDomainGroupEntry(Map<String, DomainGroup> groupMap, String domain, List<TargetMessage> targetList) {
        DomainGroup domainGroup = groupMap.getOrDefault(domain, DomainGroup.create(this, domain));
        List<DomainGroupTarget> groupList = targetList.stream().map(target -> DomainGroupTarget.of(domainGroup, target))
                .toList();
        return new DomainGroupEntry(job.jobId(), domainGroup.groupId(), groupList);
    }

    public List<Map<String, List<TargetMessage>>> groupedTargets(List<List<TargetMessage>> targetList) {
        return targetList.stream()
                .map(list ->
                    groupingTarget(list)
                )
                .toList();
    }

    private Map<String, List<TargetMessage>> groupingTarget(List<TargetMessage> list) {
        return list.stream()
                .collect(Collectors.groupingBy(this::getDomain));
    }

}
