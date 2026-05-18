package com.programdoo.transport.data.models.jsonadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.programdoo.transport.utils.TimeUtil;

import java.lang.reflect.Type;
import java.time.LocalTime;

import javax.inject.Inject;

public class LocalTimeJsonAdapter implements
        JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    @Inject
    public LocalTimeJsonAdapter() {
        super();
    }

    @Override
    public LocalTime deserialize(
            JsonElement element,
            Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        return element == null || element.isJsonNull() ? null : TimeUtil.apiParse(element.getAsString());
    }

    @Override
    public JsonElement serialize(
            LocalTime source,
            Type typeOfSource,
            JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(TimeUtil.apiFormat(source));
    }
}
