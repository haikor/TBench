package com.teck;


import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;

public class ExecuteInfo {
    public List<Long> requestTimes = new ArrayList<>();
    public int completeCount;
    public int errorCount;
    public Integer requestCount;
    public int concurrency;
    public String url;
    public String checkContent;
    public Boolean debug;


    @Override
    public String toString() {
        if (completeCount == 0) {
            return "no query";
        }
        double failRate = 100.0 * errorCount / completeCount;
        long ninetyFiveResposetime = 0L;
        if (requestTimes.size() > 0) {
            requestTimes.sort(Long::compare);
            ninetyFiveResposetime = requestTimes.get((int) (requestTimes.size() * 0.95));
        }
        LongSummaryStatistics summary = requestTimes.stream().mapToLong(e -> e).summaryStatistics();

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
