package org.example;

import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private String retornaTexto() {
        return "texto";
    }

    private static Map<String, Object> createJsonResponse(String key, Object value) {
        return Map.of(key, value);
    }

    private static Map<String, Object> createJsonResponse(Object value) {
        return createJsonResponse("response", value);
    }
    
    // Faça um método que retorn um json
    

    public static void main(String[] args) {
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Bem-vindo à API Javalin!"))
                .start(7070);

        Map<Integer, String> users = new HashMap<>();
        AtomicInteger userIdCounter = new AtomicInteger(1);

        app.get("/users", ctx -> ctx.json(users));

        app.post("/users", ctx -> {
            String name = ctx.body();
            int id = userIdCounter.getAndIncrement();
            users.put(id, name);
            ctx.json(createJsonResponse("Usuário criado"));

        });

        app.get("/users/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            String user = users.get(id);
            if (user == null) {
                throw new NotFoundResponse("Usuário não encontrado");
            }
            ctx.result(user);
            ctx.json(createJsonResponse("user", Map.of("id", id, "Nome", user)));
        });

        app.delete("/users/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (users.remove(id) == null) {
                throw new NotFoundResponse("Usuário não encontrado");
            }
            ctx.result("Usuário removido com sucesso");
        });

        app.get("/time", ctx -> {
            ctx.json(createJsonResponse("response", "Hora do servidor: " + LocalTime.now()));
        });
    }
}
