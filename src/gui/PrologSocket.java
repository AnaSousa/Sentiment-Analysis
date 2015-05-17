package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class PrologSocket {

	private static PrologSocket instance;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	
	public static void main(String[] args) {
		
		try {
			PrologSocket.getInstance().connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int res;
		try {
			res = PrologSocket.getInstance().evaluateSentence("I am Sad");
			System.out.println(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	}
	
	public void connect() throws Exception
	{
		s = new Socket("127.0.0.1", 2000);
		out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	
	public int evaluateSentence(String str) throws Exception
	{
		out.print("evaluate_str(['");
		String words[] = str.split("( )|(\\.)");
		for(int i = 0; i < words.length -1 ; i++)
		{
			out.print(words[i].toLowerCase());
			out.print("','");
		}
		out.print(words[words.length-1].toLowerCase());
        out.println("']).");
        String num[] = in.readLine().split("\\.");
        return Integer.parseInt(num[0]);
	}

}
