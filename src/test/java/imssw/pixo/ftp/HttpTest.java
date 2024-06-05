package imssw.pixo.ftp;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imssw.pixo.ftp.test.HttpService;

@SpringBootTest
public class HttpTest {

	@Autowired
	private HttpService httpService;
	
	@Test
	public void test01() {
		try {
			httpService.uploadFile("http://127.0.0.1:9002/api/upload", new File("D:/asu/test1.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
