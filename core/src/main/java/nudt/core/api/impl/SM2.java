package nudt.core.api.impl;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;



import nudt.core.api.AsymmetricalCipher;
import nudt.core.api.AsymmetricalSignature;
import nudt.core.api.AsymmetricalUtil;
import nudt.core.sm.interfaces.impl.Sm2PrivateKeyImpl;
import nudt.core.sm.interfaces.impl.Sm2PublicKeyImpl;
import nudt.core.sm.util.Sm2Util;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;



/**
 * sm2非对称操作 实现
 * @author 都市桃源
 * 2018年7月1日下午10:36:40
 * TODO
 */
public class SM2 implements AsymmetricalCipher,AsymmetricalSignature {
	
	@Override
	public byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
		return Sm2Util.encrypt( new Sm2PublicKeyImpl((BCECPublicKey) publicKey), data);
		
	}

	@Override
	public byte[] decrypt(byte[] data, PrivateKey privateKey) throws Exception {
		return Sm2Util.decrypt(new Sm2PrivateKeyImpl((BCECPrivateKey) privateKey), data);
	}

	@Override
	public byte[] sign(byte[] data, PrivateKey privateKey, String signAlgorithm) throws Exception {
		Signature signature = AsymmetricalUtil.getSignatureInstance(signAlgorithm);
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}

	@Override
	public boolean verifySign(byte[] signData, byte[] content, PublicKey publicKey, String signAlgorithm) throws Exception {
		Signature signature = AsymmetricalUtil.getSignatureInstance(signAlgorithm);
		signature.initVerify(publicKey);
		signature.update(content);
		return signature.verify(signData);
	}



}
