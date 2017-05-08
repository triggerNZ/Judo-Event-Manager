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

package au.com.jwatmuff.eventmanager.print;

import au.com.jwatmuff.eventmanager.model.cache.ResultInfoCache;
import au.com.jwatmuff.eventmanager.model.misc.PlayerGradingPoints;
import au.com.jwatmuff.eventmanager.model.vo.CompetitionInfo;
import au.com.jwatmuff.genericdb.Database;
import org.apache.velocity.context.Context;

/**
 *
 * @author James
 */
public class GradingPointsHTMLGenerator extends VelocityHTMLGenerator {
    private Database database;
    private ResultInfoCache cache;

    public GradingPointsHTMLGenerator(Database database, ResultInfoCache cache) {
        this.database = database;
        this.cache = cache;
    }

    @Override
    public void populateContext(Context c) {
        c.put("players", PlayerGradingPoints.getAllPlayerGradingPoints(cache, database));
        c.put("competitionName", database.get(CompetitionInfo.class, null).getName());
    }

    @Override
    public String getTemplateName() {
        return "gradingpoints.html";
    }
}
