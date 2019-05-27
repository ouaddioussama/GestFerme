package com.services.bean;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.component.export.PDFOptions;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Enum.Categorie;
import com.Enum.Types_Operation;
import com.Enum.Types_Reglement;
import com.Enum.Unite;
import com.dao.interfaces.InterfAchatDao;
import com.dao.interfaces.InterfProduitDao;
import com.entities.Achat;
import com.entities.Historique_Prod;
import com.entities.Produit;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.ColumnText;
//import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
//import com.lowagie.text.Paragraph;

@ManagedBean(name = "achatService")
@ViewScoped
@Service

public class AchatService extends ObjectService<Achat> implements Serializable, SelectableDataModel<Achat> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	@Autowired
	protected Achat objectToInsert;

	private List<Achat> achatListDate = null;

	private PDFOptions pdfOpt;

	// private List<Achat> filtered;

	@Autowired
	protected InterfAchatDao dao;

	@Autowired
	protected InterfProduitDao prodDao;
	@Autowired
	protected LoginService loginService;

	private Set<String> listUnite = new HashSet<String>();
	private Set<String> listReglement = new HashSet<String>();
	boolean existe = false;
	boolean hide = false;
	private boolean paginatorActive = true;

	/** utilisÃ© dans la vÃ©rification d'existence dans la liste des achats **/

	public AchatService() {

	}

	public Achat getObjectToInsert() {
		return objectToInsert;
	}

	public void setObjectToInsert(Achat objectToInsert) {
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

	public List<Achat> getAchatListDate() {
		return achatListDate;
	}

	public void setAchatListDate(List<Achat> achatListDate) {
		this.achatListDate = achatListDate;
	}

	public boolean getHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public void changeHide() {
		System.out.println("inside setHide");
		this.setHide(true);
		System.out.println(this.getHide());
	}

	public void resetHide() {
		System.out.println("inside resetHide");
		this.setHide(false);
		System.out.println(this.getHide());
	}

	public void activatePaginator() {
		paginatorActive = true;
	}

	public void deactivatePaginator() {
		paginatorActive = false;
	}

	public boolean isPaginatorActive() {
		return paginatorActive;
	}

	public PDFOptions getPdfOpt() {
		return pdfOpt;
	}

	public void setPdfOpt(PDFOptions pdfOpt) {
		this.pdfOpt = pdfOpt;
	}

	public void customizationOptions() {
		pdfOpt = new PDFOptions();
		pdfOpt.setFacetBgColor("#F88017");
		pdfOpt.setFacetFontColor("#0000ff");
		pdfOpt.setFacetFontStyle("BOLD");
		pdfOpt.setCellFontSize("12");
	}

	public void create() throws Exception {
		System.out.println("inside create");

		if (objectToInsert != null) {

			System.out.println("inside create");
			objectToInsert.setDateOperation(new Date());
			dao.createInstance(objectToInsert);

			if (loginService.getEmployeetoLog() != null) {
				objectToInsert.setUser_logged(loginService.getEmployeetoLog());
			}

			// ajout de la quantite actuelle au produit
			Produit p = prodDao.findById(objectToInsert.getProduit().getId());
			if (p != null) {
				// ajouter à l'historique
				addToHisto(p);
				// modifier la valeur actuelle du produit
				double quantite_act = p.getQuantite_actuelle() + objectToInsert.getQuantite();
				p.setQuantite_actuelle(quantite_act);
				prodDao.updateIstance(p);

			}

			try {
				listObjects.add(objectToInsert);

			} catch (Exception e) {
				listObjects = (List<Achat>) dao.findAll();

			}
			Help.msg = "insere avec Succes";
			reset();

		} else {
			throw new Exception("objectToInsert can not be null");
		}
	}

	@PostConstruct
	public void init() {
		customizationOptions();

		dataModel = new LazyDataModel<Achat>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Achat> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				// TODO Auto-generated method stub
				setRowCount(dao.getCountAll());
				return dao.LazyList(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder));
			}

		};

		listObjects = (List<Achat>) dao.findAll();
		System.out.println(listObjects.size());

		for (Unite u : Unite.values()) {
			listUnite.add(u.toString());
		}

		for (Types_Reglement t : Types_Reglement.values()) {
			listReglement.add(t.toString());
		}

	}

	public List<Achat> getListAchatByDate(long nbr) {
		List<Achat> ListAchatDate = (List<Achat>) dao.getAllByDate(nbr);
		return ListAchatDate;
	}

	public void reset() {
		objectToInsert = new Achat();

	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		Achat editedModele = (Achat) event.getObject();

		if (editedModele != null) {
			dao.updateIstance(editedModele);
			Help.msg = "mise a jour faite avec Succes";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Achat c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			Help.msg = "supprime avec Succes";

		} else {
			throw new Exception("objectSelected can not be null");
		}
	}

	public void tryMe() {
		System.out.println("inside try me");
		System.out.println(objectToInsert.getRef_bon_achat());

	}

	// Recuperer la liste des achats selon type de produit
	public List<Achat> getListAchat(Categorie c) {
		List<Achat> listAchat = null;
		if (listObjects != null) {
			try {
				listAchat = listObjects.stream().filter((p) ->

				p.getProduit().getCategorie() == null ? null : p.getProduit().getCategorie() == c

				).collect(Collectors.toList());

			} catch (Exception e) {

			}

		}

		return listAchat;
	}

	// Recuperer la liste des achats selon type de produit
	public List<Achat> getListAchatDate(int nbreDay, Categorie c) throws ParseException {
		System.out.println("debut:" + new Date());
		List<Achat> listAll = new ArrayList<Achat>();

		// Date currentD = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date currentD = sdf.parse(sdf.format(new Date()));
		// listObjects = (List<Achat>) dao.findAll();
		if (listObjects != null) {
			try {
				// System.out.println(currentD);
				// System.out.println(addDays(currentD, nbreDay));

				// listAchat.forEach(p -> System.out.println(p.getDateDePaiement()));

				for (Achat a : listObjects) {
					if (nbreDay == 1 && (sdf.parse(sdf.format(a.getDateDePaiement())).equals((currentD)))
							&& (a.getProduit().getCategorie() == c)) {
						listAll.add(a);
					} else if (nbreDay != 1
							&& sdf.parse(sdf.format(a.getDateDePaiement())).after((addDays(currentD, 0)))
							&& sdf.parse(sdf.format(a.getDateDePaiement())).before((addDays(currentD, nbreDay + 1)))

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

	public List<Achat> getListAchatAgricole() {
		title = "List des Achats des materiels agricols";
		Arabictitle = "قائمة المشتريات الخاصة بالمواد الفلاحية";

		return getListAchat(Categorie.Agricole_Achat);

	}

	public List<Achat> getListAnimeaux() {
		title = "List des Achats des Animaux";
		Arabictitle = "قائمة المشتريات الخاصة بالمواشي";
		return getListAchat(Categorie.Animeaux_Achat);

	}

	public List<Achat> getListAchatAuto() {

		return getListAchat(Categorie.Auto_Achat);

	}

	public List<Achat> getListAchatAutre() {

		return getListAchat(Categorie.Autre_Achat);

	}

	/**
	 * List of Date
	 * 
	 * @throws ParseException
	 **/

	public List<Achat> getListAchatAgricoleD(int nbr) throws ParseException {

		return getListAchatDate(nbr, Categorie.Agricole_Achat);

	}

	public List<Achat> getListAnimeauxD(int nbr) throws ParseException {

		return getListAchatDate(nbr, Categorie.Animeaux_Achat);

	}

	public List<Achat> getListAchatAutoD(int nbr) throws ParseException {

		return getListAchatDate(nbr, Categorie.Auto_Achat);

	}

	public List<Achat> getListAchatAutreD(int nbr) throws ParseException {

		return getListAchatDate(nbr, Categorie.Autre_Achat);

	}

	public void onRowSelect(SelectEvent event) throws IOException {

		Achat editedModele = (Achat) event.getObject();
		// System.out.println(editedModele.toString());
		if (editedModele != null) {
			// objectSelected=editedModele;
		}

	}

	public void calclMontant() {
		if (objectToInsert != null) {
			objectToInsert.setMontant_global(objectToInsert.getPrix_unitaire() * objectToInsert.getQuantite());
		}
	}

	public void verifRefAchatAgricole() throws BreakException {
		verifRefAchat(Categorie.Agricole_Achat);

	}

	public void verifRefAchatAnimaux() throws BreakException {
		verifRefAchat(Categorie.Animeaux_Achat);

	}

	public void verifRefAchatLait() throws BreakException {
		verifRefAchat(Categorie.Lait);

	}

	public void verifRefAchatTransport() throws BreakException {
		verifRefAchat(Categorie.Auto_Achat);

	}

	public void verifRefAchatAutre() throws BreakException {
		verifRefAchat(Categorie.Autre_Achat);
	}

	// Recuperer la liste des achats selon type de produit
	public void verifRefAchat(Categorie c) throws BreakException {
		System.out.println("inside verifRefAchat");
		System.out.println("ref Bon:" + objectToInsert.getRef_bon_achat());
		if (listObjects != null && objectToInsert != null) {
			try {
				System.out.println(listObjects.size());
				//
				listObjects.stream().filter(f -> f.getProduit().getCategorie() == c).forEach((p) -> {
					System.out.println(p.getRef_bon_achat().intValue());
					existe = objectToInsert.getRef_bon_achat() == null ? false
							: objectToInsert.getRef_bon_achat().intValue() == p.getRef_bon_achat().intValue() ? true
									: false;
					System.out.println("ref inside loop:" + p.getRef_bon_achat().intValue());
					if (existe) {
						throw new BreakException();
					}

				}

				);

			} catch (BreakException e) {
				Help.error_msg = "reference d'achat deja existant!" + "\n";
				Help.error_msg2 = "هدا الرقم موجود مسبقاً";
				objectToInsert.setRef_bon_achat(null);
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void addToHisto(Produit p) throws Exception {
		double quantite_act = p.getQuantite_actuelle() + objectToInsert.getQuantite();

		Historique_Prod hp = new Historique_Prod();
		hp.setDate_operation(new Date());
		hp.setProduit(p);
		hp.setQuantite_actuel(Double.valueOf(quantite_act));
		hp.setType_operation(Types_Operation.AUGMENTER);
		hp.setQuantite_precedente(p.getQuantite_actuelle());
		hp.setQuantite_operation(objectToInsert.getQuantite());
		System.out.println("Employee:²" + loginService.getEmployeetoLog().getNom());
		if (loginService.getEmployeetoLog() != null) {
			hp.setUser_logged(loginService.getEmployeetoLog());
		}

		// faire appel à la methode create du Service HistoriqueService
		HistoriqueService histoS = new HistoriqueService();
		histoS.setObjectToInsert(hp);
		histoS.create();
		histoS.getListObjects().add(hp);

	}

	public Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	@Override
	public Achat getRowData(String rowKey) {
		for (Achat mandatory : dataModel) {
			if (mandatory.getId().toString().equals(rowKey))
				return mandatory;
		}
		return null;
	}

	@Override
	public Object getRowKey(Achat mandatory) {
		return mandatory;
	}



	public void postProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
		Document pdf = (Document) document;
		pdf.open();
		// pdf.setPageSize(PageSize.A4);

		// pdf.add(new Paragraph("fin in the rectangle." +
		// listObjects.get(0).getMontant_global()));

	}


	}


