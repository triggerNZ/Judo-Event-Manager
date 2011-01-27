/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.jwatmuff.eventmanager.print;

import au.com.jwatmuff.eventmanager.db.FightDAO;
import au.com.jwatmuff.eventmanager.db.ResultDAO;
import au.com.jwatmuff.eventmanager.model.info.PlayerPoolInfo;
import au.com.jwatmuff.eventmanager.model.misc.PlayerCodeParser;
import au.com.jwatmuff.eventmanager.model.misc.PlayerCodeParser.CodeInfo;
import au.com.jwatmuff.eventmanager.model.misc.PlayerCodeParser.CodeType;
import au.com.jwatmuff.eventmanager.model.misc.PlayerCodeParser.FightPlayer;
import au.com.jwatmuff.eventmanager.model.misc.PoolPlayerSequencer;
import au.com.jwatmuff.eventmanager.model.vo.CompetitionInfo;
import au.com.jwatmuff.eventmanager.model.vo.Fight;
import au.com.jwatmuff.eventmanager.model.vo.Player;
import au.com.jwatmuff.eventmanager.model.vo.Pool;
import au.com.jwatmuff.eventmanager.model.vo.Pool.Place;
import au.com.jwatmuff.eventmanager.model.vo.Result;
import au.com.jwatmuff.genericdb.Database;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import org.apache.velocity.context.Context;

/**
 *
 * @author James
 */
public class DrawHTMLGenerator extends VelocityHTMLGenerator {
    private Database database;
    private int poolID;
    private boolean showResults;
    private boolean fullDocument;
    private boolean firstPage;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public DrawHTMLGenerator(Database database, int poolID, boolean showResults, boolean fullDocument, boolean firstPage) {
        this.database = database;
        this.poolID = poolID;
        this.showResults = showResults;
        this.fullDocument = fullDocument;
        this.firstPage = firstPage;
    }

    public DrawHTMLGenerator(Database database, int poolID, boolean showResults, boolean fullDocument) {
        this(database, poolID, showResults, fullDocument, false);
    }

    public DrawHTMLGenerator(Database database, int poolID, boolean showResults) {
        this(database, poolID, showResults, true);
    }

    public DrawHTMLGenerator(Database database, int poolID) {
        this(database, poolID, false);
    }

