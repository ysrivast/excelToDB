package com.example.demo;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import com.example.demo.model.Person;

public class UserExcelRowMapper implements RowMapper<Person> {
 
    @Override
    public Person mapRow(RowSet rowSet) throws Exception {
    	Person user = new Person();
 
    	
    	user.setFirstName(rowSet.getColumnValue(0));
    	user.setLastName(rowSet.getColumnValue(1));
        user.setEmail(rowSet.getColumnValue(2));
        user.setAge(Double.parseDouble(rowSet.getColumnValue(3)));
 
        return user;
    }
}