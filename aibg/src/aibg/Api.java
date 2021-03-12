package aibg;

import actions.*;
import com.google.gson.Gson;
import dto.GameState;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Api {
    private static final String URL = "https://aibg2021.herokuapp.com/";

    private static GameState parseResponse(String response) {
        return new Gson().fromJson(response, GameState.class);
    }

    private static String createURLFromAction(Action action, int playerId, int gameId) {
        String path = "";
        String params = "";
        if (action instanceof FreeASpot) {
            FreeASpot a = (FreeASpot) action;
            path =  "freeASpot";
            params = "&x=" + a.x + "&y=" + a.y;
        }
        else if (action instanceof Move) {
            Move a = (Move) action;
            path =  "move";
            params = "&direction=" + a.direction + "&distance=" + a.distance;
        }
        else if (action instanceof Teleport || action == null) {
            path = "move";
            params = "&direction=w&distance=1";
        }
        else if (action instanceof SkipATurn) {
            path =  "skipATurn";
        }
        else if (action instanceof StealKoalas) {
            path = "stealKoalas";
        }
        else if (action instanceof MakeGame) {
            path = "makeGame";
        }
        /*else if (action instanceof BotVsBot) {
            BotVsBot a = (BotVsBot) action;
            path = "botVSbot";
            params = "&player1Id=" + a.playerId1 + "&player2Id=" + a.playerId2;
        }*/
        else if (action instanceof JoinGame) {
            path = "joinGame";
        }
        return URL + path + "?playerId=" + playerId + "&gameId=" + gameId + params;
    }

    public static GameState playAction(Action action, int playerId, int gameId) {
        String urlString = createURLFromAction(action, playerId, gameId);
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream responseStream = connection.getInputStream();
            String responseString;
            try (Scanner scanner = new Scanner(responseStream, StandardCharsets.UTF_8.name())) {
                responseString = scanner.useDelimiter("\\A").next();
            }
            return parseResponse(responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
