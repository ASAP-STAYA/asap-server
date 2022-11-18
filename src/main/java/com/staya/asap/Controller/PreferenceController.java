package com.staya.asap.Controller;

import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Service.PreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/preference")
@CrossOrigin(maxAge = 3600)
public class PreferenceController {
    private PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PreferenceDTO getPreferenceInfoByUserId(@PathVariable("userId") Integer id) {
        return preferenceService.getPreferenceByUserId(id);
    }
}
