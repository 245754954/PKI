/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package NUDT;

import NUDT.config.ClientConfig;
import NUDT.config.ClientConfigLoader;
import NUDT.config.JsonConfigReader;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdResponse;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@ChannelHandler.Sharable
public final class SocksServerConnectHandler extends SimpleChannelInboundHandler<SocksCmdRequest> {
    private static final Logger LOG = LoggerFactory.getLogger(SocksServerConnectHandler.class);

    private final Bootstrap b = new Bootstrap();

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final SocksCmdRequest request) throws Exception {

        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(final Future<Channel> future) throws Exception {
                        final Channel outboundChannel = future.getNow();
                        if (future.isSuccess()) {
                            ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, request.addressType()))
                                    .addListener(new ChannelFutureListener() {
                                        @Override
                                        public void operationComplete(ChannelFuture channelFuture) {
                                            ctx.pipeline().remove(SocksServerConnectHandler.this);
                                            outboundChannel.pipeline().addLast(new RelayHandler(ctx.channel()));
                                            //LOG.debug("outboundChannel: "+outboundChannel.pipeline());
                                            ctx.pipeline().addLast(new RelayHandler(outboundChannel));
                                            //LOG.debug("clientChannelContext: "+ctx.pipeline());

                                        }
                                    });
                        } else {
                            ctx.channel().writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                            SocksServerUtils.closeOnFlush(ctx.channel());
                        }
                    }
                });
        //客户端向本地代理发送消息的通道
        final Channel inboundChannel = ctx.channel();

        b.group(inboundChannel.eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new DirectClientHandler(promise));


        //连接remote 代理服务器的地址和端口
        //获取配置文件，得到远程代理服务器的地址和端口
        ClientConfigLoader clientConfigLoader = new ClientConfigLoader(new JsonConfigReader<>());
        clientConfigLoader.loadConfig("client_config.json", ClientConfig.class);
        //客户端代理监听本地端口，端口设置在client_config.json配置文件里
        b.connect(ClientConfig.INSTANCE.getServerHost(), ClientConfig.INSTANCE.getServerPort()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    final ByteBuf buf = Unpooled.copiedBuffer(request.host() + ":" + request.port() + "zjp",CharsetUtil.UTF_8);
                    LOG.debug(buf.toString(CharsetUtil.UTF_8));
                    future.channel().writeAndFlush(buf);
                    // Connection established use handler provided results
                } else
                {
                    // Close the connection if the connection attempt has failed.
                    ctx.channel().writeAndFlush(
                            new SocksCmdResponse(SocksCmdStatus.FAILURE, request.addressType()));
                    SocksServerUtils.closeOnFlush(ctx.channel());
                }
            }
        });


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SocksServerUtils.closeOnFlush(ctx.channel());
    }
}
