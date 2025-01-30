package org.abx.persistence.creds.dao;

import org.abx.persistence.creds.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);

    @Override
    void delete(Permission permission);

}
