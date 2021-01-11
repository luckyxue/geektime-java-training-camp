# GeekTime Java Training Camp Course

Fork From https://github.com/JavaCourse00/JavaCourseCodes

感谢 kimmking 老师的分享

# 极客时间Java进阶训练营目录

## 第一周：JVM 进阶 - Java开发者大厂面试必知必会

1. JVM 基础知识、Java 字节码技术、JVM 类加载器、JVM 内存模型、JVM 启动参数详解；
2. JDK 内置命令行工具、JDK 内置图形界面工具、JDWP 简介、JMX 与相关工具；
3. 常见的 JVM GC 算法（Parallel GC/CMS GC/G1 GC）基本原理和特点；
4. 新一代 GC 算法（Java11 ZGC/Java12 Shenandoah) 和 Oracle GraalVM；
5. GC 日志解读与分析、JVM 的线程堆栈等数据分析、内存 dump 和内存分析工具；
6. fastThread 相关工具以及面临复杂问题时的几个高级工具的使用；
7. JVM 问题排查分析的常用手段、性能调优的最佳实践经验等；
8. JVM 相关的常见面试问题必知必会、全面分析。

## 第二周：NIO 技术 - 构建高吞吐服务器的终极武器

1. 同步/异步、阻塞/非阻塞、BIO、NIO、AIO、Reactor/Proactor；
2. ByteBuff/Acceptor/Channel/Handler、NioEventLoopGroup/EventLoop、bossGroup/workerGroup；
3. Netty 的启动和执行过程、线程模型、事件驱动、服务端和客户端的使用方式；
4. 常见的 API Gateway/HTTP Server、SEDA 原理、业务 API 网关的功能和结构；
5. Throughout/TPS/QPS、Latency/P99/P95/P90、ApacheBench/Wrk/JMeter/LoadRunner。

## 第三周：并发编程 - 多核处理器时代高性能的秘诀

1. Java 多线程基础：线程、锁、synchronized、volatile/final、sleep/await/notify/fork/join；
2. Java 并发包基础：线程池 Executor、AQS/CAS、Atomic 原子操作、Lock/ReadWriteLock/Condition、Callable/Future；
3. Java 并发容器与工具：BlockingQueue/CopyOnWriteList/ConcurrentHashMap、CountDownLatch/CyclicBarrier/Semaphore等；
4. 其他：万金油 ThreadLocal，化繁为简 Java8 parallelStream 等。

## 第四周：开发框架 - 深入理解 Spring 等主流框架思想

1. Spring 技术体系（Spring Core/Web/MVC/Data/Messaging、Spring Boot 等）；
2. ORM 技术体系（JPA、Hibernate、MyBatis 等）。

## 第五周：系统性能优化 - 学会性能分析与 MySQL 优化

1. 系统可观测性（日志、调用链跟踪、指标度量），80/20 优化原则，CPU、内存、磁盘/网络 IO 等分析；
2. MySQL 的锁、事务、索引、并发级别、死锁、执行计划、慢 SQL 统计、缓存失效、参数优化；
3. 库表设计优化，引擎选择，表结构优化设计，列类型选择，索引设计，外键等；
4. SQL 查询优化，索引选择，连接优化，聚合查询优化，Union 优化，子查询优化，条件优化等；
5. 场景分析，主键生成与优化，高效分页，快速导入导出数据，解决死锁问题等。

## 第六周：超越分库分表 - 掌握海量业务数据的应对之道

1. MySQL 主从复制，Binlog，Row/Statement 模式，主从切换，读写分离，数据库扩容；
2. 数据库垂直拆分与水平拆分，分库分表，分布式主键，分表算法，SQL 限制，数据迁移，实时同步；
3. Spring 动态切换数据库，TDDL/Sharding-JDBC 框架，MyCat/Sharding-Proxy 中间件；
4. 数据库拆分的最佳实践，分布式事务最佳实践，多租户的最佳实践。

## 第七周：分布式服务 - 复杂业务系统架构演进必由之路

1. 基础知识：RPC、通信与数据协议、WebService、Hessian、REST、gRPC、Protocol Buffers 等；
2. 服务化：服务治理、配置管理、注册发现、服务分组、版本管理、集群管理、负载均衡、限流与降级熔断等；
3. 框架：Apache Dubbo 的功能与原理分析，Spring Cloud 体系，具体的案例实践；
4. 微服务：微服务架构的 6 个最佳实践，从微服务到服务网格、云原生的介绍。

## 第八周：分布式缓存 - 复杂业务系统访问提速第一法宝

1. 缓存的应用场景，缓存加载策略与失效策略，缓存与数据库同步等；
2. 缓存预热、缓存失效、缓存击穿、缓存雪崩、多级缓存、缓存与 Spring+ORM 框架集成；
3. 缓存中间件，Redis（几种常用数据结构、分布式锁、Lua 支持、集群），Hazelcast（Java 数据结构、内存网格、事务支持、集群）；
4. 缓存的应用场景，排行数据展示，分布式 ID 生成，Session 共享，热点账户操作等。

## 第九周：分布式消息 - 复杂业务系统关系解耦不二法门

1. 消息队列的基本知识，Broker 与 Client，消息模式（点对点、发布订阅），消息协议（STOMP、JMS、AMQP、OpenMessaging 等），消息 QoS（最多一次、最少一次、有且仅有一次），消息重试，延迟投递，事务性，消息幂等与去重；
2. 消息中间件：ActiveMQ 的简单入门，Kafka 的基本功能与使用，高可用（集群、分区、副本）、性能，RabbitMQ 和 RocketMQ，Pulsar 的简单介绍；
3. 消息的 4 个主要功能，搭建一个 Kafka 集群，实现常用的消息发送、消息消费功能；
4. 典型使用场景，使用 MQ 实现交易订单的处理，动手实现一个简化版的消息队列。

## 第十周：分布式系统架构 - 如何设计高并发高可用的 Java 系统

1. 业务分析、功能性需求、非功能性需求、高可用、高性能、稳定性、易用性、扩展性、可维护性、安全性等；
2. “4+1” Views、TOGAF、架构方案、业务架构、数据架构、设计文档、技术选型、部署文档、运维文档等；
3. 分布式服务化、分布式消息中间件、分布式缓存、分布式文件系统、监控告警系统、权限与认证中心等。

## 第十一周：业务系统重构 - 重构遗留系统是架构师的必修技能

1. 分析系统现状，给出明确的各项指标，了解各方对指标的期望和差距；
2. 给出多个可选的改造或重做方案，明确各方案的优缺点，提供决策依据；
3. 方案上的适当妥协，各方达成一致，快速推动重构工作启动和展开；
4. 细化具体的方案细节，形成路径，争取足够的资源，恰当的时间窗口；
5. 小步快跑，迅速取得阶段性成果，不影响业务整体的规划和发展；
6. 保持业务连续性，多做监控、兼容和特性开关，给改造加上保险丝和缓冲区；
7. 及时评估改进进展，更新方案和路线、资源和时间，推动改造顺利进行；
8. 复盘总结相关经验，提出更多建议和改进办法，实现经验分享，方法复用。

## 第十二周：架构师修炼之道 - 如何升级打怪终成一线技术专家

1. 分享我个人的成长第一手经验，升级打怪，成为架构师、技术专家、技术总监；
2. 六个硬能力：技术能力、设计能力、抽象能力、管理能力、结构化思考能力、系统化分析能力；
3. 七个软实力：大局观能力、沟通协作能力、持续学习能力、关注力、探索力、决策力、自我驱动力。
