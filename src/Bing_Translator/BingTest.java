// Test the output of Bing-API and Bing-GUI

package Bing_Translator;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.Test;

import bsh.ParseException;

public class BingTest {

	@Test
	public void TestBing() throws IOException, InterruptedException, ParseException {
		Assert.assertEquals(Bing_Translator.Bing_API.getText(), Bing_Translator.Bing_GUI.translate());
	}
}


