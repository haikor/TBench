package com.teck;

import lombok.var;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

public class BenchExecutor extends ThreadPoolExecutor {
    private ExecuteInfo executeInfo = new ExecuteInfo();
    RestTemplate restTemplate;


    public BenchExecutor(int concurrency) {
        super(concurrency, concurrency,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        this.executeInfo.concurrency = concurrency;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //60s
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        restTemplate = new RestTemplate(requestFactory);
    }

    public void execute(String url, Integer requestCount) {
        this.executeInfo.requestCount = requestCount;

        for (Integer i = 0; i < requestCount; i++) {
            this.execute(() -> {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();

                try {

                    var res = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
                    stopWatch.stop();

                    synchronized (this) {
                        executeInfo.requestTimes.add(stopWatch.getTotalTimeMillis());

                        if (!res.getStatusCode().is2xxSuccessful()) {
                            executeInfo.errorCount++;
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
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

    public ExecuteInfo statistic() {
        return executeInfo;
    }


    public int getCompletedRate() {
        return 100 * executeInfo.completeCount / executeInfo.requestCount;
    }
}
