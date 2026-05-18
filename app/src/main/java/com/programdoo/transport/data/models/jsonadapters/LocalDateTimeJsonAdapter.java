package com.programdoo.transport.data.models.jsonadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.programdoo.transport.utils.DateUtil;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import javax.inject.Inject;

public class LocalDateTimeJsonAdapter implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
    @Inject
    public LocalDateTimeJsonAdapter() {
        super();
    }


    @Override
    public LocalDateTime deserialize(JsonElement element, Type typeofT, JsonDeserializationContext context)
        throws JsonParseException {
        return element == null || element.isJsonNull() ? null : DateUtil.apiParse(element.getAsString());
    }

    @Override
    public JsonElement serialize(LocalDateTime source, Type typeOfSource, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(DateUtil.apiFormat(source));
    }
}
