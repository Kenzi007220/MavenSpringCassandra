package example2;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


public class CassandraApp {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraApp.class);

    private static Cluster cluster;
    private static Session session;

    public static void main(String[] args) {

        try {

            cluster = Cluster.builder().addContactPoints(InetAddress.getLocalHost()).build();

            session = cluster.connect("mykeyspace");

            CassandraOperations cassandraOps = new CassandraTemplate(session);

            cassandraOps.insert(new Person("1234567890", "David", 40));
            cassandraOps.insert(new Person("1234", "Alison", 30));

//            Select s = QueryBuilder.select().from("person");
//            s.where(QueryBuilder.eq("id", "1234567890"));
//           ;

            String cqlAll = "select * from person";

            List<Person> results = cassandraOps.select(cqlAll, Person.class);
            for (Person p : results) {
                LOG.info(String.format("Found People with Name [%s] for id [%s]", p.getName(), p.getId()));
            }
            results.forEach(System.out::println);
            cassandraOps.truncate("person");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}