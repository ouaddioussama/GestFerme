package com.services.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.component.export.PDFOptions;
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
import com.entities.Fich_Remb_Lait;
import com.entities.Historique_Prod;
import com.entities.Produit;
import com.entities.Vente;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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
	protected Fich_Remb_Lait objectTosearch;

	boolean existe = false;

	private ArrayList<Vente> filtredAchatAgricole = new ArrayList<Vente>();
	private ArrayList<Vente> filtredAchatAnimaux = new ArrayList<Vente>();;
	private ArrayList<Vente> filtredAchatAuto = new ArrayList<Vente>();;
	private ArrayList<Vente> filtredAchatAutre = new ArrayList<Vente>();;

	private String[] headers = { "Numero \n الرقم ", "ref_Bon \n مرجع البيع", "Produit \n المنتوج", "Client \n الزبون",
			"DateVente \n تاريخ البيع ", "quantite  \n  كمية ", "Montant \n  المبلغ" };

	/**
	 * utilisé dans le calcul de la somme du montant depuis date debut vers date de
	 * fin
	 **/

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

	public Fich_Remb_Lait getObjectTosearch() {
		return objectTosearch;
	}

	public void setObjectTosearch(Fich_Remb_Lait objectTosearch) {
		this.objectTosearch = objectTosearch;
	}

	public ArrayList<Vente> getFiltredAchatAgricole() {
		return filtredAchatAgricole;
	}

	public void setFiltredAchatAgricole(ArrayList<Vente> filtredAchatAgricole) {
		this.filtredAchatAgricole = filtredAchatAgricole;
	}

	public ArrayList<Vente> getFiltredAchatAnimaux() {
		return filtredAchatAnimaux;
	}

	public void setFiltredAchatAnimaux(ArrayList<Vente> filtredAchatAnimaux) {
		this.filtredAchatAnimaux = filtredAchatAnimaux;
	}

	public ArrayList<Vente> getFiltredAchatAuto() {
		return filtredAchatAuto;
	}

	public void setFiltredAchatAuto(ArrayList<Vente> filtredAchatAuto) {
		this.filtredAchatAuto = filtredAchatAuto;
	}

	public ArrayList<Vente> getFiltredAchatAutre() {
		return filtredAchatAutre;
	}

	public void setFiltredAchatAutre(ArrayList<Vente> filtredAchatAutre) {
		this.filtredAchatAutre = filtredAchatAutre;
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
				addToHisto(p);
				// modifier la valeur actuelle du produit
				double quantite_act = p.getQuantite_actuelle() + objectToInsert.getQuantite();
				p.setQuantite_actuelle(quantite_act);
				prodDao.updateIstance(p);

			}

			try {
				listObjects.add(objectToInsert);
				updatefilterswhenAdd();

			} catch (Exception e) {
				listObjects = (List<Vente>) dao.findAll();

			}
			Help.msg = "insere avec Succes";
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

		initFilters();

	}

	public void reset() {
		objectToInsert = new Vente();

	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		Vente editedModele = (Vente) event.getObject();

		if (editedModele != null) {
			dao.updateIstance(editedModele);
			updateLists(editedModele);

			Help.msg = "mise e  jour faite avec Succes";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Vente c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			Help.msg = "supprime© avec Succes";

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

	public void initFilters() {
		filtredAchatAgricole = (ArrayList<Vente>) getListVente(Categorie.Agricole_Vente);
		filtredAchatAnimaux = (ArrayList<Vente>) getListVente(Categorie.Animeaux_Vente);
		filtredAchatAuto = (ArrayList<Vente>) getListVente(Categorie.Auto_Vente);
		filtredAchatAutre = (ArrayList<Vente>) getListVente(Categorie.Autre_Vente);

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

	/**
	 * List Vente
	 * 
	 * @throws ParseException
	 **/
	public List<Vente> getListVenteAgricoleD(int nbr) throws ParseException {

		return getListVenteDate(nbr, Categorie.Agricole_Vente);

	}

	public List<Vente> getListVenteAnimeauxD(int nbr) throws ParseException {
		try {
			System.out.println("getListVenteAnimeaux length:" + getListVente(Categorie.Animeaux_Vente).size());

		} catch (Exception e) {
			// TODO: handle exception
		}

		return getListVenteDate(nbr, Categorie.Animeaux_Vente);

	}

	public List<Vente> getListVenteAutoD(int nbr) throws ParseException {

		return getListVenteDate(nbr, Categorie.Auto_Vente);

	}

	public List<Vente> getListVenteAutreD(int nbr) throws ParseException {

		return getListVenteDate(nbr, Categorie.Autre_Vente);

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
					if (p.getRef_bon_achat() != null && objectToInsert.getRef_bon_achat() != null) {
						existe_ref_achat = objectToInsert.getRef_bon_achat() == null ? false
								: p.getRef_bon_achat().intValue() == objectToInsert.getRef_bon_achat().intValue() ? true
										: false;
						System.out.println("ref inside loop:" + p.getRef_bon_achat().intValue());
					}
					if (existe_ref_achat) {
						throw new BreakException();
					}

				}

				);

			} catch (BreakException e) {
				Help.error_msg = "reference d'achat deja existant!" + "\n";
				Help.error_msg2 = "هدا الرقم موجود مسبقا ";
				objectToInsert.setRef_bon_achat(0);
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
				Help.error_msg2 = "هدا الرقم موجود مسبقا";
				objectToInsert.setRef_bon_vente(0);
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// Recuperer la liste des achats selon type de produit
	public void verifRefVente(Categorie c) throws BreakException {
		System.out.println("inside verifRefAchat");
		System.out.println("ref Bon:" + objectToInsert.getRef_bon_achat());
		if (listObjects != null && objectToInsert != null) {
			try {

				//
				listObjects.stream().filter(f -> f.getProduit().getCategorie() == c && f.getRef_bon_vente() != null)
						.forEach((p) -> {
							existe = objectToInsert.getRef_bon_vente() == null ? false
									: objectToInsert.getRef_bon_vente().intValue() == p.getRef_bon_vente().intValue()
											? true
											: false;
							System.out.println("ref inside loop:" + p.getRef_bon_vente().intValue());
							if (existe) {
								throw new BreakException();
							}

						}

				);

			} catch (BreakException e) {
				Help.error_msg = "reference de vente deja existant!" + "\n";
				Help.error_msg2 = "هدا الرقم موجود مسبقاً";
				objectToInsert.setRef_bon_vente(0);
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void verifRefVenteAgricole() throws BreakException {
		verifRefVente(Categorie.Agricole_Vente);

	}

	public void verifRefVenteAnimaux() throws BreakException {
		verifRefVente(Categorie.Animeaux_Vente);

	}

	public void verifRefVenteLait() throws BreakException {
		verifRefVente(Categorie.Lait);

	}

	public void verifRefVenteTransport() throws BreakException {
		verifRefVente(Categorie.Auto_Vente);

	}

	public void verifRefVenteAutre() throws BreakException {
		verifRefVente(Categorie.Autre_Vente);
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
		// listObjects = (List<Achat>) dao.findAll();
		if (listObjects != null) {
			try {
				// System.out.println(currentD);
				// System.out.println(addDays(currentD, nbreDay));

				// listAchat.forEach(p -> System.out.println(p.getDateDePaiement()));

				for (Vente a : listObjects) {
					if (nbreDay == 1 && a.getDateDePaiement() != null && a.getProduit() != null
							&& (sdf.parse(sdf.format(a.getDateDePaiement())).equals((currentD)))
							&& (a.getProduit().getCategorie() == c)) {
						listAll.add(a);
					} else if (nbreDay != 1 && a.getDateDePaiement() != null && a.getProduit() != null
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

	public Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	public void calculeMontantTotal() throws ParseException {
		System.out.println("inside calculeMontantTotal !");

		if (objectTosearch != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MMdd");
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date currentD = sdf.parse(sdf.format(objectTosearch.getDate_debut()));
			Date currentF = sdf.parse(sdf.format(objectTosearch.getDate_fin()));

			String strDateD = sdf.format(objectTosearch.getDate_debut());
			String strDateF = sdf.format(objectTosearch.getDate_fin());

			System.out.println(currentD + " " + currentF);
			System.out.println(currentD + " " + currentF);

			System.out.println(strDateD + " " + strDateD);
			System.out.println(strDateF + " " + strDateF);

			Date result = currentF.after(currentD) ? currentF : currentD;
			System.out.println(" top date is :" + result);

			Date k = sdf.parse(strDateD);
			System.out.println(k);

			/** traitement special de la recherche **/

		}

	}

	public List<Vente> getlistVenteLaitDateSearch()

	{
		if (objectTosearch != null && objectTosearch.getDate_debut() != null && objectTosearch.getDate_fin() != null) {
			return getlistVenteByDateSearch(Categorie.Lait, objectTosearch.getDate_debut(),
					objectTosearch.getDate_fin());

		}

		return Collections.emptyList();

	}

	public void preProcessPDF(Object document)
			throws IOException, BadElementException, DocumentException, com.itextpdf.text.DocumentException {
		Document pdf = (Document) document;
		pdf.open();

		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();

		ec.setResponseContentType("application/pdf");
		ec.setResponseHeader("Content-disposition", "inline=filename=file.pdf");

		OutputStream out = ec.getResponseOutputStream();
		createPdfTitles(pdf, out);

		pdf.setPageSize(PageSize.A4);

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String logo = externalContext.getRealPath("") + "/resources/images/logo.jpg";
		Image img = Image.getInstance(logo);
		pdf.add(img);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);

	}

	public List<Vente> getlistVenteByDateSearch(Categorie c, Date dateInf, Date dateSup) {
		List<Vente> listAll = new ArrayList<Vente>();
		listAll = listObjects.stream()
				.filter((p) -> (p.getProduit() != null && p.getProduit().getCategorie() == c
						&& p.getDateDePaiement().after(addDays(dateInf, -1))
						&& p.getDateDePaiement().before((addDays(dateSup, 1))))

				).collect(Collectors.toList());

		return listAll;
	}

	public void createPdfTitles(Document document, OutputStream out)
			throws DocumentException, IOException, com.itextpdf.text.DocumentException {

		// Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, out);
		// PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new
		// File("result7.pdf")));
		document.open();
		FacesContext context = FacesContext.getCurrentInstance();

		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String path = servletContext.getRealPath("");
		String allPath = path + "/resources/font/arialuni.ttf";

		BaseFont bf = BaseFont.createFont(allPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(bf, 14);
		ColumnText column = new ColumnText(writer.getDirectContent());
		column.setSimpleColumn(196, 690, 669, 56);
		column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

		column.addElement(new Paragraph(this.title, font));
		Paragraph pp = new Paragraph(this.Arabictitle, font);
		column.addElement(pp);
		column.addElement(Chunk.NEWLINE);
		column.addElement(Chunk.NEWLINE);

		column.go();
		// document.add(Chunk.NEWLINE);

		// document.close();
		System.out.println("done!");

	}

	public void changeTitles(String titleFr, String titleAr) {
		this.title = titleFr;
		this.Arabictitle = titleAr;
	}

	public void createPdf(String type)
			throws DocumentException, IOException, com.itextpdf.text.DocumentException, ParseException {
		switch (type) {
		case "Ag":
			generatePdf(filtredAchatAgricole);
		case "An":
			generatePdf(filtredAchatAnimaux);
		case "Auto":
			generatePdf(filtredAchatAuto);
		case "Autre":
			generatePdf(filtredAchatAutre);
			break;

		default:
			break;
		}

	}

	public void generatePdf(List<Vente> filtredAchat) throws DocumentException, IOException, ParseException {

		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		ec.setResponseContentType("application/pdf");
		ec.setResponseHeader("Content-disposition", "inline=filename=file.pdf");

		System.out.println("heeeeeeeeeeere:" + filtredAchatAgricole.size());

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, ec.getResponseOutputStream());

		// PdfWriter writer = PdfWriter.getInstance(document, out);
		// PdfWriter writer = PdfWriter.getInstance(document, out);
		document.open();
		/** Ajout de l image **/
		String logo = ec.getRealPath("") + "/resources/images/logo.jpg";
		Image img = Image.getInstance(logo);
		document.add(img);
		document.add(Chunk.NEWLINE);
		/** Ajout des titres **/

		PdfPTable pdfTableH = new PdfPTable(1);
		pdfTableH.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		pdfTableH.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfTableH.setWidthPercentage(100);
		pdfTableH.getDefaultCell().setBorder(0);

		String path = ec.getRealPath("");
		String allPath = path + "/resources/font/arialuni.ttf";

		BaseFont bf = BaseFont.createFont(allPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font font = new Font(bf, 14);

		PdfPCell celltitre = new PdfPCell(new Paragraph(this.title, font));
		celltitre.setHorizontalAlignment(Element.ALIGN_CENTER);
		celltitre.setBorder(0);
		pdfTableH.addCell(celltitre);

		PdfPCell celltitreAr = new PdfPCell(new Paragraph(this.Arabictitle, font));
		celltitreAr.setHorizontalAlignment(Element.ALIGN_CENTER);
		celltitreAr.setBorder(0);
		pdfTableH.addCell(celltitreAr);

		PdfPCell emptyCell = new PdfPCell(new Paragraph("\n", font));
		emptyCell.setBorder(0);
		pdfTableH.addCell(emptyCell);

		document.add(pdfTableH);
		document.add(Chunk.NEWLINE);

		/** Ajout du headers **/

		// PdfPTable pdfTable = new PdfPTable(headers.length);
		PdfPTable pdfTable = new PdfPTable(new float[] { 15, 15, 20, 20, 20, 15, 10 });
		pdfTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfTable.setWidthPercentage(100);

		for (String h : headers) {
			PdfPCell cell1 = new PdfPCell(new Paragraph(h, font));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell1);

		}

		int index = 1;
		double total = 0;
		/** Add Body **/
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

		for (Vente a : filtredAchat) {

			PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(index), font));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			// cell1.setPaddingLeft(0);
			pdfTable.addCell(cell1);

			PdfPCell cell12 = new PdfPCell(
					new Paragraph(a.getRef_bon_achat() != null ? String.valueOf(a.getRef_bon_achat()) : "", font));
			cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell12);

			PdfPCell cell2 = new PdfPCell(
					new Paragraph(a.getProduit() != null ? a.getProduit().getDesignation_prod() : "", font));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell2);
			PdfPCell cell3 = new PdfPCell(new Paragraph(a.getClient() != null ? a.getClient().getNom() : "", font));
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell3);

			PdfPCell cell4 = new PdfPCell(
					new Paragraph((a.getDateAchat() != null ? dateFormat.format(a.getDateAchat()) : ""), font));
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell4);

			PdfPCell cell5 = new PdfPCell(new Paragraph(String.valueOf(a.getQuantite()), font));
			cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell5);

			PdfPCell cell6 = new PdfPCell(new Paragraph(String.valueOf(a.getMontant_global()), font));
			cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell6);

			System.out.println("m!" + a.getMontant_global());
			System.out.println("t!" + total);
			// calcul total Montant
			total += a.getMontant_global();
			index++;
		}
		// column2.addElement(Chunk.NEWLINE);
		document.add(pdfTable);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Montant Global " + String.format("%.2f", total) + " DH"));

		document.close();
		writer.close();
		System.out.println("done!");

	}

	public void updateLists(Vente f) {
		updateFileteredList(f);
		updateObjectList(f);
	}

	public void updateFileteredList(Vente f) {

		int index = 0;

		for (Vente fou : listObjects_filtered) {
			if (fou.getId() == f.getId()) {
				break;
			}
			index++;
		}

		listObjects_filtered.set(index, f);

	}

	public void updateObjectList(Vente f) {

		int index = 0;

		for (Vente fou : listObjects) {
			if (fou.getId() == f.getId()) {
				break;
			}
			index++;
		}

		listObjects.set(index, f);

	}
	
	public void updatefilterswhenAdd() {
		if (objectToInsert != null && objectToInsert.getProduit() != null) {

			Categorie cat = objectToInsert.getProduit().getCategorie();
			if (cat == Categorie.Agricole_Vente)
				filtredAchatAgricole.add(objectToInsert);
			else if (cat == Categorie.Animeaux_Vente)
				filtredAchatAnimaux.add(objectToInsert);
			else if (cat == Categorie.Autre_Vente)
				filtredAchatAutre.add(objectToInsert);
			else if (cat == Categorie.Auto_Vente)
				filtredAchatAuto.add(objectToInsert);

		}

	}

}
