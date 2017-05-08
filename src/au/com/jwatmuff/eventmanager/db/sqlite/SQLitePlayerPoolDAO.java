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

import au.com.jwatmuff.eventmanager.db.PlayerPoolDAO;
import au.com.jwatmuff.eventmanager.model.vo.PlayerPool;
import au.com.jwatmuff.genericdb.distributed.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 *
 * @author James
 */
public class SQLitePlayerPoolDAO implements PlayerPoolDAO {
    private final SimpleJdbcTemplate template;
    private String tableName = "player_has_pool";

    private final static String
            PLAYER_ID_FIELD = "player_id",
            POOL_ID_FIELD = "pool_id",
            PLAYER_POSITION_FIELD = "player_pos",
            PLAYER_POSITION_FIELD_2 = "player_pos_2",
            SEED_FIELD = "seed",
            STATUS_FIELD = "status",
            APPROVED_FIELD = "approved",
            VALID_FIELD = "is_valid",
            TIMESTAMP_FIELD = "last_updated";
    
    
    public SQLitePlayerPoolDAO(SimpleJdbcTemplate template) {
        this.template = template;
    }
    
    protected SQLitePlayerPoolDAO(SimpleJdbcTemplate template, String tableName) {
        this(template);
        this.tableName = tableName;
    }
    
    private static final ParameterizedRowMapper<PlayerPool> mapper =
            new ParameterizedRowMapper<PlayerPool>() {
        @Override
        public PlayerPool mapRow(ResultSet rs, int rowNum) throws SQLException {
            boolean locked = rs.getString("is_locked").equals("true");
            PlayerPool pp = new PlayerPool(locked);

            pp.setPlayerID(rs.getInt(PLAYER_ID_FIELD));
            pp.setPoolID(rs.getInt(POOL_ID_FIELD));
            pp.setPlayerPosition(rs.getInt(PLAYER_POSITION_FIELD));
            pp.setPlayerPosition2(rs.getInt(PLAYER_POSITION_FIELD_2));
            pp.setSeed(rs.getInt(SEED_FIELD));
            pp.setStatus(PlayerPool.Status.fromString(rs.getString(STATUS_FIELD)));
            pp.setApproved(rs.getBoolean(APPROVED_FIELD));
            pp.setValid(rs.getBoolean(VALID_FIELD));
            pp.setTimestamp(new Timestamp(rs.getDate(TIMESTAMP_FIELD).getTime()));
            
            return pp;
        }
    };
    
    @Override
    public PlayerPool get(Object id) {
        assert (id instanceof PlayerPool.Key) : "Key must be of type PlayerPool.Key";
        try {
            PlayerPool.Key key = (PlayerPool.Key)id;
            final String sql = "SELECT * FROM player_has_pool WHERE player_id = ? AND pool_id = ?";
            return template.queryForObject(sql, mapper, key.playerID, key.poolID);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    @Override
    public void add(PlayerPool pp) {
        final String sql = "INSERT INTO player_has_pool (player_id, pool_id, approved, player_pos, player_pos_2, seed, status, is_locked, is_valid, last_updated) VALUES (:playerID, :poolID, :approved, :playerPosition, :playerPosition2, :seed, :status, :locked, :valid, :timestamp);";
        SqlParameterSource params = new BeanPropertySqlParameterSource(pp);
        template.update(sql, params);        
    }
        
    @Override
    public void update(PlayerPool pp) {
        // had to comment this out to get player withdrawals working
        // TODO: revisit and think about what will happen if computers get out of sync
        //if(pp.isLocked() || get(pp.getID()).isLocked())
        //    throw new RuntimeException("Cannot update locked PlayerPool");
        final String sql = "UPDATE player_has_pool SET approved=:approved, player_pos=:playerPosition, player_pos_2=:playerPosition2, seed=:seed, status=:status, is_locked=:locked, is_valid=:valid, last_updated=:timestamp WHERE player_id=:playerID AND pool_id=:poolID";
        SqlParameterSource params = new BeanPropertySqlParameterSource(pp);
        template.update(sql, params);        
    }
    
    @Override
    public void delete(PlayerPool pp) {
        throw new RuntimeException("Delete not supported");
    }
    
    @Override
    public Collection<PlayerPool> findForPlayer(int playerID) {
        try {
            final String sql = "SELECT player_has_pool.* FROM player_has_pool, pool WHERE player_has_pool.pool_id = pool.id AND pool.is_valid AND player_id = ? AND player_has_pool.is_valid";
            return template.query(sql, mapper, playerID);
        } catch(EmptyResultDataAccessException e) {
            return new ArrayList<PlayerPool>();
        }
    }
    
    @Override
    public Collection<PlayerPool> findForPool(int poolID) {
        try {
            final String sql = "SELECT * FROM player_has_pool WHERE pool_id = ? AND is_valid";
            return template.query(sql, mapper, poolID);
        } catch(EmptyResultDataAccessException e) {
            return new ArrayList<PlayerPool>();
        }
    }

    @Override
    public Class<PlayerPool> getDataClass() {
        return PlayerPool.class;
    }
}
