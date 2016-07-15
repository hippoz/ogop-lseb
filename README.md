# ogop-lseb
ogop-lseb demo with/without netty
## Run

Start the server first with the following two options:

### OIO Server

<code>java moc.ogop.ahsp.demo.oio.OioServerMain</code>

### NETTY NIO Server

<code>java moc.ogop.ahsp.demo.nio.NettyNioServerMain</code>

The start the client:

### OIO Client

<code>java mocmoc.ogop.ahsp.demo.oio.OioClientMain</code>

## About

It demo and compares the one-thread-per-connection server side architecture and netty NIO. The client side use a persistent connection which is shared by multiple threads.





