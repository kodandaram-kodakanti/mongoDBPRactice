package pluralsight.airportmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pluralsight.airportmanagement.domain.FlightInformation;

public interface FlightRepository extends MongoRepository<FlightInformation, String> {

}