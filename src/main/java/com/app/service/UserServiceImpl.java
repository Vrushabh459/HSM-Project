package com.app.service;
/*
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Mapper.UserMapper;
import com.app.dao.UserDao;
import com.app.dto.UserDTO;
import com.app.model.User;
import com.app.model.UserRole;
import com.app.Mapper.*;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao UserRepository;
	
	@Override
	public void addUser(UserDTO userdto) {
		
		User user = UserMapper.userInstance.UserDTOtoUser(userdto);
		UserRepository.save(user);
	}

	@Override
	public List<UserDTO> getUserlist() {
		// TODO Auto-generated method stub
		
		List<User> userslist = UserRepository.findAll();
		List<UserDTO> dtolist = UserMapper.userInstance.toUserDTO(userslist);
		return dtolist;
	}

	@Override
	public UserDTO getUser(int id) {
		// TODO Auto-generated method stub
		Optional<User> result= UserRepository.findById(id);
		
		UserDTO user = null;
		
		if(result.isPresent())
		{
			
			user = UserMapper.userInstance.UsertoUserDTO(result.get());
		}
		else
		{
			//we didnt find the employee
			throw new RuntimeException("Did not find employee id = "+id);
		}
		
		return user;
	}

	@Override
	public void deleteUser(int id) {
		// TODO Auto-generated method stub
		UserRepository.deleteById(id);
	}
	public List<UserDTO> findBySocietyId(long id){
		List<User> userlist = UserRepository.findUsersBySocietyId(id);
		List<UserDTO> dtos = UserMapper.userInstance.toUserDTO(userlist);
		return dtos;
	}
	public List<UserDTO> findByRole(UserRole role){
		List<User> userlist = UserRepository.findUserByRole(role);
		List<UserDTO> dtos = UserMapper.userInstance.toUserDTO(userlist);
		return dtos;
	}
}
*/