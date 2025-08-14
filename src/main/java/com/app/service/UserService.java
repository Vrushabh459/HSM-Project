package com.app.service;

import java.util.List;

import com.app.dto.UserDTO;

import com.app.model.UserRole;

public interface UserService {
	
	public void addUser(UserDTO user);
	public List<UserDTO> getUserlist();
	public UserDTO getUser(int id);
	public void deleteUser(int id);
	public List<UserDTO> findBySocietyId(long id);
	public List<UserDTO> findByRole(UserRole role);
}
