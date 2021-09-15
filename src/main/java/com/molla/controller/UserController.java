package com.molla.controller;

import com.molla.exciptions.UserNotFoundException;
import com.molla.exportcsv.UserCsvExporter;
import com.molla.exportexcel.UserExcelExporter;
import com.molla.exportpdf.UserPdfExporter;
import com.molla.model.Role;
import com.molla.model.User;
import com.molla.services.RoleService;
import com.molla.services.UserService;
import com.molla.services.serviceImp.UserServiceImp;
import com.molla.util.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/admin/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private String defaultRedirectURL = "redirect:/admin/users/page/1?sortField=firstName&sortDir=asc";

    private final RoleService roleService;
    private final UserService userService;

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("")
    public String listFirstPage(Model model) {
        LOGGER.info("UserController | listFirstPage is started");

        return listByPage(1, "firstName", "asc", null, model);
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model) {

        LOGGER.info("UserController | listByPage is started");

        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> users = page.getContent();

        long startCount = (pageNum - 1) * UserServiceImp.USERS_PER_PAGE + 1;
        long endCount = startCount + UserServiceImp.USERS_PER_PAGE - 1;

        if (endCount > page.getTotalPages()) {
            endCount = page.getTotalPages();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("users", users);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);

        return "users/index";
    }

    @GetMapping("/new")
    public String newUser(Model model) {

        LOGGER.info("UserController | newUser is called");

        List<Role> listRoles = roleService.listRoles();

        LOGGER.info("UserController | newUser | listRoles.size() : " + listRoles.size());

        User user = new User();
        user.setEnabled(true);

        LOGGER.info("UserController | newUser | user : " + user.toString());

        model.addAttribute("user", user);
        model.addAttribute("roles", listRoles);
        model.addAttribute("pageTitle", "Create New User");

        return "users/user_form";
    }


    @PostMapping("/save")
    public String saveUser(User user,
                           @RequestParam("fileImage") MultipartFile file,
                           RedirectAttributes redirectAttributes) throws IOException {

        LOGGER.info("UserController | try to save user : " + user.toString());

        if (!file.isEmpty()) {

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            user.setPhotos(fileName);
            User savedUser = userService.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUpload.cleanDir(uploadDir);
            FileUpload.saveFile(uploadDir, fileName, file);

        } else {
            if (user.getPhotos().isEmpty())
                user.setPhotos(null);

            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been added saved successfully.");


        return "redirect:/admin/users";

    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        LOGGER.info("UserController | editUser is called");

        try {
            User user = userService.get(id);

            LOGGER.info("UserController | editUser | user : " + user.toString());

            List<Role> listRoles = roleService.listRoles();

            LOGGER.info("UserController | editUser | listRoles.size() : " + listRoles.size());

            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (Name: " + user.getFullName() + ")");
            model.addAttribute("roles", listRoles);

            return "users/user_form";

        } catch (UserNotFoundException ex) {

            LOGGER.error("UserController | editUser | ex.getMessage() : " + ex.getMessage());

            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        LOGGER.info("UserController | deleteUser is called");

        try {
            userService.delete(id);

            LOGGER.info("UserController | deleteUser | delete completed");

            redirectAttributes.addFlashAttribute("messageSuccess",
                    "The user ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException ex) {

            LOGGER.error("UserController | deleteUser | ex.getMessage() : " + ex.getMessage());

            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }

        return defaultRedirectURL;
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {

        LOGGER.info("UserController | updateUserEnabledStatus is called");

        userService.updateUserEnabledStatus(id, enabled);

        LOGGER.info("UserController | updateUserEnabledStatus completed");

        String status = enabled ? "enabled" : "disabled";

        LOGGER.info("UserController | updateUserEnabledStatus | status : " + status);

        String message = "The user ID " + id + " has been " + status;

        LOGGER.info("UserController | updateUserEnabledStatus | message : " + message);

        if(message.contains("enabled")) {
            redirectAttributes.addFlashAttribute("messageSuccess", message);
        }else {
            redirectAttributes.addFlashAttribute("messageError", message);
        }


        return defaultRedirectURL;
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {

        LOGGER.info("UserController | exportToCSV is called");

        List<User> listUsers = userService.listAll();

        LOGGER.info("UserController | exportToCSV | listUsers.size() : " + listUsers.size());

        UserCsvExporter exporter = new UserCsvExporter();


        LOGGER.info("UserController | exportToCSV | export is starting");

        exporter.export(listUsers, response);

        LOGGER.info("UserController | exportToCSV | export completed");
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        LOGGER.info("UserController | exportToExcel is called");

        List<User> listUsers = userService.listAll();

        LOGGER.info("UserController | exportToExcel | listUsers.size() : " + listUsers.size());

        UserExcelExporter exporter = new UserExcelExporter();

        LOGGER.info("UserController | exportToExcel | export is starting");

        exporter.export(listUsers, response);

        LOGGER.info("UserController | exportToExcel | export completed");
    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {

        LOGGER.info("UserController | exportToPDF is called");

        List<User> listUsers = userService.listAll();

        LOGGER.info("UserController | exportToPDF | listUsers.size() : " + listUsers.size());

        UserPdfExporter exporter = new UserPdfExporter();

        LOGGER.info("UserController | exportToPDF | export is starting");

        exporter.export(listUsers, response);

        LOGGER.info("UserController | exportToPDF | export completed");
    }

}
