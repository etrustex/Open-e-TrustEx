package eu.europa.ec.cipa.etrustex.services.report.xls;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import eu.europa.ec.cipa.etrustex.domain.PartyRole;
import eu.europa.ec.cipa.etrustex.services.IPartyAgreementService;
import eu.europa.ec.cipa.etrustex.types.AvailibilityType;
import eu.europa.ec.cipa.etrustex.types.ConfidentialityType;
import eu.europa.ec.cipa.etrustex.types.IntegrityType;

@Transactional
@Service("icaResultExcelView")
public class ICAResultXlsExportService {
	
	@Autowired private IPartyAgreementService partyAgreementService;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public HSSFSheet buildExcelDocument(Map<String, Object> model) throws Exception {		
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		int startRowIndex = 0; 
		int startColIndex = 0;
		
		//Create new worksheet
		  HSSFSheet worksheet = workbook.createSheet("Result");
		  
		//Build layout 
		  // Build title, date, and column headers
		  buildReport(worksheet, startRowIndex, startColIndex);
		
		//Fill report
		  fillReport(worksheet, startRowIndex, startColIndex, (List<InterchangeAgreement>)model.get("ICAS"), (String)model.get("bd"));  

		return workbook.getSheetAt(0);
	}
	
	private void buildReport(HSSFSheet worksheet, int startRowIndex, int startColIndex) {		
		worksheet.setColumnWidth(0, 5000);
		worksheet.setColumnWidth(1, 5000);
		worksheet.setColumnWidth(2, 5000);
		worksheet.setColumnWidth(3, 5000);
		worksheet.setColumnWidth(4, 5000);
		worksheet.setColumnWidth(5, 5000);
		worksheet.setColumnWidth(6, 5000);
		worksheet.setColumnWidth(7, 5000);
		worksheet.setColumnWidth(8, 5000);
		worksheet.setColumnWidth(9, 5000);
		worksheet.setColumnWidth(10, 5000);
		worksheet.setColumnWidth(11, 5000);
		worksheet.setColumnWidth(12, 5000);
		worksheet.setColumnWidth(13, 5000);
		worksheet.setColumnWidth(14, 5000);
		worksheet.setColumnWidth(15, 5000);
		worksheet.setColumnWidth(16, 5000);
		worksheet.setColumnWidth(17, 5000);
		worksheet.setColumnWidth(18, 5000);
		
		buildTitle(worksheet, startRowIndex, startColIndex);
		
		buildHeaders(worksheet, ++startRowIndex, startColIndex);
	}
	
