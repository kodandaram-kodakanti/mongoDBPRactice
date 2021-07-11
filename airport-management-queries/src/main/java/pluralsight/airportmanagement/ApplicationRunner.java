package pluralsight.airportmanagement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ExecutableRemoveOperation.ExecutableRemove;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.DeleteResult;

import pluralsight.airportmanagement.domain.Aircraft;
import pluralsight.airportmanagement.domain.FlightInformation;
import pluralsight.airportmanagement.domain.FlightPrinter;
import pluralsight.airportmanagement.domain.FlightType;
import pluralsight.airportmanagement.queries.FlightInformationQueries;
import pluralsight.airportmanagement.repository.FlightRepository;

@Component
public class ApplicationRunner implements CommandLineRunner {
	
	/*
	 * @Value("${mongodb.connection.uri}") private String connectionURI;
	 */

	private FlightInformationQueries flightInformationQueries;

	@Autowired private MongoTemplate mongoTemplete;
	
	@Autowired
	private FlightRepository flightRepository;

	public ApplicationRunner(FlightInformationQueries flightInformationQueries) {
		this.flightInformationQueries = flightInformationQueries;
	}

	@Override
	public void run(String... args) {
		//System.out.println(connectionURI);
		
		System.setProperty("jdk.tls.trustNameService", "true");
		ConnectionString connectionString = new ConnectionString("mongodb+srv://mongo_user1:July2021@mongo-cluster.u58ji.mongodb.net/airport-management");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
		MongoClient mongoClient = MongoClients.create(settings);
		
		FlightInformation flight = new FlightInformation();
		flight.setAircraft(new Aircraft("SpiceJet", 280));
		flight.setDelayed(true);
		flight.setDepartureCity("Chennai");
		flight.setDepartureDate(LocalDate.now().plusDays(2));
		flight.setDescription("Chennai to Bangalore");
		flight.setDestinationCity("Bangalore");
		flight.setType(FlightType.Internal);
		//flightRepository.save(flight);
		mongoTemplete.insert(flight);

		//mongoTemplete.getCollectionNames().forEach(docClass -> System.out.println(docClass));
		
		//ExecutableRemove<FlightInformation>  removed = mongoTemplete.remove(FlightInformation.class);
		//DeleteResult all = removed.all();
		//System.out.println("Deleted Count: " + all.getDeletedCount());
		
		
		
		/*
		 * List<FlightInformation> list = mongoTemplete.findAllAndRemove( new
		 * Query().with(Sort.by(Sort.Direction.ASC, "departure")).with(PageRequest.of(0,
		 * 3)), FlightInformation.class);
		 */
		 
		
		//System.out.println("$$$$$$$$$$$$$$$$$$$$$");
		//list.stream().forEach(flight1 -> System.out.println(flight1.toString()));

		
		  System.out.println("-----\nQUERY: All flights ordered by departure");
		  List<FlightInformation> allFlightsOrdered =
		  this.flightInformationQueries.findAll("departure", 0, 3);
		  FlightPrinter.print(allFlightsOrdered);
		  
		  System.out.println("-----\nQUERY: Free text search: Chennai");
		  List<FlightInformation> flightsByFreeText =
		  this.flightInformationQueries.findByFreeText("Chennai");
		  FlightPrinter.print(flightsByFreeText);
		 
	}
}
