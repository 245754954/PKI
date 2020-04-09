package example.op;



import nudt.core.api.AsymmetricalCipher;
import nudt.core.api.AsymmetricalSignature;
import nudt.core.api.impl.RSA;
import nudt.core.openssl.cert.CertUtil;
import nudt.core.util.ByteStringUtil;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;



/**
 * rsa 操作示例
 * @author 都市桃源
 * 2018年7月1日下午11:27:27
 * TODO
 */
public class RSAOPExample {
	public static void main(String[] args) throws Exception {
		//存储基础目录
		 String client="E:\\client\\cert\\rsa\\client\\client.p12";
		 KeyStore keyStore = CertUtil.readKeyStore(client, "123456");
		 String alias="taoyuanx-client";
		 String password="123456";
		 String data=randomData(512);
		 //SHA1withRSA:浅显的理解，用SHA算法进行签名，用RSA算法进行加密。
		 String signAlgorithm="SHA1WITHRSA";

		 testOP(CertUtil.getPublicKey(keyStore,alias ), CertUtil.getPrivateKey(keyStore, password, alias), data,signAlgorithm);
	}
	public static void testOP(PublicKey publicKey,PrivateKey privateKey,String data,String signAlgorithm) throws Exception {
		AsymmetricalCipher op=new RSA();
		//RSA公钥加密
		byte[] encrypt = op.encrypt(data.getBytes(), publicKey);
		//将RSA加密后的字节数组转换成base64字符串，也可以转换成其他类型，这里有三种选择
//		public static final int BASE64=1;
//		public static final int HEX=2;
//		public static final int ASCII=3;
		String enData= ByteStringUtil.toString(encrypt, ByteStringUtil.BASE64);

		//对加密后的数据，采用私钥解密
		String deData=new String(op.decrypt(encrypt, privateKey));

		AsymmetricalSignature as=(AsymmetricalSignature) op;
		// //SHA1withRSA:浅显的理解，用SHA算法进行签名，用RSA算法进行加密。
		//这里对于铭文数据，首先采用SHA1算法产生签名，然后对于签名用RSA进行加密
		byte[] sign = as.sign(data.getBytes(), privateKey, signAlgorithm);
		//对于签名加密后的结果转换成base64编码
		String signData= ByteStringUtil.toString(sign, ByteStringUtil.BASE64);
		//验证签名的结果，来验证元数据是否被修改，也就是该签名是否和数据对应
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
