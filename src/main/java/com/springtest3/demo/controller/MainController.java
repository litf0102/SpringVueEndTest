package com.springtest3.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtest3.demo.model.Persons;
import com.springtest3.demo.util.PaginationFormatting;
import com.springtest3.demo.util.PaginationMultiTypeValuesHelper;
import com.springtest3.demo.dao.PersonsDao;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import java.util.*;

@RestController
@RequestMapping("/api/persons")
public class MainController {

    @Autowired
    private PersonsDao PersonsDao;

    @Value(("${com.springtest3.paginatio.max-per-page}"))
    Integer maxPerPage;

    /*
     * @api {GET} /api/persons/sex Get all sexList
     * @apiName getSexAll
     * @apiVersion 1.0.0
     * @apiExample Example usage:
     *
     *     http /api/persons/sex
     *
     * @apiSuccess {String} label
     * @apiSuccess {String} value
     */
    @RequestMapping(value = "/sex", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSexAll() {
        ArrayList<Map<String, String>> results = new ArrayList<>();

        for (Object value : PersonsDao.findSex()) {
            Map<String, String> sex = new HashMap<>();
            sex.put("label", value.toString());
            sex.put("value", value.toString());
            results.add(sex);
        }
        ResponseEntity<ArrayList<Map<String, String>>> responseEntity = new ResponseEntity<>(results,
                HttpStatus.OK);

        return responseEntity;
    }

    /*
     *   @api {GET} /api/persons  
     *   @apiName getPersonsAll
     *   @apiVersion 1.0.0
     *
     *   @apiExample Example usage:
     *
     *       All person：
     *       http /api/persons
     *       http /api/persons?sex=xxx&email=xx
     *       http /api/persons?sex=xxx
     *       http /api/persons?email=xx
     *
     *   @apiParam {String} sex
     *   @apiParam {String} email
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, PaginationMultiTypeValuesHelper> getPersonsAll(
            @RequestParam(value = "page", required = false) Integer pages,
            @RequestParam("sex") String sex,
            @RequestParam("email") String email
    ) {
        if (pages == null) {
            pages = 1;
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pages - 1, maxPerPage, sort);
        PaginationFormatting paginInstance = new PaginationFormatting();

        return paginInstance.filterQuery(sex, email, pageable);
    }

    /*
    *    @api {GET} /api/persons/detail/:id  details info
    *    @apiName getUserDetail
    *    @apiVersion 1.0.0
    *
    *    @apiExample {httpie} Example usage:
    *
    *        http GET http://127.0.0.1:8000/api/persons/detail/1
    */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Persons> getUserDetail(@PathVariable Long id) {
        Optional<Persons> user = PersonsDao.findById(id);

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    /*
     *  @api {PUT} /api/persons/detail/:id  update person info
     *  @apiName updateUser
     *  @apiVersion 1.0.0
     *
     *  @apiParam {String} phone
     *  @apiParam {String} zone
    */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Persons updateUser(@PathVariable Long id, @RequestBody Persons data) {
        Optional<Persons> user = PersonsDao.findById(id);
        user.get().setPhone(data.getPhone());
        user.get().setZone(data.getZone());

        return PersonsDao.save(user.get());
    }

}