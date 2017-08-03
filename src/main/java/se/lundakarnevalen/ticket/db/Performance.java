package se.lundakarnevalen.ticket.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import se.lundakarnevalen.ticket.db.framework.Column;
import se.lundakarnevalen.ticket.db.framework.Mapper;

public class Performance extends Entity {
	@Column(name = "id")
	public final int id;
	@Column(name = "show_id")
	protected int show_id;
	@Column(name = "start_date")
	protected Timestamp start;

	private static final String TABLE = "`performances`";
	private static final String COLS = Entity.getCols(Performance.class);

	private Performance(int id) throws SQLException {
		this.id = id;
	}

	private static Performance create(ResultSet rs) throws SQLException {
		Performance perf = new Performance(rs.getInt("id"));
		perf.show_id = rs.getInt("show_id");
		perf.start = rs.getTimestamp("start_date");
		return perf;
	}

	public static List<Performance> getAll() throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE;
		return new Mapper<Performance>(getCon(), query).toEntityList(rs -> Performance.create(rs));
	}

	public static List<Performance> getByShow(int show_id) throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE + " WHERE `show_id`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setInt(1, show_id);
		return new Mapper<Performance>(stmt).toEntityList(rs -> Performance.create(rs));
	}

	public static Performance getSingle(long id) throws SQLException {
		String query = "SELECT " + COLS + " FROM " + TABLE + " WHERE `id`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setLong(1, id);
		return new Mapper<Performance>(stmt).toEntity(rs -> Performance.create(rs));
	}

	public static Performance create(int show_id, JSONObject input) throws SQLException, JSONException {
		String query = "INSERT INTO " + TABLE + " SET `show_id`=?, `start_date`=?";
		PreparedStatement stmt = prepare(query);
		stmt.setInt(1, show_id);
		stmt.setTimestamp(2, Timestamp.valueOf(input.getString("start") + ":00"));
		int id = executeInsert(stmt);
		return getSingle(id);
	}
}
