package com.rpc.servelt.impl;

/**
 * @author 王贤
 */
public class UniversityStudentServiceImpl implements StudentService{

    @Override
    public String study(String book) {
        return "universityStudent" +book;
    }
}
