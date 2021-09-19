package com.molla.controller;

import com.molla.exciptions.BrandNotFoundException;
import com.molla.exciptions.CategoryNotFoundException;
import com.molla.exportcsv.BrandCsvExporter;
import com.molla.exportexcel.BrandExcelExporter;
import com.molla.exportpdf.BrandPdfExporter;
import com.molla.model.Brand;
import com.molla.model.Category;
import com.molla.services.BrandService;
import com.molla.services.CategoryService;
import com.molla.util.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/save")
    public String saveCategory(Brand brand,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes ra) throws IOException {

        LOGGER.info("BrandController | saveBrand is started");

        LOGGER.info("BrandController | saveBrand | multipartFile.isEmpty() : " + multipartFile.isEmpty());

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            LOGGER.info("BrandController | saveBrand | fileName : " + fileName);

            brand.setLogo(fileName);

            Brand savedBrand = brandService.save(brand);
            String uploadDir = "brand-logos/" + savedBrand.getId();

            LOGGER.info("BrandController | saveBrand | savedBrand : " + savedBrand.toString());
            LOGGER.info("BrandController | saveCategory | uploadDir : " + uploadDir);

            FileUpload.cleanDir(uploadDir);
            FileUpload.saveFile(uploadDir, fileName, multipartFile);
        } else {
            brandService.save(brand);
        }

        ra.addFlashAttribute("messageSuccess", "The brand has been saved successfully.");
        return "redirect:/admin/brands";
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {

        LOGGER.info("BrandController | exportToCSV is started");

        List<Brand> listBrands = brandService.listAll();

        LOGGER.info("BrandController | exportToCSV | listBrands : " + listBrands.toString());

        BrandCsvExporter exporter = new BrandCsvExporter();
        exporter.export(listBrands, response);

        LOGGER.info("BrandController | exportToCSV | export completed");

    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        LOGGER.info("BrandController | exportToExcel is called");

        List<Brand> listBrands = brandService.listAll();

        LOGGER.info("BrandController | exportToExcel | listBrands Size : " + listBrands.size());

        BrandExcelExporter exporter = new BrandExcelExporter();

        LOGGER.info("BrandController | exportToExcel | export is starting");

        exporter.export(listBrands, response);

        LOGGER.info("BrandController | exportToExcel | export completed");

    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {

        LOGGER.info("BrandController | exportToPDF is called");

        List<Brand> listBrands = brandService.listAll();

        LOGGER.info("BrandController | exportToPDF | listBrands Size : " + listBrands.size());

        BrandPdfExporter exporter = new BrandPdfExporter();

        LOGGER.info("BrandController | exportToPDF | export is starting");

        exporter.export(listBrands, response);

        LOGGER.info("BrandController | exportToPDF | export completed");

    }

    @GetMapping("/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model,
                              RedirectAttributes ra) {

        LOGGER.info("BrandController | editBrand is started");

        try {
            Brand brand = brandService.get(id);
            List<Category> categories = categoryService.listCategoriesUsedInForm();

            LOGGER.info("BrandController | editBrand | brand : " + brand.toString());
            LOGGER.info("BrandController | editBrand | listCategories : " + categories.toString());


            model.addAttribute("categoriesList", categories);
            model.addAttribute("brand", brand);
            model.addAttribute("pageTitle", "Edit brand (ID: " + id + ")");

            return "brands/brand_form";

        } catch (BrandNotFoundException ex) {

            LOGGER.info("BrandController | editBrand | messageError : " + ex.getMessage());
            ra.addFlashAttribute("messageError", ex.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id,
                              RedirectAttributes redirectAttributes) {

        LOGGER.info("BrandController | deleteBrand is started");
        LOGGER.info("BrandController | deleteBrand | id : " + id);

        try {
            brandService.delete(id);

            LOGGER.info("BrandController | deleteBrand | brand deleted");

            String brandDir = "../brand-logos/" + id;

            LOGGER.info("BrandController | deleteBrand | brandDir : " + brandDir);

            FileUpload.removeDir(brandDir);

            LOGGER.info("BrandController | deleteBrand | FileUploadUtil.removeDir is over");

            LOGGER.info("BrandController | deleteBrand | brandDir : " + brandDir);

            redirectAttributes.addFlashAttribute("messageSuccess",
                    "The brand ID " + id + " has been deleted successfully");

        } catch (BrandNotFoundException ex) {
            LOGGER.info("BrandController | deleteBrand | messageError : " + ex.getMessage());
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }

        return "redirect:/admin/brands";

    }

}
