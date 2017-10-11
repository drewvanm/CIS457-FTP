import java.io.*; 
import java.net.*;
import java.util.*;
//
//
class FTPServer {
    
 public static void main(String argv[]) throws Exception 
    { 
            ServerSocket welcomeSocket = new ServerSocket(8698);
               
            while(true)
            {
		//wait for client to "connect" to welcomeSocket
                Socket connectionSocket = welcomeSocket.accept();
                
		//spawn a thread to handle the controll socket with client
		ControllConnectionThread handler = new ControllConnectionThread(connectionSocket);
		handler.start();
	    }
    }
}

class ControllConnectionThread extends Thread
{
    private Socket client; //control socket
    DataOutputStream outToClient; //write data to client
    BufferedReader inFromClient;  //read commands from client
    
    String frstln; 
    String fromClient;
    String clientCommand;
    byte[] data;
    int port;
            
    public ControllConnectionThread(Socket socket)
    {
	
	client = socket;
	
	try
	    {
		//set up connections for control socket
		inFromClient = 
		    new BufferedReader(new InputStreamReader(client.getInputStream()));
		outToClient = 
		    new DataOutputStream(client.getOutputStream());
	    }
	catch (IOException ioEx)
	    {
		ioEx.printStackTrace();
	    }
    }
    
    public void run()
    {
	do
	    {
		try
		    {
			fromClient = inFromClient.readLine();
                 
			StringTokenizer tokens = new StringTokenizer(fromClient);
			//his code, client will always send message in form of
			// "port# command\n" commands are:
			// "list:", "retr: <filename>", "stor: <filename>", "quit:"
			frstln = tokens.nextToken();
			port = Integer.parseInt(frstln);
			clientCommand = tokens.nextToken();
			
			if(clientCommand.equals("list:"))
			    { 
				
				Socket dataSocket = new Socket(client.getInetAddress(), port);
				DataOutputStream  dataOutToClient = 
				    new DataOutputStream(dataSocket.getOutputStream());
		      
				//..........................
		      
				dataOutToClient.writeUTF("hi from server");
     
				//}

				dataSocket.close();
				System.out.println("Data Socket closed");
			    }
        
			//......................
             

			if(clientCommand.equals("retr:"))
			    {
				// ..............................
				//..............................
			    }
		    }
		catch(Exception e)
		    {
			System.out.println("exception");
		    }
	    }while (!fromClient.equals("QUIT"));
        
    }
}
