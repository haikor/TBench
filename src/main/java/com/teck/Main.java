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
        options.addOption("u", "url", true, "Url to bench");
        options.addOption("r", "request", true, "Request count");
        options.addOption("c", "concurrency", true, "Concurrency of cli");
        options.addOption("cc", "check-content", true, "check return data to insure response is ok");
        options.addOption("d", "debug", false, "print debug info");


        CommandLine commandLine = parser.parse(options, args);
        if (!commandLine.hasOption("u")) {
            System.out.println("UseAge: -u {url} -q {requestCount} -c {concurrency}");
            return;
        }

        var u = Optional.ofNullable(commandLine.getOptionValue("u")).orElse("https://www.baidu.com");
        var r = Integer.valueOf(Optional.ofNullable(commandLine.getOptionValue("r")).orElse("1"));
        var c = Integer.valueOf(Optional.ofNullable(commandLine.getOptionValue("c")).orElse("10"));
        var cc = Optional.ofNullable(commandLine.getOptionValue("cc")).orElse("");
        var d = commandLine.hasOption("d");

        var begin = System.currentTimeMillis();
        BenchExecutor benchExecutetor = new BenchExecutor(new ExecuteInfo() {{
            this.url = u;
            this.requestCount = r;
            this.concurrency = c;
            this.checkContent = cc;
            this.debug = d;
        }});

        System.out.println("start bench:" + u);

        benchExecutetor.execute();

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
