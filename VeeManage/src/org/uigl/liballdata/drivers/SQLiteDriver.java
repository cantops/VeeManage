package org.uigl.liballdata.drivers;

import org.uigl.liballdata.BindParam;
import org.uigl.liballdata.Cursor;
import org.uigl.liballdata.DatabaseDriver;
import org.uigl.liballdata.DatabaseException;
import org.uigl.liballdata.Statement;

public class SQLiteDriver extends DatabaseDriver {

	@Override
	public void setConnection(Object... connectionParams) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public long open(boolean keepAlive) {
		//You can't open it so return 0;
		return 0;
	}

	@Override
	public long close(boolean saveConnection) {
		//You can't close it so return 0;
		return 0;
	}

	@Override
	public boolean isOpen() {
		//Technically always open.
		return true;
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Statement statement) throws DatabaseException {
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT");
		
		if (statement.getDistinct())
			query.append("DISTINCT");
		
		// Handle column names.
		if (statement.getColumns() == null || statement.getColumns().length == 0) {
			query.append("*");
		} else {
			int i = 0;
			for(;i < statement.getColumns().length; i++) {
				query.append(statement.getColumns()[i]).append(",");
			}
			query.append(statement.getColumns()[i]);
		}
		
		query.append("FROM").append(statement.getTable());
		
		if (statement.getSelection() != null)
			query.append(statement.getSelection());
		else
			query.append("WHERE 1=1");

		if (statement.getGroupBy() != null)
			query.append(statement.getGroupBy());
		
		if (statement.getHaving() != null)
			query.append(statement.getHaving());
		
		if (statement.getOrderBy() != null)
			query.append(statement.getOrderBy());
		
		if (statement.getLimit() != null)
			query.append(statement.getLimit());
		
		return rawQuery(query.toString(), statement.getSelectionArgs());
	}

	@Override
	public long dispose() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cursor rawQuery(String sql) throws DatabaseException {
		return rawQuery(sql, (BindParam[]) null);
	}
	
	@Override
	public Cursor rawQuery(String sql, BindParam ... params) throws DatabaseException {

		sql = bindParams(sql, params);
		
		//TODO:Do query.
		return null;
	}

	@Override
	public long insert(Statement statement) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long update(Statement statement) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long delete(Statement statement) {
		// TODO Auto-generated method stub
		return 0;
	}

}
