package com.bence.mate.services;

import com.bence.mate.exceptions.DataNotFoundException;
import com.bence.mate.daos.ApplicationDAO;
import com.bence.mate.models.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileService {

    private Map<String, Profile> profiles = ApplicationDAO.PROFILES;

    public Profile addProfile(Profile profile) {
        profile.setId((long) (profiles.size() + 1));
        profiles.put(profile.getProfileName(), profile);

        return profiles.get(profile.getProfileName());
    }

    public Profile updateProfile(Profile profile) {
        if(profile.getProfileName() == null) return null;
        profiles.put(profile.getProfileName(), profile);

        return profiles.get(profile.getProfileName());
    }

    public Profile getProfile(String profileName){
        Profile profile = profiles.get(profileName);
        if (profile == null) {
            throw new DataNotFoundException(String.format("There is no profile with name: '%s'", profileName));
        }
        return profile;
    }

    public List<Profile> getAllProfilesPaginated(int start, int size) {
        List<Profile> list = new ArrayList<>(profiles.values());
        if (start + size > list.size()) return new ArrayList<>();
        return list.subList(start, start + size);
    }

    public void removeProfile(String profileName){
        profiles.remove(profileName);
    }

    public List<Profile> getAllProfiles() {
        return new ArrayList<>(profiles.values());
    }
}
