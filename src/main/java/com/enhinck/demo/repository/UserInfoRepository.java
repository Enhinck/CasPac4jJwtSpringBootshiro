package com.enhinck.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enhinck.demo.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

	UserInfo findByUsername(String username);

	UserInfo findByUsernameAndPassword(String username, String password);
}
