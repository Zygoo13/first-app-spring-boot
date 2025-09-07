package com.zygoo132.first_app.repositories;

import com.zygoo132.first_app.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
