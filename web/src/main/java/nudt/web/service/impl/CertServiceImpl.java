package nudt.web.service.impl;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;


import nudt.core.exception.CertException;
import nudt.core.openssl.cert.CertUtil;
import nudt.web.common.CAConstant;
import nudt.web.config.AppConfig;
import nudt.web.config.CaConfig;
import nudt.web.dto.CertReq;
import nudt.web.dto.CertResult;
import nudt.web.dto.KeyPairResult;
import nudt.web.entity.Cert;
import nudt.web.repository.CertRepository;
import nudt.web.service.CertService;
import nudt.web.util.KeyPairUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CertServiceImpl implements CertService {
//	Map<KeyType,CaConfig> allCaConfig
	//allConfig是一个HashMap类型，每一个key对应了该类型的算法下，根证书的相关信息

	@Autowired
	AppConfig appConfig;

	@Autowired
	CertRepository certRepository;
	@Override
	public CertResult certToUser(CertReq req, Integer keySize) throws Exception
	{
		//获取某类型算法下CA配置文件的相关信息，这里caType有四种类型，0-3,返回该类型下根CA的证书信息
		//这个CaConfig对象包含根CA的公钥，私钥，证书文件，根CA的Distinguished Name，以根证书的存放路径
		//以及签名算法的类型
		CaConfig caConfig = appConfig.getByKeyType(req.getCatype());

		//certResult包含用户公钥私钥以及用户的签名后的证书
		CertResult certResult = new CertResult();
		//决定签名算法
		String signAlg = req.getSignAlg() == null ? caConfig.getSignAlg() : req.getSignAlg();

		//产生密钥对类型，用于加密解密
		KeyPairResult keyPairResult = KeyPairUtil.gen(CAConstant.KeyType.forValue(req.getCatype()), keySize);
		//产生用户公钥
		PublicKey pub = keyPairResult.getPub();
		//产生用户私钥
		PrivateKey pri = keyPairResult.getPri();

		//创建用户公钥对应的CA签字的证书，参数分别代表1：用户公钥，2：CA公钥 ，3：CA的私钥 ，4：CA的私钥，
		//5：CA的相关信息，也就是Distinguished Name，6：用户的相关信息，7：证书的起始日期，8：证书的结束日期
		//9：证书的序列号
		//10：证书的签名算法
		X509Certificate userCert = CertUtil.makeUserCert(pub, caConfig.getPub(), caConfig.getPri(),
				caConfig.getIssuerDN(), req.getUserDN(), req.getNotBefore(), req.getNotAfter(),
				new BigInteger(req.getSerialNumber()), signAlg);

		//设置用户私钥
		certResult.setPri(pri);
		//设置用户公钥
		certResult.setPub(pub);
		//设置用户的数字证书x.509格式
		certResult.setCert(userCert);
		//返回公钥私钥以及证书
		return certResult;

	}

	@Override
	public KeyPairResult createKeyPair(CAConstant.KeyType type, Integer keySize) throws Exception {
		KeyPairResult keyPairResult = KeyPairUtil.gen(type, keySize);
		return keyPairResult;
}

	@Override
	public Cert findCertBySerialNumber(String serialNumber) {

		return certRepository.findCertBySerialNumber(serialNumber);
	}

	@Override
	public Cert findCertByUsername(String username) {
		return certRepository.findCertByUsername(username);
	}

	@Override
	public void save(Cert cert) {
		certRepository.save(cert);
	}

	@Override
	@Transactional
	public void deleteBySerialNumber(String serialNumber) {
		certRepository.deleteBySerialNumber(serialNumber);
	}



	//判断证书的有效期,查看证书是否已经超过有效期
	@Override
	public boolean verifyCertificateValidity(X509Certificate certificate) throws CertificateNotYetValidException {

		String s = CertUtil.judgeValidity(certificate);
		if("ok".equals(s))
		{
			return true;
		}
		return false;
	}


	//利用身份认证与管理服务器的公钥来验证数字证书签名的有效性
	@Override
	public boolean verifyUserCert(X509Certificate userCert, PublicKey CAPublicKey) throws CertException {


		Boolean b =CertUtil.verifyUserCert(userCert,CAPublicKey);

		return b;
	}


}
