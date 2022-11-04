package com.example.dao;

import com.example.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataDAO extends JpaRepository<RawData, Long> {

}