    @Override
    public void populateContext(Context c) {

//Add page break at the top of the page if this isn't the first page

        if(firstPage) c.put("first", "true");

//Print the competition information and division
        CompetitionInfo compInfo = database.get(CompetitionInfo.class, null);
        c.put("competitionName", compInfo.getName());
        c.put("competitionInfo",
                "Location: " + compInfo.getLocation() + ", " +
                DATE_FORMAT.format(compInfo.getStartDate()) + " to " +
                DATE_FORMAT.format(compInfo.getEndDate()));
        
        Pool pool = database.get(Pool.class, poolID);

        c.put("division", pool.getDescription());
        
        assert(pool != null && pool.getLockedStatus() != Pool.LockedStatus.UNLOCKED);

//Print the player names and information

        List<Player> playerList = new ArrayList<Player>();
        List<PlayerPoolInfo> playerPoolInfoList = PoolPlayerSequencer.getPlayerSequence(database, poolID);
        int i = 0;
        for(PlayerPoolInfo playerPoolInfo : playerPoolInfoList) {
            i++;
            
            if(playerPoolInfo == null) {
                c.put("player" + i, "BYE");
                continue;
            }
            Player player = playerPoolInfo.getPlayer();
            playerList.add(player);
//            c.put("player" + i, p.getLastName() + ", " + p.getFirstName().charAt(0) );
            c.put("player" + i, player.getLastName() + ", " + player.getFirstName() );
            if (player.getTeam() != null) {
                c.put("playerRegion" + i, player.getTeam() + " (" + player.getShortGrade() + ")" );
            } else {
                c.put("playerRegion" + i, " (" + player.getShortGrade() + ")" );
            }
        }
        while(i++ < 64) { //TODO: this should not be an arbitrary limit
            c.put("player" + i, "BYE");
        }

//Print the draw fight number or the mat fight number

        List<Fight> fights = database.findAll(Fight.class, FightDAO.FOR_POOL, poolID);
        /* add fight numbers */
        for(i = 1; i <= fights.size(); i++) {
            c.put("fightNumber" + i, i);
        }

//Add and convert the draw codes to player names. Adds WX and LX codes for individual fight results.

        if(showResults) {
            PlayerCodeParser parser = PlayerCodeParser.getInstance(database, poolID);

            Set<String> codes = new HashSet<String>();
            /* add all codes used in any fights */
            for(Fight fight : fights) {
                codes.addAll(Arrays.asList(fight.getPlayerCodes()));
            }
            /* add winner and loser codes for all fights */
            for(i = 1; i <= fights.size(); i++) {
                codes.add("W" + i);
                codes.add("L" + i);
            }

            for(String code : codes) {
                FightPlayer fightPlayer = parser.parseCode(code);
                switch(fightPlayer.type) {
                    case NORMAL:
                        c.put(PlayerCodeParser.getORCodes(code)[0],
                              fightPlayer.player.getLastName() + ", " + fightPlayer.player.getFirstName().charAt(0));
                        break;
                    case ERROR:
                        c.put(PlayerCodeParser.getORCodes(code)[0], "--"+code); // mark error with --
                        break;
                    case UNDECIDED:
                        c.put(PlayerCodeParser.getORCodes(code)[0], code);
                        break;
                    case BYE:
                        c.put(PlayerCodeParser.getORCodes(code)[0], "BYE");
                        break;
                    default:
                        c.put(PlayerCodeParser.getORCodes(code)[0], fightPlayer.type.toString());
                        break;
                }
            }

//Adds the place names and winning player names

            i = 0;
            List<Place> places = pool.getPlaces();
            for(Place place : places) {
//Add the place codes to list of codes
                codes.addAll(Arrays.asList(PlayerCodeParser.getORCodes(place.code)));
                FightPlayer fightPlayer = parser.parseCode(place.code);

                i++;
                switch(fightPlayer.type) {
                    case NORMAL:
                        if(fightPlayer.player != null) {
                            if (fightPlayer.player.getTeam() != null) {
                                c.put("place" + i, place.name + ": " + fightPlayer.player.getLastName() + ", " + fightPlayer.player.getFirstName() + " -- " + fightPlayer.player.getTeam());
                            } else {
                                c.put("place" + i, place.name + ": " + fightPlayer.player.getLastName() + ", " + fightPlayer.player.getFirstName());
                            }
                        } else {
                            c.put("place" + i, place.name + ": Error" + "--" + fightPlayer.code);
                        }
                        break;
                    case ERROR:
                        c.put("place" + i, place.name + "--" + fightPlayer.code); // mark error with --
                        break;
                    case UNDECIDED:
                            c.put("place" + i, place.name + ": UNDECIDED");
                        break;
                    default:
                        c.put("place" + i, place.name + "--" + fightPlayer.code);
                        break;
                }
            }

//Adds the score and points for each fight

            i = 0;
            for(Fight fight : fights) {
                i++;
                List<Result> results = database.findAll(Result.class, ResultDAO.FOR_FIGHT, fight.getID());
                if(!results.isEmpty()) {
                    c.put("result_" + i, results.get(0).getScores()[0].displayString() + " / " + results.get(0).getScores()[1].displayString() + "  " + results.get(0).getDurationString());
                }
            }
            
//Look for round robins

            Map<Character,int[]> roundRobinMap = new HashMap<Character,int[]>();
            for(String code : codes) {
                CodeInfo codeInfo = PlayerCodeParser.getCodeInfo(code);
                if(codeInfo.type == CodeType.ROUNDROBIN){
                    if(!roundRobinMap.containsKey(codeInfo.prefix.charAt(1)))
                        roundRobinMap.put(codeInfo.prefix.charAt(1), codeInfo.params);
                }
            }
            
//Print results of round robin fights
            
            for(Character roundRobinPnt : roundRobinMap.keySet() ){

                Map<Integer,Integer> wins = new HashMap<Integer,Integer>();
                Map<Integer,Integer> points = new HashMap<Integer,Integer>();
                for(PlayerPoolInfo player : playerPoolInfoList) {
                    if(player!=null){
                        if(!wins.containsKey(player.getPlayer().getID()))
                            wins.put(player.getPlayer().getID(), 0);
                        if(!points.containsKey(player.getPlayer().getID()))
                            points.put(player.getPlayer().getID(), 0);
                    }
                }

                for(int fightNumber : roundRobinMap.get(roundRobinPnt)) {
                    List<Result> results = database.findAll(Result.class, ResultDAO.FOR_FIGHT, fights.get(fightNumber-1).getID());
                    if(!results.isEmpty()) {
                        int[] scores = results.get(0).getSimpleScores(database);
                        int[] ids = results.get(0).getPlayerIDs();

                        c.put("R" + roundRobinPnt + fightNumber + "P1", scores[0]);
                        c.put("R" + roundRobinPnt + fightNumber + "P2", scores[1]);

                        if(scores[0] > scores[1]) {
                            wins.put(ids[0],wins.get(ids[0])+1);
                            points.put(ids[0],points.get(ids[0])+scores[0]);
                        } else {
                            wins.put(ids[1],wins.get(ids[1])+1);
                            points.put(ids[1],points.get(ids[1])+scores[1]);
                        }
                    }
                }

//Adds total wins and points

                i = 0;
                for(PlayerPoolInfo playerPoolInfo : playerPoolInfoList) {
                    i++;
                    if(playerPoolInfo!=null){
                        Player player = playerPoolInfo.getPlayer();
                        c.put("R" + roundRobinPnt + "Wins" + i , wins.get(player.getID()) );
                        c.put("R" + roundRobinPnt + "Points" + i , points.get(player.getID()) );
                    }
                }
            }

        }
        c.put("fullDocument", fullDocument);
    }

    @Override
    public String getTemplateName() {
        return database.get(Pool.class, poolID).getTemplateName() + ".html";
    }
}
