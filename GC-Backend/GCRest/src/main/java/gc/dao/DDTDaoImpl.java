package gc.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gc.db.DBDdt;
import gc.model.DDT;
import gc.utils.Constants;

public class DDTDaoImpl implements Dao<DDT> {
	private static final Logger logger = LogManager.getLogger(DDTDaoImpl.class.getName());

	@Override
	public DDT get(int id) {
		DDT ddtData = DBDdt.getDDT(conn, id);
		return ddtData;
	}

	@Override
	public List<DDT> getAll() {
		List<DDT> ddtData = DBDdt.queryDDT(conn);
		return ddtData;
	}

	@Override
	public void save(DDT ddt) {
		try {
			int id = DBDdt.insertDDT(conn, ddt);
			ddt.setId(id);
			conn.commit();
		} catch (SQLException e) {
			logger.error("Error in method insertEvent: {}", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(Constants.ROLLBACK_ERROR, e1);
			}
		}
	}

	@Override
	public void update(DDT t, String[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(DDT t) {
		// TODO Auto-generated method stub
		
	}
}