package dal.asd.catme.dao;

import dal.asd.catme.beans.Course;
import dal.asd.catme.beans.Student;
import dal.asd.catme.beans.User;

import java.sql.Connection;

public interface IStudentDao
{
    public boolean enroll(Student s, Course c, Connection con);
}