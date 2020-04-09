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




/**
 * @author 都市桃源
 * 2018年6月23日下午8:25:17
 * rsa CA example 
 * TODO
 */
public class RSACATestExample {
	//存储基础目录
	public static final String baseCertPath="e://client/cert/rsa/";
	static {
		Security.addProvider(ProviderInstance.getBCProvider());
		try {
			File baseFileDir=new File(baseCertPath);
			File caDir=new File(baseCertPath+"ca");
			File clientDir=new File(baseCertPath+"client");
			if(!baseFileDir.exists()) {
				baseFileDir.mkdirs();
			}
			if(!caDir.exists()) {
				caDir.mkdirs();
			}
			if(!clientDir.exists()) {
				clientDir.mkdirs();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ca证书 的ca_cer_base64 地址
	static String caCert_base64=baseCertPath+"ca/ca_base64.cer";
	// ca证书的ca.cer地址
	static String caCert_cer=baseCertPath+"ca/ca.cer";
	
	// ca 公钥的ca_pub.pem地址
	static String caPublicPath=baseCertPath+"ca/ca_pub.pem";
	
	
	// 用户证书 私钥存储地址
	static String caPrivatePath=baseCertPath+"ca/ca_pri.pem";
	//用户证书的DN
	static String userDN="C=CN,ST=BJ,L=BJ,O=zfc,OU=zfc,CN=clients,E=lianglei_lzx@163.com";
	//签发者DN
	static String issuerDN="C=CN,ST=BJ,L=BJ,O=NUDT,OU=Department of Computer Science,CN=NUDT,E=245754954@qq.com";
	// 用户 p12 存储地址
	static String caPKCS12savepath=baseCertPath+"ca/ca.p12";


	//签名算法位数
	static Integer keySize=2048;
	public static void main(String[] args) throws Exception {

		Date notBefore=new Date();
		BigInteger serialNumber=BigInteger.valueOf(1L);
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		Date notAfter=instance.getTime();
		String signHash="SHA1";
		String alg="RSA";
		//生成根CA的证书，因为参数是issuerDN，这里的证书是自签名的
		testCreateCA(issuerDN, notBefore, notAfter, serialNumber, signHash, alg);

		X509Certificate CACert = CertUtil.readX509Cert(caCert_base64);
		PrivateKey privateKey = CertUtil.readPrivateKeyPem(caPrivatePath);
		//给用户创建证书
		testRSA(CACert, privateKey, userDN);
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
		//创建自签名的证书
		X509Certificate makeUserSelfSignCert = CertUtil.makeUserSelfSignCert(keyPair.getPublic(), keyPair.getPrivate(), userDN, notBefore, notAfter, serialNumber, signHash.toUpperCase()+"WITH"+alg.toUpperCase());
		//保存证书文件，这里是x.509格式
		CertUtil.saveX509CertBase64(makeUserSelfSignCert, caCert_base64);
		//这里是pkcs12格式
		CertUtil.savePKCS12(makeUserSelfSignCert, keyPair.getPrivate(), "zfc", "123456", caPKCS12savepath);


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
	 * @throws Exception
	 */
	public static void testRSA(X509Certificate CACert, PrivateKey privateKey,String userDN) throws Exception {
		ICA ica=new CAImpl();
		//配置根CA的证书和私钥
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		Date notAfter=instance.getTime();
		BigInteger serialNumber=BigInteger.valueOf(1L);
		String signAlg= CAImpl.DEFAULT_SIGN_ALG;
		//给用户签发证书
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), userDN, notBefore, notAfter, serialNumber, signAlg);
		//保存为用户签发的一系列证书文件，包括用户的公钥私钥等等
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"client/client_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"client/client.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"client/client_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"client/client_pub.pem");
		//将用户的相关信息以别名的形式进行存储，存储到密钥库
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "zfc", "123456", baseCertPath+"client/client.p12");


		/**
		 * 输出生成的文件
		 */
		KeyStore readKeyStore = CertUtil.readKeyStore(caPKCS12savepath, "123456");
		
		System.out.println(CertUtil.getPrivateKey(readKeyStore, "123456", "zfc"));
		
		System.out.println(CertUtil.getPublicKey(readKeyStore, "zfc"));
		X509Certificate x509Certificate1 = CertUtil.readX509Cert(caCert_base64);
		System.out.println(x509Certificate1);
		
		System.out.println(CertUtil.verifyUserCert(x509Certificate, CACert.getPublicKey()));
	}
	/**
	 * 创建密钥对生成器
	 * @return
	 * @throws Exception
	 */
	public static KeyPair testCreateKeyPair() throws Exception {
		KeyPairGenerator kpg=kpg=KeyPairGenerator.getInstance("RSA",BouncyCastleProvider.PROVIDER_NAME);
		kpg.initialize(keySize);
		KeyPair keyPair = kpg.generateKeyPair();
		return keyPair;
	}
}
