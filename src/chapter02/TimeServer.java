package chapter02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author:tyy
 * @date:2020/10/19 19:46
 * @version:0.0.1
 */

/**
 * 同步阻塞的服务端代码
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port:" + port);
            Socket socket = null;
            //通过IO任务线程池构造一个伪异步现象
            TimeServerHandlerExecutePoll singleExecutor=new TimeServerHandlerExecutePoll(50,10000);
            while (true) {
                socket = server.accept(); //监听客户端的连接，如果没有客户端接入，会在这里阻塞
                new Thread(new TimeServerHandler(socket)).start();        //同步IO
                singleExecutor.execute(new TimeServerHandler(socket));    //伪异步IO
            }
        } finally {
            if (server != null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
