package com.staya.asap.Service;

import com.staya.asap.Model.DB.PreferenceDTO;
import com.staya.asap.Repository.PreferenceRepo;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {
    private final PreferenceRepo preferenceRepo;

    public PreferenceService(PreferenceRepo preferenceRepo) {
        this.preferenceRepo = preferenceRepo;
    }

    public PreferenceDTO getPreferenceById(Integer id) {
        return this.preferenceRepo.findById(id);
    }

    public PreferenceDTO getPreferenceByUserId(Integer userId) {
        return this.preferenceRepo.findByUserId(userId);
    }

    // TODO: 없어져야 함
    public PreferenceDTO getPreferenceByUserName(String userName) {
        return this.preferenceRepo.findByUserName(userName);
    }

    public void createPreference(PreferenceDTO preference) {
        this.preferenceRepo.createPreference(preference);
    }

    public void updatePreference(PreferenceDTO preference, String userName) {
        this.preferenceRepo.updatePreference(preference, userName);
    }

    public void updateWeights(PreferenceDTO preference, Integer userId){
        this.preferenceRepo.updateWeights(preference,userId);
    }
}
