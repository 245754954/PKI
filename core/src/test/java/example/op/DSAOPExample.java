package example.op;



import nudt.core.api.AsymmetricalSignature;
import nudt.core.api.impl.DSA;
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
public class DSAOPExample {
	public static void main(String[] args) throws Exception {
		//存储基础目录
		 String client="E:/client/cert/dsa/client/client.p12";
		 KeyStore keyStore = CertUtil.readKeyStore(client, "123456");
		 String alias="taoyuanx-client";
		 String password="123456";
		 String data=randomData(30);
		 String signAlgorithm="SHA1WITHDSA";
		 testOP(CertUtil.getPublicKey(keyStore,alias ), CertUtil.getPrivateKey(keyStore, password, alias), data,signAlgorithm);
	}
	public static void testOP(PublicKey publicKey,PrivateKey privateKey,String data,String signAlgorithm) throws Exception {
		AsymmetricalSignature op=new DSA();
		//SHA1算法产生数据摘要，然后DSA算法签名
		byte[] sign = op.sign(data.getBytes(), privateKey, signAlgorithm);
		//签名结果转换为base64字符串
		String signData= ByteStringUtil.toString(sign, ByteStringUtil.BASE64);
		//验证签名
		boolean verifySign = op.verifySign(sign, data.getBytes(), publicKey, signAlgorithm);
		
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
