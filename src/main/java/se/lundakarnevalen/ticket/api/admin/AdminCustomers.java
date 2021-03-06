package se.lundakarnevalen.ticket.api.admin;

import io.swagger.annotations.Api;
import se.lundakarnevalen.ticket.api.desk.DeskCustomers;
import se.lundakarnevalen.ticket.db.Customer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Api
@Path("/admin/customers")
@RolesAllowed("ADMIN")
@Produces("application/json; charset=UTF-8")
public class AdminCustomers extends DeskCustomers {
	@GET
	public Response getCustomers() throws SQLException {
		List<Customer> customers = Customer.getAll();
		return status(200).entity(customers).build();
	}
}
