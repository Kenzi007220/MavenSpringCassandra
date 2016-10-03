package main.java.example3.test;

import com.datastax.driver.core.Session;
import main.java.example3.BasicConfiguration;
import main.java.example3.BasicUserRepository;
import main.java.example3.User;
import main.java.example3.util.CassandraVersion;
import main.java.example3.util.RequiresCassandraKeyspace;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Version;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BasicConfiguration.class)
public class BasicUserRepositoryTests {

    public final static Version CASSANDRA_2_2_7 = Version.parse("2.2.7");

    @ClassRule public final static RequiresCassandraKeyspace CASSANDRA_KEYSPACE = RequiresCassandraKeyspace.onLocalhost();

    @Autowired
    BasicUserRepository repository;
    @Autowired Session session;
    User user;

    @Before
    public void setUp() {

        user = new User();
        user.setId(42L);
        user.setUsername("foobar");
        user.setFirstname("firstname");
        user.setLastname("lastname");
    }

    /**
     * Saving an object using the Cassandra Repository will create a persistent representation of the object in Cassandra.
     */
    @Test
    public void findSavedUserById() {

        user = repository.save(user);

        assertThat(repository.findOne(user.getId()), is(user));
    }

    /**
     * Cassandra can be queries by using query methods annotated with {@link @Query}.
     */
    @Test
    public void findByAnnotatedQueryMethod() {

        repository.save(user);

        assertThat(repository.findUserByIdIn(1000), is(nullValue()));
        assertThat(repository.findUserByIdIn(42), is(equalTo(user)));
    }

    /**
     * Spring Data Cassandra supports query derivation so annotating query methods with
     * {@link org.springframework.data.cassandra.repository.Query} is optional. Querying columns other than the primary
     * key requires a secondary index.
     */
    @Test
    public void findByDerivedQueryMethod() throws InterruptedException {

        session.execute("CREATE INDEX IF NOT EXISTS user_username ON users (uname);");
		/*
		  Cassandra secondary indexes are created in the background without the possibility to check
		  whether they are available or not. So we are forced to just wait. *sigh*
		 */
        Thread.sleep(1000);

        repository.save(user);

        assertThat(repository.findUserByUsername(user.getUsername()), is(user));
    }

    /**
     * Spring Data Cassandra supports {@code LIKE} and {@code CONTAINS} query keywords to for SASI indexes.
     */
    @Test
    public void findByDerivedQueryMethodWithSASI() throws InterruptedException {

        assumeTrue(CassandraVersion.getReleaseVersion(session).isGreaterThanOrEqualTo(CASSANDRA_2_2_7));

        session.execute("CREATE CUSTOM INDEX ON users (lname) USING 'org.apache.cassandra.index.sasi.SASIIndex';");
		/*
		  Cassandra secondary indexes are created in the background without the possibility to check
		  whether they are available or not. So we are forced to just wait. *sigh*
		 */
        Thread.sleep(1000);

        repository.save(user);

        assertThat(repository.findUsersByLastnameStartsWith("last"), hasItem(user));
    }
}