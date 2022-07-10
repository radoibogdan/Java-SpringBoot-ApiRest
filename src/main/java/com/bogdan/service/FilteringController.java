package com.bogdan.service;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {
    @GetMapping("/filter1")
    public MappingJacksonValue filter1() {
        List<UserDetails> details = UserDetails.getDetails();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId", "userName");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserDetails", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(details);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @GetMapping("/filter2")
    public MappingJacksonValue filter2() {
        List<UserDetails> details = UserDetails.getDetails();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("panNumber", "userName");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserDetails", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(details);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }
}

// Var 1 - Ne pas envoyer le panNumber à l'utilisateur
//@JsonIgnoreProperties(value = {"panNumber"})
@JsonFilter("UserDetails")
class UserDetails {
    private int userId;
    private String userName;
//    @JsonIgnore //  Var 2 - Ne pas envoyer le panNumber à l'utilisateur
    private String panNumber;

    UserDetails(int userId, String userName, String panNumber) {
        this.userId = userId;
        this.userName = userName;
        this.panNumber = panNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public static List<UserDetails> getDetails() {
        return Arrays.asList(
                new UserDetails(11,"Dan", "AAAA"),
                new UserDetails(12,"Alex", "SSSS"),
                new UserDetails(13,"Pierre", "FFFF"));
    }
}
