/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.jwatmuff.eventmanager.model.misc;

import au.com.jwatmuff.eventmanager.db.FightDAO;
import au.com.jwatmuff.eventmanager.model.draw.ConfigurationFile;
import au.com.jwatmuff.eventmanager.model.info.FightInfo;
import au.com.jwatmuff.eventmanager.model.info.PlayerPoolInfo;
import au.com.jwatmuff.eventmanager.model.vo.CompetitionInfo;
import au.com.jwatmuff.eventmanager.model.vo.Fight;
import au.com.jwatmuff.eventmanager.model.vo.Player;
import au.com.jwatmuff.eventmanager.model.vo.Pool;
import au.com.jwatmuff.genericdb.Database;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;	
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.velocity.context.Context;

/**
 *
 * @author James
 */
public class PlayerCodeParser {
    private static final Logger log = Logger.getLogger(PlayerCodeParser.class);


    // matches all possible player codes
    static String matcherPlayer = "P";
    static String matcherWinnerLooser = "[LW]+";
    static String matcherRoundRobin = "RAP?|RAT?|RBT?|RBP?|RFT?|RFP?";
    static String matcherBestOfThree = "TAP?|TAT?";
    static String matcherTieBreak = "RAT?|RBT?|RFT?|TAT?";
    private static Pattern codePattern = Pattern.compile("(P|[LW]+|RAP?|RAT?|RBP?|RBT?|RFP?|RFT?|TAP?|TAT?)([0-9]+)(?:-([0-9]+(?:-[0-9]+)*))?");
    
    public static enum PlayerType {
        NORMAL, BYE, UNDECIDED, EMPTY, ERROR
    }
    
    public static class FightPlayer {
        public PlayerType type;
        public Player player;
        public PlayerPoolInfo playerPoolInfo;
        public String code;
        public Pool division;
        
        @Override
        public String toString() {
            switch(type) {
                case NORMAL:
                    if(player.getTeam().isEmpty()){
                        return player.getFirstName() + " " + player.getLastName();
                    } else {
                        return player.getFirstName() + " " + player.getLastName() + " (" + player.getTeam() + ")";
                    }
                case BYE:
                    return "Bye";
                case EMPTY:
                    return "Reserved";
                case UNDECIDED:
                    return getPrefix(code) + getNumber(code) + " : " + division.getDescription();
            }
            return "Error";
        }
    }
    
    public static enum CodeType {
        PLAYER, WINNERLOOSER, ROUNDROBIN, BESTOFTHREE, ERROR
    }

    public static class CodeInfo {
        public String code;
        public CodeType type;
        public String prefix;
        public int number;
        public int[] params;
    }
    
    public static class PlayerRRScore {
        int playerID;
        int playerPos1;
        int playerPos2;
        Map<Integer,Integer>  fightPoints = new HashMap<Integer,Integer>();
        int wins;
        int points;
        int place;
        }

    private static Comparator<PlayerRRScore> PLAYERS_SCORE_COMPARATOR_RR = new Comparator<PlayerRRScore>(){
            @Override
            public int compare(PlayerRRScore p0, PlayerRRScore p1) {
                return (p1.wins - p0.wins != 0) ? p1.wins - p0.wins : p1.points - p0.points;
            }
        };

    private static Comparator<PlayerRRScore> PLAYERS_POS1_COMPARATOR_RR = new Comparator<PlayerRRScore>(){
            @Override
            public int compare(PlayerRRScore p0, PlayerRRScore p1) {
                return p0.playerPos1 - p1.playerPos1;
            }
        };

    private static Comparator<PlayerRRScore> PLAYERS_POS2_COMPARATOR_RR = new Comparator<PlayerRRScore>(){
            @Override
            public int compare(PlayerRRScore p0, PlayerRRScore p1) {
                return p0.playerPos2 - p1.playerPos2;
            }
        };

    private static Comparator<PlayerRRScore> PLAYERS_SCORE_COMPARATOR_BT = new Comparator<PlayerRRScore>(){
            @Override
            public int compare(PlayerRRScore p0, PlayerRRScore p1) {
                return p1.wins - p0.wins;
            }
        };

    private static Comparator<PlayerRRScore> PLAYERS_POS_COMPARATOR_BT = new Comparator<PlayerRRScore>(){
            @Override
            public int compare(PlayerRRScore p0, PlayerRRScore p1) {
                return 0;
            }
        };

        
    private PlayerCodeParser() {}
    
    public static boolean isValidCode(String code) {
        String[] orCodes = getORCodes(code);
        for(String orCode:orCodes){
            if(!codePattern.matcher(orCode).matches())
            return false;
        }
        return true;
    }
    
    public static String getPrefix(String code) {
        String[] orCodes = getORCodes(code);
        Matcher matcher = codePattern.matcher(orCodes[0]);
        if(matcher.matches())
            return matcher.group(1);
        else
            throw new IllegalArgumentException("Invalid player code: " + code);
    }
    
    public static int getNumber(String code) {
        String[] orCodes = getORCodes(code);
        Matcher matcher = codePattern.matcher(orCodes[0]);
        if(matcher.matches())
            return Integer.parseInt(matcher.group(2));
        else
            throw new IllegalArgumentException("Invalid player code: " + code);
    }

