package com.teck;


import lombok.var;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("h", "host", true, "Url to bench");
        options.addOption("r", "request", true, "Request count");
        options.addOption("c", "concurrency", true, "Concurrency of cli");
        CommandLine commandLine = parser.parse(options, args);
        if (!commandLine.hasOption("h")) {
            System.out.println("UseAge: -h {host} -q {requestCount} -c {concurrency}");
            return;
        }

        var url = Optional.ofNullable(commandLine.getOptionValue("h")).orElse("https://www.baidu.com");
        var requestCount = Integer.valueOf(Optional.ofNullable(commandLine.getOptionValue("r")).orElse("1"));
        var concurrency = Integer.valueOf(Optional.ofNullable(commandLine.getOptionValue("c")).orElse("10"));

        System.out.println(url);

        var begin = System.currentTimeMillis();
        BenchExecutor benchExecutetor = new BenchExecutor(concurrency);
        benchExecutetor.execute(url, requestCount);

        benchExecutetor.shutdown();
        while (!benchExecutetor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
            printProcess(begin, benchExecutetor);
        }
        printProcess(begin, benchExecutetor);


        var statistic = benchExecutetor.statistic();

        System.out.println("\ntotal cost:\t" + (System.currentTimeMillis() - begin) + "ms");
        System.out.println(statistic.toString());


    }

    private static void printProcess(long begin, BenchExecutor benchExecutetor) {
        System.out.println(benchExecutetor.getCompletedRate() + "% "
                + " " + benchExecutetor.statistic().completeCount + "/"
                + benchExecutetor.statistic().errorCount + "/"
                + benchExecutetor.statistic().requestCount
                + " " + (System.currentTimeMillis() - begin) + "ms"

        );
    }
}
