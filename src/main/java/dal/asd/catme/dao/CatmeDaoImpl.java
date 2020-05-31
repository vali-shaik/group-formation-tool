package dal.asd.catme.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dal.asd.catme.beans.Course;
import dal.asd.catme.database.DatabaseAccess;
import dal.asd.catme.util.CatmeUtil;

@Component
public class CatmeDaoImpl implements ICatmeDao {

	@Autowired
	DatabaseAccess database;
	
	@Override
	public List<Course> getAllCourses() {
		List<Course> listOfCourses=new ArrayList<>();
		ResultSet resultSet;
		try {
			resultSet = database.getConnection().createStatement().executeQuery(CatmeUtil.SELECT_COURSE_QUERY);
			
			while(resultSet.next()) {
				Course course = new Course();
				course.setCourseId(resultSet.getString(CatmeUtil.COURSE_ID_FIELD));
				course.setCourseName(resultSet.getString(CatmeUtil.COURSE_NAME_FIELD));
				listOfCourses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


		
		return listOfCourses;
	}	
}