    public static int[] getParameters(String code) {
        String[] orCodes = getORCodes(code);
        Matcher matcher = codePattern.matcher(orCodes[0]);
        if(matcher.matches()) {
            if(matcher.groupCount() >= 3) {
                String[] paramsStr = matcher.group(3).split("-");
                int[] params = new int[paramsStr.length];
                for(int i = 0; i < paramsStr.length; i++) {
                    params[i] = Integer.valueOf(paramsStr[i]);
                }
                return params;
            } else {
                return new int[0];
            }
        } else {
            throw new IllegalArgumentException("Invalid player code: " + code);
        }
    }
    
    public static List<Integer> getAscendant(String code) {
        List<Integer> fightNumbers = new ArrayList<Integer>();
        String[] codes = getORCodes(code);
        for(String thisCode : codes){
            CodeInfo codeInfo = getCodeInfo(thisCode);
            switch(codeInfo.type) {
                case PLAYER:
                    break;
                case WINNERLOOSER:
                    fightNumbers.add(codeInfo.number);
                    break;
                case ROUNDROBIN:
                    for(int newNumber : codeInfo.params)
                        fightNumbers.add(newNumber);
                    break;
                case BESTOFTHREE:
                    for(int newNumber : codeInfo.params)
                        fightNumbers.add(newNumber);
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }
        return fightNumbers;
    }

    public static CodeInfo getCodeInfo(String code) {
        CodeInfo codeInfo = new CodeInfo();
        codeInfo.code = code;
        if(!isValidCode(code)){
            codeInfo.type = CodeType.ERROR;
            return codeInfo;
        }
                                                // e.g. RBT2-3-4-5
        codeInfo.prefix = getPrefix(code);      //      RBT
        codeInfo.number = getNumber(code);      //      2

        if(codeInfo.prefix.matches(matcherPlayer))
            codeInfo.type = CodeType.PLAYER;
        else if(codeInfo.prefix.matches(matcherWinnerLooser))
            codeInfo.type = CodeType.WINNERLOOSER;
        else if(codeInfo.prefix.matches(matcherRoundRobin)){
            codeInfo.type = CodeType.ROUNDROBIN;
            codeInfo.params = getParameters(code);  //      3,4,5
        } else if (codeInfo.prefix.matches(matcherBestOfThree)) {
            codeInfo.type = CodeType.BESTOFTHREE;
            codeInfo.params = getParameters(code);  //      3,4,5
        } else
            throw new IllegalArgumentException("Code not formatted correctly: '" + code + "'");
        return codeInfo;
    }

    public static String[] getORCodes(String code) {
        String[] codes = code.split("\\|");
        for(int i = 0; i < codes.length; i++){
            codes[i] = codes[i].trim();
        }
        return codes;
    }

    public static boolean isTieBreak(String code) {
        if(!isValidCode(code)){
            return false;
        }
        String prefix = getPrefix(code);      //      RBT
        return prefix.matches(matcherTieBreak);
    }

    private PlayerPoolInfo getPlayerPoolInfo(int playerID) {
        for(PlayerPoolInfo playerPoolInfo: playerInfoList ){
            if(playerPoolInfo != null && playerPoolInfo.getPlayer().getID() == playerID){
                return playerPoolInfo;
            }
        }
        return null;
    }

    public FightPlayer parseCode(String code) {
        if(playerInfoList == null || playerInfoList.isEmpty())
            throw new IllegalArgumentException("Must supply a non-empty list of PlayerPoolInfos");

        String[] codes = getORCodes(code);
        
        if(codes.length == 1){
            if(parseredCodes.containsKey(codes[0]))
                return parseredCodes.get(codes[0]);
            // Use prefix to work out which parsing method to call
            String prefix = getPrefix(codes[0]);
            if(prefix.matches(matcherPlayer)){
                parseredCodes.put(codes[0], parsePLWCode(codes[0]));
                return parseredCodes.get(codes[0]);
            }
            else if(prefix.matches(matcherWinnerLooser)){
                parseredCodes.put(codes[0], parsePLWCode(codes[0]));
                return parseredCodes.get(codes[0]);
            }
            else if(prefix.matches(matcherRoundRobin)){
                parseredCodes.put(codes[0], parseRRCode(codes[0]));
                return parseredCodes.get(codes[0]);
            }
            else if(prefix.matches(matcherBestOfThree)){
                parseredCodes.put(codes[0], parseRRCode(codes[0]));
                return parseredCodes.get(codes[0]);
            }
            else
                throw new IllegalArgumentException("Code not formatted correctly: '" + code + "'");
        } else if(getCodeInfo(codes[0]).type == CodeType.ROUNDROBIN || getCodeInfo(codes[0]).type == CodeType.BESTOFTHREE){

            String prefix = getPrefix(codes[0]);   //      RBT
            int number = getNumber(codes[0]);      //      2
            int[] params = getParameters(codes[0]);//      3,4,5

            FightPlayer fightPlayer = parseCode(codes[0]);
            if(fightPlayer.type == PlayerType.EMPTY) {
                int codePnt = 1;
                for(int i=1; i < number; i++) {
                    String newCode = prefix + i;
                    for(int j = 0; j < params.length; j++)
                        newCode = newCode + "-" + params[j];
                    if(parseCode(newCode).type == PlayerType.EMPTY)
                        codePnt = codePnt + 1;
                }
                if(codePnt<codes.length){
                    fightPlayer = parseCode(codes[codePnt]);
                    fightPlayer.code = codes[0];
                }
            }
            fightPlayer.code = codes[0];
            parseredCodes.put(codes[0], fightPlayer);
            return parseredCodes.get(codes[0]);
        } else {
            throw new IllegalArgumentException("Invalid player code: " + code);
        }
    }

    private List<FightInfo> getRRFightList(String code) {
        List<FightInfo> roundRobinFightInfoList = new ArrayList<FightInfo>();
        List<String> allCodes = new ArrayList<String>();
        List<Integer> allPlayerIDs = new ArrayList<Integer>();
        
        int[] params = getParameters(code);//      3,4,5

        for(int roundRobinFight : params) {
            if(roundRobinFight > fightInfoList.size()) {
                throw new RuntimeException("Round Robin fight numbers bigger than number of fights");
            }
            FightInfo fightInfo = fightInfoList.get(roundRobinFight-1);

            String[] codes = fightInfo.getAllPlayerCode();
            FightPlayer[] fightPlayers = new FightPlayer[] {
                parseCode(codes[0]),
                parseCode(codes[1])
            };
            if(fightPlayers[0].type != PlayerType.BYE && fightPlayers[1].type != PlayerType.BYE){
//                if(!fightInfo.resultKnown()) {
//                    roundRobinFightInfoList.clear();
//                    return roundRobinFightInfoList;
//                }
                roundRobinFightInfoList.add(fightInfo);
                if(fightPlayers[0].type != PlayerType.UNDECIDED && !allPlayerIDs.contains(fightPlayers[0].player.getID())){
                    allPlayerIDs.add(fightPlayers[0].player.getID());
                    allCodes.add(codes[0]);
                }
                if(fightPlayers[1].type != PlayerType.UNDECIDED && !allPlayerIDs.contains(fightPlayers[1].player.getID())){
                    allPlayerIDs.add(fightPlayers[1].player.getID());
                    allCodes.add(codes[1]);
                }
            } else {
                if(fightPlayers[0].playerPoolInfo != null && fightPlayers[1].playerPoolInfo != null){
                    if(fightPlayers[0].playerPoolInfo.isWithdrawn() || fightPlayers[1].playerPoolInfo.isWithdrawn()){
                        roundRobinFightInfoList.add(fightInfo);
                        if(!allPlayerIDs.contains(fightPlayers[0].player.getID())){
                            allPlayerIDs.add(fightPlayers[0].player.getID());
                            allCodes.add(codes[0]);
                        }
                        if(!allPlayerIDs.contains(fightPlayers[1].player.getID())){
                            allPlayerIDs.add(fightPlayers[1].player.getID());
                            allCodes.add(codes[1]);
                        }
                    }
                }
            }
        }
        for(int i = 0; i<allPlayerIDs.size(); i++){
            for(int j = 0; j<allPlayerIDs.size(); j++){
                boolean hasFight = false;
                for(FightInfo fightInfo: roundRobinFightInfoList){
                    String[] codes = fightInfo.getAllPlayerCode();
                    FightPlayer[] fightPlayers = new FightPlayer[] {
                        parseCode(codes[0]),
                        parseCode(codes[1])
                    };
                    if(fightPlayers[0].player != null && fightPlayers[1].player != null){
                        if((fightPlayers[0].player.getID() == allPlayerIDs.get(i) || fightPlayers[1].player.getID() == allPlayerIDs.get(i))){
                            if((fightPlayers[0].player.getID() == allPlayerIDs.get(j) || fightPlayers[1].player.getID() == allPlayerIDs.get(j))){
                                hasFight = true;
                                break;
                            }
                        }
                    }
                }
                if(!hasFight){
                    if(getCodeInfo(allCodes.get(i)).type == CodeType.ROUNDROBIN){
                        int[] newParams = getParameters(allCodes.get(i));
                        for(int roundRobinFight : newParams) {
                            if(roundRobinFight > fightInfoList.size()) {
                                throw new RuntimeException("Round Robin fight numbers bigger than number of fights");
                            }
                            FightInfo fight = fightInfoList.get(roundRobinFight-1);

                            String[] codes = fight.getAllPlayerCode();
                            FightPlayer[] fightPlayers = new FightPlayer[] {
                                parseCode(codes[0]),
                                parseCode(codes[1])
                            };
                            if(fightPlayers[0].playerPoolInfo != null && fightPlayers[1].playerPoolInfo != null){
                                if(fightPlayers[0].playerPoolInfo.getPlayer().getID().equals(allPlayerIDs.get(i)) || fightPlayers[1].playerPoolInfo.getPlayer().getID().equals(allPlayerIDs.get(i))){
                                    if(fightPlayers[0].playerPoolInfo.getPlayer().getID().equals(allPlayerIDs.get(j)) || fightPlayers[1].playerPoolInfo.getPlayer().getID().equals(allPlayerIDs.get(j))){
                                        roundRobinFightInfoList.add(fight);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return roundRobinFightInfoList;
    }

    private FightPlayer parseRRCode(String code) {

        FightPlayer fightPlayer = new FightPlayer();
        CodeType codeType = getCodeInfo(code).type;

        fightPlayer.code = code;
        for(PlayerPoolInfo playerPoolInfo : playerInfoList) {
            if(playerPoolInfo != null) {
                fightPlayer.division = playerPoolInfo.getPool();
                break;
            }
        }

        List<FightInfo> roundRobinFightInfoList = getRRFightList(code);

                                           // e.g. RBT2-3-4-5
        String prefix = getPrefix(code);   //      RBT
        int number = getNumber(code);      //      2
        int[] params = getParameters(code);//      3,4,5

        for(FightInfo fight : roundRobinFightInfoList) {

            String[] codes = fight.getAllPlayerCode();
            FightPlayer[] fightPlayers = new FightPlayer[] {
                parseCode(codes[0]),
                parseCode(codes[1])
            };
            if(fightPlayers[0].type != PlayerType.BYE && fightPlayers[1].type != PlayerType.BYE){
                if(!fight.resultKnown()) {
                    switch(prefix.charAt(2)) {
                        case 'T':
                            fightPlayer.type = PlayerType.EMPTY;
                            return fightPlayer;
                        case 'P':
                            fightPlayer.type = PlayerType.UNDECIDED;
                            return fightPlayer;
                        default:
                            fightPlayer.type = PlayerType.ERROR;
                            return fightPlayer;
                    }
                }
            }
        }

        if(roundRobinFightInfoList.isEmpty()){
            switch(prefix.charAt(2)) {
                case 'T':
                    fightPlayer.type = PlayerType.EMPTY;
                    return fightPlayer;
                case 'P':
                    fightPlayer.type = PlayerType.UNDECIDED;
                    return fightPlayer;
                default:
                    fightPlayer.type = PlayerType.ERROR;
                    return fightPlayer;
            }
        } else {
            List<PlayerRRScore> PlayerRRScore = roundRobinResults(codeType, roundRobinFightInfoList);
            if(PlayerRRScore.size()<number){
                fightPlayer.type = PlayerType.BYE;
                return fightPlayer;
            }

            boolean isTie = false;
            switch(prefix.charAt(2)) {

                case 'T':
// Offset number by start of Tie
                    for(int tieStart = 0 ; tieStart < PlayerRRScore.size()-1 ; tieStart++){
                        if(PlayerRRScore.get(tieStart).place == PlayerRRScore.get(tieStart+1).place ){
                            number = number + tieStart;
                            break;
                        }
                    }

// Check for Tie with another player
                    isTie = false;
                    if(PlayerRRScore.size() >= number){
                        if(PlayerRRScore.size() > number){
                            if(PlayerRRScore.get(number-1).place == PlayerRRScore.get(number).place ){
                                isTie = true;
                            }
                        }
                        if (number > 1){
                            if(PlayerRRScore.get(number-1).place == PlayerRRScore.get(number-2).place ){
                                isTie = true;
                            }
                        }
                    }
                    if(isTie){
                        fightPlayer.type = PlayerType.NORMAL;
                        fightPlayer.playerPoolInfo = getPlayerPoolInfo(PlayerRRScore.get(number-1).playerID);
                        fightPlayer.player = fightPlayer.playerPoolInfo.getPlayer();
                        if(fightPlayer.playerPoolInfo.isWithdrawn())
                            fightPlayer.type = PlayerType.BYE;
                        return fightPlayer;
                    } else {
                        fightPlayer.type = PlayerType.BYE;
                        return fightPlayer;
                    }

                case 'P':
// Check for Tie with another player
                    isTie = false;
                    if(PlayerRRScore.size() > number){
                        if(PlayerRRScore.get(number-1).place == PlayerRRScore.get(number).place ){
                            isTie = true;
                        }
                    }
                    if (number>1){
                        if(PlayerRRScore.get(number-1).place == PlayerRRScore.get(number-2).place ){
                            isTie = true;
                        }
                    }
                    if(isTie){
                        fightPlayer.type = PlayerType.EMPTY;
                        return fightPlayer;
                    } else {
                        fightPlayer.type = PlayerType.NORMAL;
                        fightPlayer.playerPoolInfo = getPlayerPoolInfo(PlayerRRScore.get(number-1).playerID);
                        fightPlayer.player = fightPlayer.playerPoolInfo.getPlayer();
                        if(fightPlayer.playerPoolInfo.isWithdrawn())
                            fightPlayer.type = PlayerType.BYE;
                        return fightPlayer;
                    }

                default:
                    fightPlayer.type = PlayerType.ERROR;
                    return fightPlayer;
            }
        }
    }

    private List<PlayerRRScore> roundRobinScores(List<FightInfo> roundRobinFightInfoList) {

//Add all playerPoolInfoList in fights to map
        Map<Integer,PlayerRRScore> playerRRScoresMap = new HashMap<Integer,PlayerRRScore>();

        for(FightInfo fightInfo : roundRobinFightInfoList) {
            String[] codes = fightInfo.getAllPlayerCode();
            FightPlayer[] fightPlayers = new FightPlayer[] {
                parseCode(codes[0]),
                parseCode(codes[1])
            };
            for(FightPlayer fightPlayer : fightPlayers){
                if (fightPlayer.player != null && !playerRRScoresMap.containsKey(fightPlayer.player.getID())){
                    PlayerRRScore playerRRScore = new PlayerRRScore();
                    playerRRScore.playerID = fightPlayer.player.getID();
                    playerRRScore.playerPos1 = fightPlayer.playerPoolInfo.getPlayerPool().getPlayerPosition();
                    playerRRScore.playerPos2 = fightPlayer.playerPoolInfo.getPlayerPool().getPlayerPosition2();
                    playerRRScore.wins = 0;
                    playerRRScore.points = 0;
                    playerRRScore.place = 0;
                    playerRRScoresMap.put(playerRRScore.playerID,playerRRScore);
                }
            }
        }

//Calculate accumulated wins and points
        for(FightInfo fightInfo : roundRobinFightInfoList) {
            if(fightInfo.resultKnown()){
                int winningPlayerSimpleScore = fightInfo.getWinningPlayerSimpleScore(configurationFile);
                PlayerRRScore winPlayerRRScore = playerRRScoresMap.get(fightInfo.getWinningPlayerID());
                winPlayerRRScore.wins = winPlayerRRScore.wins+1;
                winPlayerRRScore.points = winPlayerRRScore.points + winningPlayerSimpleScore;
                playerRRScoresMap.put(fightInfo.getWinningPlayerID(), winPlayerRRScore);
                winPlayerRRScore.fightPoints.put(fightInfo.getLosingPlayerID(),winningPlayerSimpleScore);
                playerRRScoresMap.put(fightInfo.getWinningPlayerID(), winPlayerRRScore);

                PlayerRRScore losePlayerRRScore = playerRRScoresMap.get(fightInfo.getLosingPlayerID());
                losePlayerRRScore.fightPoints.put(fightInfo.getWinningPlayerID(),0);
                playerRRScoresMap.put(fightInfo.getLosingPlayerID(), losePlayerRRScore);
            } else {
                String[] codes = fightInfo.getAllPlayerCode();
                FightPlayer[] fightPlayers = new FightPlayer[] {
                    parseCode(codes[0]),
                    parseCode(codes[1])
                };
                if(fightPlayers[0].playerPoolInfo != null && fightPlayers[1].playerPoolInfo != null && fightPlayers[0].playerPoolInfo.isWithdrawn() ^ fightPlayers[1].playerPoolInfo.isWithdrawn()){
                    if(fightPlayers[0].playerPoolInfo.isWithdrawn()){
                        PlayerRRScore winPlayerRRScore = playerRRScoresMap.get(fightPlayers[1].player.getID());
                        winPlayerRRScore.wins = winPlayerRRScore.wins+1;
                        winPlayerRRScore.points = winPlayerRRScore.points + configurationFile.getIntegerProperty("defaultVictoryPointsIppon", 10);
                        winPlayerRRScore.fightPoints.put(fightPlayers[0].player.getID(),configurationFile.getIntegerProperty("defaultVictoryPointsIppon", 10));
                        playerRRScoresMap.put(fightPlayers[1].player.getID(), winPlayerRRScore);

                        PlayerRRScore losePlayerRRScore = playerRRScoresMap.get(fightPlayers[0].player.getID());
                        losePlayerRRScore.fightPoints.put(fightPlayers[1].player.getID(),0);
                        playerRRScoresMap.put(fightPlayers[0].player.getID(), losePlayerRRScore);

                    }else if(fightPlayers[1].playerPoolInfo.isWithdrawn()){
                        PlayerRRScore winPlayerRRScore = playerRRScoresMap.get(fightPlayers[0].player.getID());
                        winPlayerRRScore.wins = winPlayerRRScore.wins+1;
                        winPlayerRRScore.points = winPlayerRRScore.points + configurationFile.getIntegerProperty("defaultVictoryPointsIppon", 10);
                        winPlayerRRScore.fightPoints.put(fightPlayers[1].player.getID(),configurationFile.getIntegerProperty("defaultVictoryPointsIppon", 10));
                        playerRRScoresMap.put(fightPlayers[0].player.getID(), winPlayerRRScore);

                        PlayerRRScore losePlayerRRScore = playerRRScoresMap.get(fightPlayers[1].player.getID());
                        losePlayerRRScore.fightPoints.put(fightPlayers[0].player.getID(),0);
                        playerRRScoresMap.put(fightPlayers[1].player.getID(), losePlayerRRScore);
                    }
                }
            }
        }

//Convert map to a list for sorting
        List<PlayerRRScore> playerRRScoresList = new ArrayList<PlayerRRScore>();
        for(Integer playerID : playerRRScoresMap.keySet()) {
            playerRRScoresList.add(playerRRScoresMap.get(playerID));
        }

//Sort List
        Collections.sort(playerRRScoresList, PLAYERS_POS1_COMPARATOR_RR);

        return playerRRScoresList;
    }

    public Context roundRobinContext(String code, Context c, Boolean showResults) {

                                           // e.g. RBT2-3-4-5
        String prefix = getPrefix(code);   //      RBT

        List<FightInfo> roundRobinFightInfoList = getRRFightList(code);
        List<PlayerRRScore> playerRRScoresList = roundRobinScores(roundRobinFightInfoList);
        Collections.sort(playerRRScoresList, PLAYERS_POS1_COMPARATOR_RR);

        for(int i = 0; i < playerRRScoresList.size(); i++ ){
            PlayerRRScore playerRRScores = playerRRScoresList.get(i);
            PlayerPoolInfo playerPoolInfo = getPlayerPoolInfo(playerRRScores.playerID);
            Player player = playerPoolInfo.getPlayer();
            if(playerPoolInfo.isWithdrawn())
                c.put("R" + prefix.charAt(1) + "P" + (i+1) , player.getLastName() + ", " + player.getFirstName() + " (FG)" );
            else
                c.put("R" + prefix.charAt(1) + "P" + (i+1) , player.getLastName() + ", " + player.getFirstName() );
            
            c.put("R" + prefix.charAt(1) + "PRegion" + (i+1) , player.getTeam() + " (" + player.getShortGrade() + ")" );
            if(showResults){
                for(int j = 0; j < playerRRScoresList.size(); j++ ){
                    if(i !=j && playerRRScores.fightPoints.get(playerRRScoresList.get(j).playerID) != null)
                        c.put("R" + prefix.charAt(1) + "P" + (i+1) + "P" + (j+1) , playerRRScores.fightPoints.get(playerRRScoresList.get(j).playerID)+"" );
                }
                c.put("R" + prefix.charAt(1) + "Wins" + (i+1) , playerRRScores.wins+"" );
                c.put("R" + prefix.charAt(1) + "Points" + (i+1) , playerRRScores.points+"" );
            }
        }
        return c;
    }

    private List<PlayerRRScore> roundRobinResults(CodeType codeType, List<FightInfo> roundRobinFightInfoList) {

        List<PlayerRRScore> playerRRScoresList = roundRobinScores(roundRobinFightInfoList);

        Comparator<PlayerRRScore> PLAYERS_SCORE_COMPARATOR = PLAYERS_SCORE_COMPARATOR_RR;
        Comparator<PlayerRRScore> PLAYERS_POS_COMPARATOR = PLAYERS_POS2_COMPARATOR_RR;
        if(codeType == CodeType.BESTOFTHREE){
            PLAYERS_SCORE_COMPARATOR = PLAYERS_SCORE_COMPARATOR_BT;
            PLAYERS_POS_COMPARATOR = PLAYERS_POS_COMPARATOR_BT;
        }

//Sort List
        Collections.sort(playerRRScoresList, PLAYERS_SCORE_COMPARATOR);


//find equals
        List<PlayerRRScore> playerRRTieList = new ArrayList<PlayerRRScore>();
        int i = 0;
        while(i < playerRRScoresList.size()) {
            int j = i;
            playerRRTieList.clear();
            while(j < playerRRScoresList.size()-1 && PLAYERS_SCORE_COMPARATOR.compare(playerRRScoresList.get(j), playerRRScoresList.get(j+1))==0){
                j++;
            }
            if(j > i){
                for(int k = i; k <= j; k++)
                    playerRRTieList.add(playerRRScoresList.get(k));
            }

// Detect all withdrawn
            boolean allWithdrawn = true;
            for(PlayerRRScore playerRRTie : playerRRTieList){
                if(!getPlayerPoolInfo(playerRRTie.playerID).isWithdrawn()){
                    allWithdrawn = false;
                    break;
                }
            }

// Are ZERO players tied?
            if(playerRRTieList.isEmpty()){
                playerRRScoresList.get(i).place = i;

// Are ALL players tied?
            } else if(playerRRScoresList.size()==playerRRTieList.size()){   //if all equal return place = 0

                for(int k = 0; k < playerRRScoresList.size(); k++){
                    playerRRScoresList.get(k).place = 0;
                }
                Collections.sort(playerRRScoresList, PLAYERS_POS_COMPARATOR);
                return playerRRScoresList;

// Are all players witdhrawn
            }else if(allWithdrawn){
                for(int k = i; k <= j; k++){
                    playerRRScoresList.get(k).place = j;
                }
// Are SOME players tied?
            }else{
                List<FightInfo> pairsOfFightInfoList = new ArrayList<FightInfo>();
                int matchedIDs;
                for(FightInfo fightInfo : roundRobinFightInfoList){
                    matchedIDs = 0;
                    for(int k = 0; k < playerRRTieList.size(); k++){
                        String[] codes = fightInfo.getAllPlayerCode();
                        FightPlayer[] fightPlayers = new FightPlayer[] {
                            parseCode(codes[0]),
                            parseCode(codes[1])
                        };
                        if(playerRRTieList.get(k).playerID == fightPlayers[0].player.getID() || playerRRTieList.get(k).playerID == fightPlayers[1].player.getID()){
                            matchedIDs++;
                        }
                    }
                    if(matchedIDs == 2)
                        pairsOfFightInfoList.add(fightInfo);
                }
                playerRRTieList = roundRobinResults(codeType, pairsOfFightInfoList);
                for(int k = 0; k < playerRRTieList.size(); k++){
                    playerRRTieList.get(k).place = playerRRTieList.get(k).place+i;
                    playerRRScoresList.set(i+k, playerRRTieList.get(k));
                }
                i = i + playerRRTieList.size()-1;
            }

            i++;
        }

        return playerRRScoresList;
    }

    private FightPlayer parsePLWCode(String code) {
        FightPlayer fightPlayer = new FightPlayer();

        fightPlayer.code = code;
        for(PlayerPoolInfo playerPoolInfo : playerInfoList) {
            if(playerPoolInfo != null) {
                fightPlayer.division = playerPoolInfo.getPool();
                break;
            }
        }
        
        String prefix = getPrefix(code);
        int number = getNumber(code);

        if(prefix.equals("P")) {
            if(playerInfoList.size() < number) {
                fightPlayer.type = PlayerType.BYE;
                return fightPlayer;
            } else if(playerInfoList.get(number-1) == null) {
                fightPlayer.type = PlayerType.BYE;
                return fightPlayer;
            } else {
                fightPlayer.playerPoolInfo = playerInfoList.get(number-1);
                fightPlayer.player = fightPlayer.playerPoolInfo.getPlayer();
                if(fightPlayer.playerPoolInfo.isWithdrawn()){
                    fightPlayer.type = PlayerType.BYE;
                }else{
                    fightPlayer.type = PlayerType.NORMAL;
                }
                return fightPlayer;
            }
        }

        if(number > fightInfoList.size()) {
            fightPlayer.type = PlayerType.ERROR;
            return fightPlayer;
        }

        FightInfo fight = fightInfoList.get(number-1);
        while(prefix.length() > 0) {
            isABye:
            if(!fight.resultKnown()) {
                // This handles fights with a bye player, and always returns the
                // first bye player as the loser and the second bye or player as the winner.
                // This works through all levels.

                String[] codes = fight.getAllPlayerCode();
                FightPlayer[] fightPlayers = new FightPlayer[] {
                    parseCode(codes[0]),
                    parseCode(codes[1])
                };

// Both Bye: Return bye
                if(fightPlayers[0].type == PlayerType.BYE && fightPlayers[1].type == PlayerType.BYE){
                    fightPlayer.type = PlayerType.BYE;
                    return fightPlayer;
                }

// Bye: Return other fight code
                for(int i = 0; i < 2; i++) {
                    int j = 1 - i; // other player
                    if(fightPlayers[i].type == PlayerType.BYE) {
                        if(prefix.equals("W")) return fightPlayers[j];
                        if(prefix.equals("L")) return fightPlayers[i];

                        switch(prefix.charAt(prefix.length()-1)) {
                            case 'W':
                                code = codes[j];
                                break isABye;
                            case 'L':
                                code = codes[i];
                                break isABye;
                            default:
                                fightPlayer.type = PlayerType.ERROR;
                                return fightPlayer;
                        }
                    }
                }
// Empty: Return Empty
                for(int i = 0; i < 2; i++) {
                    if(fightPlayers[i].type == PlayerType.EMPTY) {
                        fightPlayer.type = PlayerType.EMPTY;
                        return fightPlayer;
                    }
                }
                fightPlayer.type = PlayerType.UNDECIDED;
                return fightPlayer;

            } else {

                if(prefix.equals("W")){
                    fightPlayer.type = PlayerType.NORMAL;
                    fightPlayer.playerPoolInfo = getPlayerPoolInfo(fight.getWinningPlayerID());
                    fightPlayer.player = fightPlayer.playerPoolInfo.getPlayer();
                    if(fightPlayer.playerPoolInfo.isWithdrawn())
                        fightPlayer.type = PlayerType.BYE;
                    if(fightPlayer.player == null)
                        fightPlayer.type = PlayerType.ERROR;
                    return fightPlayer;
                }
                if(prefix.equals("L")){
                    fightPlayer.type = PlayerType.NORMAL;
                    fightPlayer.playerPoolInfo = getPlayerPoolInfo(fight.getLosingPlayerID());
                    fightPlayer.player = fightPlayer.playerPoolInfo.getPlayer();
                    if(fightPlayer.playerPoolInfo.isWithdrawn())
                        fightPlayer.type = PlayerType.BYE;
                    if(fightPlayer.player == null)
                        fightPlayer.type = PlayerType.ERROR;
                    return fightPlayer;
                }

                switch(prefix.charAt(prefix.length()-1)) {
                    case 'W':
                        code = fight.getWinningPlayerCode();
                        break;
                    case 'L':
                        code = fight.getLosingPlayerCode();
                        break;
                    default:
                        fightPlayer.type = PlayerType.ERROR;
                        return fightPlayer;
                }
            }
            
            if(!isValidCode(code) || getPrefix(code).equals("P")) {
                fightPlayer.type = PlayerType.ERROR;
                return fightPlayer;
            }

            try {
                fight = fightInfoList.get(getNumber(code)-1);
            } catch(IndexOutOfBoundsException e) {
                fightPlayer.type = PlayerType.ERROR;
                return fightPlayer;
            }
                    
            prefix = prefix.substring(0, prefix.length()-1);
        }
        
        fightPlayer.type = PlayerType.ERROR;
        return fightPlayer;
    }

    public boolean canPlayerUnWithdraw(int playerID) {
        List<Integer> fightNumbers = new ArrayList<Integer>();
        for(FightInfo fightInfo:fightInfoList){
            if(!fightInfo.resultKnown()){
                String[] codes = fightInfo.getAllPlayerCode();
                FightPlayer[] fightPlayers = new FightPlayer[] {
                    parseCode(codes[0]),
                    parseCode(codes[1])
                };
                for(FightPlayer fightPlayer:fightPlayers){
                    if(fightPlayer.type==PlayerType.BYE)
                        if(fightPlayer.playerPoolInfo != null)
                            if(fightPlayer.playerPoolInfo.isWithdrawn())
                                if(fightPlayer.player.getID() == playerID)
                                    fightNumbers.add(fightInfo.getFightPostion());
                }
            }
        }
        for(FightInfo fightInfo:fightInfoList){
            if(fightInfo.resultKnown()){
                String[] codes = fightInfo.getAllPlayerCode();
                List<Integer> pastFightNumbers = new ArrayList<Integer>();
                pastFightNumbers.addAll(getAscendant(codes[0]));
                pastFightNumbers.addAll(getAscendant(codes[1]));
                for(Integer pastFightNumber : pastFightNumbers){
                    if(fightNumbers.contains(pastFightNumber))
                        return false;
                }
            }
        }
        return true;
    }

    public boolean isSameTeam(String[] codes) {
        if(!isValidCode(codes[0]) || !isValidCode(codes[1])){
            return false;
        }
        System.out.print(codes);
        FightPlayer fightPlayer0 = parseCode(codes[0]);
        FightPlayer fightPlayer1 = parseCode(codes[1]);
        if(fightPlayer0.type == PlayerType.NORMAL && fightPlayer0.type == PlayerType.NORMAL && fightPlayer0.player != null && fightPlayer1.player != null)
            return fightPlayer0.player.getTeam().equalsIgnoreCase(fightPlayer1.player.getTeam());
        else
            return false;
    }

    public ArrayList<Integer> getDependentFights(int fightPosition) {
        ArrayList<Integer> dependentFights = new ArrayList<Integer>();
        dependentFights.add(fightPosition);

        for(String code : fightInfoList.get(fightPosition-1).getAllPlayerCode()) {
            List<Integer> ascendantNumbers = PlayerCodeParser.getAscendant(code);
            for(Integer ascendantNumber : ascendantNumbers) {
                dependentFights.addAll(getDependentFights(ascendantNumber));
            }
        }

        return dependentFights;
    }

    public ArrayList<Integer> getSameTeamFights() {
        ArrayList<Integer> sameTeamFights = new ArrayList<Integer>();

        for(FightInfo fightInfo : fightInfoList) {
            String[] codes = fightInfo.getAllPlayerCode();
            if(isSameTeam(codes))
                sameTeamFights.add(fightInfo.getFightPostion());
        }

        return sameTeamFights;
    }

    public boolean hasCommonPlayers(int[] fightPositions) {
        int i = fightPositions[0];
        int j = fightPositions[1];
        if(i < j) { int k = i; i = j; j = k; } //swap
        
        List<String> codesi = new ArrayList<String>();
        for(String code : fightInfoList.get(i-1).getAllPlayerCode()) {
            codesi.addAll(Arrays.asList(getORCodes(code)));
        }
        
        List<String> codesj = new ArrayList<String>();
        for(String code : fightInfoList.get(j-1).getAllPlayerCode()) {
            codesj.addAll(Arrays.asList(getORCodes(code)));
        }
        
        for(String codei : codesi) {
            CodeInfo codeInfo = getCodeInfo(codei);
            switch(codeInfo.type) {
                case PLAYER:
                    for(String codej : codesj)
                        if(codei.equals(codej)) return true;
                    break;
                case WINNERLOOSER:{
                    int n = getNumber(codei);
                    if(n == j){
                        return true;
                    }
                    for(String codej : codesj){
                        if(codei.equals(codej)){
                            return true;
                        }
                    }
                    break;
                }
                case ROUNDROBIN:
                    for(String codej : codesj){
                        if(codei.equals(codej)){
                            return true;
                        }
                    }
                    break;
                case BESTOFTHREE:
                    int[] params = getParameters(codei);
                    for(String codej : codesj){
                        if(codei.equals(codej)){
                            return true;
                        }
                    }
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public ArrayList<Player> getPossiblePlayers(int fightPosition) {

        ArrayList<Player> possiblePlayers = new ArrayList<Player>();
        for(String code : fightInfoList.get(fightPosition-1).getAllPlayerCode()) {
            if(PlayerCodeParser.getCodeInfo(code).type == PlayerCodeParser.CodeType.PLAYER) {
                FightPlayer tempPlayer = parseCode(code);
                if(tempPlayer.type == PlayerCodeParser.PlayerType.NORMAL)
                    if(tempPlayer.player != null){
                        if(!possiblePlayers.contains(tempPlayer.player))
                            possiblePlayers.add(tempPlayer.player);
                    }
            } else {
                for(int newNumber : getAscendant(code)){
                    for(Player player : getPossiblePlayers(newNumber)){
                        if(!possiblePlayers.contains(player))
                            possiblePlayers.add(player);
                    }
                }
            }
        }
        return possiblePlayers;
    }

    public int totalNumberOfFights() {

        return fightInfoList.size();
    }

    public int minimumNumberOfFights() {

        int minimumNumberOfFights = 0;
        for(FightInfo fightInfo: fightInfoList){

            String[] codes = fightInfo.getAllPlayerCode();
            FightPlayer[] fightPlayers = new FightPlayer[] {
                parseCode(codes[0]),
                parseCode(codes[1])
            };
            if(fightPlayers[0].type != PlayerType.BYE && fightPlayers[1].type != PlayerType.BYE){
                if(fightPlayers[0].type != PlayerType.EMPTY && fightPlayers[1].type != PlayerType.EMPTY){
                    minimumNumberOfFights++;
                }
            }
        }
        return minimumNumberOfFights;
    }
    
    /* Parser instance stuff */

    private List<PlayerPoolInfo> playerInfoList;
    private List<FightInfo> fightInfoList;
    private ConfigurationFile configurationFile;
    Map<String,FightPlayer> parseredCodes = new HashMap<String,FightPlayer>();

    private PlayerCodeParser(Database database, int poolID) {
        playerInfoList = PoolPlayerSequencer.getPlayerSequence(database, poolID);
        List<Fight> fights = new ArrayList<Fight>(database.findAll(Fight.class, FightDAO.FOR_POOL, poolID));

        fightInfoList = new ArrayList<FightInfo>();
        for(Fight fight : fights)
            fightInfoList.add(FightInfo.getFightInfo(database, fight));

        CompetitionInfo ci = database.get(CompetitionInfo.class, null);
        configurationFile = ConfigurationFile.getConfiguration(ci.getDrawConfiguration());
    }

    public static PlayerCodeParser getInstance(Database database, int poolID) {
        PlayerCodeParser parser = new PlayerCodeParser(database, poolID);
        return parser;
    }

    public static FightPlayer parseCode(Database database, String code, int poolID) throws DatabaseStateException {
        return PlayerCodeParser.getInstance(database, poolID).parseCode(code);
    }

    public static Context getRRResults(Database database, String code, int poolID, Context c, Boolean showResults) throws DatabaseStateException {
        return PlayerCodeParser.getInstance(database, poolID).roundRobinContext(code, c, showResults);
    }
}
