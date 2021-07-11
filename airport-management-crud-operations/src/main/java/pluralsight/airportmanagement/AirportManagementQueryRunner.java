package pluralsight.airportmanagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pluralsight.airportmanagement.domain.FlightInformation;
import pluralsight.airportmanagement.domain.FlightPrinter;
import pluralsight.airportmanagement.queries.FlightInformationQueries;

@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class AirportManagementQueryRunner implements CommandLineRunner {

	@Autowired
	private FlightInformationQueries flightInformationQueries;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("-----\nQUERY: All flights ordered by departure");
		List<FlightInformation> allFlightsOrdered = this.flightInformationQueries.findAll("departure", 0, 5);
		FlightPrinter.print(allFlightsOrdered);

		System.out.println("-----\nQUERY: Free text search: Rome");
		List<FlightInformation> flightsByFreeText = this.flightInformationQueries.findByFreeText("Rome");
		FlightPrinter.print(flightsByFreeText);

	}

}
