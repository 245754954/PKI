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
			File serverDir=new File(baseCertPath+"server");
			File clientProxyDir=new File(baseCertPath+"clientProxy");
			File JGateWayProxyDir=new File(baseCertPath+"jGateWayProxy");
			File RGateWayProxyDir=new File(baseCertPath+"rGateWayProxy");
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

			if (!clientProxyDir.exists())
			{
				clientProxyDir.mkdirs();
			}

			if(!JGateWayProxyDir.exists())
			{
				JGateWayProxyDir.mkdirs();
			}

			if(!RGateWayProxyDir.exists())
			{
				RGateWayProxyDir.mkdirs();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ca证书 的ca_cer_base64 地址
	static String caCert_base64=baseCertPath+"ca/rsa_ca_base64.cer";
	// ca证书的ca.cer地址
	static String caCert_cer=baseCertPath+"ca/rsa_ca.cer";
	
	// ca 公钥的ca_pub.pem地址
	static String caPublicPath=baseCertPath+"ca/rsa_ca_pub.pem";
	
	
	// 用户证书 私钥存储地址
	static String caPrivatePath=baseCertPath+"ca/rsa_ca_pri.pem";
	//用户证书的DN
	static String userDN="CN=rsa_client,OU=接入网关下的终端用户,O=接入网关,L=HuNan,ST=ChangSha";
	//服务器证书，由根证书签发数字证书
	static String serverDN="CN=rsa_server,OU=融合交换网关服务器,O=融合交换网关,L=HuNan,ST=ChangSha";
	//客户端代理书证书
	static String clientProxyDN="CN=clientProxy,OU=客户端代理,O=客户端代理,L=HuNan,ST=ChangSha";
	//接入网关证书
	static String jGateWayProxyDN="CN=jGateWayProxy,OU=接入网关,O=接入网关,L=HuNan,ST=ChangSha";
	//融合交换网关
	static String rGateWayProxyDN="CN=rGateWayProxy,OU=融合交换网关,O=融合交换网关,L=HuNan,ST=ChangSha";
	//签发者DN
	static String issuerDN="CN=RSA National University of Defence Technology,OU=Department of Computer Science,O=NUDT,L=HuNan,ST=ChangSha";
	// 用户 p12 存储地址
	static String caPKCS12savepath=baseCertPath+"ca/rsa_ca.p12";


	//签名算法位数
	static Integer keySize=2048;
	public static void main(String[] args) throws Exception {

		Date notBefore=new Date();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String signHash="SHA256";
		String alg="RSA";
		//生成根CA的证书，因为参数是issuerDN，这里的证书是自签名的
		testCreateCA(issuerDN, notBefore, notAfter, serialNumber, signHash, alg);

		X509Certificate CACert = CertUtil.readX509Cert(caCert_base64);
		PrivateKey privateKey = CertUtil.readPrivateKeyPem(caPrivatePath);
		//给用户创建证书
		testRSA(CACert, privateKey, userDN);
		//创建服务器的证书，该证书由CA签名
		testRSAServer(CACert,privateKey,serverDN);
		//客户端代理证书
		testRSAclientProxy(CACert,privateKey,clientProxyDN);
		//接入网关证书
		testRSAJGateWay(CACert,privateKey,jGateWayProxyDN);
		//融合交换网关证书
		testRSARGateWay(CACert,privateKey,rGateWayProxyDN);
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
		CertUtil.savePKCS12(makeUserSelfSignCert, keyPair.getPrivate(), "rsa_ca", "123456", caPKCS12savepath);

	}

	//创建接入网关证书
	public static void testRSAJGateWay(X509Certificate CACert, PrivateKey privateKey,String jGateWayDN) throws Exception {
		ICA ica=new CAImpl();
		//配置根CA的证书和私钥
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		String signAlg= "SHA256withRSA";
		//给用户签发证书
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), jGateWayDN, notBefore, notAfter, serialNumber, signAlg);
		//保存为用户签发的一系列证书文件，包括用户的公钥私钥等等
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"jGateWayProxy/j_gateway_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"jGateWayProxy/j_gateway.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"jGateWayProxy/j_gateway_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"jGateWayProxy/j_gateway_pub.pem");
		//将用户的相关信息以别名的形式进行存储，存储到密钥库
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "rsa_jgateway", "123456", baseCertPath+"jGateWayProxy/j_gateway.p12");

		System.out.println(CertUtil.verifyUserCert(x509Certificate, CACert.getPublicKey()));
	}
	//创建客户端代理数字证书
	public static void testRSAclientProxy(X509Certificate CACert, PrivateKey privateKey,String clientProxyDN) throws Exception {
		ICA ica=new CAImpl();
		//配置根CA的证书和私钥
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		String signAlg= "SHA256withRSA";
		//给用户签发证书
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), clientProxyDN, notBefore, notAfter, serialNumber, signAlg);
		//保存为用户签发的一系列证书文件，包括用户的公钥私钥等等
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"clientProxy/client_porxy_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"clientProxy/client_porxy.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"clientProxy/client_porxy_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"clientProxy/client_porxy_pub.pem");
		//将用户的相关信息以别名的形式进行存储，存储到密钥库
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "rsa_clientproxy", "123456", baseCertPath+"clientProxy/clientproxy.p12");
		System.out.println(CertUtil.verifyUserCert(x509Certificate, CACert.getPublicKey()));
	}
	//创建融合交换网关证书
	public static void testRSARGateWay(X509Certificate CACert, PrivateKey privateKey,String rGateWayDN) throws Exception {
		ICA ica=new CAImpl();
		//配置根CA的证书和私钥
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		String signAlg= "SHA256withRSA";
		//给用户签发证书
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), rGateWayDN, notBefore, notAfter, serialNumber, signAlg);
		//保存为用户签发的一系列证书文件，包括用户的公钥私钥等等
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"rGateWayProxy/r_gateway_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"rGateWayProxy/r_gateway.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"rGateWayProxy/r_gateway_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"rGateWayProxy/r_gateway_pub.pem");
		//将用户的相关信息以别名的形式进行存储，存储到密钥库
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "rsa_rgateway", "123456", baseCertPath+"rGateWayProxy/r_gateway.p12");

		System.out.println(CertUtil.verifyUserCert(x509Certificate, CACert.getPublicKey()));
	}

	//创建业务服务器证书
	public static void testRSAServer(X509Certificate CACert, PrivateKey privateKey,String serverDN) throws Exception {
		ICA ica=new CAImpl();
		//配置根CA的证书和私钥
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		String signAlg= "SHA256withRSA";
		//给用户签发证书
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), serverDN, notBefore, notAfter, serialNumber, signAlg);
		//保存为用户签发的一系列证书文件，包括用户的公钥私钥等等
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"server/rsa_server_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"server/rsa_server.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"server/rsa_server_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"server/rsa_server_pub.pem");
		//将用户的相关信息以别名的形式进行存储，存储到密钥库
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "rsa_server", "123456", baseCertPath+"server/rsa_server.p12");

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
	 * @throws Exception
	 */
	public static void testRSA(X509Certificate CACert, PrivateKey privateKey,String userDN) throws Exception {
		ICA ica=new CAImpl();
		//配置根CA的证书和私钥
		ica.config(CACert,privateKey);
		KeyPair keyPair = testCreateKeyPair();
		Date notBefore=new Date();
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 20);
		Date notAfter=instance.getTime();
		String serialNumber1 = String.valueOf(System.currentTimeMillis());
		BigInteger serialNumber=new BigInteger(serialNumber1);
		String signAlg= "SHA256withRSA";
		//给用户签发证书
		X509Certificate x509Certificate = ica.makeUserCert(keyPair.getPublic(), CACert.getIssuerDN().toString(), userDN, notBefore, notAfter, serialNumber, signAlg);
		//保存为用户签发的一系列证书文件，包括用户的公钥私钥等等
		CertUtil.saveX509CertBase64(x509Certificate, baseCertPath+"client/rsa_client_base64.cer");
		CertUtil.saveX509CertBinary(x509Certificate, baseCertPath+"client/rsa_client.cer");
		CertUtil.savePrivateKeyPem(keyPair.getPrivate(), baseCertPath+"client/rsa_client_pri.pem");
		CertUtil.savePublicKeyPem(keyPair.getPublic(), baseCertPath+"client/rsa_client_pub.pem");
		//将用户的相关信息以别名的形式进行存储，存储到密钥库
		CertUtil.savePKCS12(x509Certificate, keyPair.getPrivate(), "rsa_client", "123456", baseCertPath+"client/rsa_client.p12");
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
