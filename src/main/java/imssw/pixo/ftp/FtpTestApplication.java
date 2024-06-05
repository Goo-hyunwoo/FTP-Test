package imssw.pixo.ftp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import imssw.pixo.ftp.server.FtpServerCustomFactory;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
public class FtpTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtpTestApplication.class, args);
	}

}

@Component
@RequiredArgsConstructor
class ApplicationRun implements CommandLineRunner {

	private final FtpServerCustomFactory ftp;

	@Override
	public void run(String... args) throws Exception {
		ftp.start();
	}
	
	@PreDestroy
	public void shutdownHook() {
		ftp.stop();
	}
	
}
