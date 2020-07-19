# TBench

Bench tool made by Teck

## v1 stand-alone 2020-07-19

support custom url,concurrency,request count

UseAge: 
```
>> java -jar TBench.jar -u {url} -q {requestCount} -c {concurrency}
```


```
> java -jar  TBench-1.0-SNAPSHOT-jar-with-dependencies.jar -u https://www.baidu.com -r 1000 -c 100
https://www.baidu.com
0%  0/0/1000 254ms
0%  0/0/1000 355ms
0%  0/0/1000 460ms
0%  0/0/1000 565ms
0%  0/0/1000 665ms
0%  0/0/1000 766ms
0%  6/0/1000 866ms
1%  18/0/1000 970ms
6%  62/0/1000 1071ms
6%  69/0/1000 1175ms
8%  89/0/1000 1278ms
9%  91/0/1000 1381ms
10%  100/0/1000 1485ms
11%  112/0/1000 1588ms
13%  133/0/1000 1690ms
14%  145/0/1000 1794ms
17%  178/0/1000 1898ms
19%  196/0/1000 1999ms
20%  207/0/1000 2100ms
22%  220/0/1000 2200ms
24%  242/0/1000 2305ms
26%  265/0/1000 2408ms
28%  286/0/1000 2513ms
30%  304/0/1000 2615ms
32%  321/0/1000 2715ms
48%  486/0/1000 2817ms
61%  614/0/1000 2920ms
77%  776/0/1000 3022ms
92%  928/0/1000 3126ms
97%  972/0/1000 3229ms
97%  976/0/1000 3334ms
97%  977/0/1000 3438ms
99%  996/0/1000 3542ms
99%  998/0/1000 3645ms
99%  999/0/1000 3747ms
99%  999/0/1000 3848ms
99%  999/0/1000 3949ms
99%  999/0/1000 4050ms
99%  999/0/1000 4155ms
99%  999/0/1000 4256ms
99%  999/0/1000 4360ms
99%  999/0/1000 4465ms
100%  1000/0/1000 4506ms

total cost:     4506ms
concurrency:    100
total request:  1000
total error:    0
fail rate:      0.0%
max time:       4364ms
min time:       11ms
avg time:       306.312ms
total time:     306312ms
95% response time:      888ms
qps:    326.4645198359842


```


## v2 cluster version
support culster

