package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.Constants;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.arpanbags.products.arpanbagsproducts.Constants.FAILED_TO_LOAD_PROFANITY_WORDS;

@Component
public class ProfaneWordService {


    Set<String> profaneWords = new HashSet<>();

    @PostConstruct
    public void loadWords() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(Constants.PROFANITY_VULGAR_WORDS_LIST).getInputStream()))) {

            profaneWords = reader.lines()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            throw new RuntimeException(FAILED_TO_LOAD_PROFANITY_WORDS, e);
        }
    }

    public boolean containsProfanity(String input) {
        if (input == null) return false;
        String lower = input.toLowerCase();
        return profaneWords.stream().anyMatch(lower::contains);
    }

    public boolean isInvalid(String input) {
        return containsProfanity(input);
    }

    public Optional<String> findInvalidField(Map<String, String> fields) {
        return fields.entrySet().stream()
                .filter(entry -> isInvalid(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
    }

}
