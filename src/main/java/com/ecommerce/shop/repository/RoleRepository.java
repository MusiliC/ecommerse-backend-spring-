package com.ecommerce.shop.repository;

import com.ecommerce.shop.models.AppRole;
import com.ecommerce.shop.models.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
