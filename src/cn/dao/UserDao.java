package cn.dao;

import cn.domain.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;
	static{
		TeacherDao teacherDao = TeacherDao.getInstance();
		users = new TreeSet<User>();
		User user = null;
		try {
			user = new User(1,"st","st",new Date(),teacherDao.find(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		users.add(user);
		try {
			users.add(new User(2,"lx","lx",new Date(),teacherDao.find(2)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			users.add(new User(3,"wx","wx",new Date(),teacherDao.find(3)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			users.add(new User(4,"lf","lf",new Date(),teacherDao.find(4)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public Collection<User> findAll(){
		return UserDao.users;
	}
	
	public User find(Integer id){
		User desiredUser = null;
		for (User user : users) {
			if(id.equals(user.getId())){
				desiredUser =  user; 
				break;
			}
		}
		return desiredUser;
	}
	
	public boolean update(User user){
		users.remove(user);
		return users.add(user);		
	}
	
	public boolean add(User user){
		return users.add(user);		
	}

	public boolean delete(Integer id){
		User user = this.find(id);
		return this.delete(user);
	}
	
	public boolean delete(User user){
		return users.remove(user);
	}
	
	
	public static void main(String[] args){
		UserDao dao = new UserDao();
		Collection<User> users = dao.findAll();
		display(users);
	}

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}
	
	
}
