package main.java.example3;


import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface BasicUserRepository extends CrudRepository<User, Long> {

    /**
     * Sample method annotated with {@link Query}. This method executes the CQL from the {@link Query} value.
     *
     * @param id
     * @return
     */
    @Query("SELECT * from users where user_id in(?0)")
    User findUserByIdIn(long id);

    /**
     * Derived query method. This query corresponds with {@code SELECT * FROM users WHERE uname = ?0}.
     * {@link User#username} is not part of the primary so it requires a secondary index.
     *
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * Derived query method using SASI (SSTable Attached Secondary Index) features through the {@code LIKE} keyword. This
     * query corresponds with {@code SELECT * FROM users WHERE uname LIKE '?0%'}. {@link User#username} is not part of the
     * primary so it requires a secondary index.
     *
     * @param lastnamePrefix
     * @return
     */
    List<User> findUsersByLastnameStartsWith(String lastnamePrefix);
}