// Automating Bing Translator-API. 

package Bing_Translator;

import java.io.BufferedReader;
import Read_credentials.ReadCredentials;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import bsh.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Bing_API {

	static String accessToken;
	static public String clientId=ReadCredentials.credentials("clientId");
	static public String clientSecret=ReadCredentials.credentials("client_secretID");
	public String token;
	public static final String DatamarketAccessUri = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
	public static ArrayList<String> translatedList= new ArrayList<>();

	public static String tokenRequest() throws IOException{

		clientId= URLEncoder.encode(clientId,"UTF-8");
		clientSecret= URLEncoder.encode(clientSecret,"UTF-8");
		String request;
		request="grant_type=client_credentials&client_id="+ clientId +"&client_secret="+ clientSecret +"&scope=http://api.microsofttranslator.com";
		URL url= new URL(DatamarketAccessUri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(request);
		wr.flush();
		wr.close();


		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

	public static String  translateText(String from,String to,String text)throws IOException, ParseException{

		URL url2= new URL("http://api.microsofttranslator.com/V2/Http.svc/Translate?text="+URLEncoder.encode(text,"UTF-8")+"&from="+from+"&to="+to);
		HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
		conn2.setRequestProperty("Authorization", "Bearer "+accessToken);
		conn2.setRequestMethod("GET");
		conn2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		conn2.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		conn2.disconnect();
		return response.toString();
	}

	public static ArrayList<String> getText() throws IOException, ParseException{
		infoToken();
		BufferedReader br = new BufferedReader(new FileReader(ReadCredentials.credentials("file_api")));
		String line=null;

		List<String> lines=new ArrayList<String>();		
		br.readLine();
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();
		for(int i=0;i<lines.size();i++){
			String values[]= lines.get(i).split(",");
			String output=translateText(values[0], values[1], values[2]);
			output= output.substring(output.indexOf(">") + 1);
			output= output.substring(0, output.indexOf("<"));
			translatedList.add(output);
		}
		return translatedList;
	}

	public static void infoToken()throws IOException, ParseException
	{
		String tok=tokenRequest();

		JSONParser parsor= new JSONParser();
		JSONObject object = null;
		try {
			object = (JSONObject) parsor.parse(tok);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		accessToken= (String)object.get("access_token");
	}

}

