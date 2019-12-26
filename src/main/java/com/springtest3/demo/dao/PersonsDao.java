package com.springtest3.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springtest3.demo.model.Persons;

public interface PersonsDao extends JpaRepository<Persons, Long>, JpaSpecificationExecutor<Persons> {
    public static final String FIND_SEX = "select DISTINCT sex from Persons p";

    @Query(FIND_SEX)
    List<String> findSex();

    Page<Persons> findAll(Pageable pageable);

    Page<Persons> findBySexAndEmailContains(String sexName, String emailName, Pageable pageable);

    //@Query(value="select p from Persons p where p.sex = ?1 order by ?#{#pageable}",
    //		countQuery="select count(*) from Persons p where p.sex = ?1")
    Page<Persons> findBySex(String sex, Pageable pageable);

    Optional<Persons> findById(Long id);
}
