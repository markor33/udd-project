package com.example.udd.respository;

import com.example.udd.model.DummyTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<DummyTable, Integer> {
}
