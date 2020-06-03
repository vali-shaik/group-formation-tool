package dal.asd.catme.dao;

import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dal.asd.catme.database.DatabaseAccess;
@Component
public class CourseDaoImpl implements ICourseDao{
	
	@Autowired
	DatabaseAccess db;

	@Override
	public int checkCourseRegistration(String bannerId, String courseId, Connection con) {
		int rowCount = 0;
		// TODO Auto-generated method stub
		try {
			String query = "SELECT EXISTS(SELECT * FROM Enrollment WHERE BannerId = '" + bannerId + "' AND CourseId = '" + courseId + "');";

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			rowCount = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowCount;
	}

	@Override
	public int checkCourseExists(String courseId, Connection con) {
		int rowCount = 0;
		// TODO Auto-generated method stub
		try {
			String query = "SELECT EXISTS(SELECT * FROM Course WHERE CourseId  = '" + courseId + "');";
			ResultSet rs = db.executeQuery(query);
			rs.next();
			rowCount = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowCount;
	}

}
