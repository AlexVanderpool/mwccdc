package com.itccloud.mwccdc.controller;

import com.itccloud.mwccdc.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PersonController {

    @GetMapping("/persons")
    public String getPersons(Model model) {
        List<Person> persons = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/persons2.csv")))) {

            if (br == null) {
                System.out.println("CSV file not found!");
                model.addAttribute("persons", persons);
                model.addAttribute("title", "Persons List");
                return "layout";  // Render layout even if CSV missing
            }

            String line;
            boolean firstLine = true;
            long idCounter = 1;

            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                if (line.trim().isEmpty()) continue;

                String[] fields = line.split(",", 7);
                if (fields.length < 7) continue;

                String firstName = fields[0].trim().replace("\"", "");
                String lastName = fields[1].trim().replace("\"", "");
                String gender = fields[2].trim().replace("\"", "");
                String fullAddress = (fields[3] + ", " + fields[4] + ", " + fields[5] + " " + fields[6])
                        .replace("\"", "").trim();

                persons.add(new Person(idCounter++, firstName, lastName, gender, fullAddress));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Pass data to Thymeleaf
        model.addAttribute("persons", persons);
        model.addAttribute("title", "Persons List");

        // Render the layout page
        return "layout";
    }
}
