package com.molla.controller;

import com.molla.exportcsv.BrandCsvExporter;
import com.molla.exportexcel.BrandExcelExporter;
import com.molla.exportpdf.BrandPdfExporter;
import com.molla.model.Brand;
import com.molla.model.Category;
import com.molla.services.BrandService;
import com.molla.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/brands")
public class BrandController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

    private final BrandService brandService;
    private final CategoryService categoryService;

    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String listAll(Model model) {
        LOGGER.info("BrandController | listFirstPage is started");

        model.addAttribute("brands", brandService.listAll());

        return "brands/index";
    }

    @GetMapping("/new")
    public String newBrand(Model model) {

        LOGGER.info("BrandController | newBrand is started");

        List<Brand> brands = brandService.listAll();
        List<Category> categories = categoryService.listAll("asc", null);

        model.addAttribute("brand", new Brand());
        model.addAttribute("brands", brands);
        model.addAttribute("categoriesList", categories);
        model.addAttribute("pageTitle", "Create New Brand");

        LOGGER.info("BrandController | newBrand | brands : " + brands.toString());

        return "brands/brand_form";

    }


    @GetMapping("/brands/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {

        LOGGER.info("BrandController | exportToCSV is started");

        List<Brand> listBrands = brandService.listAll();

        LOGGER.info("BrandController | exportToCSV | listBrands : " + listBrands.toString());

        BrandCsvExporter exporter = new BrandCsvExporter();
        exporter.export(listBrands, response);

        LOGGER.info("BrandController | exportToCSV | export completed");

    }

    @GetMapping("/brands/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        LOGGER.info("BrandController | exportToExcel is called");

        List<Brand> listBrands = brandService.listAll();

        LOGGER.info("BrandController | exportToExcel | listBrands Size : " + listBrands.size());

        BrandExcelExporter exporter = new BrandExcelExporter();

        LOGGER.info("BrandController | exportToExcel | export is starting");

        exporter.export(listBrands, response);

        LOGGER.info("BrandController | exportToExcel | export completed");

    }

    @GetMapping("/brands/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {

        LOGGER.info("BrandController | exportToPDF is called");

        List<Brand> listBrands = brandService.listAll();

        LOGGER.info("BrandController | exportToPDF | listBrands Size : " + listBrands.size());

        BrandPdfExporter exporter = new BrandPdfExporter();

        LOGGER.info("BrandController | exportToPDF | export is starting");

        exporter.export(listBrands, response);

        LOGGER.info("BrandController | exportToPDF | export completed");

    }


}
