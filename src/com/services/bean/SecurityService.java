package com.services.bean;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.interfaces.InterfSecurityDao;
import com.entities.Security;

@ManagedBean(name = "securityService")
@ViewScoped
@Service
public class SecurityService extends ObjectService<Security> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private Security security;

	@Autowired
	protected InterfSecurityDao dao;

	@Autowired
	protected Security objectToInsert;

	public SecurityService() {

	}

	/**
	 * @return the security
	 */
	public Security getSecurity() {
		security.setMac_adresse(CalcMacAdresse());
		return security;
	}

	/**
	 * @param security
	 *            the security to set
	 */
	public void setSecurity(Security security) {
		this.security = security;
	}

	public Security getObjectToInsert() {
		return objectToInsert;
	}

	public void setObjectToInsert(Security objectToInsert) {
		this.objectToInsert = objectToInsert;
	}

	public String CalcMacAdresse() {

		InetAddress ip;
		String macAdr = "";
		try {

			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			System.out.print("Current MAC address : ");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			System.out.println(sb.toString());
			macAdr = sb.toString();

		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (SocketException e) {

			e.printStackTrace();

		}

		return macAdr;

	}

	public void create() throws Exception {
		System.out.println("inside create");

		if (objectToInsert != null) {
			System.out.println("inside create");
			dao.createInstance(objectToInsert);
			try {
				listObjects.add(objectToInsert);

			} catch (Exception e) {
				e.printStackTrace();
				listObjects = (List<Security>) dao.findAll();
			}
			Help.msg = "insere avec Succes";
			reset();

		} else {
			throw new Exception("objectToInsert can not be null");
		}
	}

	public void printAll() {
		System.out.println(dao.findAll().size());

	}

	@PostConstruct
	public void init() {

		listObjects = (List<Security>) dao.findAll();
		System.out.println(listObjects.size());

	}

	public void reset() {
		objectToInsert = new Security();

	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		Security editedModele = (Security) event.getObject();

		if (editedModele != null) {
			dao.updateIstance(editedModele);
			Help.msg = "mise a jour faite avec Succes";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Security c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			Help.msg = "supprime avec Succes";

		} else {
			throw new Exception("objectSelected can not be null");
		}
	}
	
	public void test() {
		System.out.println("inside test !");
	}

}
