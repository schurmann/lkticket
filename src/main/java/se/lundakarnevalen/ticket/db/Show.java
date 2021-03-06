package se.lundakarnevalen.ticket.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import se.lundakarnevalen.ticket.db.framework.Column;
import se.lundakarnevalen.ticket.db.framework.Mapper;

public class Show extends Entity {
	@Column
	public final int id;
	@Column
	@Getter
	protected String name;
	@Column
	@Getter
	protected String description;

	private static final String TABLE = "`shows`";
	private static final String COLS = Entity.getCols(Show.class);

	private Show(int id) throws SQLException {
		this.id = id;
	}

	private static Show create(ResultSet rs) throws SQLException {
		Show show = new Show(rs.getInt("id"));
		populateColumns(show, rs);
		return show;
	}

	public static List<Show> getAll() throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE;
		return new Mapper<Show>(getCon(), query).toEntityList(rs -> Show.create(rs));
	}

	public static Show getSingle(long id) throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE + " WHERE `id`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setLong(1, id);
		return new Mapper<Show>(stmt).toEntity(rs -> Show.create(rs));
	}

	public static Show create(JSONObject input) throws SQLException, JSONException {
		String query = "INSERT INTO " + TABLE + " SET `name`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setString(1, input.getString("name"));
		int id = executeInsert(stmt);
		stmt.getConnection().close();
		return getSingle(id);
	}

	public void setName(String name) throws SQLException {
		String query = "UPDATE " + TABLE + " SET `name`=? WHERE `id`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setString(1, name);
		stmt.setInt(2, id);
		stmt.executeUpdate();
	}
}
