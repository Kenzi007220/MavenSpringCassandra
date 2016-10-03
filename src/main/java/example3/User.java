package main.java.example3;



import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;


@NoArgsConstructor
@Table(value = "users")
public class User {

    @PrimaryKey("user_id")
    private Long id;

    @Column("uname")
    private String username;
    @Column("fname")
    private String firstname;
    @Column("lname")
    private String lastname;

//    public User() {
//        this.setId(id);
//    }
/*

    public User() {

        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }
*/

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }
}