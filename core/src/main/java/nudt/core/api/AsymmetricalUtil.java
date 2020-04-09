package nudt.core.api;
/**
 * cipherUtil 
 * @author 都市桃源
 * 2018年7月1日下午10:42:42
 * TODO
 */

import java.security.Security;
import java.security.Signature;

import javax.crypto.Cipher;


import nudt.core.openssl.ProviderInstance;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class AsymmetricalUtil {
	static {
			Security.addProvider(ProviderInstance.getBCProvider());
	}
	
	/**
	 * cipher 获取
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static Cipher getCipherInstance(String algorithm) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm,BouncyCastleProvider.PROVIDER_NAME);
		return cipher;
	}
	
	/**
	 * cipher 获取
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static Signature getSignatureInstance(String algorithm) throws Exception {
		Signature signature=Signature.getInstance(algorithm,BouncyCastleProvider.PROVIDER_NAME);
		return signature;
	}
}