package imssw.pixo.ftp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

@Component
public class FtpService {
	
	public boolean doConnect(FTPClient client, String ip, int port) {
		boolean flag = false;
		try {
			client.connect(ip, port);
//			client.setCharset(Charset.defaultCharset());
			client.enterLocalPassiveMode();
//			client.setControlEncoding("utf-8");
			flag = true;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean doLogin(FTPClient client, String id, String password) {
		try {
			return  client.login(id, password);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean doConfig(FTPClient client) {
		try {
//			boolean ftm = client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
//			log.info("fileType {}", ft);
			return client.setFileType(FTP.BINARY_FILE_TYPE); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean doChangeWorkingDirectory(FTPClient client, String path) {
		boolean flag = false;
		try {
			flag = client.changeWorkingDirectory(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean doPut(FTPClient client, File file) {
		try (FileInputStream fis = new FileInputStream(file);) {
			return client.storeFile(file.getName(), fis);
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public String[] getFiles(FTPClient client) {
		try {
			return  client.listNames();
		} catch (IOException e) {
			e.printStackTrace();
			return new String[] {};
		}
	}
	
	public boolean doRemove(FTPClient client, String fileNm) {
		boolean flag = false;
		try {
			flag = client.deleteFile(fileNm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean doLogoutConnect(FTPClient client) {
		try {
			client.logout();
			client.disconnect();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
