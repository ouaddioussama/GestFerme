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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
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
import com.entities.Vente;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.ColumnText;
//import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
//import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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
	private ArrayList<Achat> filtredAchatAgricole = new ArrayList<Achat>();
	private ArrayList<Achat> filtredAchatAnimaux = new ArrayList<Achat>();;
	private ArrayList<Achat> filtredAchatAuto = new ArrayList<Achat>();;
	private ArrayList<Achat> filtredAchatAutre = new ArrayList<Achat>();;

	private PDFOptions pdfOpt;

	private String[] headers = { "Numero \n الرقم ", "ref_Bon \n مرجع الشراء", "Produit \n المنتوج",
			"Fournisseur \n المزود", "DateAchat \n تاريخ الشراء ", "quantite  \n  كمية ", "Montant \n  المبلغ" };

	// private List<Achat> filtered;

	@Autowired
	protected InterfAchatDao dao;

	@Autowired
	protected InterfProduitDao prodDao;
	@Autowired
	protected LoginService loginService;

	@Autowired
	protected SocieteService societeService;

	private Set<String> listUnite = new HashSet<String>();
	private Set<String> listReglement = new HashSet<String>();
	boolean existe = false;
	boolean hide = false;
	private boolean paginatorActive = true;
	/** Lazy Models **/
	private LazyDataModel<Achat> modelAgricole;
	private LazyDataModel<Achat> modelAnimaux;
	private LazyDataModel<Achat> modelLait;
	private LazyDataModel<Achat> modelAuto;
	private LazyDataModel<Achat> modelAutre;

	/** utilisÃ© dans la vÃ©rification d'existence dans la liste des achats **/

	public AchatService() {

	}

	public LazyDataModel<Achat> getModelAgricole() {
		return modelAgricole;
	}

	public void setModelAgricole(LazyDataModel<Achat> modelAgricole) {
		this.modelAgricole = modelAgricole;
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

	public List<Achat> getFiltredAchatAgricole() {
		return filtredAchatAgricole;
	}

	public void setFiltredAchatAgricole(ArrayList<Achat> filtredAchatAgricole) {
		this.filtredAchatAgricole = filtredAchatAgricole;
	}

	public List<Achat> getFiltredAchatAnimaux() {
		return filtredAchatAnimaux;
	}

	public void setFiltredAchatAnimaux(ArrayList<Achat> filtredAchatAnimaux) {
		this.filtredAchatAnimaux = filtredAchatAnimaux;
	}

	public List<Achat> getFiltredAchatAuto() {
		return filtredAchatAuto;
	}

	public void setFiltredAchatAuto(ArrayList<Achat> filtredAchatAuto) {
		this.filtredAchatAuto = filtredAchatAuto;
	}

	public List<Achat> getFiltredAchatAutre() {
		return filtredAchatAutre;
	}

	public void setFiltredAchatAutre(ArrayList<Achat> filtredAchatAutre) {
		this.filtredAchatAutre = filtredAchatAutre;
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
				updatefilterswhenAdd();

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
		// customizationOptions();

		initLazyModel();

		listObjects = (List<Achat>) dao.findAll();
		// listObjects_filtered= (List<Achat>) dao.findAll();
		System.out.println(listObjects.size());

		for (Unite u : Unite.values()) {
			listUnite.add(u.toString());
		}

		for (Types_Reglement t : Types_Reglement.values()) {
			listReglement.add(t.toString());
		}
		initFilters();
	}

	public void initFilters() {
		filtredAchatAgricole = (ArrayList<Achat>) getListAchat(Categorie.Agricole_Achat);
		filtredAchatAnimaux = (ArrayList<Achat>) getListAchat(Categorie.Animeaux_Achat);
		filtredAchatAuto = (ArrayList<Achat>) getListAchat(Categorie.Auto_Achat);
		filtredAchatAutre = (ArrayList<Achat>) getListAchat(Categorie.Autre_Achat);

	}

	public void updatefilterswhenAdd() {
		if (objectToInsert != null && objectToInsert.getProduit() != null) {

			Categorie cat = objectToInsert.getProduit().getCategorie();
			if (cat == Categorie.Agricole_Achat)
				filtredAchatAgricole.add(objectToInsert);
			else if (cat == Categorie.Animeaux_Achat)
				filtredAchatAnimaux.add(objectToInsert);
			else if (cat == Categorie.Autre_Achat)
				filtredAchatAutre.add(objectToInsert);
			else if (cat == Categorie.Auto_Achat)
				filtredAchatAuto.add(objectToInsert);

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
			updateLists(editedModele);
			Help.msg = "mise a jour faite avec Succes";

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void delete(Achat c) throws Exception {

		if (c != null) {
			dao.deleteInstance(c);
			listObjects.remove(c);
			listObjects_filtered.remove(c);
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
		// return dao.listAchat(Categorie.Agricole_Achat);

	}

	public List<Achat> getListAnimeaux() {
		title = "List des Achats des Animaux";
		Arabictitle = "قائمة المشتريات الخاصة بالمواشي";

		return getListAchat(Categorie.Animeaux_Achat);
		// return dao.listAchat(Categorie.Animeaux_Achat);

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

	public void initLazyModel() {
		modelAgricole = getLazyModelInstance(Categorie.Agricole_Achat);
		modelAnimaux = getLazyModelInstance(Categorie.Animeaux_Achat);
		modelAuto = getLazyModelInstance(Categorie.Auto_Achat);
		modelAutre = getLazyModelInstance(Categorie.Autre_Achat);

	}

	public LazyDataModel<Achat> getLazyModelInstance(Categorie cat) {
		return new LazyDataModel<Achat>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Achat> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				// TODO Auto-generated method stub
				setRowCount(dao.listAchat(cat).size());
				System.out.println("siiize:" + dao.listAchat(cat).size());
				List<Achat> lines = dao.LazyList(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder),
						filters, cat);

				// lines.forEach(p -> System.out.println(p.getId()));
				// List<Achat> filtredData = new ArrayList<Achat>();
				/*
				 * for (Achat a : lines) { for (Map.Entry<String, Object> entry :
				 * filters.entrySet()) { String v = (String) entry.getValue();
				 * System.out.println("++++++++++v:" + v);// valeur entré
				 * System.out.println("----------v:" + entry.getKey()); // column name of filter
				 * 
				 * 
				 * // if(String.valueOf(c.getCode_edi()).contains(v)) { result.add(c); }
				 * 
				 * 
				 * } }
				 */

				return lines;
			}

			@Override
			public Object getRowKey(Achat achat) {
				return achat != null ? achat.getId() : null;
			}

			@Override
			public Achat getRowData(String rowKey) {
				List<Achat> fruits = dao.listAchat(cat);
				Integer value = Integer.valueOf(rowKey);

				for (Achat fruit : fruits) {
					if (fruit.getId().equals(value)) {
						return fruit;
					}
				}

				return null;
			}

		};

	}

	public void exportPDF(DataTable table, String filename) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		Exporter exporter = new CustomPDFExporter();
		exporter.export(context, table, filename, false, false, "iso-8859-1", null, null, pdfOpt);
		context.responseComplete();
	}

	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException,
			com.itextpdf.text.DocumentException, ParseException {
		Document pdf = (Document) document;
		// createPdf(pdf);
		/*
		 * pdf.open();
		 * 
		 * FacesContext fc = FacesContext.getCurrentInstance(); ExternalContext ec =
		 * fc.getExternalContext();
		 * 
		 * // HttpServletResponse response = (HttpServletResponse) //
		 * FacesContext.getCurrentInstance().getExternalContext() // .getResponse(); /*
		 * ec.setResponseContentType(contentType); // Check
		 * http://www.iana.org/assignments/media-types for all types. Use if necessary
		 * ExternalContext#getMimeType() for auto-detection based on filename.
		 * ec.setResponseContentLength(contentLength); // Set it with the file size.
		 * This header is optional. It will work if it's omitted, but the download
		 * progress will be unknown.
		 * 
		 * ec.setResponseContentType("application/pdf");
		 * ec.setResponseHeader("Content-disposition", "inline=filename=file.pdf");
		 * 
		 * 
		 * OutputStream out = ec.getResponseOutputStream(); createPdfTitles(pdf, out);
		 * 
		 * pdf.setPageSize(PageSize.A4);
		 * 
		 * ExternalContext externalContext =
		 * FacesContext.getCurrentInstance().getExternalContext(); String logo =
		 * externalContext.getRealPath("") + "/resources/images/logo.jpg"; Image img =
		 * Image.getInstance(logo); pdf.add(img); pdf.add(Chunk.NEWLINE);
		 * 
		 * pdf.add(Chunk.NEWLINE); pdf.add(Chunk.NEWLINE); pdf.add(Chunk.NEWLINE);
		 * pdf.add(Chunk.NEWLINE); pdf.add(Chunk.NEWLINE);
		 */

	}

	public void createPdfTitles(Document document, OutputStream out)
			throws DocumentException, IOException, com.itextpdf.text.DocumentException, ParseException {

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
		column.setSimpleColumn(236, 690, 669, 56);
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

	public void generatePdf(List<Achat> filtredAchat) throws DocumentException, IOException, ParseException {

		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		ec.setResponseContentType("application/pdf");
		ec.setResponseHeader("Content-disposition", "inline=filename=file.pdf");

		System.out.println("heeeeeeeeeeere:" + filtredAchatAgricole.size());

		// PdfDocument pdfDoc = new PdfDocument(new
		// PdfWriter(ec.getResponseOutputStream()));
		// Document doc = new Document(pdfDoc);

		/*
		 * OutputStream out = null; try { out = ec.getResponseOutputStream();
		 * 
		 * } catch (Exception e) { // TODO: handle exception }
		 */
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

		/*
		 * ColumnText column = new ColumnText(writer.getDirectContent());
		 * column.setSimpleColumn(200, 690, 669, 106);
		 * column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		 * 
		 * column.addElement(new Paragraph(this.title, font)); Paragraph pp = new
		 * Paragraph(this.Arabictitle, font); column.addElement(pp);
		 * column.addElement(Chunk.NEWLINE); column.addElement(Chunk.NEWLINE);
		 * 
		 * ColumnText column2 = new ColumnText(writer.getDirectContent());
		 * column2.setSimpleColumn(35, 640, 630, 440);
		 * 
		 * PdfPTable pdfTable = new PdfPTable(headers.length);
		 * pdfTable.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		 * pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		 * pdfTable.setWidthPercentage(100);
		 * 
		 */

		/** Ajout du headers **/

		// PdfPTable pdfTable = new PdfPTable(headers.length);
		PdfPTable pdfTable = new PdfPTable(new float[] { 15, 15, 20, 20, 20, 15, 15 });
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
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (Achat a : filtredAchat) {

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
			PdfPCell cell3 = new PdfPCell(
					new Paragraph(a.getFournisseur() != null ? a.getFournisseur().getNom() : "", font));
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

		// column2.addElement(Chunk.NEWLINE);

		// column.go();
		// column2.go();

		document.close();
		writer.close();
		System.out.println("done!");

	}

	public void updateLists(Achat f) {
		updateFileteredList(f);
		updateObjectList(f);
	}

	public void updateFileteredList(Achat f) {

		int index = 0;

		for (Achat fou : listObjects_filtered) {
			if (fou.getId() == f.getId()) {
				break;
			}
			index++;
		}

		listObjects_filtered.set(index, f);

	}

	public void updateObjectList(Achat f) {

		int index = 0;

		for (Achat fou : listObjects) {
			if (fou.getId() == f.getId()) {
				break;
			}
			index++;
		}

		listObjects.set(index, f);

	}

}
