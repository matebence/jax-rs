package com.bence.mate.daos;

import com.bence.mate.models.Message;
import com.bence.mate.models.Profile;

import java.util.HashMap;
import java.util.Map;

public class ApplicationDAO {

    public static Map<Long, Message> MESSAGES = new HashMap<>();

    public static Map<String, Profile> PROFILES = new HashMap<>();
}
