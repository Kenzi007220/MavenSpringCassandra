package main.java.example3.util;



        import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import lombok.experimental.UtilityClass;
import org.springframework.data.util.Version;
import org.springframework.util.Assert;

/**
 * Utility to retrieve the Cassandra release version.
 *
 * @author Mark Paluch
 */
@UtilityClass
public class CassandraVersion {

    /**
     * Retrieve the Cassandra release version.
     *
     * @param session must not be {@literal null}.
     * @return the release {@link Version}.
     */
    public static Version getReleaseVersion(Session session) {

        Assert.notNull(session, "Session must not be null");

        ResultSet resultSet = session.execute("SELECT release_version FROM system.local;");
        Row row = resultSet.one();

        return Version.parse(row.getString(0));
    }
}
