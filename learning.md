- [x] 表示掌握
- [ ] 表示未完全掌握

### 模块
#### 1.metrics-core 
主要需要掌握：五种度量，MetricRegistry
- [x] Metrics 
    - 一个空接口，表示Metrics

- [ ] Gauge 瞬时值
    - [x] CachedGauge
    - [x] DerivativeGauge
        - 注意区分传递对象(F)和被传递对象(T)
    - [x] RatioGauge
    - [ ] JmxAttributeGauge
        - 了解基本思路，但是不了解JMX
        
- [ ] Striped64
- [ ] LongAdder
- [x] Counting
- [ ] Counter 计数器
    - 有关LongAdder的内容不了解
   
- [ ] Meter 仪表盘
    - [x] Metered
    - [ ] EWMA
        - tick？
  
- [ ] ThreadLocalRandom   
- [ ] Histogram 直方图  
    - [x] Sampling
    - [x] Snapshot
    - [ ] Reservoir
        - [x] UniformReservoir  
        - [x] SlidingWindowReservoir
        - [ ] SlidingTimeWindowReservoir
            - trim()
            - ConcurrentSkipListMap
        - [ ] ExponentiallyDecayingReservoir
            - rescale()
            
- [x] Timer 
- [x] MetricRegistry
    - [x] MetricSet
        - [x] JvmAttributeGaugeSet
    - [x] MetricRegistryListener
    - [x] MetricFilter
    - [x] SharedMetricRegistries
    
- [x] ScheduledReporter
    - [x] Slf4jReporter
    - [x] CsvReporter
    - [x] ConsoleReporter
    
- [ ] JmxReporter

#### 2.metrics-servlets
metrics的一些servlet
- [x] AdminServlet
    - [x] HealthCheckServlet
    - [x] MetricsServlet
    - [x] PingServlet
    - [x] ThreadDumpServlet
        - Servlet不了解

#### 3.metrics-json
自定义的序列化方法
- [x] HealthCheckModule
- [x] MetricsModule
    - Jackson不了解

#### 4.metrics-healthchecks
定义了一个健康检查类以及注册表
- [x] HealthCheck
    - [x] ThreadDeadlockHealthCheck
    - [x] HealthCheckRegistry
    
#### 5.metrics-annotation
定义了四个注解，四个注解够可以标识一个方法（或一个字段）为一个度量
- [x] Gauge
- [x] Metered
- [x] Timed
- [x] ExceptionMetered

#### 6.metrics-benchmarks
三个微基准测试，使用的谷歌Caliper框架（TODO，可以暂时忽略）<br>
Caliper:  <https://github.com/google/caliper>
- [ ] CounterBenchmark
- [ ] MeterBenchmark
- [ ] ReservoirBenchmark

#### 7.metrics-ehcache
拓展ehcache，对缓存命中、大小等数据进行监控统计<br>
使用ehcache框架
- [x] InstrumentedCacheDecoratorFactory
- [x] InstrumentedEhcache

#### 8.metrics-ganglia
一个ScheduledReporter实现类，向Ganglia集群投递Metrics数据<br>
Ganglia: <https://developer.aliyun.com/article/27949>
- [x] GangliaReporter

#### 9.metrics-graphite
一个ScheduledReporter实现类，向Graphite服务器投递Metrics数据<br>
Graphite: <https://yumminhuang.github.io/post/graphiteandgrafana/>
- [x] Graphite
- [x] GraphiteReporter

#### 10.metrics-httpclient
扩展httpclient，对请求耗时、当前连接数、最大连接数等数据进行监控统计<br>
并提供几种http请求监控命名格式
- [x] HttpClientMetricNameStrategy
    - [x] HttpClientMetricNameStrategies
- [x] InstrumentedHttpClient
- [x] InstrumentedClientConnManager
- [x] InstrumentedRequestDirector

#### 11.metrics-jdbi
扩展jdbi，提供sql操作的计时器（timer）<br>
并提供了一些命名策略
- [x] StatementNameStrategy
    - [x] DelegatingStatementNameStrategy
        - [x] BasicSqlNameStrategy
        - [x] ContextNameStrategy
        - [x] NaiveNameStrategy
        - [x] ShortNameStrategy
        - [x] SmartNameStrategy
- [x] NameStrategies
- [x] InstrumentedTimingCollector

#### 12.metrics-jersey
jersey是一个RESTFUL请求服务JAVA框架<br>
扩展jersey，对资源方法调度进行监控统计
- [x] InstrumentedResourceMethodDispatchProvider
- [x] InstrumentedResourceMethodDispatchAdapter

#### 13.metrics-jetty
jetty是一个servlet引擎<br>
扩展jetty，统计如请求数据、响应数据、连接数据、线程数据等

##### metrics-jetty8
- [x] InstrumentedQueuedThreadPool
- [x] InstrumentedHandler
- [x] InstrumentedBlockingChannelConnector
- [x] InstrumentedSslSocketConnector
- [x] InstrumentedSslSelectChannelConnector
- [x] InstrumentedSocketConnector
- [x] InstrumentedBlockingChannelConnector
##### metrics-jetty9
- [x] InstrumentedHandler
- [x] InstrumentedQueuedThreadPool
- [x] InstrumentedConnectionFactory

#### 14.metrics-servlet
对servlet请求和相应进行捕获和统计
- [x] AbstractInstrumentedFilter
    - [x] InstrumentedFilter
- [x] InstrumentedFilterContextListener

#### 15.metrics-logback
对logback进行扩展，增加了六个数据统计
- [x] InstrumentedAppender

#### 16.metrics-log4j
对log4j进行扩展，增加了六个数据统计
- [x] InstrumentedAppender

#### 17.metrics-jvm
- [x] ThreadDump
- [x] ThreadDeadlockDetector
- [x] MemoryUsageGaugeSet
- [x] GarbageCollectorMetricSet
- [x] FileDescriptorRatioGauge
- [x] BufferPoolMetricSet
- [x] ThreadStatesGaugeSet