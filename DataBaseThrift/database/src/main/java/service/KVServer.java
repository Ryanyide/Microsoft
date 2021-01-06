package service;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import common.KVServiceImpl;
import thrift.KVService;

public class KVServer {
    public  static  final int  SERVER_PORT = 8081;
    public void startTThreadPoolServer() {
        try {
            System.out.println("KVServer TServer start ....");

            //在这里调用了 HelloWorldImpl 规定了接受的方法和返回的参数
            
           	TProcessor tprocessor = new KVService.Processor<KVService.Iface>(new KVServiceImpl());
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            while(true) {
            	TThreadPoolServer.Args ttpsArgs = new TThreadPoolServer.Args(serverTransport);
            	ttpsArgs.processor(tprocessor);
            	ttpsArgs.protocolFactory(new TBinaryProtocol.Factory());

            	TServer server = new TSimpleServer(ttpsArgs);
            	server.serve();
            }
        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        KVServer server = new KVServer();
        server.startTThreadPoolServer();
    }

}