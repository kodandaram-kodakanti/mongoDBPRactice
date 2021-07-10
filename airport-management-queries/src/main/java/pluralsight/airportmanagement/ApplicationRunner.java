package pluralsight.airportmanagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import pluralsight.airportmanagement.domain.FlightInformation;
import pluralsight.airportmanagement.domain.FlightPrinter;
import pluralsight.airportmanagement.queries.FlightInformationQueries;
import pluralsight.airportmanagement.repository.FlightRepository;

@Component
public class ApplicationRunner implements CommandLineRunner {
	
	@Value("${mongodb.connection.uri}")
	private String conntrionURI;

	private FlightInformationQueries flightInformationQueries;

	@Autowired
	private FlightRepository flightRepository;

	public ApplicationRunner(FlightInformationQueries flightInformationQueries) {
		this.flightInformationQueries = flightInformationQueries;
	}

	@Override
	public void run(String... args) {
		System.out.println(conntrionURI);
		System.setProperty("jdk.tls.trustNameService", "true");
		ConnectionString connectionString = new ConnectionString(conntrionURI);
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
		MongoClient mongoClient = MongoClients.create(settings);
		//MongoDatabase database = mongoClient.getDatabase("airport-management");

		/*
		
		FlightInformation flight = new FlightInformation();
		flight.setAircraft(new Aircraft("KingFisher", 320));
		flight.setDelayed(false);
		flight.setDepartureCity("Bangalore");
		flight.setDepartureDate(LocalDate.now().plusDays(3));
		flight.setDescription("Bangalore to Hyderabad");
		flight.setDestinationCity("Hyderabad");
		flight.setType(FlightType.Internal);
		flightRepository.save(flight);
		System.out.println("$$$$$$$$$$$$$$$$$$$$$44");
		
		*/

		
		  System.out.println("-----\nQUERY: All flights ordered by departure");
		  List<FlightInformation> allFlightsOrdered =
		  this.flightInformationQueries.findAll("departure", 0, 10);
		  FlightPrinter.print(allFlightsOrdered);
		  
		  System.out.println("-----\nQUERY: Free text search: Chennai");
		  List<FlightInformation> flightsByFreeText =
		  this.flightInformationQueries.findByFreeText("Chennai");
		  FlightPrinter.print(flightsByFreeText);
		 
	}
}
