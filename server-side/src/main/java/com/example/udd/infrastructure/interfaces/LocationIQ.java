package com.example.udd.infrastructure.interfaces;

import com.example.udd.infrastructure.model.Location;

public interface LocationIQ {
    Location search(final String query);
}
