package com.gnss.web.event;

import com.gnss.common.proto.TerminalProto;
import com.gnss.common.service.RedisService;
import com.gnss.web.command.api.MediaConfigDTO;
import com.gnss.web.common.constant.GnssConstants;
import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionStateListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 系统初始化执行</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-1-18
 */
@Component
@Slf4j
@Order(value = 1)
public class SystemInitialRunner implements CommandLineRunner {

    @Value("${gnss.middleware-ip}")
    private String host;

    @Value("${gnss.public-ip}")
    private String publicIp;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void run(String... args) throws Exception {
        initRedisClient();
        initGlobalConfig();
    }

    private void initGlobalConfig() {
        MediaConfigDTO mediaConfigDTO = new MediaConfigDTO();
        mediaConfigDTO.setServerIp(publicIp);
        mediaConfigDTO.setTcpPort(6602);
        cacheManager.getCache(GnssConstants.GLOBAL_CONFIG_CACHE_NAME)
                .put(GnssConstants.GLOBAL_CONFIG_MEDIA_SERVER, mediaConfigDTO);
    }

    private void initRedisClient() {
        RedisClient redisClient = RedisClient.create(String.format("redis://%s", host));
        redisClient.addListener(new RedisConnectionStateListener() {

            @Override
            public void onRedisConnected(RedisChannelHandler<?, ?> connection, SocketAddress socketAddress) {
                log.info("Redis客户端连接({})成功", host);
                loadTerminalInfo();
            }

            @Override
            public void onRedisDisconnected(RedisChannelHandler<?, ?> redisChannelHandler) {
                log.info("Redis客户端断开连接({})", host);
            }

            @Override
            public void onRedisExceptionCaught(RedisChannelHandler<?, ?> redisChannelHandler, Throwable throwable) {
                log.error("Redis客户端异常({})", host, throwable);
            }
        });
        redisClient.connect();
    }

    /**
     * 缓存终端信息
     */
    private void loadTerminalInfo() {
        //添加测试终端信息，开发时需要从数据库中查找所有终端信息
        List<TerminalProto> terminalProtos = new ArrayList<>();
        TerminalProto terminalInfo1 = new TerminalProto();
        terminalInfo1.setTerminalId(800001L);
        terminalInfo1.setTerminalNum("1000001");
        terminalInfo1.setTerminalSimCode("13800000001");
        terminalInfo1.setVehicleNum("粤B00001");
        terminalProtos.add(terminalInfo1);

        TerminalProto terminalInfo2 = new TerminalProto();
        terminalInfo2.setTerminalId(800002L);
        terminalInfo2.setTerminalNum("1000002");
        terminalInfo2.setTerminalSimCode("018666212741");
        terminalInfo2.setVehicleNum("粤B00002");
        terminalProtos.add(terminalInfo2);

        //删除Redis所有终端信息
        redisService.deleteAllTerminalInfo();
        //缓存终端信息
        terminalProtos.forEach(terminalProto -> {
            redisService.putTerminalInfo(terminalProto);
        });
        log.info("缓存终端信息,数量:{}", terminalProtos.size());
    }

}
