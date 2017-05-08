/*
 * EventManager
 * Copyright (c) 2008-2017 James Watmuff & Leonard Hall
 *
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.com.jwatmuff.eventmanager.db.sqlite;

import au.com.jwatmuff.genericdb.transaction.GenericTransactionalDatabase;
import au.com.jwatmuff.genericdb.transaction.Transaction;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import javax.sql.DataSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author James
 */
public class SQLiteDatabase extends GenericTransactionalDatabase {
    private static Logger log = Logger.getLogger(SQLiteDatabase.class);
    
    private SimpleJdbcTemplate template;
    private TransactionTemplate transactionTemplate;

    /** Creates a new instance of SQLiteDriver */
    public SQLiteDatabase() {
        super();
    }
    
    public void setDataSource(DataSource dataSource) {
        template = new SimpleJdbcTemplate(dataSource);
        setTransactionManager(new DataSourceTransactionManager(dataSource));
    }
    
    public void afterPropertiesSet() {
        initDatabase();
        
        addDAO(new SQLiteCompetitionDAO(template));
        addDAO(new SQLitePlayerDAO(template));
        addDAO(new SQLitePlayerDetailsDAO(template));
        addDAO(new SQLitePoolDAO(template));
        addDAO(new SQLitePlayerPoolDAO(template));
        addDAO(new SQLiteFightDAO(template));
        addDAO(new SQLiteSessionDAO(template));
        addDAO(new SQLiteSessionPoolDAO(template));
        addDAO(new SQLiteSessionFightDAO(template));
        addDAO(new SQLiteSessionLinkDAO(template));
        addDAO(new SQLiteResultDAO(template));
    }
    
    private void initDatabase()
    {
        try {
            // open database creation script
            InputStream stream = SQLiteDatabase.class.getResourceAsStream("create.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();
            
            // execute each query in script
            while(true) {
                String line = reader.readLine();
                if(line == null) break;
                sb.append(line);
                if(line.contains(";")) {
                    template.update(sb.toString());
                    sb = new StringBuilder();
                }
            }
            log.debug("Database tables created (if necessary)");
        } catch(FileNotFoundException fnfe) {
            log.error("Unable to open database create script (create.sql)", fnfe);
            throw new RuntimeException(fnfe);
        } catch(IOException ioe) {
            log.error("Error while reading database create script", ioe);
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public void perform(final Transaction t) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                t.perform();
            }
        });
    }
    
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }
}
