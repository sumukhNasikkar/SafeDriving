package com.java.mk.driving.db;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;


public class DBConnectionPool implements Runnable {
	
	private static final  String TAG = "com.java.parking.db.DBConnectionPool" ;
	private static final Logger logger = Logger.getLogger(TAG);

	private static DBConnectionPool dbConnectionPool;
	
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/safe_driving_db";
	private String username = "root";
	private String password = "root";
	private int maxConnections = 150;
	private int initialConnections = 20;
	private boolean waitIfBusy  = true;
	private Vector availableConnections;
	private Vector busyConnections;
	private boolean connectionPending = false;
	
  
	private Connection connection;
	
	private DBConnectionPool()
	{
    
		availableConnections = new Vector(initialConnections);
		busyConnections = new Vector();
    
		for(int i=0; i<initialConnections; i++) 
		{
			availableConnections.addElement(makeNewConnection());
//			logger.error("adding new connection no " + i);
		}
	}
	public static DBConnectionPool getInstance()
	{
		if(dbConnectionPool != null)
        {
        	return dbConnectionPool;
        }
		else
		{
			dbConnectionPool = new DBConnectionPool();	
			return dbConnectionPool;
        }
	}
	public synchronized Connection getConnection()throws SQLException {
		if (!availableConnections.isEmpty()) {
			Connection existingConnection =(Connection)availableConnections.lastElement();
			int lastIndex = availableConnections.size() - 1;
			availableConnections.removeElementAt(lastIndex);
//			logger.error("in getConnection giving connections no " + lastIndex);

			// If connection on available list is closed (e.g.,
			// it timed out), then remove it from available list
			// and repeat the process of obtaining a connection.
			// Also wake up threads that were waiting for a
			// connection because maxConnection limit was reached.
			if (existingConnection.isClosed()) {
				notifyAll(); // Freed up a spot for anybodqy waiting
				return(getConnection());
			} else {
				busyConnections.addElement(existingConnection);
//				logger.error("giving connections no " + lastIndex);
				return(existingConnection);
			}
		} 
		else{
      
			// Three possible cases:
			// 1) You haven't reached maxConnections limit. So
			//    establish one in the background if there isn't
			//    already one pending, then wait for
			//    the next available connection (whether or not
			//    it was the newly established one).
			// 2) You reached maxConnections limit and waitIfBusy
			//    flag is false. Throw SQLException in such a case.
			// 3) You reached maxConnections limit and waitIfBusy
			//    flag is true. Then do the same thing as in second
			//    part of step 1: wait for next available connection.
      
			if ((totalConnections() < maxConnections) && !connectionPending) {
				makeBackgroundConnection();
			} else if (!waitIfBusy) {
				logger.error(new SQLException("Connection limit reached"));
				throw new SQLException("Connection limit reached");
				
			}
			// Wait for either a new connection to be established
			// (if you called makeBackgroundConnection) or for
			// an existing connection to be freed up.
			try {
				wait();
			}catch(InterruptedException ie) {}
			// Someone freed up a connection, so try again.
			return(getConnection());
		}
	}

  // You can't just make a new connection in the foreground
  // when none are available, since this can take several
  // seconds with a slow network connection. Instead,
  // start a thread that establishes a new connection,
  // then wait. You get woken up either when the new connection
  // is established or if someone finishes with an existing
  // connection.

  private void makeBackgroundConnection() {
    connectionPending = true;
    try {
      Thread connectThread = new Thread(this);
      connectThread.start();
    } catch(OutOfMemoryError oome) {
      // Give up on new connection
		logger.error(oome);
		
    }
  }

  public void run() {
    try {
      Connection connection = makeNewConnection();
      synchronized(this) {
        availableConnections.addElement(connection);
        connectionPending = false;
        notifyAll();
      }
    } catch(Exception e) { // SQLException or OutOfMemory
      // Give up on new connection and wait for existing one
      // to free up.
		logger.error(e);
		
    }
  }

  // This explicitly makes a new connection. Called in
  // the foreground when initializing the ConnectionPool,
  // and called in the background when running.
  
  private Connection makeNewConnection(){
	  Connection connection = null;
	  try {
		  // Load database driver if not already loaded
		  Class.forName(driver).newInstance();
		  // Establish network connection to database
		  connection = DriverManager.getConnection(url,username,password);
		  //connection.set
		  logger.error("loading new connection in make new connection");
		  return connection  ;
	  } catch(ClassNotFoundException cnfe) {
		  System.out.println(cnfe);
		  logger.error(cnfe);
		  // Simplify try/catch blocks of people using this by
		  // throwing only one exception type.
	  } catch(SQLException e) {
		  System.out.println(e);
		  logger.error(e);
		  // Simplify try/catch blocks of people using this by
		  // throwing only one exception type.
	  }catch (Exception ex) {
          ex.printStackTrace();
          logger.error(ex);
      }
	  return connection;
  }

  public synchronized void free(Connection connection) {
    busyConnections.removeElement(connection);
    availableConnections.addElement(connection);
//	logger.error("freeing connection");
	
    // Wake up threads that are waiting for a connection
    notifyAll(); 
  }
    
  public synchronized int totalConnections() {
//		logger.error("totalConnections() = " + availableConnections.size() +busyConnections.size());
		
    return(availableConnections.size() +
           busyConnections.size());
  }

  /** Close all the connections. Use with caution:
   *  be sure no connections are in use before
   *  calling. Note that you are not <I>required</I> to
   *  call this when done with a ConnectionPool, since
   *  connections are guaranteed to be closed when
   *  garbage collected. But this method gives more control
   *  regarding when the connections are closed.
   */

  	public synchronized void closeAllConnections() {
  		closeConnections(availableConnections);
  		availableConnections = new Vector();
  		closeConnections(busyConnections);
  		busyConnections = new Vector();
  	}

  	private void closeConnections(Vector connections) {
  		try {
  			for(int i=0; i<connections.size(); i++) {
  				Connection connection = (Connection)connections.elementAt(i);
  				if (!connection.isClosed()) {
  					connection.close();
  				}
  			}
  		} catch(SQLException sqle) {
  			// Ignore errors; garbage collect anyhow
  		}
  	}
  	public synchronized String toString() {
  		String info =
  				"ConnectionPool(" + url + "," + username + ")" +
  						", available=" + availableConnections.size() +
  						", busy=" + busyConnections.size() +
  						", max=" + maxConnections;
  		return(info);
  	}
	
}
