package com.example.udd.dto;

import org.springframework.web.multipart.MultipartFile;

public record IndexContractRequestDTO(
        MultipartFile file,
        String firstName,
        String lastName,
        String governmentName,
        String levelOfAdministration,
        String street,
        String number,
        String city
) {
}
