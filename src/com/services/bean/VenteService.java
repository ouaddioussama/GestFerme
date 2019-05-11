package com.services.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.FilterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Enum.Categorie;
import com.Enum.Types_Operation;
import com.Enum.Types_Reglement;
import com.Enum.Unite;
import com.dao.interfaces.InterfProduitDao;
import com.dao.interfaces.InterfVenteDao;
import com.entities.Achat;
import com.entities.Historique_Prod;
import com.entities.Produit;
import com.entities.Vente;

@ManagedBean(name = "venteService")
@ViewScoped
@Service

public class VenteService extends ObjectService<Vente> implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@Autowired
	protected Vente objectToInsert;

	@Autowired
	protected InterfVenteDao dao;

	@Autowired
	protected InterfProduitDao prodDao;

	@Autowired
	protected ProduitService prodService;

	@Autowired
	protected LoginService loginService;

	private boolean existe_ref_achat;
	private boolean existe_ref_vente;

	private Set<String> listUnite = new HashSet<String>();
	private Set<String> listReglement = new HashSet<String>();

	public VenteService() {

	}

	public Vente getObjectToInsert() {
		return objectToInsert;
	}

	public void setObjectToInsert(Vente objectToInsert) {
		this.objectToInsert = objectToInsert;
	}

	public Set<String> getListUnite() {
		return listUnite;
	}

	public void setListUnite(Set<String> listUnite) {
		this.listUnite = listUnite;
	}

	public Set<String> getListReglement() {
		return listReglement;
	}

	public void setListReglement(Set<String> listReglement) {
		this.listReglement = listReglement;
	}

	public void create() throws Exception {
		System.out.println("inside create");

		if (objectToInsert != null) {

			System.out.println("inside create");
			objectToInsert.setDateOperation(new Date());
			dao.createInstance(objectToInsert);

			// ajout de la quantite actuelle au produit
			Produit p = prodDao.findById(objectToInsert.getProduit().getId());
			if (p != null) {
				addToHisto(p);
				// modifier la valeur actuelle du produit
				double quantite_act = p.getQuantite_actuelle() + objectToInsert.getQuantite();
				p.setQuantite_actuelle(quantite_act);
				prodDao.updateIstance(p);

			}

			try {
				listObjects.add(objectToInsert);

			} catch (Exception e) {
				listObjects = (List<Vente>) dao.findAll();

			}
			Help.msg = "insÃ©rÃ© avec SuccÃ©s";
			reset();

		} else {
			throw new Exception("objectToInsert can not be null");
		}
	}

	public void createLait() throws Exception {

		// List<Produit> prodList = new ArrayList<>();
		if (objectToInsert != null) {
			// if (prodService.getListObjects() != null) {
			try {
				System.out.println("siiiize:" + listObjects.size());

				if (prodService.getListProduitLaitVente() != null && prodService.getListProduitLaitVente().size() > 0) {
					objectToInsert.setProduit(prodService.getListProduitLaitVente().get(0));
				} else {
					Produit p = new Produit("Lait", Categorie.Lait);
					prodDao.createInstance(p);
					objectToInsert.setProduit(p);
				}

				/*
				 * prodList = prodService.getListProduitLaitVente().stream().filter((p) ->
				 * 
				 * p != null && p.getDesignation_prod().equals("Lait")
				 * 
				 * ).collect(Collectors.toList());
				 */

			} catch (Exception e) {
				e.printStackTrace();

			}

			// }

			create();

		}
	}

	@PostConstruct
	public void init() {

		listObjects = (List<Vente>) dao.findAll();
		System.out.println(listObjects.size());

		for (Unite u : Unite.values()) {
			listUnite.add(u.toString());
		}

		for (Types_Reglement t : Types_Reglement.values()) {
			listReglement.add(t.toString());
		}

	}

	public void reset() {
		objectToInsert = new Vente();

	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		Vente editedModele = (Vente) event.getObject();

		if (editedModele != null) {
			dao.updateIstance(editedModele);
			Help.msg = "mise Ã  jour faite avec SuccÃ¨s";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Vente c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			Help.msg = "supprimÃ© avec SuccÃ¨s";

		} else {
			throw new Exception("objectSelected can not be null");
		}
	}

	public void tryMe() {
		System.out.println("inside try me");
		System.out.println(objectToInsert.getRef_bon_achat());

	}

	// Recuperer la liste des achats selon type de produit
	public List<Vente> getListVente(Categorie c) {
		List<Vente> listVente = null;
		if (listObjects != null) {
			try {
				System.out.println("siiiize:" + listObjects.size());
				listVente = listObjects.stream().filter((p) ->

				p != null && p.getProduit() != null && p.getProduit().getCategorie() == c

				).collect(Collectors.toList());

			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		return listVente;
	}

	public List<Vente> getListVenteAgricole() {

		return getListVente(Categorie.Agricole_Vente);

	}

	public List<Vente> getListVenteAnimeaux() {
		try {
			System.out.println("getListVenteAnimeaux length:" + getListVente(Categorie.Animeaux_Vente).size());

		} catch (Exception e) {
			// TODO: handle exception
		}

		return getListVente(Categorie.Animeaux_Vente);

	}

	public List<Vente> getListVenteAuto() {

		return getListVente(Categorie.Auto_Vente);

	}

	public List<Vente> getListVenteAutre() {

		return getListVente(Categorie.Autre_Vente);

	}

	public List<Vente> getListVenteLait() {

		return getListVente(Categorie.Lait);

	}

	/** List Vente 
	 * @throws ParseException **/
	public List<Vente> getListVenteAgricoleD(int nbr) throws ParseException {

		return getListVenteDate(nbr, Categorie.Animeaux_Achat);

	}

	public List<Vente> getListVenteAnimeauxD(int nbr) throws ParseException {
		try {
			System.out.println("getListVenteAnimeaux length:" + getListVente(Categorie.Animeaux_Vente).size());

		} catch (Exception e) {
			// TODO: handle exception
		}

		return getListVenteDate(nbr,Categorie.Animeaux_Vente);

	}

	public List<Vente> getListVenteAutoD(int nbr) throws ParseException {

		return getListVenteDate(nbr,Categorie.Auto_Vente);

	}

	public List<Vente> getListVenteAutreD(int nbr) throws ParseException {

		return getListVenteDate(nbr,Categorie.Autre_Vente);

	}

	public List<Vente> getListVenteLaitD(int nbr) throws ParseException {

		return getListVente(Categorie.Lait);

	}
	
	public void calclMontant() {
		if (objectToInsert != null) {
			objectToInsert.setMontant_global(objectToInsert.getPrix_unitaire() * objectToInsert.getQuantite());
		}
	}

	public void onRowSelect(SelectEvent event) throws IOException {
		System.out.println("inside rowSelect");

		Vente editedModele = (Vente) event.getObject();
		// System.out.println(editedModele.toString());
		if (editedModele != null) {
			// objectSelected = editedModele;
		}

		System.out.println("ref:" + objectSelected.getRef_bon_achat());

	}

	public void onFilter(FilterEvent filterEvent) {
		System.out.println("inside filter");
	}

	// Recuperer la liste des achats selon type de produit
	public void verifRefAchat() throws BreakException {
		System.out.println("inside verifRefAchat");
		System.out.println("ref Bon:" + objectToInsert.getRef_bon_achat());
		if (listObjects != null && objectToInsert != null) {
			try {

				//
				listObjects.stream().forEach((p) -> {
					existe_ref_achat = objectToInsert.getRef_bon_achat() == null ? false
							: objectToInsert.getRef_bon_achat().intValue() == p.getRef_bon_achat().intValue() ? true
									: false;
					System.out.println("ref inside loop:" + p.getRef_bon_achat().intValue());
					if (existe_ref_achat) {
						throw new BreakException();
					}

				}

				);

			} catch (BreakException e) {
				Help.error_msg = "reference d'achat dÃ©ja existant!" + "\n";
				Help.error_msg2 = " Ø±Ù‚Ù… Ù�Ø§ØªÙˆØ±Ø© Ø§Ù„Ø´Ø±Ø§Ø¡ Ù…ÙˆØ¬ÙˆØ¯ Ù…Ø³Ø¨Ù‚Ø§Ù‹  Ù�ÙŠ Ù„Ø§Ø¦Ø­Ø© Ø§Ù„Ø´Ø±Ø§Ø¡";
				objectToInsert.setRef_bon_achat(null);
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// Recuperer la liste des achats selon type de produit
	public void verifRefVente() throws BreakException {
		System.out.println("inside verifRefAchat");
		if (listObjects != null && objectToInsert != null) {
			try {

				//
				listObjects.stream().forEach((p) -> {
					if (objectToInsert.getRef_bon_vente() != null && objectToInsert != null
							&& p.getRef_bon_vente() != null && p != null) {
						existe_ref_vente = objectToInsert.getRef_bon_vente().intValue() == p.getRef_bon_vente()
								.intValue();
						System.out.println("ref inside loop:" + p.getRef_bon_vente().intValue());

					}

					if (existe_ref_vente) {
						throw new BreakException();
					}

				}

				);

			} catch (BreakException e) {
				Help.error_msg = "reference de vente deja existant!" + "\n";
				Help.error_msg2 = " Ø±Ù‚Ù… Ù�Ø§ØªÙˆØ±Ø© Ø§Ù„Ø¨ÙŠØ¹  Ù…ÙˆØ¬ÙˆØ¯ Ù…Ø³Ø¨Ù‚Ø§Ù‹  Ù�ÙŠ Ù„Ø§Ø¦Ø­Ø© Ø§Ù„Ø¨ÙŠØ¹";
				objectToInsert.setRef_bon_achat(null);
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void updateFiche() {
		System.out.println("inside updateFiche ");
		System.out.println(objectSelected.getMontant_reglement());
		System.out.println(objectSelected.getDateDePaiement());
		System.out.println(objectSelected.getRef_reglement());

	}

	public void addToHisto(Produit p) throws Exception {
		double quantite_act = p.getQuantite_actuelle() + objectToInsert.getQuantite();

		Historique_Prod hp = new Historique_Prod();
		hp.setDate_operation(new Date());
		hp.setProduit(p);
		hp.setQuantite_actuel(Double.valueOf(quantite_act));
		hp.setType_operation(Types_Operation.DIMINUER);
		hp.setQuantite_precedente(p.getQuantite_actuelle());
		hp.setQuantite_operation(objectToInsert.getQuantite());
		System.out.println("Employee:²" + loginService.getEmployeetoLog().getNom());
		// hp.setUser_logged(loginService.getEmployeetoLog());

		// faire appel à la methode create du Service HistoriqueService
		HistoriqueService histoS = new HistoriqueService();
		histoS.setObjectToInsert(hp);
		histoS.create();
		histoS.getListObjects().add(hp);

	}
	
	// Recuperer la liste des achats selon type de produit
	public List<Vente> getListVenteDate(int nbreDay, Categorie c) throws ParseException {
		System.out.println("debut:" + new Date());
		List<Vente> listAll = new ArrayList<Vente>();

		// Date currentD = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date currentD = sdf.parse(sdf.format(new Date()));
		//listObjects = (List<Achat>) dao.findAll();
		if (listObjects != null) {
			try {
				// System.out.println(currentD);
				// System.out.println(addDays(currentD, nbreDay));

				// listAchat.forEach(p -> System.out.println(p.getDateDePaiement()));

				for (Vente a : listObjects) {
					if (nbreDay ==1 && (sdf.parse(sdf.format(a.getDateDePaiement())).equals((currentD)))
							&& (a.getProduit().getCategorie() == c)) {
						listAll.add(a);
					} else if (nbreDay !=1
							&& sdf.parse(sdf.format(a.getDateDePaiement())).after((addDays(currentD, 0)))
							&& sdf.parse(sdf.format(a.getDateDePaiement())).before((addDays(currentD, nbreDay+1)))

							&& (a.getProduit().getCategorie() == c)

					) {
						listAll.add(a);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

		}
		System.out.println("fin:" + new Date());

		return listAll;
	}
	
	public Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}
}
