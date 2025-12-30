package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.AppUser;
import com.example.demo.model.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByUserOrderByPinnedDescIdDesc(AppUser user);
}
