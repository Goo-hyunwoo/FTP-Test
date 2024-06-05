package imssw.pixo.ftp.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomUserManager extends AbstractUserManager {
	
	@Value("${ftp.default.path}")
	private String PATH;
	@Value("${ftp.default.id}")
	private String ID;
	@Value("${ftp.default.pwd}")
	private String PWD;
	
	private final List<BaseUser> USERS = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		List<Authority> authorities = List.of(
				new WritePermission(), 
				new ConcurrentLoginPermission(0, 0), 
				new TransferRatePermission(0, 0)
				);
		
		this.mkRoot();

		BaseUser user = new BaseUser();
		user.setName(ID);
		user.setPassword(PWD);
		user.setHomeDirectory( PATH + "/" + ID);
		user.setMaxIdleTime(0);
		user.setEnabled(true);
		user.setAuthorities(authorities);
		
		USERS.add(user);
		
	}
	
	private void mkRoot() {
		File folder = new File(PATH + "/" + ID);
		if(!folder.exists()) {
			folder.mkdirs();
		}
	}

	@Override
	public User getUserByName(String username) throws FtpException {
		return USERS.stream()
				.filter(it -> it.getName().equals(username))
				.findFirst()
				.orElseThrow(() -> new FtpException("해당 유저는 없습니다."));
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
		return (String[]) USERS.stream().map(it -> it.getName()).toArray();
	}

	@Override
	public void delete(String username) throws FtpException {
		BaseUser user = USERS.stream().filter(it -> it.getName().equals(username)).findFirst().orElseGet(null);
		if(user != null) {
			USERS.remove(user);
		}
	}

	@Override
	public void save(User user) throws FtpException {
		log.info("지원되지 않는 기능입니다.");
	}

	@Override
	public boolean doesExist(String username) throws FtpException {
		return USERS.stream().anyMatch(it -> it.getName().equals(username));
	}

	@Override
	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		if(authentication instanceof UsernamePasswordAuthentication) {
			UsernamePasswordAuthentication upauth = (UsernamePasswordAuthentication) authentication;
			String name =  upauth.getUsername();
			String password = upauth.getPassword();
			
			try {
				User user = getUserByName(name);
				if(user.getPassword().equals(password)) {
					return user;
				} else {
					throw new AuthenticationFailedException("비밀번호가 다릅니다.");
				}
			} catch (FtpException e) {
				throw new AuthenticationFailedException("계정이 없습니다.");
			}
		} else {
			throw new AuthenticationFailedException("인증에 실패했습니다.");
		}
	}

}
