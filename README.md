Spring 整合 Redis，为SSM整合Redis做准备

1、准备一个Spring 项目

2、pom引入：jedis、spring-data-redis、commons-lang3等和redis相关的jar包，以及Jackson相关jar以实现自定义序列化

3、创建spring-redis.xml，配置相关beans

4、测试的地方引入redis，主要测试对redis的增删改查
