package com.example.foruminforexchange.service;

import com.lowagie.text.DocumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.function.Function;

public interface ExportService {
    <T> byte[] exportToExcel(Pageable pageable, Function<Pageable, Page<T>> fetchFunction, String[] headers, Function<T, Object[]> dataMapper) throws IOException;

    <T> byte[] exportToPdf(Pageable pageable, Function<Pageable, Page<T>> fetchFunction, String[] headers, Function<T, Object[]> dataMapper) throws DocumentException, IOException;

}
