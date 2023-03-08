using UnityEngine;
using UnityEngine.UI;
using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

using System.Collections;
using System.Net.Http;



public class TCPClient : MonoBehaviour
{

    IPEndPoint iPEndPoint;
    Socket socket;
    Thread receiveThread;
    public Wall wall;
    public GameObject player;
    public GameObject textO;
    //private Text textT;
    byte[] buffer;                   //缓冲区，存储接收到的数据
    public static bool quit = false; //系统连接标识
    public static string message;    //客户端收到的信息
                                     // public string _ip = "127.0.0.1"; //IP地址——用户自行修改

    string host = "127.0.0.1";
    public string _ip = ""; //IP地址——用户自行修改192.168.3.17
    public int _port = 8888;         //端口号，与服务端保持一致
  
    private int ii = 0;
    bool isGET = false;
    // Use this for initialization
    void Start()
    { 
        //textT=textO.GetComponent<Text>();
        Debug.Log("start");
        //使用协程


        Init();
        StartCoroutine(tcpReceiveThread());
    }

    /// <summary>
    /// 与服务端建立连接
    /// </summary>
    public void Init()
    {
        //Debug.Log("enter");
        try
        {


            //Socket msocket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);

            //IPEndPoint iep = new IPEndPoint(IPAddress.Any, _Bport);
            //UdpClient UDPrece = new UdpClient(new IPEndPoint(IPAddress.Any, 8080));
            //IPEndPoint endpoint = new IPEndPoint(IPAddress.Any, 0);

            //byte[] buf = UDPrece.Receive(ref endpoint);
            //string msg = Encoding.Default.GetString(buf);
            //Console.WriteLine(msg);
            //if (msg[0] == 'i')
            //{
            //    _ip = msg.Substring(1);

            //}

            receiveThread = receiveThread = new Thread(new ThreadStart(getIp));
            receiveThread.Start();


        }
        catch (SocketException e)
        {
            Debug.Log(e.Message);
        }

    }
    /// <summary>
    /// 接受服务端消息
    /// </summary>
    /// <param name="message">客户端接收到的字节数据</param>
    /// <returns></returns>

    public void Receive()
    {
    
        socket.Connect(iPEndPoint);
        buffer = new byte[1024 * 1024];
        //获取信息长度
        int n = socket.Receive(buffer);
        //byte转String

        Debug.Log("receive" + ii + "\n");
        ii++;
        string message = Encoding.UTF8.GetString(buffer, 0, n);
        //数据分割函数，用户自行编写

        Debug.Log(message);
        Split_data(message);



    }

    //public void test()
    //{
    //    Socket sock = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);//初始化一个Scoket实习,采用UDP传输

    //    IPEndPoint iep = new IPEndPoint(IPAddress.Broadcast, 9095);//初始化一个发送广播和指定端口的网络端口实例

    //    sock.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.Broadcast, 1);//设置该scoket实例的发送形式

    //    string request = "你好,TEST SEND!";//初始化需要发送而的发送数据

    //    byte[] buffer = Encoding.Unicode.GetBytes(request);

    //    sock.SendTo(buffer, iep);

    //    sock.Close();

    //}
    public void getIp()
    {
        Debug.Log("客户端进入");
         
       

        //使用广播
        Socket sock = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);

        //广播端口号
        IPEndPoint iep = new IPEndPoint(IPAddress.Any, 9060);
        sock.Bind(iep);
        EndPoint ep = (EndPoint)iep;
        Debug.Log("Ready to receive…");
        Debug.Log("     " + ep.ToString());
        byte[] data = new byte[1024];
        int recv = sock.ReceiveFrom(data, ref ep);
        Debug.Log("ok");
        string msg = Encoding.ASCII.GetString(data, 0, recv);
        Debug.Log(msg + "     " + ep.ToString());
        _ip = ep.ToString().Split(":")[0];
        sock.Close();
        //
        socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
        iPEndPoint = new IPEndPoint(IPAddress.Parse(_ip), _port);
        //Debug.Log(iPEndPoint.ToString());
        isGET = true;
    }
    IEnumerator tcpReceiveThread()
    {


       
        while (true)
        {

            try
            {
                //创建接收线程
                if (isGET)
                    Receive();
            }
            catch (Exception e)
            {
            
                Debug.Log(e.Message);
                socket.Close();
                socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            }

            yield return new WaitForSeconds(0.5f);
        }

    }

    void OnApplicationQuit()
    {
        quit = true;
    }

    /// <summary>
    /// 数据分割处理函数，用户自行编写
    /// </summary>
    /// <param name="message">客户端接收到的字节数据</param>
    /// <returns></returns>
    public void Split_data(string message)
    {
        // A：设定迷宫矩阵
        // B: 角色位置
        // C: 分配角色id
        // D：控制金币消除
        if (message != null)
        {
            if (message[0] == 'A')
            {
                message = message.Substring(1);
                Wall.setWallsStatuis(message.ToCharArray());
            }
            else if (message[0] == 'B')
            {
                message = message.Substring(1);
                string[] words = message.Split(",");
                userDate.xx = float.Parse(words[0]);
                userDate.zz = float.Parse(words[1]);
                userDate.mainUp = int.Parse(words[2]) == 1;
                userDate.x2 = float.Parse(words[3]);
                userDate.z2 = float.Parse(words[4]);
                userDate.partnerUP = int.Parse(words[5]) == 0;
            }
            else if (message[0] == 'C')
            {
                message = message.Substring(1);

            }
            else if (message[0] == 'D')
            {
                message = message.Substring(1);
            }

        }
    }
}
