package se.lundakarnevalen.ticket.db;

import lombok.Getter;
import se.lundakarnevalen.ticket.db.framework.Column;
import se.lundakarnevalen.ticket.db.framework.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Customer extends Entity {
	@Column
	public final int id;
	@Column
	@Getter
	protected String email;
	@Column
	@Getter
	protected String phone;
	@Column
	@Getter
	protected String name;

	private static final String TABLE = "`customers`";
	private static final String COLS = Entity.getCols(Customer.class);

	private Customer(int id) throws SQLException {
		this.id = id;
	}

	private static Customer create(ResultSet rs) throws SQLException {
		Customer customer = new Customer(rs.getInt("id"));
		populateColumns(customer, rs);
		return customer;
	}

	public static List<Customer> getAll() throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE;
		return new Mapper<Customer>(getCon(), query).toEntityList(rs -> Customer.create(rs));
	}

	public static List<Customer> getSearch(String search) throws SQLException {
		String like = "%" + search + "%";
		String query = "SELECT " + COLS + " FROM " + TABLE + " WHERE `email` LIKE ? or `phone` LIKE ? or `name` LIKE ?";
		PreparedStatement stmt = prepare(query);
		stmt.setString(1, like);
		stmt.setString(2, like);
		stmt.setString(3, like);
		return new Mapper<Customer>(stmt).toEntityList(rs -> Customer.create(rs));
	}

	public static Customer getSingle(long id) throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE + " WHERE `id`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setLong(1, id);
		return new Mapper<Customer>(stmt).toEntity(rs -> Customer.create(rs));
	}

	public static Customer create(String name, String email, String phone) throws SQLException {
		String query = "INSERT INTO " + TABLE + " SET `email`=?, `phone`=?, `name`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setString(1, email);
		stmt.setString(2, phone);
		stmt.setString(3, name);
		int id = executeInsert(stmt);
		stmt.getConnection().close();
		return getSingle(id);
	}

	public static Customer update(long id, String name, String email, String phone) throws SQLException {
		String query = "UPDATE " + TABLE + " SET `email`=?, `phone`=?, `name`=? WHERE `id`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setString(1, email);
		stmt.setString(2, phone);
		stmt.setString(3, name);
		stmt.setLong(4, id);
		stmt.executeUpdate();
		stmt.close();
		return getSingle(id);
	}

	public static List<Customer> getByProfile(int profile_id) throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE + " WHERE `id` IN "
				+ "(SELECT `customer_id` FROM `customer_profiles` WHERE `profile_id`=?)";
		PreparedStatement stmt = prepare(query);
		stmt.setInt(1, profile_id);
		return new Mapper<Customer>(stmt).toEntityList(rs -> Customer.create(rs));
	}
}
