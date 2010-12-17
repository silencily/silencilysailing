/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project cardAssistant
 */
package net.silencily.sailing.framework.utils;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Proxy of ResultSet , to decode the string from database's encoding to "GBK" and trim the result String
 * @see #getString(int)
 * @see #getString(String)
 * @see #updateString(int, String)
 * @see #updateString(String, String)
 * @see com.coheg.framework.util.DatabaseUtils
 * @since 2005-9-22
 * @author ����
 * @version $Id: ResultSetProxy.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public final class ResultSetProxy implements ResultSet {
	 
	//~ Static fields/initializers =============================================
	
	private ResultSet rs;
	
	
	//	~ Constructors ===========================================================
	
	/**
	 * Use Prototype Pattern 
	 * @param resultSetPrototype the resultset
	 */
	public ResultSetProxy(ResultSet resultSetPrototype) {
		Assert.notNull(resultSetPrototype, " resultSetPrototype must be specified; ");
		this.rs = resultSetPrototype;
	}
	
	//	~ Methods ===========================================================

	/**
	 * @see java.sql.ResultSet#absolute(int)
	 */
	public boolean absolute(int row) throws SQLException {
		return rs.absolute(row);
	}

	/**
	 * @see java.sql.ResultSet#afterLast()
	 */
	public void afterLast() throws SQLException {
		rs.afterLast();
	}


	/**
	 * @see java.sql.ResultSet#beforeFirst()
	 */
	public void beforeFirst() throws SQLException {
		rs.beforeFirst();
	}


	/**
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	public void cancelRowUpdates() throws SQLException {
		rs.cancelRowUpdates();
	}


	/**
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		rs.clearWarnings();
	}


	/**
	 * @see java.sql.ResultSet#close()
	 */
	public void close() throws SQLException {
		rs.close();
	}


	/**
	 * @see java.sql.ResultSet#deleteRow()
	 */
	public void deleteRow() throws SQLException {
		rs.deleteRow();
	}


	/**
	 * @see java.sql.ResultSet#findColumn(java.lang.String)
	 */
	public int findColumn(String columnName) throws SQLException {
		return rs.findColumn(columnName);
	}


	/**
	 * @see java.sql.ResultSet#first()
	 */
	public boolean first() throws SQLException {
		return rs.first();
	}


	/**
	 * @see java.sql.ResultSet#getArray(int)
	 */
	public Array getArray(int i) throws SQLException {
		return rs.getArray(i);
	}


	/**
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	public Array getArray(String colName) throws SQLException {
		return rs.getArray(colName);
	}


	/**
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return rs.getAsciiStream(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	public InputStream getAsciiStream(String columnName) throws SQLException {
		return rs.getAsciiStream(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getBigDecimal(int, int)
	 */
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return rs.getBigDecimal(columnIndex, scale);
	}


	/**
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rs.getBigDecimal(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
	 */
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		return rs.getBigDecimal(columnName, scale);
	}


	/**
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return rs.getBigDecimal(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rs.getBinaryStream(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	public InputStream getBinaryStream(String columnName) throws SQLException {
		return rs.getBinaryStream(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	public Blob getBlob(int i) throws SQLException {
		return rs.getBlob(i);
	}


	/**
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	public Blob getBlob(String colName) throws SQLException {
		return rs.getBlob(colName);
	}


	/**
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		return rs.getBoolean(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String columnName) throws SQLException {
		return rs.getBoolean(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getByte(int)
	 */
	public byte getByte(int columnIndex) throws SQLException {
		return rs.getByte(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	public byte getByte(String columnName) throws SQLException {
		return rs.getByte(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		return rs.getBytes(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String columnName) throws SQLException {
		return rs.getBytes(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rs.getCharacterStream(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	public Reader getCharacterStream(String columnName) throws SQLException {
		return rs.getCharacterStream(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getClob(int)
	 */
	public Clob getClob(int i) throws SQLException {
		return rs.getClob(i);
	}


	/**
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	public Clob getClob(String colName) throws SQLException {
		return rs.getClob(colName);
	}


	/**
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	public int getConcurrency() throws SQLException {
		return rs.getConcurrency();
	}


	/**
	 * @see java.sql.ResultSet#getCursorName()
	 */
	public String getCursorName() throws SQLException {
		return rs.getCursorName();
	}


	/**
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return rs.getDate(columnIndex, cal);
	}


	/**
	 * @see java.sql.ResultSet#getDate(int)
	 */
	public Date getDate(int columnIndex) throws SQLException {
		return rs.getDate(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return rs.getDate(columnName, cal);
	}


	/**
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	public Date getDate(String columnName) throws SQLException {
		return rs.getDate(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	public double getDouble(int columnIndex) throws SQLException {
		return rs.getDouble(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(String columnName) throws SQLException {
		return rs.getDouble(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return rs.getFetchDirection();
	}


	/**
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return rs.getFetchSize();
	}


	/**
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	public float getFloat(int columnIndex) throws SQLException {
		return rs.getFloat(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	public float getFloat(String columnName) throws SQLException {
		return rs.getFloat(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getInt(int)
	 */
	public int getInt(int columnIndex) throws SQLException {
		return rs.getInt(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	public int getInt(String columnName) throws SQLException {
		return rs.getInt(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getLong(int)
	 */
	public long getLong(int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	public long getLong(String columnName) throws SQLException {
		return rs.getLong(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return rs.getMetaData();
	}


	/**
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	public Object getObject(int i, Map map) throws SQLException {
		return rs.getObject(i, map);
	}


	/**
	 * @see java.sql.ResultSet#getObject(int)
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return rs.getObject(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject(String colName, Map map) throws SQLException {
		return rs.getObject(colName, map);
	}


	/**
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	public Object getObject(String columnName) throws SQLException {
		return rs.getObject(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getRef(int)
	 */
	public Ref getRef(int i) throws SQLException {
		return rs.getRef(i);
	}


	/**
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	public Ref getRef(String colName) throws SQLException {
		return rs.getRef(colName);
	}


	/**
	 * @see java.sql.ResultSet#getRow()
	 */
	public int getRow() throws SQLException {
		return rs.getRow();
	}


	/**
	 * @see java.sql.ResultSet#getShort(int)
	 */
	public short getShort(int columnIndex) throws SQLException {
		return rs.getShort(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	public short getShort(String columnName) throws SQLException {
		return rs.getShort(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getStatement()
	 */
	public Statement getStatement() throws SQLException {
		return rs.getStatement();
	}


	/**
	 * @see java.sql.ResultSet#getString(int)
	 */
	public String getString(int columnIndex) throws SQLException {
		return DatabaseUtils.transformString2GBKAndTrim(rs.getString(columnIndex));
	}


	/**
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	public String getString(String columnName) throws SQLException {
		return DatabaseUtils.transformString2GBKAndTrim(rs.getString(columnName));
	}


	/**
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return rs.getTime(columnIndex, cal);
	}


	/**
	 * @see java.sql.ResultSet#getTime(int)
	 */
	public Time getTime(int columnIndex) throws SQLException {
		return rs.getTime(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return rs.getTime(columnName, cal);
	}


	/**
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	public Time getTime(String columnName) throws SQLException {
		return rs.getTime(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return rs.getTimestamp(columnIndex, cal);
	}


	/**
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rs.getTimestamp(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return rs.getTimestamp(columnName, cal);
	}


	/**
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return rs.getTimestamp(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getType()
	 */
	public int getType() throws SQLException {
		return rs.getType();
	}


	/**
	 * @see java.sql.ResultSet#getUnicodeStream(int)
	 */
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return rs.getUnicodeStream(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
	 */
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return rs.getUnicodeStream(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getURL(int)
	 */
	public URL getURL(int columnIndex) throws SQLException {
		return rs.getURL(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	public URL getURL(String columnName) throws SQLException {
		return rs.getURL(columnName);
	}


	/**
	 * @see java.sql.ResultSet#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return rs.getWarnings();
	}


	/**
	 * @see java.sql.ResultSet#insertRow()
	 */
	public void insertRow() throws SQLException {
		rs.insertRow();
	}


	/**
	 * @see java.sql.ResultSet#isAfterLast()
	 */
	public boolean isAfterLast() throws SQLException {
		return rs.isAfterLast();
	}


	/**
	 * @see java.sql.ResultSet#isBeforeFirst()
	 */
	public boolean isBeforeFirst() throws SQLException {
		return rs.isBeforeFirst();
	}


	/**
	 * @see java.sql.ResultSet#isFirst()
	 */
	public boolean isFirst() throws SQLException {
		return rs.isFirst();
	}


	/**
	 * @see java.sql.ResultSet#isLast()
	 */
	public boolean isLast() throws SQLException {
		return rs.isLast();
	}


	/**
	 * @see java.sql.ResultSet#last()
	 */
	public boolean last() throws SQLException {
		return rs.last();
	}


	/**
	 * @see java.sql.ResultSet#moveToCurrentRow()
	 */
	public void moveToCurrentRow() throws SQLException {
		rs.moveToCurrentRow();
	}


	/**
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	public void moveToInsertRow() throws SQLException {
		rs.moveToInsertRow();
	}


	/**
	 * @see java.sql.ResultSet#next()
	 */
	public boolean next() throws SQLException {
		return rs.next();
	}


	/**
	 * @see java.sql.ResultSet#previous()
	 */
	public boolean previous() throws SQLException {
		return rs.previous();
	}


	/**
	 * @see java.sql.ResultSet#refreshRow()
	 */
	public void refreshRow() throws SQLException {
		rs.refreshRow();
	}


	/**
	 * @see java.sql.ResultSet#relative(int)
	 */
	public boolean relative(int rows) throws SQLException {
		return rs.relative(rows);
	}


	/**
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	public boolean rowDeleted() throws SQLException {
		return rs.rowDeleted();
	}


	/**
	 * @see java.sql.ResultSet#rowInserted()
	 */
	public boolean rowInserted() throws SQLException {
		return rs.rowInserted();
	}


	/**
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	public boolean rowUpdated() throws SQLException {
		return rs.rowUpdated();
	}


	/**
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		rs.setFetchDirection(direction);
	}


	/**
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		rs.setFetchSize(rows);
	}


	/**
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	public void updateArray(int columnIndex, Array x) throws SQLException {
		rs.updateArray(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	public void updateArray(String columnName, Array x) throws SQLException {
		rs.updateArray(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		rs.updateAsciiStream(columnIndex, x, length);
	}


	/**
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
	 */
	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
		rs.updateAsciiStream(columnName, x, length);
	}


	/**
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		rs.updateBigDecimal(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
		rs.updateBigDecimal(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		rs.updateBinaryStream(columnIndex, x, length);
	}


	/**
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
	 */
	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
		rs.updateBinaryStream(columnName, x, length);
	}


	/**
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		rs.updateBlob(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	public void updateBlob(String columnName, Blob x) throws SQLException {
		rs.updateBlob(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		rs.updateBoolean(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
	 */
	public void updateBoolean(String columnName, boolean x) throws SQLException {
		rs.updateBoolean(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	public void updateByte(int columnIndex, byte x) throws SQLException {
		rs.updateByte(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
	 */
	public void updateByte(String columnName, byte x) throws SQLException {
		rs.updateByte(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		rs.updateBytes(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
	 */
	public void updateBytes(String columnName, byte[] x) throws SQLException {
		rs.updateBytes(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		rs.updateCharacterStream(columnIndex, x, length);
	}


	/**
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
	 */
	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
		rs.updateCharacterStream(columnName, reader, length);
	}


	/**
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		rs.updateClob(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	public void updateClob(String columnName, Clob x) throws SQLException {
		rs.updateClob(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	public void updateDate(int columnIndex, Date x) throws SQLException {
		rs.updateDate(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
	 */
	public void updateDate(String columnName, Date x) throws SQLException {
		rs.updateDate(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	public void updateDouble(int columnIndex, double x) throws SQLException {
		rs.updateDouble(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
	 */
	public void updateDouble(String columnName, double x) throws SQLException {
		rs.updateDouble(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	public void updateFloat(int columnIndex, float x) throws SQLException {
		rs.updateFloat(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
	 */
	public void updateFloat(String columnName, float x) throws SQLException {
		rs.updateFloat(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	public void updateInt(int columnIndex, int x) throws SQLException {
		rs.updateInt(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateInt(java.lang.String, int)
	 */
	public void updateInt(String columnName, int x) throws SQLException {
		rs.updateInt(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	public void updateLong(int columnIndex, long x) throws SQLException {
		rs.updateLong(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateLong(java.lang.String, long)
	 */
	public void updateLong(String columnName, long x) throws SQLException {
		rs.updateLong(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	public void updateNull(int columnIndex) throws SQLException {
		rs.updateNull(columnIndex);
	}


	/**
	 * @see java.sql.ResultSet#updateNull(java.lang.String)
	 */
	public void updateNull(String columnName) throws SQLException {
		rs.updateNull(columnName);
	}


	/**
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
		rs.updateObject(columnIndex, x, scale);
	}


	/**
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	public void updateObject(int columnIndex, Object x) throws SQLException {
		rs.updateObject(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object, int)
	 */
	public void updateObject(String columnName, Object x, int scale) throws SQLException {
		rs.updateObject(columnName, x, scale);
	}


	/**
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
	 */
	public void updateObject(String columnName, Object x) throws SQLException {
		rs.updateObject(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		rs.updateRef(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	public void updateRef(String columnName, Ref x) throws SQLException {
		rs.updateRef(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateRow()
	 */
	public void updateRow() throws SQLException {
		rs.updateRow();
	}


	/**
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	public void updateShort(int columnIndex, short x) throws SQLException {
		rs.updateShort(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateShort(java.lang.String, short)
	 */
	public void updateShort(String columnName, short x) throws SQLException {
		rs.updateShort(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	public void updateString(int columnIndex, String x) throws SQLException {
		rs.updateString(columnIndex, DatabaseUtils.transformString2GBKAndTrim(x));
	}


	/**
	 * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
	 */
	public void updateString(String columnName, String x) throws SQLException {
		rs.updateString(columnName, DatabaseUtils.transformString2GBKAndTrim(x));
	}


	/**
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	public void updateTime(int columnIndex, Time x) throws SQLException {
		rs.updateTime(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
	 */
	public void updateTime(String columnName, Time x) throws SQLException {
		rs.updateTime(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		rs.updateTimestamp(columnIndex, x);
	}


	/**
	 * @see java.sql.ResultSet#updateTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
		rs.updateTimestamp(columnName, x);
	}


	/**
	 * @see java.sql.ResultSet#wasNull()
	 */
	public boolean wasNull() throws SQLException {
		return rs.wasNull();
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	//~ Methods ================================================================
	


}
