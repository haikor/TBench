package com.teck;

import lombok.var;

import java.util.ArrayList;
import java.util.List;

public class ExecuteInfo {
    public List<Long> requestTimes = new ArrayList<>();
    public int completeCount;
    public int errorCount;
    public Integer requestCount;
    public int concurrency;


    @Override
    public String toString() {
        if (completeCount == 0) {
            return "no query";
        }
        var failRate = 100.0 * errorCount / completeCount;
        requestTimes.sort(Long::compare);
        var ninetyFiveResposetime = requestTimes.get((int) (requestTimes.size() * 0.95));
        var summary = requestTimes.stream().mapToLong(e -> e).summaryStatistics();

        return String.format(
                "concurrency:\t%s\n" +
                        "total request:\t%s\n" +
                        "total error:\t%s\n" +
                        "fail rate:\t%s%%\n" +
                        "max time:\t%sms\n" +
                        "min time:\t%sms\n" +
                        "avg time:\t%sms\n" +
                        "total time:\t%sms\n" +
                        "95%% response time:\t%sms\n" +
                        "qps:\t%s\n",
                concurrency,
                completeCount,
                errorCount,
                failRate,
                summary.getMax(), summary.getMin(), summary.getAverage(), summary.getSum(),
                ninetyFiveResposetime,
                1.0 * concurrency / summary.getAverage() * 1000
        );
    }
}
