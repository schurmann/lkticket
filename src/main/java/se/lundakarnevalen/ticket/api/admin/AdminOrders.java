package se.lundakarnevalen.ticket.api.admin;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import se.lundakarnevalen.ticket.api.desk.DeskOrders;

@Path("/admin/orders")
@RolesAllowed("USER")
@Produces("application/json; charset=UTF-8")
public class AdminOrders extends DeskOrders {
}
