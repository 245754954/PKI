package example.op;


import nudt.core.api.AsymmetricalCipher;
import nudt.core.api.AsymmetricalSignature;
import nudt.core.api.impl.ECDSA;
import nudt.core.openssl.cert.CertUtil;
import nudt.core.util.ByteStringUtil;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;


/**
 * ecdsa 操作示例
 * @author 都市桃源
 * 2018年7月1日下午11:27:27
 * TODO
 */
public class ECDSAOPExample {
	//KeyPairGeneratorSpi
	//ECUtil
	public static void main(String[] args) throws Exception {
		//存储基础目录
		 String client="E:/client/cert/ecdsa/client/client.p12";
		 KeyStore keyStore = CertUtil.readKeyStore(client, "123456");
		 String alias="taoyuanx-client";
		 String password="123456";
		 String data=randomData(4096);
		 String signAlgorithm="SHA1WITHECDSA";
		 testOP(CertUtil.getPublicKey(keyStore,alias ), CertUtil.getPrivateKey(keyStore, password, alias), data,signAlgorithm);
	}
	public static void testOP(PublicKey publicKey,PrivateKey privateKey,String data,String signAlgorithm) throws Exception {
		
		AsymmetricalCipher op=(AsymmetricalCipher)new ECDSA();
		//公钥加密
		byte[] encrypt = op.encrypt(data.getBytes(), publicKey);
		//将加密字节数组转换成base64
		String enData= ByteStringUtil.toString(encrypt, ByteStringUtil.BASE64);
		//私钥解密
		String deData=new String(op.decrypt(encrypt, privateKey));

		AsymmetricalSignature as=(AsymmetricalSignature)op;
		byte[] sign = as.sign(data.getBytes(), privateKey, signAlgorithm);
		String signData=ByteStringUtil.toString(sign, ByteStringUtil.BASE64);
		boolean verifySign = as.verifySign(sign, data.getBytes(), publicKey, signAlgorithm);
		
		System.out.println("原文:"+data);
		System.out.println("密文:"+enData);
		System.out.println("解密后:"+deData);
		System.out.println("签名:"+signData);
		System.out.println("验签结果:"+verifySign);
	}
	public static  char[] ch= {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','l','r','s','t','u','v'
			,'w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','L','R','S','T','U','V'
			,'W','X','Y','Z','企','附','牲','毅','门','床','朋','祥','调','怕','胞','捞','拣','酒','卜'
			,'厘','垄','里','叶','试','溉','理','曲','壳','灭','国','昂','症','亏','恼'};
	public static String randomData(int len) {
		int size=ch.length;
		Random random = new Random();
		StringBuilder buf=new StringBuilder();
		for(int i=0;i<len;i++) {
			buf.append(ch[random.nextInt(size)]);
		}
		return buf.toString();
		
	}
}