	private static void buildTitle(HSSFSheet worksheet, int startRowIndex, int startColIndex) {
		// Create font style for the report title
		Font fontTitle = worksheet.getWorkbook().createFont();
		fontTitle.setBold(true);
		fontTitle.setFontHeight((short) 280);
		// Create cell style for the report title
		HSSFCellStyle cellStyleTitle = worksheet.getWorkbook().createCellStyle();
		cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
		cellStyleTitle.setWrapText(true);
		cellStyleTitle.setFont(fontTitle);
		cellStyleTitle.setBorderTop(BorderStyle.THIN);
		cellStyleTitle.setBorderBottom(BorderStyle.THIN);
		cellStyleTitle.setBorderRight(BorderStyle.THIN);
		cellStyleTitle.setBorderLeft(BorderStyle.THIN);
		// Create report title
		HSSFRow rowTitle = worksheet.createRow((short) startRowIndex);
		rowTitle.setHeight((short) 500);
		HSSFCell cellTitle1 = rowTitle.createCell(11);
		cellTitle1.setCellValue("First Party");
		cellTitle1.setCellStyle(cellStyleTitle);
		HSSFCell cellTitle2 = rowTitle.createCell(15);
		cellTitle2.setCellValue("Second Party");
		cellTitle2.setCellStyle(cellStyleTitle);
		worksheet.addMergedRegion(new CellRangeAddress(startRowIndex,startRowIndex,11,14));
		HSSFRegionUtil.setBorderTop(BorderStyle.THIN.getCode(), new CellRangeAddress(startRowIndex,startRowIndex,11,14), worksheet, worksheet.getWorkbook());
		worksheet.addMergedRegion(new CellRangeAddress(startRowIndex,startRowIndex,15,18));
		HSSFRegionUtil.setBorderTop(BorderStyle.THIN.getCode(), new CellRangeAddress(startRowIndex,startRowIndex,15,18), worksheet, worksheet.getWorkbook());
		HSSFRegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(startRowIndex,startRowIndex,15,18), worksheet, worksheet.getWorkbook());
	}
	
	private static void buildHeaders(HSSFSheet worksheet, int startRowIndex, int startColIndex) {
		// Create font style for the headers
		Font font = worksheet.getWorkbook().createFont();
		font.setBold(true);
		// Create cell style for the headers
		HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
		headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		headerCellStyle.setFillPattern(FillPatternType.NO_FILL);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerCellStyle.setWrapText(true);
		headerCellStyle.setFont(font);
		headerCellStyle.setBorderTop(BorderStyle.THIN);
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBorderRight(BorderStyle.THIN);
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		
		// Create the column headers
		HSSFRow rowHeader = worksheet.createRow((short) startRowIndex);
		rowHeader.setHeight((short) 500);
		
		HSSFCell cell1 = rowHeader.createCell(startColIndex+0);
		cell1.setCellValue("Id");
		cell1.setCellStyle(headerCellStyle);
	
		HSSFCell cell2 = rowHeader.createCell(startColIndex+1);
		cell2.setCellValue("Business Domain");
		cell2.setCellStyle(headerCellStyle);
		
		HSSFCell cell3 = rowHeader.createCell(startColIndex+2);
		cell3.setCellValue("Created");
		cell3.setCellStyle(headerCellStyle);
		
		HSSFCell cell4 = rowHeader.createCell(startColIndex+3);
		cell4.setCellValue("Creation User");
		cell4.setCellStyle(headerCellStyle);
		
		HSSFCell cell5 = rowHeader.createCell(startColIndex+4);
		cell5.setCellValue("Modified");
		cell5.setCellStyle(headerCellStyle);
		
		HSSFCell cell6 = rowHeader.createCell(startColIndex+5);
		cell6.setCellValue("Modification User");
		cell6.setCellStyle(headerCellStyle);
		
		HSSFCell cell7 = rowHeader.createCell(startColIndex+6);
		cell7.setCellValue("Validity Start");
		cell7.setCellStyle(headerCellStyle);
		
		HSSFCell cell8 = rowHeader.createCell(startColIndex+7);
		cell8.setCellValue("Profile");
		cell8.setCellStyle(headerCellStyle);
		
		HSSFCell cell9 = rowHeader.createCell(startColIndex+8);
		cell9.setCellValue("Confidentiality");
		cell9.setCellStyle(headerCellStyle);
		
		HSSFCell cell10 = rowHeader.createCell(startColIndex+9);
		cell10.setCellValue("Integrity");
		cell10.setCellStyle(headerCellStyle);
		
		HSSFCell cell11 = rowHeader.createCell(startColIndex+10);
		cell11.setCellValue("Availability");
		cell11.setCellStyle(headerCellStyle);
		
		HSSFCell cell12 = rowHeader.createCell(startColIndex+11);
		cell12.setCellValue("Name");
		cell12.setCellStyle(headerCellStyle);
		
		HSSFCell cell13 = rowHeader.createCell(startColIndex+12);
		cell13.setCellValue("Role");
		cell13.setCellStyle(headerCellStyle);
		
		HSSFCell cell14 = rowHeader.createCell(startColIndex+13);
		cell14.setCellValue("Identifiers");
		cell14.setCellStyle(headerCellStyle);
		
		HSSFCell cell15 = rowHeader.createCell(startColIndex+14);
		cell15.setCellValue("3rd Parties");
		cell15.setCellStyle(headerCellStyle);
		
		HSSFCell cell16 = rowHeader.createCell(startColIndex+15);
		cell16.setCellValue("Name");
		cell16.setCellStyle(headerCellStyle);
		
		HSSFCell cell17 = rowHeader.createCell(startColIndex+16);
		cell17.setCellValue("Role");
		cell17.setCellStyle(headerCellStyle);
		
		HSSFCell cell18 = rowHeader.createCell(startColIndex+17);
		cell18.setCellValue("Identifiers");
		cell18.setCellStyle(headerCellStyle);
		
		HSSFCell cell19 = rowHeader.createCell(startColIndex+18);
		cell19.setCellValue("3rd Parties");
		cell19.setCellStyle(headerCellStyle);
	}
	
	private void fillReport(HSSFSheet worksheet, int startRowIndex, int startColIndex, List<InterchangeAgreement> model, String businessDomainId) {
		 // Row offset
		  startRowIndex +=2 ;
		  
		// Create cell style for the body
		  HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
		  bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
		  bodyCellStyle.setWrapText(true);
		  bodyCellStyle.setBorderTop(BorderStyle.THIN);
		  bodyCellStyle.setBorderBottom(BorderStyle.THIN);
		  bodyCellStyle.setBorderRight(BorderStyle.THIN);
		  bodyCellStyle.setBorderLeft(BorderStyle.THIN);
		  bodyCellStyle.setAlignment(HorizontalAlignment.LEFT);
		  bodyCellStyle.setVerticalAlignment(VerticalAlignment.TOP);
			
		  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");		  
		  InterchangeAgreement ica = null;
		  String value = "";
		  for (int i=startRowIndex; i+startRowIndex-2< model.size()+2; i++) {
			  ica = model.get(i-2);
			  PartyRole[] pr =  ica.getPartyRoles().toArray(new PartyRole[ica.getPartyRoles().size()]);

			// Create a new row
			   HSSFRow row = worksheet.createRow((short) i);
			   
			   HSSFCell cell1 = row.createCell(startColIndex+0);
			   cell1.setCellValue(ica.getId());
			   cell1.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell2 = row.createCell(startColIndex+1);
			   cell2.setCellValue(businessDomainId);
			   cell2.setCellStyle(bodyCellStyle);
			   
			   value =  ica.getAccessInfo().getCreationDate() != null ? dateFormat.format(ica.getAccessInfo().getCreationDate()) : ""; 
			   HSSFCell cell3 = row.createCell(startColIndex+2);
			   cell3.setCellValue(value);
			   cell3.setCellStyle(bodyCellStyle);
			   
			   value =  ica.getAccessInfo().getCreationId() != null ? ica.getAccessInfo().getCreationId() : "";
			   HSSFCell cell4 = row.createCell(startColIndex+3);
			   cell4.setCellValue(value);
			   cell4.setCellStyle(bodyCellStyle);
			  
			   value =  ica.getAccessInfo().getModificationDate() != null ? dateFormat.format(ica.getAccessInfo().getModificationDate()) : "";
			   HSSFCell cell5 = row.createCell(startColIndex+4);
			   cell5.setCellValue(value);
			   cell5.setCellStyle(bodyCellStyle);
			   
			   value =  ica.getAccessInfo().getModificationId() != null ? ica.getAccessInfo().getModificationId() : "";
			   HSSFCell cell6 = row.createCell(startColIndex+5);
			   cell6.setCellValue(value);
			   cell6.setCellStyle(bodyCellStyle);
			   
			   value =  ica.getValidityStartDate() != null ? dateFormat.format(ica.getValidityStartDate()) : "";
			   HSSFCell cell7 = row.createCell(startColIndex+6);
			   cell7.setCellValue(value);
			   cell7.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell8 = row.createCell(startColIndex+7);
			   cell8.setCellValue(ica.getProfile().getName());
			   cell8.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell9 = row.createCell(startColIndex+8);
			   cell9.setCellValue(ConfidentialityType.getByLevel(ica.getCiaLevel().getConfidentialityLevel()).getDescription());
			   cell9.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell10 = row.createCell(startColIndex+9);
			   cell10.setCellValue(IntegrityType.getByLevel(ica.getCiaLevel().getIntegrityLevel()).getDescription());
			   cell10.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell11 = row.createCell(startColIndex+10);
			   cell11.setCellValue(AvailibilityType.getByLevel(ica.getCiaLevel().getAvailabilityLevel()).getDescription());
			   cell11.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell12 = row.createCell(startColIndex+11);
			   cell12.setCellValue(pr[0].getParty().getName());
			   cell12.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell13 = row.createCell(startColIndex+12);
			   cell13.setCellValue(pr[0].getRole().getName());
			   cell13.setCellStyle(bodyCellStyle);
			   
			   String temp = "";
			   for (PartyIdentifier pI : pr[0].getParty().getIdentifiers()) {
				   temp += pI.getSchemeId().getSchemeID()+" "+pI.getValue() + '\n';
			   }
			   HSSFCell cell14 = row.createCell(startColIndex+13);
			   cell14.setCellValue(temp);
			   cell14.setCellStyle(bodyCellStyle);
			   
			   temp = "";
			   for (Party party : partyAgreementService.getThirdPartiesForDelegatingParty(pr[0].getParty().getId())) {
				   temp += party.getName() + '\n';
			   }
			   HSSFCell cell15 = row.createCell(startColIndex+14);
			   cell15.setCellValue(temp);
			   cell15.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell16 = row.createCell(startColIndex+15);
			   cell16.setCellValue(pr[1].getParty().getName());
			   cell16.setCellStyle(bodyCellStyle);
			   
			   HSSFCell cell17 = row.createCell(startColIndex+16);
			   cell17.setCellValue(pr[1].getRole().getName());
			   cell17.setCellStyle(bodyCellStyle);
			   
			   temp = "";
			   for (PartyIdentifier pI : pr[1].getParty().getIdentifiers()) {
				   temp += pI.getSchemeId().getSchemeID()+" "+pI.getValue() + '\n';
			   }
			   HSSFCell cell18 = row.createCell(startColIndex+17);
			   cell18.setCellValue(temp);
			   cell18.setCellStyle(bodyCellStyle);
			   
			   temp = "";
			   for (Party party : partyAgreementService.getThirdPartiesForDelegatingParty(pr[1].getParty().getId())) {
				   temp += party.getName() + '\n';
			   }
			   HSSFCell cell19 = row.createCell(startColIndex+18);
			   cell19.setCellValue(temp);
			   cell19.setCellStyle(bodyCellStyle);
		  }	
	}
}
