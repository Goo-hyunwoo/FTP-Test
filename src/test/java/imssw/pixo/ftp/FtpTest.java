package imssw.pixo.ftp;

import java.io.File;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import imssw.pixo.ftp.test.FtpService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class FtpTest {

	@Autowired
	private FtpService ftpService;
	
	@Test
	public void test01() {
		FTPClient client = new FTPClient();
		
		// 연결 테스트
		String ip = "127.0.0.1";
		int port = 9001;
		boolean connect = ftpService.doConnect(client, ip, port);
		
		// 로그인 테스트
		String id = "admin";
		String password = "admin";
		boolean login = ftpService.doLogin(client, id, password);
		boolean config = ftpService.doConfig(client);
		
		// 디렉토리 이동 테스트
		String path = "/logs";
		boolean changePath = false;
		if(path != null) {
			changePath = ftpService.doChangeWorkingDirectory(client, path);
		}
		
		// 업로드 테스트
//		boolean upload = false;
		File file = new File("D:/asu/test1.txt");
		boolean upload = ftpService.doPut(client, file);
		
		// 업로드 확인 테스트
		String[] files = ftpService.getFiles(client);
		boolean uploadChk = false;
		for (String f : files) {
			if(f.endsWith(file.getName())) {
				uploadChk = true;
				ftpService.doRemove(client, f);
			}
		}
		
		// 로그아웃 및 연결 해제 테스트
		boolean quit = ftpService.doLogoutConnect(client);
		log.info("connect:{}, login:{}, config:{}, changePath:{}, upload:{}, uploadChk:{}, quit:{}", connect, login, config, changePath, upload, uploadChk, quit);
	}
	
}
