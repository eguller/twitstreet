package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataObjectIF {

	void getDataFromResultSet(ResultSet rs) throws SQLException;
}
