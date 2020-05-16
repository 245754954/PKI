package example.ca;

import java.io.File;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;


import nudt.core.api.ICA;
import nudt.core.api.impl.CAImpl;
import nudt.core.openssl.ProviderInstance;
import nudt.core.openssl.cert.CertUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;




/**
 * @author 都市桃源
 * 2018年6月23日下午8:25:17
 * ecdsa CA example
 */
public class ECDSACATestExample {
	//存储基础目录
	public static final String baseCertPath="e://client/cert/ecdsa/";
	static {
		Security.addProvider(ProviderInstance.getBCProvider());
		try {
			File baseFileDir=new File(baseCertPath);
			File caDir=new File(baseCertPath+"ca");
			File clientDir=new File(baseCertPath+"client");
			File serverDir=new File(baseCertPath+"server");
			if(!baseFileDir.exists()) {
				baseFileDir.mkdirs();
			}
			if(!caDir.exists()) {
				caDir.mkdirs();
			}
			if(!clientDir.exists()) {
				clientDir.mkdirs();
			}

			if(!serverDir.exists())
			{
				serverDir.mkdirs();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ca证书 的ca_cer_base64 地址
	static String caCert_base64=baseCertPath+"ca/ecdsa_ca_base64.cer";
	// ca证书的ca.cer地址
	static String caCert_cer=baseCertPath+"ca/ecdsa_ca.cer";

	// ca 公钥的ca_pub.pem地址
	static String caPublicPath=baseCertPath+"ca/ecdsa_ca_pub.pem";


	// 用户证书 私钥存储地址
	static String caPrivatePath=baseCertPath+"ca/ecdsa_ca_pri.pem";
	//用户证书的DN
	static String userDN="CN=ecdsa_client,OU=接入网关下的终端用户,O=接入网关,L=HuNan,ST=ChangSha";
	//服务器证书，由根证书签发数字证书
	static String serverDN="CN=ecdsa_server,OU=融合交换网关服务器,O=融合交换网关,L=HuNan,ST=ChangSha";
	//签发者DN
	static String issuerDN="CN=ECDSA National University of Defence Technology,OU=Department of Computer Science,O=NUDT,L=HuNan,ST=ChangSha";
	// 用户 p12 存储地址
	static String caPKCS12savepath=baseCertPath+"ca/ecdsa_ca.p12";

	public static void main(String[] args) throws Exception {
		Date notBefore=new Date();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String signHash="SHA1";
		String alg="ECDSA";
		testCreateCA(issuerDN, notBefore, notAfter, serialNumber, signHash, alg);
		X509Certificate CACert = CertUtil.readX509Cert(caCert_base64);
		PrivateKey privateKey = CertUtil.readPrivateKeyPem(caPrivatePath);
		testECDSA(CACert, privateKey, userDN);

		testECDSAServer(CACert,privateKey,serverDN);
	}
	/**
	 * 创建初始CA
	 * @param userDN
	 * @param notBefore
	 * @param notAfter
	 * @param serialNumber
	 * @param signHash
	 * @param alg
	 * @throws Exception
	 */
	public static  void testCreateCA(String userDN, Date notBefore, Date notAfter,
									 BigInteger serialNumber,String signHash,String alg) throws Exception {
		KeyPair keyPair = testCreateKeyPair();
		CertUtil.savePublicKeyPem(keyPair.getPublic(), caPublicPath);
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), caPrivatePath);
		X509Certificate makeUserSelfSignCert = CertUtil.makeUserSelfSignCert(keyPair.getPublic(), keyPair.getPrivate(), userDN, notBefore, notAfter, serialNumber, signHash.toUpperCase()+"WITH"+alg.toUpperCase());
		CertUtil.saveX509CertBase64(makeUserSelfSignCert, caCert_base64);
		CertUtil.savePKCS12(makeUserSelfSignCert, keyPair.getPrivate(), "ecdsa_ca", "123456", caPKCS12savepath);
	}

	public static void testECDSAServer(X509Certificate CACert, PrivateKey privateKey,String userDN) throws Exception {
		ICA ica=new CAImpl();
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		String signAlg="SHA1WITHECDSA";
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), userDN, notBefore, notAfter, serialNumber, signAlg);
		//保存
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"serverecdsa_server_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"server/ecdsa_server.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"server/ecdsa_server_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"server/ecdsa_server_pub.pem");
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "ecdsa_server", "123456", baseCertPath+"server/ecdsa_server.p12");

		/**
		 * 输出生成的文件
		 */
		KeyStore readKeyStore = CertUtil.readKeyStore(caPKCS12savepath, "123456");

		System.out.println(CertUtil.getPrivateKey(readKeyStore, "123456", "ecdsa_ca"));

		System.out.println(CertUtil.getPublicKey(readKeyStore, "ecdsa_ca"));
		System.out.println(x509Certificate);

		System.out.println(CertUtil.verifyUserCert(x509Certificate, CACert.getPublicKey()));
	}

	/**
	 * 生成用户公私钥,证书
	 *  client_pri.pem 私钥文件
	 *  client_pub.pem 公钥文件
	 *   client_base64.cer base64 格式公钥证书
	 *   client.cer 二进制公钥证书
	 *    client.p12 公私钥对
	 * @param CACert
	 * @param privateKey
	 * @param userDN
	 * @param issuerDN
	 * @throws Exception
	 */
	public static void testECDSA(X509Certificate CACert, PrivateKey privateKey,String userDN) throws Exception {
		ICA ica=new CAImpl();
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		String signAlg="SHA1WITHECDSA";
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), userDN, notBefore, notAfter, serialNumber, signAlg);
		//保存
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"client/ecdsa_client_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"client/ecdsa_client.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"client/ecdsa_client_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"client/ecdsa_client_pub.pem");
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "ecdsa_client", "123456", baseCertPath+"client/ecdsa_client.p12");

		/**
		 * 输出生成的文件
		 */
		KeyStore readKeyStore = CertUtil.readKeyStore(caPKCS12savepath, "123456");

		System.out.println(CertUtil.getPrivateKey(readKeyStore, "123456", "ecdsa_ca"));

		System.out.println(CertUtil.getPublicKey(readKeyStore, "ecdsa_ca"));
		System.out.println(x509Certificate);

		System.out.println(CertUtil.verifyUserCert(x509Certificate, CACert.getPublicKey()));
	}
	/**
	 * 创建密钥对生成器
	 * @return
	 * @throws Exception
	 */
	public static KeyPair testCreateKeyPair() throws Exception {
		KeyPairGenerator kpg=kpg=KeyPairGenerator.getInstance("ECDSA",BouncyCastleProvider.PROVIDER_NAME);
		KeyPair keyPair = kpg.generateKeyPair();
		return keyPair;
	}
}