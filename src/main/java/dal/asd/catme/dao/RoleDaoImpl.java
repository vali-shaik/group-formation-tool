package dal.asd.catme.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dal.asd.catme.beans.Enrollment;
import dal.asd.catme.database.DatabaseAccess;
import dal.asd.catme.util.CatmeUtil;
@Component
public class RoleDaoImpl implements IRoleDao{
	
	@Autowired
	DatabaseAccess db;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	ICourseDao courseDao;
	
	Connection con = null;
	
		
	@Override
	public int assignRole(String bannerId, int roleId) {
		String query = "INSERT IGNORE INTO UserRole (UserRoleId, BannerId, RoleId) VALUES ( NULL, '" +
				bannerId + "' , '" + roleId + "' );";

		int rs = 0;
		try {
			rs = db.executeUpdate(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	@Override
	public int addInstructor(int courseId, int userRoleId) {
		// TODO Auto-generated method stub
		String query = "INSERT IGNORE INTO CourseInstructor (CourseInstructorId, CourseId, UserRoleId) VALUES ( NULL,'" +
				courseId + "' , '" + userRoleId +"' );";

		int rs = 0;
		try {
			rs = db.executeUpdate(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	@Override
	public int checkCourseInstructor(String bannerId, int courseId) {
		// TODO Auto-generated method stub
		int rowCount = 0;
		// TODO Auto-generated method stub
		try {
			String query = "SELECT EXISTS(WITH temp AS ( SELECT ci.UserRoleId,ci.CourseId, ur.BannerId FROM CourseInstructor ci INNER JOIN UserRole ur ON ci.UserRoleId = ur.UserRoleId ) SELECT * FROM temp WHERE temp.BannerId = '"+ bannerId +"' AND temp.CourseId = "+courseId+");";
			ResultSet rs = db.executeQuery(query);
			rs.next();
			rowCount = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowCount;
	}
	
	@Override
	public int checkUserRole(String bannerId, int roleId) {
		
		int rowCount = 0;
		// TODO Auto-generated method stub
		try {
			String query = "SELECT EXISTS(SELECT * FROM UserRole WHERE BannerId = '"+ bannerId +"' AND RoleId = "+roleId+");";
			ResultSet rs = db.executeQuery(query);
			rs.next();
			rowCount = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowCount;
		
	}

	@Override
	public int getUserRoleId(String bannerId, int roleId) {
		
		int userRoleId = -1;
		// TODO Auto-generated method stub
		try {
			String query = "SELECT UserRoleId FROM UserRole WHERE BannerId = '"+ bannerId +"' AND RoleId = "+roleId+";";
			ResultSet rs = db.executeQuery(query);
			rs.next();
			userRoleId = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userRoleId;
		
	}
	
	@Override
	public int assignTa(Enrollment user) {
		// TODO Auto-generated method stub
		int isAssigned = 0;
		
		try {
			con = db.getConnection();
			
			//If the user exists
			if(0 != userDao.checkExistingUser(user.bannerId)){
				
				//If the user is not currently taking the course
				if(0 == courseDao.checkCourseRegistration(user.bannerId, user.courseId)){
					
					//If the user is not already registered as an instructor for the course
					if(0 == checkCourseInstructor(user.bannerId, user.courseId)){
						
						//If the UserRoleId already exists for the role
						if(0 != checkUserRole(user.bannerId, CatmeUtil.TA_ROLE_ID)) {
							
							//Get the UserRoleId
							int userRoleId = getUserRoleId(user.bannerId, CatmeUtil.TA_ROLE_ID);
							
							//Add the user to the CourseInstructor Table
							addInstructor(user.courseId, userRoleId);
							isAssigned = 1;
							
						}
						
						//If the UserRoleId doesn't exist for the role
						else {
							
							//Assign the TA role to the user in UserRole relation
							assignRole(user.bannerId, CatmeUtil.TA_ROLE_ID);
							
							//Get the UserRoleId
							int userRoleId = getUserRoleId(user.bannerId, CatmeUtil.TA_ROLE_ID);
							
							//Add the user to the CourseInstructor Table
							addInstructor(user.courseId, userRoleId);
							isAssigned = 1;
							
						}
						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (con != null) {
		        try {
		            con.close();
		        } catch (SQLException e) { e.printStackTrace(); }
		    }
		}

		
		return isAssigned;
	}

}
