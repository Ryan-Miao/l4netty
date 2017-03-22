package hello.server;

import hello.Constant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ryan on 2017/3/22.
 */
public class HelloServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloServer.class);


    private int port;

    public HelloServer(int port) {
        this.port = port;
        bind();
    }

    private void bind() {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024); //连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();
                    p.addLast(new ServerHandler());
                }
            });
            ChannelFuture f = bootstrap.bind(port).sync();
            if (f.isSuccess()) {
                LOGGER.info("stat netty success , port：{}" , this.port);
            }
            // 关闭连接
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            LOGGER.error("start netty failed." , e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        HelloServer helloServer = new HelloServer(Constant.PORT);
    }
}
