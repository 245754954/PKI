package nudt.web.config;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import lombok.Data;

/**
 * @author 都市桃源
 * 2018年9月14日 下午2:08:00
 * ca 配置
*/
@Data
public class CaConfig {
	/**
	 * pub 公钥
	 * pri 私钥
	 * issuerDN 签发者dn
	 * defaultSignAlg 默认签名算法
	 * caType ca类型参见 CAConstant
	 */
	private PublicKey pub;
	private PrivateKey pri;
	private X509Certificate cert;
	private String issuerDN;

	/**
	 * certPath 根证书地址
	 * priPath 根私钥地址
	 * clientCertBasePath 签发客户端证书存储地址
	 * signAlg 默认签名算法
	 */
	private String certPath;
	private String priPath;
	private String clientCertBasePath;
	private String signAlg;


	public PublicKey getPub() {
		return pub;
	}

	public void setPub(PublicKey pub) {
		this.pub = pub;
	}

	public PrivateKey getPri() {
		return pri;
	}

	public void setPri(PrivateKey pri) {
		this.pri = pri;
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}

	public String getIssuerDN() {
		return issuerDN;
	}

	public void setIssuerDN(String issuerDN) {
		this.issuerDN = issuerDN;
	}

	public String getCertPath() {
		return certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	public String getPriPath() {
		return priPath;
	}

	public void setPriPath(String priPath) {
		this.priPath = priPath;
	}

	public String getClientCertBasePath() {
		return clientCertBasePath;
	}

	public void setClientCertBasePath(String clientCertBasePath) {
		this.clientCertBasePath = clientCertBasePath;
	}

	public String getSignAlg() {
		return signAlg;
	}

	public void setSignAlg(String signAlg) {
		this.signAlg = signAlg;
	}
}
