package com.chaeshin.boo.repository.user;


import com.chaeshin.boo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends CrudRepository<User, Long>, BaseUserCrudRepository {
}
