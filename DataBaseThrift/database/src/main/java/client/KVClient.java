package client;

import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import thrift.KVService;

public class KVClient {

    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8081;

    /**
     *
     * @param userName
     */
    public void startClient(String userName) {
        TTransport transport = null;
        try {
        	while(true) {
        		transport = new TSocket(SERVER_IP, SERVER_PORT);
        		// 协议要和服务端一致
        		TProtocol protocol = new TBinaryProtocol(transport);

        		KVService.Client client = new KVService.Client(protocol);
        		transport.open();
        	
        		Scanner s = new Scanner(System.in);
        		String temp=s.nextLine()+"\r\n"; 
        		String result = client.command(temp);
        		System.out.println("Operation : " + result);
        		transport.close();
        	}
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        KVClient client = new KVClient();
        client.startClient("yide-ran");

    }

}