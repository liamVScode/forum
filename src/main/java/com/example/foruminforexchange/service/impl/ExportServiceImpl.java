package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.service.ExportService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
public class ExportServiceImpl implements ExportService {


    @Override
    public <T> byte[] exportToExcel(Pageable pageable, Function<Pageable, Page<T>> fetchFunction, String[] headers, Function<T, Object[]> dataMapper) throws IOException {
        Page<T> page = fetchFunction.apply(pageable);
        List<T> dataList = page.getContent();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Tạo header cho file
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Đổ dữ liệu vào các dòng tiếp theo
        int rowIdx = 1;
        for (T data : dataList) {
            Row row = sheet.createRow(rowIdx++);
            Object[] cellValues = dataMapper.apply(data);
            for (int i = 0; i < cellValues.length; i++) {
                row.createCell(i).setCellValue(cellValues[i] != null ? cellValues[i].toString() : "");
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    @Override
    public <T> byte[] exportToPdf(Pageable pageable, Function<Pageable, Page<T>> fetchFunction, String[] headers, Function<T, Object[]> dataMapper) throws DocumentException, IOException {
        Page<T> page = fetchFunction.apply(pageable);
        List<T> dataList = page.getContent();

        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable table = new PdfPTable(headers.length);
        addTableHeader(table, headers);
        addRows(table, dataList, dataMapper);

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setPhrase(new Paragraph(header, font));
            table.addCell(cell);
        }
    }

    private <T> void addRows(PdfPTable table, List<T> dataList, Function<T, Object[]> dataMapper) {
        for (T data : dataList) {
            Object[] cellValues = dataMapper.apply(data);
            for (Object value : cellValues) {
                table.addCell(value != null ? value.toString() : "");
            }
        }
    }
}
