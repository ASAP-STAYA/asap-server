package com.staya.asap.Controller;

import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Service.PreferenceService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void patchPreferenceInfo(@RequestBody PreferenceDTO preferenceDTO) {
        // TODO: 유저의 이름이 아닌 이메일이나 pk로 식별해야 함
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        preferenceService.updatePreference(preferenceDTO, userName);
    }
}
