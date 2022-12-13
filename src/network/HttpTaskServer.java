package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exception.ManagerSaveException;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8081;
    HttpServer httpServer;

    public HttpTaskServer(TaskManager httpManager) throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskHandler(httpManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    private Integer getIdFromExchange(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getRawQuery();
        String[] splitUrl = query.split("=");
        return Integer.parseInt(splitUrl[1]);
    }

    public class HttpTaskHandler implements HttpHandler {
        private final TaskManager manager;
        private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .serializeNulls()
                .create();

        public HttpTaskHandler(TaskManager manager) {
            this.manager = manager;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            URI uri = httpExchange.getRequestURI();
            String path = uri.getPath();
            String requestMethod = httpExchange.getRequestMethod();
            String response = null;

            switch (requestMethod) {
                case "GET":
                    if (path.endsWith("/task/") && uri.getRawQuery() == null) {
                        response = gson.toJson(manager.viewAllTask());
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (path.contains("/task/") && uri.getRawQuery() != null) {
                        Integer id = getIdFromExchange(httpExchange);
                        try {
                            response = gson.toJson(manager.getTaskById(id));
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (path.endsWith("/epic/") && uri.getRawQuery() == null) {
                        response = gson.toJson(manager.viewAllEpic());
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (path.contains("/epic/") && uri.getRawQuery() != null) {
                        Integer id = getIdFromExchange(httpExchange);
                        try {
                            response = gson.toJson(manager.getEpicById(id));
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (path.contains("/subtask/epic")) {
                        Integer id = getIdFromExchange(httpExchange);
                        try {
                            response = gson.toJson(manager.getEpicById(id).getInnerSubTask().values());
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (path.endsWith("/subtask/") && uri.getRawQuery() == null) {
                        response = gson.toJson(manager.viewAllSubtask());
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (path.contains("/subtask/") && uri.getRawQuery() != null) {
                        Integer id = getIdFromExchange(httpExchange);
                        try {
                            response = gson.toJson(manager.getEpicById(id));
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                    } else if (path.endsWith("/tasks/")) {
                        response = gson.toJson(manager.getPrioritizedTasks());
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    if (path.endsWith("/history")) {
                        response = gson.toJson(manager.getTaskHistory());
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case "POST":
                    String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    if (path.contains("/task/")) {
                        Task taskFromJson = gson.fromJson(body, Task.class);
                        if (taskFromJson.getId() == null) {
                            try {
                                manager.addTask(taskFromJson);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {
                            try {
                                manager.updateTask(taskFromJson.getId(), taskFromJson);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    } else if (path.contains("/epic/")) {
                        Epic epicFromJson = gson.fromJson(body, Epic.class);
                        if (epicFromJson.getId() == null) {
                            try {
                                manager.addEpic(epicFromJson);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {
                            try {
                                manager.updateEpic(epicFromJson.getId(), epicFromJson);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    } else if (path.contains("/subtask/")) {
                        SubTask subtaskFromJson = gson.fromJson(body, SubTask.class);
                        if (subtaskFromJson.getId() == null) {
                            try {
                                manager.addSubTask(subtaskFromJson);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {
                            try {
                                manager.updateSubTask(subtaskFromJson.getId(), subtaskFromJson);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    }
                    break;

                case "DELETE":
                    if (path.endsWith("/task/") && uri.getRawQuery() == null) {
                        try {
                            manager.deleteAllTask();
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            e.printStackTrace();
                        }
                    } else if (path.contains("/task/") && uri.getRawQuery() != null) {
                        Integer id = getIdFromExchange(httpExchange);
                        try {
                            manager.deleteTaskById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            e.printStackTrace();
                        }
                    } else if (path.endsWith("/epic/") && uri.getRawQuery() == null) {
                        try {
                            manager.deleteAllEpic();
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            e.printStackTrace();
                        }
                    } else if (path.contains("/epic/") && uri.getRawQuery() != null) {
                        Integer id = getIdFromExchange(httpExchange);
                        try {
                            manager.deleteEpicById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            e.printStackTrace();
                        }
                    } else if (path.endsWith("/subtask/") && uri.getRawQuery() == null) {
                        try {
                            manager.deleteAllSubTask();
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            e.printStackTrace();
                        }
                    } else if (path.contains("/subtask/") && uri.getRawQuery() != null) {
                        Integer id = getIdFromExchange(httpExchange);
                        try {
                            manager.deleteSubTaskById(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (ManagerSaveException e) {
                            e.printStackTrace();
                        }
                    }
                default:
                    httpExchange.sendResponseHeaders(405, 0);
            }
            try (
                    OutputStream stream = httpExchange.getResponseBody()) {
                if (response != null) {
                    stream.write(response.getBytes());
                }
            }
        }
    }
}