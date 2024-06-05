package imssw.pixo.ftp.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FtpServerCustomFactory {
	
	@Value("${ftp.port}")
	private int PORT;

	private final CustomEventHandler eventHandler;
	private final CustomUserManager userManager;
	private FtpServer server;
	
	public void start() throws FtpException {
		this.server = makeServer();
		this.server.start();
	}
	
	public void stop() {
		if(this.server != null && !this.server.isStopped()) {
			this.server.stop();
		}
	}
	
	
	private FtpServer makeServer() {
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		
		factory.setPort(PORT);
		serverFactory.addListener("default", factory.createListener());
		serverFactory.setUserManager(this.userManager);
		
		Map<String, Ftplet> map = new HashMap<>();
		map.put("DefaultFtplet", this.eventHandler);
		
		serverFactory.setFtplets(map);
		
		
		return serverFactory.createServer();
	}
	
}
