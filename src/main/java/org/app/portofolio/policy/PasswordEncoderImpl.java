package org.app.portofolio.policy;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
*
* @author tekosulaiman@yahoo.com
*/
@SuppressWarnings("deprecation")
public class PasswordEncoderImpl implements PasswordEncoder{

	@Override
	public String encodePassword(String string, Object object)throws DataAccessException {
		return BCrypt.hashpw(string, BCrypt.gensalt());
	}

	@Override
	public boolean isPasswordValid(String plaintext, String hashed, Object object)throws DataAccessException {
		return BCrypt.checkpw(hashed, plaintext);
	}
}