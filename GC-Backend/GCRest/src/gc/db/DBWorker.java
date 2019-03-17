package gc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.einvoice.ContattiType;
import gc.model.Worker;

public class DBWorker {
	private static final Logger logger = LogManager
			.getLogger(DBWorker.class.getName());

	public static int insertWorker(Connection conn, Worker workerData)
			throws SQLException {
		String sql = "INSERT INTO gestione_cantieri.worker (name, surname, gender, fiscalCode, birthDate, "
				+ "birthPlace, birthProvPlace, married, telephone, email) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pstm = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pstm.setString(1, workerData.getName());
		pstm.setString(2, workerData.getSurname());
		pstm.setString(3, workerData.getGender());
		pstm.setString(4, workerData.getFiscalCode());
		pstm.setDate(5, workerData.getBirthDate());
		pstm.setString(6, workerData.getBirthPlace());
		pstm.setString(7, workerData.getBirthProvPlace());
		pstm.setBoolean(8, workerData.isMarried());
		if (workerData.getContacts() != null) {
			pstm.setString(9, workerData.getContacts().getTelefono());
			pstm.setString(10, workerData.getContacts().getEmail());
		}

		logger.info("insertWorker: " + pstm.toString());

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
			throw new SQLException(
					"Inserting worker failed, no rows affected.");
		}

		try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				workerData.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Inserting worker failed, no ID obtained.");
			}
		}
		pstm.close();
		return workerData.getId();
	}

	public static List<Worker> queryWorker(Connection conn)
			throws SQLException {
		String sql = "SELECT name, surname, gender, fiscalCode, birthDate, birthPlace, birthProvPlace, "
				+ "married, telephone, email FROM gestione_cantieri.worker";

		PreparedStatement pstm = conn.prepareStatement(sql);
		logger.info("queryWorker: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		List<Worker> list = new ArrayList<Worker>();
		while (rs.next()) {
			Worker worker = new Worker();
			worker.setName(rs.getString("name"));
			worker.setSurname(rs.getString("surname"));
			worker.setGender(rs.getString("gender"));
			worker.setFiscalCode(rs.getString("fiscalCode"));
			worker.setBirthDate(rs.getDate("birthDate"));
			worker.setBirthPlace(rs.getString("birthPlace"));
			worker.setBirthProvPlace(rs.getString("birthProvPlace"));
			worker.setMarried(rs.getBoolean("married"));
			ContattiType contacts = new ContattiType();
			contacts.setTelefono(rs.getString("telephone"));
			contacts.setEmail(rs.getString("email"));
			worker.setContacts(contacts);
			list.add(worker);
		}

		pstm.close();
		return list;
	}

	public static void updateWorker(Connection conn, Worker worker)
			throws SQLException {
		String sql = "UPDATE gestione_cantieri.worker "
				+ "SET name = ?, surname = ?, gender = ?, fiscalCode = ?, birthDate = ?, "
				+ "birthPlace = ?, birthProvPlace = ?, married = ?, telephone = ?, email = ? "
				+ "WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setString(1, worker.getName());
		pstm.setString(2, worker.getSurname());
		pstm.setString(3, worker.getGender());
		pstm.setString(4, worker.getFiscalCode());
		pstm.setDate(5, worker.getBirthDate());
		pstm.setString(6, worker.getBirthPlace());
		pstm.setString(7, worker.getBirthProvPlace());
		pstm.setBoolean(8, worker.isMarried());
		if (worker.getContacts() != null) {
			pstm.setString(9, worker.getContacts().getTelefono());
			pstm.setString(10, worker.getContacts().getEmail());
		}
		pstm.setInt(11, worker.getId());

		logger.info("updateWorker: " + pstm.toString());

		pstm.executeUpdate();
		pstm.close();
	}

	public static Worker selectWorker(Connection conn, Worker worker)
			throws Exception {
		String sql = "SELECT id, name, surname, gender, fiscalCode, birthDate, birthPlace, birthProvPlace, "
				+ "married, telephone, email "
				+ "FROM gestione_cantieri.worker "
				+ "WHERE id = ?";

		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setInt(1, worker.getId());
		logger.info("selectWorker: " + pstm.toString());

		ResultSet rs = pstm.executeQuery();
		Worker newWorker = null;
		while (rs.next()) {
			newWorker = new Worker();
			newWorker.setId(rs.getInt("id"));
			newWorker.setName(rs.getString("name"));
			newWorker.setSurname(rs.getString("surname"));
			newWorker.setGender(rs.getString("gender"));
			newWorker.setFiscalCode(rs.getString("fiscalCode"));
			newWorker.setBirthDate(rs.getDate("birthDate"));
			newWorker.setBirthPlace(rs.getString("birthPlace"));
			newWorker.setBirthProvPlace(rs.getString("birthProvPlace"));
			newWorker.setMarried(rs.getBoolean("married"));
			ContattiType contacts = new ContattiType();
			contacts.setTelefono(rs.getString("telephone"));
			contacts.setEmail(rs.getString("email"));
			newWorker.setContacts(contacts);
		}
		pstm.close();
		return newWorker;
	}
}
