package pluralsight.airportmanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import pluralsight.airportmanagement.domain.FlightInformation;

/*
This component will be the main entry point for
our use cases. It should get executed after the seeding
process
 */

@Service
@Order(2)
public class ApplicationRunner implements CommandLineRunner {
    private MongoTemplate mongoTemplate;

    public ApplicationRunner(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        markAllFlightsToRomeAsDelayed();
        removeFlightsWithDurationLessThanTwoHours();
    }

    void markAllFlightsToRomeAsDelayed() {
    	System.out.println("Updating All Flights To Rome as Delayed");
        Query departingFromRome = Query.query(
                Criteria.where("destination").is("Rome")
        );

        Update setDelayed = Update.update("isDelayed", true);

        this.mongoTemplate.updateMulti(
                departingFromRome,
                setDelayed,
                FlightInformation.class);
    }

    void removeFlightsWithDurationLessThanTwoHours() {
        Query lessThanTwoHours = Query.query(
                        Criteria.where("durationMin").lt(120)
                );

        mongoTemplate.findAllAndRemove(lessThanTwoHours, FlightInformation.class);
    }
}
