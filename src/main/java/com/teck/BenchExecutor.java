package com.teck;

import lombok.var;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class BenchExecutor extends ThreadPoolExecutor {
    private ExecuteInfo executeInfo;
    RestTemplate restTemplate;

    public BenchExecutor(ExecuteInfo executeInfo) {
        super(executeInfo.concurrency, executeInfo.concurrency,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        restTemplate = new RestTemplate(requestFactory);
        this.executeInfo = executeInfo;
    }


    public void execute() {

        for (Integer i = 0; i < executeInfo.requestCount; i++) {
            this.execute(() -> {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();

                try {

                    var res = restTemplate.exchange(executeInfo.url, HttpMethod.GET, null, String.class);
                    stopWatch.stop();

                    synchronized (this) {
                        executeInfo.requestTimes.add(stopWatch.getTotalTimeMillis());

                        debug(res::getBody);

                        if (!res.getStatusCode().is2xxSuccessful()
                                || !StringUtils.isEmpty(executeInfo.checkContent)
                                && !Objects.requireNonNull(res.getBody()).contains(executeInfo.checkContent)) {
                            executeInfo.errorCount++;
                        }
                    }
                } catch (Exception ex) {
                    debug(ex::toString);
                    synchronized (this) {
                        executeInfo.errorCount++;
                    }
                } finally {
                    synchronized (this) {
                        executeInfo.completeCount++;
                    }
                }
            });
        }

    }

    private void debug(Supplier provideInfo) {
        if (executeInfo.debug) {
            System.out.println(new Date().toLocaleString() + " " + provideInfo.get());
        }
    }


    public ExecuteInfo statistic() {
        return executeInfo;
    }


    public int getCompletedRate() {
        return 100 * executeInfo.completeCount / executeInfo.requestCount;
    }
}
