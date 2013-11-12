package org.uigl.liballdata;

import org.uigl.liballdata.DataTypes;

public abstract class AbstractCursor implements Cursor {
	
	protected long mPosition;
	protected long mRowId;
	protected Database mDatabase;
	protected Statement mStatement;
	protected int mRowColumnId;
	protected boolean mClosed;
	
	public AbstractCursor() {
		mPosition = -1;
		mRowColumnId = -1;
		mClosed = false;
	}

	public abstract Cursor requery();

	public void close() {
		mClosed = true;
		onClose();
	}
	
	public boolean isClosed() {
		return mClosed;
	}
	
	abstract protected void onClose();
	abstract public String[] getColumnNames();
	abstract public long getCount();
	abstract protected boolean onMove(long currentPosition, long newPosition);

	public boolean canUpdate() {
		return false;
	}

	public Database getDatabase() {
		return mDatabase;
	}
	
	public Statement getStatement() {
		return mStatement;
	}

	public int getColumnCount() {
		return getColumnNames().length;
	}

	public long getPosition() {
		return mPosition;
	}

	public boolean moveToNext() {
		return moveToPosition(mPosition + 1L);
	}

	public boolean moveToPrevious() {
		return moveToPosition(mPosition - 1L);
	}

	public boolean move(long offset) {
		return moveToPosition(mPosition + offset);
	}

	public boolean moveToPosition(long position) {
		final long count = getCount();
		boolean retVal = false;
		
        if (position >= count) {
            mPosition = count;
            retVal = false;
        } else if (position < 0) {
            mPosition = -1;
            retVal = false;
        } else if (position == mPosition) {
            retVal = true;
        } else if ( onMove(mPosition, position) ) {
            mPosition = position;
            if (mRowColumnId != -1)
                mRowId = Long.valueOf(getInt(mRowColumnId));
        } else {
            mPosition = -1;
            retVal = false;
        }

        return retVal;
	}

	public boolean moveToFirst() {
		return moveToPosition(0L);
	}

	public boolean moveToLast() {
		return moveToPosition(getCount() - 1L);
	}

	public boolean isFirst() {
		return mPosition == 0L && getCount() != 0L;
	}

	public boolean isLast() {
		final long count = getCount();
		return mPosition == (count - 1L) && count != 0L;
	}

	@Override
	public int getColumnIndex(String name) {
		int periodIndex = name.lastIndexOf('.');
		String columnName;
		if (periodIndex >= 0)
			columnName = name.substring(periodIndex + 1);
		else
			columnName = name;
		
		int i = 0, columnCount = getColumnNames().length;
		String[] columnNames = getColumnNames();
		for (; i < columnCount; i++)
			if (columnNames[i].equals(columnName))
				return i;
		
		return -1;
	}

	@Override
	public String getColumnName(int column) {
		return getColumnNames()[column];
	}

	abstract public DataTypes getType(int column);
	abstract public Long getInt(int column);
	abstract public Double getReal(int column);
	abstract public String getText(int column);
	abstract public Byte[] getBlob(int column);
	abstract public boolean isNull(int column);

	public void setInt(int column, Long value) {
		throw new UnsupportedOperationException("Cursor does not support updates.");
	}

	public void setReal(int column, Double value) {
		throw new UnsupportedOperationException("Cursor does not support updates.");
	}

	public void setText(int column, String value) {
		throw new UnsupportedOperationException("Cursor does not support updates.");
	}

	public void setBlob(int column, Byte[] value) {
		throw new UnsupportedOperationException("Cursor does not support updates.");
	}

	public void setNull(int column) {
		throw new UnsupportedOperationException("Cursor does not support updates.");
	}
}
