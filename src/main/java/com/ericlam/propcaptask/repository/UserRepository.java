package com.ericlam.propcaptask.repository;

import com.ericlam.propcaptask.dao.PropUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PropUser, String> {
}
