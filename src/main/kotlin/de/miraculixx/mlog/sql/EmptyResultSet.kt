package de.miraculixx.mlog.sql

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.util.*

class EmptyResultSet: ResultSet {
    private fun getException() = NullPointerException("This ResultSet is empty. Check with ResultSet#next() before!")
    
    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        throw getException()
    }

    override fun isWrapperFor(iface: Class<*>?) = false

    override fun close() {}

    override fun next() = false

    override fun wasNull() = true

    override fun getString(columnIndex: Int): String {
        throw getException()
    }

    override fun getString(columnLabel: String?): String {
        throw getException()
    }

    override fun getBoolean(columnIndex: Int): Boolean {
        throw getException()
    }

    override fun getBoolean(columnLabel: String?): Boolean {
        throw getException()
    }

    override fun getByte(columnIndex: Int): Byte {
        throw getException()
    }

    override fun getByte(columnLabel: String?): Byte {
        throw getException()
    }

    override fun getShort(columnIndex: Int): Short {
        throw getException()
    }

    override fun getShort(columnLabel: String?): Short {
        throw getException()
    }

    override fun getInt(columnIndex: Int): Int {
        throw getException()
    }

    override fun getInt(columnLabel: String?): Int {
        throw getException()
    }

    override fun getLong(columnIndex: Int): Long {
        throw getException()
    }

    override fun getLong(columnLabel: String?): Long {
        throw getException()
    }

    override fun getFloat(columnIndex: Int): Float {
        throw getException()
    }

    override fun getFloat(columnLabel: String?): Float {
        throw getException()
    }

    override fun getDouble(columnIndex: Int): Double {
        throw getException()
    }

    override fun getDouble(columnLabel: String?): Double {
        throw getException()
    }

    @Deprecated("Deprecated in Java")
    override fun getBigDecimal(columnIndex: Int, scale: Int): BigDecimal {
        throw getException()
    }

    @Deprecated("Deprecated in Java")
    override fun getBigDecimal(columnLabel: String?, scale: Int): BigDecimal {
        throw getException()
    }

    override fun getBigDecimal(columnIndex: Int): BigDecimal {
        throw getException()
    }

    override fun getBigDecimal(columnLabel: String?): BigDecimal {
        throw getException()
    }

    override fun getBytes(columnIndex: Int): ByteArray {
        throw getException()
    }

    override fun getBytes(columnLabel: String?): ByteArray {
        throw getException()
    }

    override fun getDate(columnIndex: Int): Date {
        throw getException()
    }

    override fun getDate(columnLabel: String?): Date {
        throw getException()
    }

    override fun getDate(columnIndex: Int, cal: Calendar?): Date {
        throw getException()
    }

    override fun getDate(columnLabel: String?, cal: Calendar?): Date {
        throw getException()
    }

    override fun getTime(columnIndex: Int): Time {
        throw getException()
    }

    override fun getTime(columnLabel: String?): Time {
        throw getException()
    }

    override fun getTime(columnIndex: Int, cal: Calendar?): Time {
        throw getException()
    }

    override fun getTime(columnLabel: String?, cal: Calendar?): Time {
        throw getException()
    }

    override fun getTimestamp(columnIndex: Int): Timestamp {
        throw getException()
    }

    override fun getTimestamp(columnLabel: String?): Timestamp {
        throw getException()
    }

    override fun getTimestamp(columnIndex: Int, cal: Calendar?): Timestamp {
        throw getException()
    }

    override fun getTimestamp(columnLabel: String?, cal: Calendar?): Timestamp {
        throw getException()
    }

    override fun getAsciiStream(columnIndex: Int): InputStream {
        throw getException()
    }

    override fun getAsciiStream(columnLabel: String?): InputStream {
        throw getException()
    }

    @Deprecated("Deprecated in Java")
    override fun getUnicodeStream(columnIndex: Int): InputStream {
        throw getException()
    }

    @Deprecated("Deprecated in Java")
    override fun getUnicodeStream(columnLabel: String?): InputStream {
        throw getException()
    }

    override fun getBinaryStream(columnIndex: Int): InputStream {
        throw getException()
    }

    override fun getBinaryStream(columnLabel: String?): InputStream {
        throw getException()
    }

    override fun getWarnings(): SQLWarning {
        throw getException()
    }

    override fun clearWarnings() {
        throw getException()
    }

    override fun getCursorName(): String {
        throw getException()
    }

    override fun getMetaData(): ResultSetMetaData {
        throw getException()
    }

    override fun getObject(columnIndex: Int): Any {
        throw getException()
    }

    override fun getObject(columnLabel: String?): Any {
        throw getException()
    }

    override fun getObject(columnIndex: Int, map: MutableMap<String, Class<*>>?): Any {
        throw getException()
    }

    override fun getObject(columnLabel: String?, map: MutableMap<String, Class<*>>?): Any {
        throw getException()
    }

    override fun <T : Any?> getObject(columnIndex: Int, type: Class<T>?): T {
        throw getException()
    }

    override fun <T : Any?> getObject(columnLabel: String?, type: Class<T>?): T {
        throw getException()
    }

    override fun findColumn(columnLabel: String?): Int {
        throw getException()
    }

    override fun getCharacterStream(columnIndex: Int): Reader {
        throw getException()
    }

    override fun getCharacterStream(columnLabel: String?): Reader {
        throw getException()
    }

    override fun isBeforeFirst() = true

    override fun isAfterLast() = false

    override fun isFirst() = false

    override fun isLast() = false

    override fun beforeFirst() {}

    override fun afterLast() {}

    override fun first() = false

    override fun last() = false

    override fun getRow() = -1

    override fun absolute(row: Int) = false

    override fun relative(rows: Int) = false

    override fun previous() = false

    override fun setFetchDirection(direction: Int) {}

    override fun getFetchDirection() = 0

    override fun setFetchSize(rows: Int) {}

    override fun getFetchSize() = 0

    override fun getType(): Int {
        throw getException()
    }

    override fun getConcurrency(): Int {
        throw getException()
    }

    override fun rowUpdated(): Boolean {
        throw getException()
    }

    override fun rowInserted(): Boolean {
        throw getException()
    }

    override fun rowDeleted(): Boolean {
        throw getException()
    }

    override fun updateNull(columnIndex: Int) {}

    override fun updateNull(columnLabel: String?) {}

    override fun updateBoolean(columnIndex: Int, x: Boolean) {}

    override fun updateBoolean(columnLabel: String?, x: Boolean) {}

    override fun updateByte(columnIndex: Int, x: Byte) {}

    override fun updateByte(columnLabel: String?, x: Byte) {}

    override fun updateShort(columnIndex: Int, x: Short) {}

    override fun updateShort(columnLabel: String?, x: Short) {}

    override fun updateInt(columnIndex: Int, x: Int) {}

    override fun updateInt(columnLabel: String?, x: Int) {}

    override fun updateLong(columnIndex: Int, x: Long) {}

    override fun updateLong(columnLabel: String?, x: Long) {}

    override fun updateFloat(columnIndex: Int, x: Float) {}

    override fun updateFloat(columnLabel: String?, x: Float) {}

    override fun updateDouble(columnIndex: Int, x: Double) {}

    override fun updateDouble(columnLabel: String?, x: Double) {}

    override fun updateBigDecimal(columnIndex: Int, x: BigDecimal?) {}

    override fun updateBigDecimal(columnLabel: String?, x: BigDecimal?) {}

    override fun updateString(columnIndex: Int, x: String?) {}

    override fun updateString(columnLabel: String?, x: String?) {}

    override fun updateBytes(columnIndex: Int, x: ByteArray?) {}

    override fun updateBytes(columnLabel: String?, x: ByteArray?) {}

    override fun updateDate(columnIndex: Int, x: Date?) {}

    override fun updateDate(columnLabel: String?, x: Date?) {}

    override fun updateTime(columnIndex: Int, x: Time?) {}

    override fun updateTime(columnLabel: String?, x: Time?) {}

    override fun updateTimestamp(columnIndex: Int, x: Timestamp?) {}

    override fun updateTimestamp(columnLabel: String?, x: Timestamp?) {}

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Int) {}

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Int) {}

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Long) {}

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Long) {}

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?) {}

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?) {}

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Int) {}

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Int) {}

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Long) {}

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Long) {}

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?) {}

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?) {}

    override fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Int) {}

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Int) {}

    override fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Long) {}

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {}

    override fun updateCharacterStream(columnIndex: Int, x: Reader?) {}

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?) {}

    override fun updateObject(columnIndex: Int, x: Any?, scaleOrLength: Int) {}

    override fun updateObject(columnIndex: Int, x: Any?) {}

    override fun updateObject(columnLabel: String?, x: Any?, scaleOrLength: Int) {}

    override fun updateObject(columnLabel: String?, x: Any?) {}

    override fun insertRow() {}

    override fun updateRow() {}

    override fun deleteRow() {}

    override fun refreshRow() {}

    override fun cancelRowUpdates() {}

    override fun moveToInsertRow() {}

    override fun moveToCurrentRow() {}

    override fun getStatement(): Statement? = null

    override fun getRef(columnIndex: Int): Ref {
        throw getException()
    }

    override fun getRef(columnLabel: String?): Ref {
        throw getException()
    }

    override fun getBlob(columnIndex: Int): Blob {
        throw getException()
    }

    override fun getBlob(columnLabel: String?): Blob {
        throw getException()
    }

    override fun getClob(columnIndex: Int): Clob {
        throw getException()
    }

    override fun getClob(columnLabel: String?): Clob {
        throw getException()
    }

    override fun getArray(columnIndex: Int): Array {
        throw getException()
    }

    override fun getArray(columnLabel: String?): Array {
        throw getException()
    }

    override fun getURL(columnIndex: Int): URL {
        throw getException()
    }

    override fun getURL(columnLabel: String?): URL {
        throw getException()
    }

    override fun updateRef(columnIndex: Int, x: Ref?) {}

    override fun updateRef(columnLabel: String?, x: Ref?) {}

    override fun updateBlob(columnIndex: Int, x: Blob?) {}

    override fun updateBlob(columnLabel: String?, x: Blob?) {}

    override fun updateBlob(columnIndex: Int, inputStream: InputStream?, length: Long) {}

    override fun updateBlob(columnLabel: String?, inputStream: InputStream?, length: Long) {}

    override fun updateBlob(columnIndex: Int, inputStream: InputStream?) {}

    override fun updateBlob(columnLabel: String?, inputStream: InputStream?) {}

    override fun updateClob(columnIndex: Int, x: Clob?) {
        throw getException()
    }

    override fun updateClob(columnLabel: String?, x: Clob?) {}

    override fun updateClob(columnIndex: Int, reader: Reader?, length: Long) {}

    override fun updateClob(columnLabel: String?, reader: Reader?, length: Long) {}

    override fun updateClob(columnIndex: Int, reader: Reader?) {
        throw getException()
    }

    override fun updateClob(columnLabel: String?, reader: Reader?) {}

    override fun updateArray(columnIndex: Int, x: Array?) {}

    override fun updateArray(columnLabel: String?, x: Array?) {}

    override fun getRowId(columnIndex: Int): RowId? = null

    override fun getRowId(columnLabel: String?): RowId? = null

    override fun updateRowId(columnIndex: Int, x: RowId?) {}

    override fun updateRowId(columnLabel: String?, x: RowId?) {}

    override fun getHoldability() = ResultSet.CLOSE_CURSORS_AT_COMMIT

    override fun isClosed() = true

    override fun updateNString(columnIndex: Int, nString: String?) {}

    override fun updateNString(columnLabel: String?, nString: String?) {}

    override fun updateNClob(columnIndex: Int, nClob: NClob?) {}

    override fun updateNClob(columnLabel: String?, nClob: NClob?) {}

    override fun updateNClob(columnIndex: Int, reader: Reader?, length: Long) {}

    override fun updateNClob(columnLabel: String?, reader: Reader?, length: Long) {}

    override fun updateNClob(columnIndex: Int, reader: Reader?) {}

    override fun updateNClob(columnLabel: String?, reader: Reader?) {}

    override fun getNClob(columnIndex: Int): NClob {
        throw getException()
    }

    override fun getNClob(columnLabel: String?): NClob {
        throw getException()
    }

    override fun getSQLXML(columnIndex: Int): SQLXML {
        throw getException()
    }

    override fun getSQLXML(columnLabel: String?): SQLXML {
        throw getException()
    }

    override fun updateSQLXML(columnIndex: Int, xmlObject: SQLXML?) {}

    override fun updateSQLXML(columnLabel: String?, xmlObject: SQLXML?) {}

    override fun getNString(columnIndex: Int): String {
        throw getException()
    }

    override fun getNString(columnLabel: String?): String {
        throw getException()
    }

    override fun getNCharacterStream(columnIndex: Int): Reader {
        throw getException()
    }

    override fun getNCharacterStream(columnLabel: String?): Reader {
        throw getException()
    }

    override fun updateNCharacterStream(columnIndex: Int, x: Reader?, length: Long) {}

    override fun updateNCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {}

    override fun updateNCharacterStream(columnIndex: Int, x: Reader?) {}

    override fun updateNCharacterStream(columnLabel: String?, reader: Reader?) {}
}