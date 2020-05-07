package nudt.web.service;


import nudt.core.exception.CertException;
import nudt.web.common.CAConstant;
import nudt.web.dto.CertReq;
import nudt.web.dto.CertResult;
import nudt.web.dto.KeyPairResult;
import nudt.web.entity.Cert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.PublicKey;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

/**
 * @author 都市桃源
 * 2018年9月14日 下午1:37:18
 *证书服务
*/

public interface CertService {
	//给用户的公钥签名
	CertResult certToUser(CertReq req, Integer keySize) throws Exception;
	//创建公钥私钥对
	KeyPairResult createKeyPair(CAConstant.KeyType type, Integer keySize) throws Exception;


	//根据证书的序列号，查找与证书相关的信息
	public Cert findCertBySerialNumber(String serialNumber);

	//根据用户的名字或者个体的名字来查找证书的相关信息
	public  Cert findCertByUsername(String username);

	//保存证书的相关信息
	public void save(Cert cert);

	//根据数字证书的序列号，删除数据库的数字证书记录
	public void deleteBySerialNumber(String serialNumber);



	public boolean verifyCertificateValidity(X509Certificate certificate) throws CertificateNotYetValidException;


	public boolean verifyUserCert( X509Certificate userCert, PublicKey CAPublicKey) throws CertException;

	//根据用户的名字删除用户的所有证书文件
	public  boolean certRevoke(String username) throws CertException;

}
