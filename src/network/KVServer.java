package network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVServer {
    public static final int PORT = 8079;
    private final String apiToken;
    private final HttpServer kvServer;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        kvServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        kvServer.createContext("/register", this::register);
        kvServer.createContext("/save", this::save);
        kvServer.createContext("/load", this::load);
    }

    public static URI getServerURL() {
        return URI.create("http://localhost:" + PORT);
    }

    private void load(HttpExchange h) {
        try (h) {
            System.out.println("KVServer: /load");
            if (!hasAuth(h)) {
                System.out.println("KVServer: запрос не авторизован, нужен параметр в query API_TOKEN со значением " +
                        "апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("KVServer: Key для получения пустой. key указывается в пути: /load/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                if (data.containsKey(key)) {
                    sendText(h, " ");
                    System.out.println("KVServer: значение ключа " + key + " отправлено");
                    h.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("KVServer: ключа " + key + " нет");
                    h.sendResponseHeaders(404, 0);
                }
            } else {
                System.out.println("KVServer: /load ждёт GET-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void save(HttpExchange h) throws IOException {
        try (h) {
            System.out.println("KVServer: /save");
            if (!hasAuth(h)) {
                System.out.println("KVServer: запрос не авторизован, нужен параметр в query API_TOKEN со значением " +
                        "апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("KVServer: Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("KVServer: Value для сохранения пустой. value указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("KVServer: значение для ключа " + key + " успешно обновлено!");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("KVServer: /save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    private void register(HttpExchange h) throws IOException {
        try (h) {
            System.out.println("KVServer: /register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
                System.out.println("KVServer: регистрация прошла успешно");
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        kvServer.start();
    }

    public void stop() {
        kvServer.stop(0);
        System.out.println("остановили KVсервер" + PORT);
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}