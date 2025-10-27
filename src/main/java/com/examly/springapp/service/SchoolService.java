package com.examly.springapp.service;

import com.examly.springapp.Entity.School;
import com.examly.springapp.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolService {
    @Autowired
    private SchoolRepository schoolRepository;

    public School createSchool(School school) {
        return schoolRepository.save(school);
    }

    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    public Optional<School> getSchoolById(Long id) {
        return schoolRepository.findById(id);
    }

    public School updateSchool(Long id, School update) {
        return schoolRepository.findById(id).map(school -> {
            school.setName(update.getName());
            school.setDistrict(update.getDistrict());
            school.setAddress(update.getAddress());
            school.setContactInfo(update.getContactInfo());
            return schoolRepository.save(school);
        }).orElse(null);
    }

    public void deleteSchool(Long id) {
        schoolRepository.deleteById(id);
    }
}
