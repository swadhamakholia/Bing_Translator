// Automate Bing Translator-GUI.

package Bing_Translator;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import Read_credentials.ReadCredentials;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


public class Bing_GUI {

	public static ArrayList<String> translate() throws InterruptedException{

		WebDriver driver = new FirefoxDriver();
		driver.get("https://www.bing.com/translator");


		File file = new File(ReadCredentials.credentials("file_gui")); // Input csv file with 'from', 'to' languages and text
		BufferedReader br = null;
		String line = "";
		String []lang;
		String from,to;
		ArrayList<String> translatedList= new ArrayList<>();
		try {

			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				Thread.sleep(2000);
				// use comma as separator
				lang = line.split(",");
				String word= lang[2];
				if(lang[0].equals("")) {
					from= "Auto-Detect";
				}
				else {
					from= lang[0];
				}

				to= lang[1];

				driver.findElement(By.cssSelector("#srcText")).clear();
				driver.findElement(By.xpath(".//*[@class='col translationContainer sourceText']/div[1]/div[1]")).click();
				driver.findElement(By.xpath(".//*[@class='col translationContainer sourceText']/div[1]/div[1]/div[1]/table/tbody/tr[*]/td[text() = '"+from+"']")).click();
				driver.findElement(By.cssSelector("#srcText")).sendKeys(word);
				Thread.sleep(2000);
				driver.findElement(By.xpath(".//*[@class='col translationContainer destinationText']/div[1]/div[1]")).click();
				driver.findElement(By.xpath(".//*[@class='col translationContainer destinationText']/div[1]/div[1]/div[1]/table/tbody/tr[*]/td[text() = '"+to+"']")).click();
				Thread.sleep(2000);
				System.out.println((driver.findElement(By.cssSelector("#destText")).getText()));
				translatedList.add((driver.findElement(By.cssSelector("#destText")).getText()));

			}
		}
		catch(FileNotFoundException e) {
			System.out.println("exception: "+ e);
		}
		catch (IOException e) {
			System.out.println("exception: "+ e);
		}
		finally {
			if(br != null) {
				try {
					br.close();
				}
				catch(IOException e) {
					e.printStackTrace();;
				}
			}
		}
		return translatedList;

	}
}