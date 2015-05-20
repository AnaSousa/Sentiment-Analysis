package logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.lang.Runtime;

public class PrologSocket {

	private static PrologSocket instance;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private Process prolog_server;
	
	public static void main(String[] args) {
		
		try {
			PrologSocket.getInstance().connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int res;
		try {
			res = PrologSocket.getInstance().evaluateSentence("I am happy");
			System.out.println(res);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static PrologSocket getInstance()
	{
		if(instance==null)
			instance = new PrologSocket();
		return instance;
	}
	
	private PrologSocket()
	{
		s = new Socket();
	}
	
	public void connect()
	{
		try {
			String os = System.getProperty("os.name");
			String cmd = "./prolog/server";
			if(os.toLowerCase().contains("windows"))
				cmd = "./prolog/server.exe";
			prolog_server = Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000);
			s.connect(new InetSocketAddress("127.0.0.1", 2000), 1000);
			s.setSoTimeout(5000);
			System.out.println("Connection Established");
			out = new PrintWriter(s.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (Exception e) {
			System.err.println("Couldnt connect to prolog: " + e.getMessage());
		}
	}
	
	public int evaluateSentence(String str) throws Exception
	{
			/*int res = 0;
			String phrases[] = str.split("\\.");
			for (String string : phrases) {
				 res += evaluationSub(string);
			}
			return res;*/
		return evaluationSub(str);
	}

	private int evaluationSub(String str) throws Exception {
		out.print("evaluate_str(['");
		String words[] = str.split("(?!\\w).");
		for(int i = 0; i < words.length -1 ; i++)
		{
			if(words[i].length() > 0)
			{
				out.print(words[i].toLowerCase());
				//System.out.println(words[i].toLowerCase());
				out.print("','");	
			}
		}
		if(words[words.length-1].length() > 0)
			out.print(words[words.length-1].toLowerCase());
        out.println("']).");
        String num[];
		try {
			num = in.readLine().split("\\.");
	        return Integer.parseInt(num[0]);
		} catch (Exception e) {
			System.err.println("error while evaluating");
			prolog_server.destroyForcibly();
			Thread.sleep(2000);
			s = new Socket();
			connect();
			throw new Exception("error while evaluating");
		}
	}

}
