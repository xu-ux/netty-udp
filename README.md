## Netty-UDP学习demo

## Netty中UDP相关类

Netty中提供了大量的类来支持UDP应用程序的编写，主要包括：

| 名称 | 描述 |
| --- | --- |
| interface AddressedEnvelope<M, A extends SocketAddress>extends ReferenceCounted | 定义一个消息，其包装了另一个消息并带有发送者和接收者地址。其中 M 是消息类型； A 是地址类型 |
| class DefaultAddressedEnvelope<M, A extends SocketAddress>implements AddressedEnvelope<M,A> | 提供了 interface AddressedEnvelope 的默认实现 |
| class DatagramPacket extends DefaultAddressedEnvelope<ByteBuf, InetSocketAddress> implements ByteBufHolder | 扩展了 DefaultAddressedEnvelope 以使用 ByteBuf 作为消息数据容器，其中存在比较重要的方法：通过content()来获取消息内容、通过sender()来获取发送者的消息、通过recipient()来获取接收者的消息 |
| interface DatagramChannel extends Channel | 扩展了 Netty 的 Channel 抽象以支持 UDP 的多播组管理 |
| class NioDatagramChannnel extends AbstractNioMessageChannel implements DatagramChannel | 定义了一个能够发送和接收 Addressed-Envelope 消息的 Channel 类型 |

Netty 的 `DatagramPacket` 是一个简单的消息容器，`DatagramChannel` 实现用它来和远程节点通信。类似于在我们先前的类比中的明信片，它包含了接收者（和可选的发送者）的地址以及消息的有效负载本身

## UDP单播

单播的传输模式，是定义为发送消息给一个由唯一的地址所标识的单一的网络目的地。而面向连接的协议和无连接协议都是支持这种模式的


## UDP广播

### 注册服务的业务探究

|服务端 | 客户端 |
| --- | --- |
|有固定业务IP和业务端口| 随机端口和非固定IP   |
|没有特定消息给客户端 |需要发送数据给服务端|
|需要广播给所有客户端告知自己的业务IP和业务端口|需要广播给兄弟客户端告知自己知道的【服务端-UDP单播订阅端口】|

         
1.服务端配置【服务端-UDP单播订阅端口】【UDP广播订阅端口】

2.服务端间隔时间广播给【UDP广播订阅端口】，自己的服务器信息

3.客户端配置【UDP广播订阅端口】

4.客户端知道【服务端-UDP单播订阅端口】，负载均衡发送log信息；不知道，则发送广播告知全网客户端和服务端【UDP广播订阅端口】需要知道服务端信息

4.1 已经知道服务端信息的客户端发送server port给【UDP广播订阅端口】、服务端发送自己的server port给【UDP广播订阅端口】

4.2 客户端通过Set容器保存服务端IP和PORT,负载均衡发送log信息

影响范围：第4步，每次有一个客户端上线，将影响全网已知道【服务端-UDP单播订阅端口】服务器发送一次 广播报文