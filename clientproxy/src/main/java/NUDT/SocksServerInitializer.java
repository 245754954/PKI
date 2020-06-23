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

import NUDT.config.ContextSSLFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.socks.SocksInitRequestDecoder;
import io.netty.handler.codec.socks.SocksMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;
import java.nio.charset.Charset;

public final class SocksServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SocksMessageEncoder socksMessageEncoder = new SocksMessageEncoder();
    private final SocksServerHandler socksServerHandler = new SocksServerHandler();

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new SocksInitRequestDecoder());
        p.addLast(socksMessageEncoder);
        p.addLast(socksServerHandler);

//        SSLEngine engine = ContextSSLFactory.getSslContext().createSSLEngine();
//        engine.setUseClientMode(true); //设置为服务端模式
//        engine.setNeedClientAuth(false); //需要验证客户端
//        p.addFirst("ssl", new SslHandler(engine));   //这个handler需要加到最前面
    }
}
