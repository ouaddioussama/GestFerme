package com.services.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.dao.interfaces.InterfDao;
import com.entities.Achat;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;

@ManagedBean(name = "ObjectBean")
@ViewScoped
public class ObjectService<T> extends LazyDataModel<T> implements Serializable {

	private Class<T> classG;

	protected T objectToInsert;
	protected T objectSelected;
	protected InterfDao<T> daoObject;

	protected List<T> listObjects;
	private List<Achat> filtered;
	protected List<T> listObjects_filtered = new ArrayList<T>();
	protected String indice_path = "";
	protected String path = "/views/" + indice_path + "/index";
	protected LazyDataModel<T> dataModel;
	protected String title = "";
	protected String Arabictitle = "";

	public ObjectService() {

	};

	public ObjectService(Class<T> clazz) {
		this.classG = clazz;
	}

	public T getObjectToInsert() {
		return objectToInsert;
	}

	public void setObjectToInsert(T objectToInsert) {
		this.objectToInsert = objectToInsert;
	}

	public List<T> getListObjects() {

		return listObjects;
	}

	public void setListObjects(List<T> listObjects) {
		this.listObjects = listObjects;
	}

	public InterfDao<T> getDaoObject() {
		return daoObject;
	}

	public void setDaoObject(InterfDao<T> daoObject) {
		this.daoObject = daoObject;
	}

	public T getObjectSelected() {
		return objectSelected;
	}

	public void setObjectSelected(T objectSelected) {
		this.objectSelected = objectSelected;
	}

	public List<T> getListObjects_filtered() {
		if (daoObject != null) {
			listObjects_filtered = daoObject.findAll();

		}
		return listObjects_filtered;
	}

	public void setListObjects_filtered(List<T> listObjects_filtered) {
		this.listObjects_filtered = listObjects_filtered;
	}

	public List<Achat> getFiltered() {
		return filtered;
	}

	public void setFiltered(List<Achat> filtered) {
		this.filtered = filtered;
	}

	@PostConstruct
	public void init() {
		// listObjects=(List<T>)dao.findAll();

		dataModel = new LazyDataModel<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				// TODO Auto-generated method stub
				setRowCount(daoObject.getCountAll());
				return daoObject.LazyList(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder));
			}

		};
		if (daoObject != null) {
			listObjects = (List<T>) daoObject.findAll();

		}
		// listObjects =dao.findAll();
		// listObjects_filtered =dao.findAll();

	}

	public void create() throws Exception {
		System.out.println("inside create");

		if (objectToInsert != null) {
			/*
			 * System.out.println("inside create"); dao.createInstance(objectToInsert);
			 * listObjects.add(objectToInsert); Help.msg = "insere avec Succes";
			 * Help.goTo(path); objectToInsert=null;
			 */

		} else {
			throw new Exception("objectToInsert can not be null");
		}
	}

	public void update() throws Exception {
		daoObject.updateIstance(objectSelected);

	}

	public void delete() throws Exception {
		if (objectSelected != null) {
			daoObject.deleteInstance(objectSelected);
			listObjects.remove(objectSelected);
			Help.msg = "supprime avec Succes";
			Help.goTo(path);

		} else {
			throw new Exception("objectSelected can not be null");
		}
	}

	public void onRowEdit(RowEditEvent event) throws IOException {

		T editedModele = (T) event.getObject();

		if (editedModele != null) {
			daoObject.updateIstance(editedModele);
			RequestContext requestContext = RequestContext.getCurrentInstance();
			Help.msg = "mise e jour faite avec Succes";
			// Help.goTo(path);

		} else {
			System.out.println("objectToInsert is null !");
		}

	}

	public void callMe() {
		System.out.println("callMe");
	}
	/*
	 * public void onRowSelect(SelectEvent event) throws IOException {
	 * 
	 * Client editedModele = (Client) event.getObject();
	 * Help.msg="Client : "+editedModele.getNom_client()+"selectionee";
	 * anotherDisplay(); System.out.println(editedModele.getNom_client());
	 * 
	 * 
	 * 
	 * }
	 */

	public void onRowSelect(SelectEvent event) throws IOException {

		T editedModele = (T) event.getObject();
		System.out.println(editedModele.toString());

	}

	/**
	 * @return the dataModel
	 */
	public LazyDataModel<T> getDataModel() {
		return dataModel;
	}

	/**
	 * @param dataModel
	 *            the dataModel to set
	 */
	public void setDataModel(LazyDataModel<T> dataModel) {
		this.dataModel = dataModel;
	}

	public void preProcessPDF(Object document)
			throws IOException, BadElementException, DocumentException, com.itextpdf.text.DocumentException {
		Document pdf = (Document) document;
		pdf.open();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		ServletOutputStream out = response.getOutputStream();
		createPdfTitles(pdf, out);

		pdf.setPageSize(PageSize.A4);

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String logo = externalContext.getRealPath("") + "/resources/images/logo.jpg";
		// File.separator + "resources" + File.separator + "images"
		// + File.separator + "logo.jpg";

		Image img = Image.getInstance(logo);
		// img.setWidthPercentage(50);
		// img.setTop(2);
		// float scaler = ((pdf.getPageSize().getWidth() - pdf.leftMargin() -
		// pdf.rightMargin() - 0) / pdf.getPageSize().getWidth())* 100;
		// img.scalePercent(scaler);
		img.scaleAbsolute(150, 300);
		pdf.add(img);

		Paragraph paragraph = new Paragraph(title);

		// Font f = new Font(Font.BOLD, 50.0f, Font.UNDERLINE);

		// paragraph.setFont(f);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		pdf.add(Chunk.NEWLINE);

		// pdf.add(paragraph);

		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);

		pdf.add(Chunk.NEWLINE);

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
		column.setSimpleColumn(196, 670, 669, 56);
		column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

		column.addElement(new Paragraph(title, font));
		Paragraph pp = new Paragraph(Arabictitle, font);
		column.addElement(pp);

		column.go();
		// document.add(Chunk.NEWLINE);

		// document.close();
		System.out.println("done!");

	}
}
